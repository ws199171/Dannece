package com.ssw.stockchart.listener;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewParent;

import com.ssw.stockchart.render.BaseRenderer;
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

    /**
     * 记录开始点击的点
     */
    private PointF mTouchStartPoint = new PointF();

    private float mSavedXDist = 1f;
    private float mSavedDist = 1f;

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
    private float drawStartIndex = 0;

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

    private float mDragTriggerDist;

    private float lastMoveX = 0f;


    public LineChartTouchListener(@NonNull BaseCharView charView) {
        super(charView);
        mDragTriggerDist = BaseRenderer.dpToPx(charView.getContext(), 3);
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

    private void saveTouchStart(MotionEvent event) {
        mTouchStartPoint.x = event.getX();
        mTouchStartPoint.y = event.getY();
    }

    protected static float distance(float eventX, float startX, float eventY, float startY) {
        float dx = eventX - startX;
        float dy = eventY - startY;
        return (float) Math.sqrt(dx * dx + dy * dy);
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
        computeDrawIndex2(realCentFocusX, scalingWeight, formerLeftVariety, formerRightVariety);
    }

    private void computeDrawIndex2(float realCentFocusX, float scalingWeight, float formerLeftVariety, float formerRightVariety) {
        //上此缩放动作后显示的数据
        float lastShowNumber = this.showNumbers;
        //左右两边的变化
        float leftVariety = (realCentFocusX - this.cellWidth / 2) / this.cellWidth;
        float rightVariety = (this.coordinateWidth - realCentFocusX - this.cellWidth / 2) / this.cellWidth;
        float nowShowNumber = (float) Math.ceil(leftVariety) + (float) Math.ceil(rightVariety) + 1f;
        //判断当前显示的数量和数据长度的大小
        if (nowShowNumber > charView.getData().dataSize()) {
            //当前需要绘制的数量，不足以完全铺满屏幕
            this.drawStartIndex = 0;
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
                float showDifference = drawEndIndex - drawStartIndex;
                //下标发生错误需要纠正
                float showVariety = CharView.MAX_KLINE_NUMBER - showDifference;
                this.startX = 0;
                float tempStatIndex = this.drawStartIndex - (int) Math.ceil(showVariety * scalingWeight);
                if (tempStatIndex < 0) {
                    drawStartIndex = 0;
                } else {
                    drawStartIndex = tempStatIndex;
                }
                this.drawEndIndex = this.drawStartIndex + CharView.MAX_KLINE_NUMBER;
                if (this.drawEndIndex >= charView.getData().dataSize()) {
                    this.drawEndIndex = charView.getData().dataSize();
                }
                this.showNumbers = CharView.MAX_KLINE_NUMBER;
            } else {
                //是否存在小数
                boolean decimal = leftVariety > 0;
                //左边显示的部分
                int leftVarietyInt = (int) Math.ceil(leftVariety);
                float tempStatIndex = this.drawStartIndex - (leftVarietyInt - (int) Math.ceil(formerLeftVariety));
                float tempEndIndex = tempStatIndex + showNumbers;
                computeZoomIn(tempStatIndex, tempEndIndex, decimal, leftVariety - leftVarietyInt);
            }
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
                this.drawStartIndex = tempStatIndex;
                this.drawEndIndex = tempEndIndex;
                this.startX = this.cellWidth * (decimalShow - 1);
            } else {
                //发生缩小越界错误
                if (tempEndIndex >= dataSize) {
                    this.drawEndIndex = dataSize;
                    float dicStatIndex = this.drawEndIndex - this.showNumbers;
                    if (dicStatIndex >= 0) {
                        this.drawStartIndex = dicStatIndex;
                        this.startX = this.cellWidth * (decimalShow - 1);
                    } else {
                        this.drawStartIndex = 0;
                        this.startX = 0;
                    }
                } else if (tempStatIndex < 0) {
                    this.drawStartIndex = 0;
                    this.startX = 0;
                    float dicEndIndex = this.drawStartIndex + this.showNumbers;
                    if (dicEndIndex <= dataSize) {
                        this.drawEndIndex = dicEndIndex;
                    } else {
                        this.drawEndIndex = dataSize;
                    }
                }
            }
        } else {
            if (tempStatIndex >= 0 && tempEndIndex <= dataSize) {
                this.drawStartIndex = tempStatIndex;
                this.drawEndIndex = tempEndIndex;
            } else {
                //发生缩小越界错误
                if (tempEndIndex >= dataSize) {
                    this.drawEndIndex = dataSize;
                    float dicStatIndex = this.drawEndIndex - this.showNumbers;
                    if (dicStatIndex >= 0) {
                        this.drawStartIndex = dicStatIndex;
                    } else {
                        this.drawStartIndex = 0;
                    }
                } else if (tempStatIndex < 0) {
                    this.drawStartIndex = 0;
                    float dicEndIndex = this.drawStartIndex + this.showNumbers;
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
            float showDifference = drawEndIndex - drawStartIndex;
            //下标发生错误需要纠正
            float showVariety = showDifference - CharView.MIN_KLINE_NUMBER;
            this.startX = 0;
            this.drawStartIndex = this.drawStartIndex + (int) Math.ceil(showVariety * scalingWeight);
            this.drawEndIndex = this.drawStartIndex + CharView.MIN_KLINE_NUMBER;
            this.showNumbers = CharView.MIN_KLINE_NUMBER;
        } else {
            //是否存在小数
            float decimalShow = leftVariety - (int) leftVariety;
            boolean decimal = decimalShow > 0;
            //左边显示的部分
            float tempStatIndex = this.drawStartIndex + ((int) Math.ceil(formerLeftVariety) - (int) Math.ceil(leftVariety));
            float tempEndIndex = tempStatIndex + showNumbers;
            //如果存在小数，小数部分的数值，将放到左边，右边保持完整
            if (decimal) {
                if ((tempEndIndex - tempStatIndex) >= CharView.MIN_KLINE_NUMBER) {
                    this.drawStartIndex = tempStatIndex;
                    this.drawEndIndex = tempEndIndex;
                    this.startX = this.cellWidth * (decimalShow - 1);
                }
            } else {
                if ((tempEndIndex - tempStatIndex) >= CharView.MIN_KLINE_NUMBER) {
                    this.drawStartIndex = tempStatIndex;
                    this.drawEndIndex = tempEndIndex;
                    this.startX = 0;
                }
            }
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
        this.drawStartIndex = info[1];
        this.drawEndIndex = info[2];
        this.showNumbers = info[3];
        this.startX = info[4];
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // gestureDetector.onTouchEvent(event);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() >= 2) {
                    saveTouchStart(event);
                    mSavedXDist = getXDist(event);
                    mSavedDist = spacing(event);
                    if (mSavedDist > 10) {
                        chartGesture = ChartGesture.ZOOM_NONE;
                    }
                    midPoint(mTouchPointCenter, event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                switch (chartGesture) {
                    case DRAG:
                        charView.disableScroll();
                        float distanceX = event.getX() - lastMoveX;
                        performScroll(-distanceX);
                        lastMoveX = event.getX();
                        break;
                    case ZOOM_IN:
                    case ZOOM_OUT:
                    case ZOOM_NONE:
                        charView.disableScroll();
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
                    case NONE:
                        if (Math.abs(distance(event.getX(), mTouchStartPoint.x, event.getY(),
                                mTouchStartPoint.y)) > mDragTriggerDist) {
                            chartGesture = ChartGesture.DRAG;
                            lastMoveX = event.getX();
                        }
                        break;
                    default:
                        break;
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
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return performScroll(distanceX);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        fling((int) e2.getX(), (int) -velocityX, (int) -velocityY);
        return true;
    }

    private int flingCurrX = 0;

    @Override
    public void fling(int startX, int velocityX, int velocityY) {
        flingCurrX = startX;
        mScroller.forceFinished(true);
        mScroller.fling(startX, 0, velocityX, 0, 1, 100 * 1000, 0, 0);
        Log.i("###", "---------fling start-------- startX " + startX + "  velocityX " + velocityX);
        invalidateChart(true);
    }


    /**
     * 滑动逻辑封装
     *
     * @param distanceX -
     * @return -
     */
    private boolean performScroll(float distanceX) {
        boolean invalidate = scroll(distanceX);
        if (invalidate) {
            chartGesture = ChartGesture.DRAG;
            invalidateChart(true);
        }
        return invalidate;
    }

    @Override
    public boolean scroll(float distanceX) {
        if (distanceX < 0) {
            //向右滑动
            if (drawStartIndex > 0) {
                float distanceDragX = distanceX - startX;
                if (distanceDragX < 0) {
                    //如果  < 0 证明当前 drawStartIndex 已经发生了变化，需要调整大小
                    float dragNumber = -distanceDragX / this.cellWidth;
                    if (dragNumber > 1) {
                        //拖动的条目数大于1
                        int number = (int) dragNumber;
                        float distance = dragNumber - number;
                        this.startX = this.cellWidth * (distance - 1);
                        this.drawStartIndex = drawStartIndex - number;
                        this.drawEndIndex = drawEndIndex - number;
                    } else {
                        this.startX = -this.cellWidth - distanceDragX;
                        this.drawStartIndex = drawStartIndex - 1;
                        this.drawEndIndex = drawEndIndex - 1;
                    }
                } else if (distanceDragX == 0) {
                    this.startX = 0;
                } else {
                    this.startX = -distanceDragX;
                }
            }
            //校验是否越界
            if (drawStartIndex == 0) {
                drawStartIndex = 0;
                drawEndIndex = drawStartIndex + showNumbers;
                //当滑动到最左边时特殊处理，达到平滑的效果
                if (this.startX < 0) {
                    this.startX = this.startX - distanceX;
                    if (this.startX > 0) {
                        this.startX = 0;
                    }
                }
            } else if (drawStartIndex < 0) {
                drawStartIndex = 0;
                drawEndIndex = drawStartIndex + showNumbers;
                this.startX = 0;
            }
        } else {
            //向左滑动
            if (drawEndIndex < charView.getData().dataSize()) {
                //未发生下标变化，只需要修改偏移
                if (distanceX < (cellWidth + startX)) {
                    startX = startX - distanceX;
                } else {
                    float distanceDragX = distanceX - (cellWidth + startX);
                    if (distanceDragX == 0) {
                        this.startX = 0;
                        this.drawStartIndex = this.drawStartIndex + 1;
                        this.drawEndIndex = drawEndIndex + 1;
                    } else if (distanceDragX > 0) {
                        //当前坐标肯定发生了变化
                        float dragNumber = distanceDragX / this.cellWidth;
                        if (dragNumber > 1) {
                            this.startX = -distanceX % this.cellWidth;
                            this.drawEndIndex = drawEndIndex + (int) dragNumber + 1;
                            this.drawStartIndex = drawStartIndex + 1;
                        } else {
                            this.startX = -distanceDragX;
                            this.drawEndIndex = drawEndIndex + 1;
                            this.drawStartIndex = drawStartIndex + 1;
                        }
                    } else if (distanceDragX < 0) {
                        this.startX = startX - distanceX;
                    }
                }
            }

            //校验是否越界
            if (drawEndIndex == charView.getData().dataSize()) {
                drawEndIndex = charView.getData().dataSize();
                drawStartIndex = drawEndIndex - showNumbers;
                //当滑动到最左边时特殊处理，达到平滑的效果
                if (this.startX < 0) {
                    this.startX = this.startX + distanceX;
                    if (this.startX > 0) {
                        this.startX = 0;
                    }
                }
            } else if (drawEndIndex > charView.getData().dataSize()) {
                drawEndIndex = charView.getData().dataSize();
                drawStartIndex = drawEndIndex - showNumbers;
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
            Log.i("###", "---------fling scrolling-------- currX " + currX + "  distanceX " + distanceX);
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
            charView.resetDrawData(invalidate, this.cellWidth, this.drawStartIndex, this.drawEndIndex, this.showNumbers, this.startX);
        }
    }

}
