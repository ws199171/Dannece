package com.ssw.stockchart.listener;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewParent;

import com.ssw.stockchart.widget.BaseCharView;
import com.ssw.stockchart.widget.CharView;


/**
 * 行情手势控制
 *
 * @author saisai
 */
public class LineChartTouchListener extends BaseChartTouchListener {


    /**
     * 当前的手势状态
     */
    private BaseChartTouchListener.ChartGesture chartGesture = BaseChartTouchListener.ChartGesture.NONE;


    /**
     * the minimum distance between the pointers that will trigger a zoom gesture
     */
    private float mMinScalePointerDistance;

    private float mSavedXDist = 1f;

    /**
     * X轴偏移量
     */
    private float startX = 0;

    /**
     * 每个占的宽度
     */
    private float cellWidth = 0;

    /**
     * 可见画布宽度
     */
    private float coordinateWidth = 0f;

    /**
     * 每个ITEM的最小宽度
     */
    private float cellWidthMin = 0f;

    /**
     * 每个ITEM的最大宽度
     */
    private float cellWidthMax = 0f;

    /**
     * 当前显示的条目数量
     */
    private float showNumbers = 0;

    /**
     * 开始绘制的位置
     */
    private float drawStatIndex = 0;

    /**
     * 结束绘制的位置
     */
    private float drawEndIndex = 0;

    /**
     * 缩放状态
     */
    private BaseChartTouchListener.ChartZoomState charZoomState;

    /**
     * 触摸中心坐标点
     */
    private PointF mTouchPointCenter = new PointF();


    public LineChartTouchListener(@NonNull BaseCharView charView) {
        super(charView);
    }

    /**
     * returns the distance between two pointer touch points
     *
     * @param event -
     * @return -
     */
    private static float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * calculates the distance on the x-axis between two pointers (fingers on
     * the display)
     *
     * @param e -
     * @return -
     */
    private static float getXDist(MotionEvent e) {
        float x = Math.abs(e.getX(0) - e.getX(1));
        return x;
    }

    /**
     * Determines the center point between two pointer touch points.
     *
     * @param point -
     * @param event -
     */
    private static void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.x = (x / 2f);
        point.y = (y / 2f);
    }

    /**
     * 计算每个柱子的宽度
     *
     * @param focusX     - 两指焦点X位置
     * @param spanXRatio - 缩放比率
     */
    private void computeCellWidth(float focusX, float spanXRatio) {

        float radioFocusX = (focusX - startX) / this.cellWidth;
        int focusXInt = (int) Math.ceil(radioFocusX);
        float decimalRadioFocusX = radioFocusX - (int) radioFocusX;
        //处理后的缩放中心点
        float realCentFocusX = focusX + (0.5f - decimalRadioFocusX) * cellWidth;
        //在没有缩放后前后显示的数量
        float formerLeftVariety = (realCentFocusX - this.cellWidth / 2) / this.cellWidth;
        float formerRightVariety = (this.coordinateWidth - realCentFocusX - this.cellWidth / 2) / this.cellWidth;
        //这里每次缩放的系数按照当前实际绘制的宽度的1/10来计算
        this.cellWidth = cellWidth * spanXRatio;
        //宽度校验
        if (this.cellWidth <= this.cellWidthMin) {
            this.cellWidth = this.cellWidthMin;
            this.charZoomState = BaseChartTouchListener.ChartZoomState.ZOOM_MIN;
        } else if (this.cellWidth >= this.cellWidthMax) {
            this.cellWidth = this.cellWidthMax;
            this.charZoomState = BaseChartTouchListener.ChartZoomState.ZOOM_MAX;
        } else {
            this.charZoomState = BaseChartTouchListener.ChartZoomState.NORMAL;
        }
        //缩放位置权重
        float scalingWeight = focusXInt / showNumbers;
        Log.i("###", "-------computeCellWidth---  focusX = " + focusX + " spanXRatio = " + spanXRatio + " scalingWeight = " + scalingWeight + " cellWidth = " + cellWidth);
        computeDrawIndex2(realCentFocusX, scalingWeight, formerLeftVariety, formerRightVariety);
    }

    private void computeDrawIndex2(float realCentFocusX, float scalingWeight, float formerLeftVariety, float formerRightVariety) {
        //上此缩放动作后显示的数据
        float lastShowNumber = this.showNumbers;
        //左右两边的变化
        float leftVariety = (realCentFocusX - this.cellWidth / 2) / this.cellWidth;
        float rightVariety = (this.coordinateWidth - realCentFocusX - this.cellWidth / 2) / this.cellWidth;
        float nowShowNumber = (float) Math.ceil(leftVariety) + (float) Math.ceil(rightVariety) + 1f;
        Log.i("###", "-- computeDrawIndex2 --   nowShowNumber = " + nowShowNumber + " charZoomState = " + charZoomState + " chartGesture = " + chartGesture + " leftVariety " + leftVariety + " formerLeftVariety " + formerLeftVariety);
        //判断当前显示的数量和数据长度的大小
        if (nowShowNumber > charView.getData().dataSize()) {
            //当前需要绘制的数量，不足以完全铺满屏幕
            this.drawStatIndex = 0;
            this.drawEndIndex = charView.getData().dataSize();
            this.startX = 0;
            if (this.charZoomState == BaseChartTouchListener.ChartZoomState.ZOOM_MIN) {
                this.showNumbers = CharView.MAX_KLINE_NUMBER;
            } else if (charZoomState == BaseChartTouchListener.ChartZoomState.ZOOM_MAX) {
                this.showNumbers = CharView.MIN_KLINE_NUMBER;
            } else {
                this.showNumbers = nowShowNumber;
            }
        } else {
            // 当前显示的条目数和上此显示的条目数的差值
            float differenceShowNumber = nowShowNumber - lastShowNumber;
            this.showNumbers = nowShowNumber;
            if (chartGesture == BaseChartTouchListener.ChartGesture.ZOOM_IN) {
                computeZoomInProcess(nowShowNumber, differenceShowNumber, scalingWeight, leftVariety, rightVariety, formerLeftVariety);
            }
            //如果是放大
            else if (chartGesture == BaseChartTouchListener.ChartGesture.ZOOM_OUT) {
                computeZoomOutProcess(nowShowNumber, differenceShowNumber, scalingWeight, leftVariety, rightVariety, formerLeftVariety);
                Log.i("###", " ZOOM_OUT  drawStatIndex  = " + drawStatIndex + " drawEndIndex = " + drawEndIndex);
            }
        }
    }


    /**
     * 计算缩小的核心
     *
     * @param nowShowNumber        - 当前显示的条目数量
     * @param differenceShowNumber - 当前显示的条目数和上次显示的差值
     * @param scalingWeight        - 缩放权重 （依左边为参照）
     * @param leftVariety          - 左边变化的条目数
     * @param rightVariety         - 右边变化的条目数
     */
    private void computeZoomInProcess(float nowShowNumber, float differenceShowNumber, float scalingWeight, float leftVariety, float rightVariety, float formerLeftVariety) {
        boolean illegal = false;
        //如果是缩小
        if (chartGesture == BaseChartTouchListener.ChartGesture.ZOOM_IN) {
            //检验下标的合法性
            if (showNumbers > CharView.MAX_KLINE_NUMBER) {
                illegal = true;
            }
            if (illegal) {
                //检验下标的合法性
                float showDifference = drawEndIndex - drawStatIndex;
                //下标发生错误需要纠正
                float showVariety = CharView.MAX_KLINE_NUMBER - showDifference;
                this.startX = 0;
                float tempStatIndex = this.drawStatIndex - (int) Math.ceil(showVariety * scalingWeight);
                Log.i("###", " tempStatIndex --- " + "  illegal = " + illegal + " tempStatIndex " + tempStatIndex);
                if (tempStatIndex < 0) {
                    drawStatIndex = 0;
                } else {
                    drawStatIndex = tempStatIndex;
                }
                this.drawEndIndex = this.drawStatIndex + CharView.MAX_KLINE_NUMBER;
                if (this.drawEndIndex >= charView.getData().dataSize()) {
                    this.drawEndIndex = charView.getData().dataSize();
                }
                this.showNumbers = CharView.MAX_KLINE_NUMBER;
            } else {
                //是否存在小数
                boolean decimal = leftVariety > 0;
                //左边显示的部分
                int leftVarietyInt = (int) Math.ceil(leftVariety);
                float tempStatIndex = this.drawStatIndex - (leftVarietyInt - (int) Math.ceil(formerLeftVariety));
                float tempEndIndex = tempStatIndex + showNumbers;
                Log.i("###", " ZoomIn tempStatIndex =  " + tempStatIndex + " tempEndIndex =  " + tempEndIndex + " decimal " + decimal + " leftVariety " + leftVariety + " showNumbers = " + showNumbers);
                computeZoomIn(tempStatIndex, tempEndIndex, decimal, leftVariety - leftVarietyInt);
            }
            Log.i("###", "   ZOOM_IN  drawStatIndex  = " + drawStatIndex + " drawEndIndex = " + drawEndIndex);
        }
    }


    /**
     * 缩小控制
     *
     * @param tempStatIndex - 临时绘制开始位置
     * @param tempEndIndex  - 临时绘制结束位置
     * @param decimal       - 是否存在小数 ， true 存在 false 不存在
     * @param decimalShow   - 显示的当前蜡烛数量不足一个的部分
     */
    private void computeZoomIn(float tempStatIndex, float tempEndIndex, boolean decimal, float decimalShow) {
        int dataSize = charView.getData().dataSize();
        //如果存在小数，小数部分的数值，将放到左边，右边保持完整
        if (decimal) {
            if (tempStatIndex >= 0 && tempEndIndex <= dataSize) {
                this.drawStatIndex = tempStatIndex;
                this.drawEndIndex = tempEndIndex;
                this.startX = this.cellWidth * (decimalShow - 1);
            } else {
                //发生缩小越界错误
                if (tempEndIndex >= dataSize) {
                    this.drawEndIndex = dataSize;
                    float dicStatIndex = this.drawEndIndex - this.showNumbers;
                    if (dicStatIndex >= 0) {
                        this.drawStatIndex = dicStatIndex;
                        this.startX = this.cellWidth * (decimalShow - 1);
                    } else {
                        this.drawStatIndex = 0;
                        this.startX = 0;
                    }
                } else if (tempStatIndex < 0) {
                    this.drawStatIndex = 0;
                    this.startX = 0;
                    float dicEndIndex = this.drawStatIndex + this.showNumbers;
                    if (dicEndIndex <= dataSize) {
                        this.drawEndIndex = dicEndIndex;
                    } else {
                        this.drawEndIndex = dataSize;
                    }
                }
            }
            Log.i("###", " tempStatIndex --- " + "  drawEndIndex = " + drawEndIndex + " drawStatIndex " + drawStatIndex + " showNumbers " + showNumbers);
        } else {
            if (tempStatIndex >= 0 && tempEndIndex <= dataSize) {
                this.drawStatIndex = tempStatIndex;
                this.drawEndIndex = tempEndIndex;
            } else {
                //发生缩小越界错误
                if (tempEndIndex >= dataSize) {
                    this.drawEndIndex = dataSize;
                    float dicStatIndex = this.drawEndIndex - this.showNumbers;
                    if (dicStatIndex >= 0) {
                        this.drawStatIndex = dicStatIndex;
                    } else {
                        this.drawStatIndex = 0;
                    }
                } else if (tempStatIndex < 0) {
                    this.drawStatIndex = 0;
                    float dicEndIndex = this.drawStatIndex + this.showNumbers;
                    if (dicEndIndex <= dataSize) {
                        this.drawEndIndex = dicEndIndex;
                    } else {
                        this.drawEndIndex = dataSize;
                    }
                }
            }
            this.startX = 0;
        }
    }

    /**
     * 计算放大的核心
     *
     * @param nowShowNumber        - 当前显示的条目数量
     * @param differenceShowNumber - 当前显示的条目数和上次显示的差值
     * @param scalingWeight        - 缩放权重 （依左边为参照）
     * @param leftVariety          - 左边变化的条目数
     * @param rightVariety         - 右边变化的条目数
     */
    private void computeZoomOutProcess(float nowShowNumber, float differenceShowNumber, float scalingWeight, float leftVariety, float rightVariety, float formerLeftVariety) {
        boolean illegal = false;
        //检验下标的合法性
        if (showNumbers < CharView.MIN_KLINE_NUMBER) {
            illegal = true;
        }
        if (illegal) {
            float showDifference = drawEndIndex - drawStatIndex;
            //下标发生错误需要纠正
            float showVariety = showDifference - CharView.MIN_KLINE_NUMBER;
            this.startX = 0;
            this.drawStatIndex = this.drawStatIndex + (int) Math.ceil(showVariety * scalingWeight);
            this.drawEndIndex = this.drawStatIndex + CharView.MIN_KLINE_NUMBER;
            this.showNumbers = CharView.MIN_KLINE_NUMBER;
        } else {
            //是否存在小数
            float decimalShow = leftVariety - (int) leftVariety;
            boolean decimal = decimalShow > 0;
            //左边显示的部分
            float tempStatIndex = this.drawStatIndex + ((int) Math.ceil(formerLeftVariety) - (int) Math.ceil(leftVariety));
            float tempEndIndex = tempStatIndex + showNumbers;
            //如果存在小数，小数部分的数值，将放到左边，右边保持完整
            if (decimal) {
                if ((tempEndIndex - tempStatIndex) >= CharView.MIN_KLINE_NUMBER) {
                    this.drawStatIndex = tempStatIndex;
                    this.drawEndIndex = tempEndIndex;
                    this.startX = this.cellWidth * (decimalShow - 1);
                }
            } else {
                if ((tempEndIndex - tempStatIndex) >= CharView.MIN_KLINE_NUMBER) {
                    this.drawStatIndex = tempStatIndex;
                    this.drawEndIndex = tempEndIndex;
                    this.startX = 0;
                }
            }
            Log.i("###", "ZoomOut tempStatIndex =  " + tempStatIndex + " tempEndIndex =  " + tempEndIndex + " decimal " + decimal + " leftVariety " + leftVariety + " showNumbers = " + showNumbers);
        }
    }

    public void measure(float coordinateWidth, float cellWidthMin, float cellWidthMax) {
        this.coordinateWidth = coordinateWidth;
        this.cellWidthMin = cellWidthMin;
        this.cellWidthMax = cellWidthMax;
    }

    @Override
    public void resetTouchData(float... info) {
        super.resetTouchData(info);
        this.cellWidth = info[0];
        this.drawStatIndex = info[1];
        this.drawEndIndex = info[2];
        this.showNumbers = info[3];
        this.startX = info[4];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mSavedXDist = getXDist(event);
                midPoint(mTouchPointCenter, event);
                break;
            case MotionEvent.ACTION_MOVE:
                ViewParent parent = charView.getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                if (event.getPointerCount() >= 2) {
                    float totalDist = spacing(event);
                    if (totalDist > mMinScalePointerDistance) {
                        float xDist = getXDist(event);
                        float scaleX = xDist / mSavedXDist;
                        if (scaleX > 1) {
                            //放大
                            chartGesture = ChartGesture.ZOOM_OUT;
                        } else {
                            //缩小
                            chartGesture = ChartGesture.ZOOM_IN;
                        }
                        computeCellWidth(mTouchPointCenter.x, scaleX);
                        invalidateChart(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                chartGesture = ChartGesture.NONE;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        mScroller.forceFinished(true);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i("###", "------onLongPress-------");
        super.onLongPress(e);
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i("###", "------onScroll-------  distanceX =" + distanceX + " distanceY = " + distanceY);
        boolean invalidate = scroll(distanceX);
        if (invalidate) {
            chartGesture = ChartGesture.DRAG;
            invalidateChart(true);
        }
        return invalidate;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i("###", "------onFling-------");
        fling((int) e2.getX(), (int) -velocityX, (int) -velocityY);
        return true;
    }

    private int flingCurrX = 0;

    @Override
    public void fling(int startX, int velocityX, int velocityY) {
        flingCurrX = startX;
        mScroller.forceFinished(true);
        mScroller.fling(startX, 0, velocityX, 0, 1, 100 * 1000, 0, 0);
        invalidateChart(true);
    }

    @Override
    public boolean scroll(float distanceX) {
        if (distanceX < 0) {
            //向右滑动
            if (drawStatIndex == 0) {
                //已经是左极限
                return false;
            } else {
                if (startX == 0) {
                    float dragNumber = (-distanceX) / this.cellWidth;
                    if (dragNumber > 1) {
                        //拖动的条目数大于1
                        int number = (int) dragNumber;
                        float distance = dragNumber - number;
                        this.startX = this.cellWidth * (distance - 1);
                        this.drawStatIndex = drawStatIndex - number;
                        this.drawEndIndex = drawEndIndex - number;
                    } else {
                        this.startX = distanceX;
                        this.drawStatIndex = drawStatIndex - 1;
                        this.drawEndIndex = drawEndIndex - 1;
                    }
                } else {
                    float distanceDragX = distanceX - startX;
                    if (distanceDragX < 0) {
                        //如果  < 0 证明当前 drawStatIndex 已经发生了变化，需要调整大小
                        float dragNumber = (-distanceX) / this.cellWidth;
                        if (dragNumber > 1) {
                            //拖动的条目数大于1
                            int number = (int) dragNumber;
                            float distance = dragNumber - number;
                            this.startX = this.cellWidth * (distance - 1);
                            this.drawStatIndex = drawStatIndex - number;
                            this.drawEndIndex = drawEndIndex - number;
                        } else {
                            this.startX = distanceDragX;
                            this.drawStatIndex = drawStatIndex - 1;
                            this.drawEndIndex = drawEndIndex - 1;
                        }
                    } else if (distanceDragX == 0) {
                        this.startX = 0;
                    } else {
                        this.startX = -distanceDragX;
                    }
                }
                //校验是否越界
                if (drawStatIndex < 0) {
                    drawStatIndex = 0;
                    drawEndIndex = drawStatIndex + showNumbers;
                    this.startX = 0;
                }
            }
        } else {
            //向左滑动
            if (drawEndIndex >= charView.getData().dataSize()) {
                //已经是右极限
                return false;
            } else {
                //未发生下标变化，只需要修改偏移
                if (distanceX < (cellWidth + startX)) {
                    startX = startX - distanceX;
                } else {
                    if (startX == 0) {
                        float dragNumber = distanceX / this.cellWidth;
                        float numberDecimal = dragNumber - (int) dragNumber;
                        if (numberDecimal > 0) {
                            this.startX = -distanceX % this.cellWidth;
                            this.drawStatIndex = drawStatIndex + (int) dragNumber + 1;
                            this.drawEndIndex = drawEndIndex + (int) dragNumber + 1;
                        } else {
                            this.startX = 0;
                            this.drawStatIndex = drawStatIndex + dragNumber;
                            this.drawEndIndex = drawEndIndex + dragNumber;
                        }
                    } else {
                        float distanceDragX = distanceX - (cellWidth + startX);
                        if (distanceDragX == 0) {
                            this.startX = 0;
                            this.drawStatIndex = drawStatIndex + 1;
                            this.drawEndIndex = drawEndIndex + 1;
                        } else if (distanceDragX > 0) {
                            //当前坐标肯定发生了变化
                            float dragNumber = distanceDragX / this.cellWidth;
                            float numberDecimal = dragNumber - (int) dragNumber;
                            if (numberDecimal > 0) {
                                this.startX = -distanceX % this.cellWidth;
                                this.drawStatIndex = drawStatIndex + (int) dragNumber + 2;
                                this.drawEndIndex = drawEndIndex + (int) dragNumber + 2;
                            } else {
                                this.startX = 0;
                                this.drawStatIndex = drawStatIndex + dragNumber + 1;
                                this.drawEndIndex = drawEndIndex + dragNumber + 1;
                            }
                        } else if (distanceDragX < 0) {
                            this.startX = startX - distanceX;
                        }
                    }
                }
            }

            //校验是否越界
            if (drawEndIndex >= charView.getData().dataSize()) {
                drawEndIndex = charView.getData().dataSize();
                drawStatIndex = drawEndIndex - showNumbers;
                this.startX = 0;
            }
        }

        return true;
    }

    @Override
    public void computeScroll() {
        boolean invalidate = false;
        if (mScroller.computeScrollOffset()) {
            int currX = mScroller.getCurrX();
            int distanceX = currX - flingCurrX;
            Log.i("###", "------computeScroll------- distanceX " + distanceX);
            invalidate = scroll(distanceX);
            flingCurrX = currX;
        }
        if (invalidate) {
            invalidateChart(true);
        }
    }

    /**
     * 刷新图表
     *
     * @param invalidate -
     */
    private void invalidateChart(boolean invalidate) {
        if (charView != null) {
            charView.resetDrawData(invalidate, this.cellWidth, this.drawStatIndex, this.drawEndIndex, this.showNumbers, this.startX);
        }
    }
}
