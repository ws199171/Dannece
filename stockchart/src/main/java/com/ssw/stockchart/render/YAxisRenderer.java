package com.ssw.stockchart.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import java.util.List;


/**
 * Y轴上面的渲染器
 *
 * @author saisai
 * @date 2018/6/11
 */
public class YAxisRenderer extends BaseRenderer {

    /**
     * 左边文字画笔
     */
    private Paint leftTextPaint = null;
    /**
     * 右边文字画笔
     */
    private Paint rightTextPaint = null;
    /**
     * 底部文字画笔
     */
    private Paint bottomTextPaint = null;
    /**
     * 主要是展示当前的图表信息画笔
     */
    private Paint infoPaint = null;
    /**
     * 当前背景的矩行
     */
    private RectF backgroundRect;
    /**
     * 经线画笔
     */
    private Paint mLinePaint = null;
    /**
     * 虚线画笔
     */
    private Paint mDashLinePaint = null;
    /**
     * 坐标系底部空余
     */
    public static int MARGIN_BOTTOM = 30;
    /**
     * 坐标系顶部空余
     */
    public static final int MARGIN_TOP = 20;
    /**
     * 需要展示的信息
     */
    private List<String> info;
    /**
     * 每个信息文字的颜色
     */
    private List<Integer> colors;
    /**
     * 坐标系刻度文字大小
     */
    private float coordinateTextSize = 18;

    /**
     * MA距离TOP的高度
     */
    private int maPaddingTop = 0;

    /**
     * 当前最大极值
     */
    private float YMax = 0;

    public YAxisRenderer(boolean isSubRender, int showType, @NonNull Context mContext) {
        super(isSubRender, showType, mContext);
        init();
    }

    @Override
    public void makeDrawData(int position) {
        if (isSubRender) {
            YMax = subYMax;
            yPriceDifference = subYMax - subYMin;
            effectiveHeight = mSubRect.bottom - mSubRect.top;
            yRectDifference = (effectiveHeight - maHeight);
            effectiveWeight = mSubRect.right - mSubRect.left;
            xRectDifference = effectiveWeight - paddingLeft;
        } else {
            YMax = mainYMax;
            yPriceDifference = mainYMax - mainYMin;
            effectiveHeight = mMainRect.bottom - mMainRect.top;
            yRectDifference = (effectiveHeight - maHeight);
            effectiveWeight = mMainRect.right - mMainRect.left;
            xRectDifference = effectiveWeight - paddingLeft;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        drawLatitude(canvas, true, false);
    }

    @Override
    public void init() {

        MARGIN_BOTTOM = (int) dpToPx(mContext, 15);
        backgroundRect = new RectF();

        //初始化经线画笔
        this.mLinePaint = new Paint();
        this.mLinePaint.setStyle(Paint.Style.FILL);
        this.mLinePaint.setColor(mCoordinatesLineColor == 0 ? 0xFFCCCCCC : mCoordinatesLineColor);
        this.mLinePaint.setStrokeWidth(1f);

        //初始化虚线画笔
        mDashLinePaint = new Paint();
        mDashLinePaint.setStyle(Paint.Style.FILL);
        mDashLinePaint.setColor(mCoordinatesLineColor == 0 ? 0xFFCCCCCC : mCoordinatesLineColor);
        mDashLinePaint.setStrokeWidth(1f);
        mDashLinePaint.setPathEffect(new DashPathEffect(new float[]{4, 4, 4, 4}, 4));

        //初始化左边文字画笔
        this.leftTextPaint = new Paint();
        leftTextPaint.setTextSize(coordinateTextSize);
        leftTextPaint.setAntiAlias(true);
        leftTextPaint.setColor(0xFF666666);
        //初始化右边文字画笔
        this.rightTextPaint = new Paint();
        rightTextPaint.setTextSize(coordinateTextSize);
        rightTextPaint.setAntiAlias(true);
        rightTextPaint.setColor(0xFF666666);
        //初始化底部文字画笔
        this.bottomTextPaint = new Paint();
        bottomTextPaint.setTextSize(coordinateTextSize);
        bottomTextPaint.setAntiAlias(true);
        bottomTextPaint.setColor(0xFF666666);

        //信息画笔
        infoPaint = new Paint();

        //初始化空隙,该空隙用于文字与边距和纬线之间的距离
        space = 2;

        maPaddingTop = (int) dpToPx(mContext, 8);
    }

    /**
     * 画纬线
     *
     * @param canvas         画布
     * @param isNeedShowLine 是否画纬线
     * @param isNeedShowText 是否画纬线刻度
     */
    private void drawLatitude(Canvas canvas, boolean isNeedShowLine, boolean isNeedShowText) {
        //纬线左边的文字是否画到坐标系内部 默认画外面
        boolean leftTextDrawIn = paddingLeft == 0;
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(1.0f);
        //纬线总条数
        int latitudeNums = data.getLeftScaleTextArraySize();
        //保证左右的内容数据量一至
        if (latitudeNums != data.getRightScaleTextArraySize()) {
            if (data.getRightScaleTextArraySize() == 0) {
                String[] strings = new String[latitudeNums];
                for (int i = 0; i < latitudeNums; i++) {
                    strings[i] = "";
                }
                data.setRightScaleTextArray(strings);
            }
        }

        leftTextPaint.setColor(Color.parseColor("#666666"));
        rightTextPaint.setColor(Color.parseColor("#666666"));

        //纬线间宽度
        float latitudeSpace = effectiveHeight / (latitudeNums - 1);

        Paint.FontMetrics fm = new Paint.FontMetrics();
        leftTextPaint.getFontMetrics(fm);
        for (int i = 0; i < latitudeNums; i++) {
            //重新设置字体大小
            leftTextPaint.setTextSize(coordinateTextSize);
            rightTextPaint.setTextSize(coordinateTextSize);
            //计算文字宽度然后再对比宽度

            //左边刻度文字
            String leftScale = data.getLeftScaleTextArrayByIndex(i);
            //左边文字宽度
            float leftScaleWidth = leftTextPaint.measureText(leftScale);
            //左边文字高度
            float leftScaleHeight = Math.abs(fm.ascent) - fm.descent;

            //右边刻度文字
            String rightScale = data.getRightScaleTextArrayByIndex(i);
            //右边文字宽度
            float rightScaleWidth = rightTextPaint.measureText(rightScale);
            //右边文字高度
            float rightScaleHeight = Math.abs(fm.ascent) - fm.descent;

            //第一条纬线
            if (i == 0) {
                //最后一条纬线只画text不画线条 所以当isNeedShowText为false时可以跳过这次循环
                if (!isNeedShowText) {
                    continue;
                }
                //画纬线
                if (showType != K_DAY) {
                    if (showType != TIME_DAY &&
                            showType != TIME_GGT_DAY &&
                            showType != TIME_FIVE_DAY &&
                            showType != CONTRAST_VIEW &&
                            showType != TIME_VOLUME) {

                        if (!isHorizontal || leftTextDrawIn) {
                            //画纬线刻度(左)
                            canvas.drawText(leftScale, space, leftScaleHeight * 3 + space, leftTextPaint);
                        } else {
                            //画纬线刻度(左)
                            canvas.drawText(leftScale, space + (paddingLeft - leftScaleWidth - 10), leftScaleHeight * 3 + space, leftTextPaint);
                        }

                    } else {

                        if (showType == TIME_VOLUME) {
                            if (!isHorizontal || leftTextDrawIn) {
                                //画纬线刻度(左)
                                canvas.drawText(leftScale, space, leftScaleHeight + 10 + space, leftTextPaint);
                                //画纬线刻度(右)
                                canvas.drawText(rightScale, effectiveWeight - rightScaleWidth - space, leftScaleHeight + 10 + space, rightTextPaint);
                            } else {
                                //画纬线刻度(左)
                                canvas.drawText(leftScale, space + (paddingLeft - leftScaleWidth - 10), leftScaleHeight + 5 + space, leftTextPaint);

                                //画纬线刻度(右)
                                canvas.drawText(rightScale, effectiveWeight - rightScaleWidth - space, leftScaleHeight + 5 + space, rightTextPaint);
                            }
                        } else {

                            //分时图第一个位置向上便宜高度的10%
                            if (!isHorizontal || leftTextDrawIn) {
                                //画纬线刻度(左)
                                canvas.drawText(leftScale, space, leftScaleHeight * 2, leftTextPaint);
                                //画纬线刻度(右)
                                canvas.drawText(rightScale, effectiveWeight - rightScaleWidth - space, leftScaleHeight * 2, rightTextPaint);
                            } else {
                                //画纬线刻度(左)
                                canvas.drawText(leftScale, space + (paddingLeft - leftScaleWidth - 10), leftScaleHeight * 2, leftTextPaint);

                                //画纬线刻度(右)
                                canvas.drawText(rightScale, effectiveWeight - rightScaleWidth - space, leftScaleHeight * 2, rightTextPaint);
                            }
                        }

                    }

                } else {
                    if (!isHorizontal || leftTextDrawIn) {
                        //画纬线刻度(右) ,但是使用的 左刻度值的数据
                        canvas.drawText(leftScale, space, leftScaleHeight * 2, leftTextPaint);
                    } else {
                        //画纬线刻度(左)
                        canvas.drawText(leftScale, space + (paddingLeft - leftScaleWidth - 10), leftScaleHeight * 2, leftTextPaint);
                    }
                }
            }
            //最后一条纬线
            else if (i == latitudeNums - 1) {
                //最后一条纬线只画text不画线条 所以当isNeedShowText为false时可以跳过这次循环
                if (!isNeedShowText) {
                    continue;
                }
                if (showType != K_DAY) {
                    if (showType != TIME_DAY &&
                            showType != TIME_GGT_DAY &&
                            showType != TIME_HK_DAY &&
                            showType != TIME_FIVE_DAY &&
                            showType != TIME_VOLUME &&
                            showType != CONTRAST_VIEW) {

                        if (!isHorizontal || leftTextDrawIn) {
                            //非横屏的最后一个非分时五日线的指标不绘制
                            canvas.drawText(leftScale, space, effectiveHeight - leftScaleHeight + space, leftTextPaint);
                        } else {
                            if (showType == VOLUME) {
                                String tempString;

                                if (YMax / 100000000 >= 1) {
                                    tempString = "亿";
                                } else if (YMax / 100000 >= 1) {
                                    tempString = "万";
                                } else {
                                    tempString = "0";
                                }
                                //画纬线刻度(左)
                                canvas.drawText(tempString, space + (paddingLeft - leftScaleWidth - leftTextPaint.measureText(tempString)), effectiveHeight - space, leftTextPaint);
                            } else {
                                //画纬线刻度(左)
                                canvas.drawText(leftScale, space + (paddingLeft - leftScaleWidth - 10), effectiveHeight - space, leftTextPaint);
                            }
                        }

                    } else {
                        if (!isHorizontal || leftTextDrawIn) {
                            //非横屏 如果最后一条危险的数据类型是 TIME_VOLUM 那么就不进行绘制
                            if (showType != TIME_VOLUME) {
                                //位置想下偏移10%
                                canvas.drawText(leftScale, space, effectiveHeight - leftScaleHeight, leftTextPaint);
                                //画纬线刻度(右)
                                canvas.drawText(rightScale, effectiveWeight - rightScaleWidth - space, effectiveHeight - leftScaleHeight, rightTextPaint);
                            }
                        } else {
                            //画纬线刻度(左)
                            if (showType == TIME_VOLUME) {
                                String tempString;

                                if (YMax / 100000000 >= 1) {
                                    tempString = "亿";
                                } else if (YMax / 100000 >= 1) {
                                    tempString = "万";
                                } else {
                                    tempString = "0";
                                }
                                canvas.drawText(tempString, space + (paddingLeft - leftScaleWidth - leftTextPaint.measureText(tempString)), effectiveHeight - space, leftTextPaint);
                            } else {
                                canvas.drawText(leftScale, space + (paddingLeft - leftScaleWidth - 15), effectiveHeight - leftScaleHeight, leftTextPaint);
                            }
                            //画纬线刻度(右)
                            canvas.drawText(rightScale, effectiveWeight - rightScaleWidth - space, effectiveHeight - leftScaleHeight, rightTextPaint);
                        }
                    }

                } else {

                    if (!isHorizontal || leftTextDrawIn) {
                        //画纬线刻度(右) ,但是使用的 左刻度值的数据
                        canvas.drawText(leftScale, space, effectiveHeight - leftScaleHeight, leftTextPaint);
                    } else {
                        //画纬线刻度(左)
                        canvas.drawText(leftScale, space + (paddingLeft - leftScaleWidth - 10), effectiveHeight - leftScaleHeight, leftTextPaint);
                    }
                }

            } else {//中间的纬线

                //画纬线
                //纬线间隙,i此时应从1开始,因为第一个if屏蔽了0
                float tempLatitudeSpace = i * latitudeSpace;

                if (!isNeedShowLine) {
                    //当前坐标是分时五日，如果不是中间的线都画成虚线
                    if (showType == TIME_GGT_DAY ||
                            showType == TIME_DAY ||
                            showType == TIME_FIVE_DAY ||
                            showType == TIME_HK_DAY) {
                        if (i == (latitudeNums - 1) / 2) {
                            //画成实线
                            canvas.drawLine(paddingLeft, tempLatitudeSpace, effectiveWeight, tempLatitudeSpace, mLinePaint);
                        } else {
                            //画成虚线
                            canvas.drawLine(paddingLeft, tempLatitudeSpace, effectiveWeight, tempLatitudeSpace, mDashLinePaint);
                        }
                    } else {
                        canvas.drawLine(paddingLeft, tempLatitudeSpace, effectiveWeight, tempLatitudeSpace, mLinePaint);
                    }
                }

                if (!isNeedShowText) {
                    //如果不画文字 跳过此次循环
                    continue;
                }
                if (showType != K_DAY) {
                    if (showType != TIME_DAY &&
                            showType != TIME_GGT_DAY &&
                            showType != TIME_FIVE_DAY &&
                            showType != CONTRAST_VIEW &&
                            showType != TIME_VOLUME &&
                            showType != TIME_HK_DAY) {
                        if (!isHorizontal || leftTextDrawIn) {
                            canvas.drawText(leftScale, space, tempLatitudeSpace + leftScaleHeight / 2, leftTextPaint);
                        } else {
                            //画纬线刻度(左)
                            canvas.drawText(leftScale, space + (paddingLeft - leftScaleWidth - 10), tempLatitudeSpace + leftScaleHeight / 2, leftTextPaint);
                        }
                    } else {
                        if (showType == TIME_VOLUME) {
                            if (!isHorizontal || leftTextDrawIn) {
                                canvas.drawText(leftScale, space, tempLatitudeSpace + leftScaleHeight / 2, leftTextPaint);
                                //画纬线刻度(右)
                                canvas.drawText(rightScale, effectiveWeight - rightScaleWidth - space, tempLatitudeSpace + rightScaleHeight / 2, rightTextPaint);
                            } else {
                                //画纬线刻度(左)
                                canvas.drawText(leftScale, space + (paddingLeft - leftScaleWidth - 10), tempLatitudeSpace + leftScaleHeight / 2, leftTextPaint);
                                //画纬线刻度(右)
                                canvas.drawText(rightScale, effectiveWeight - rightScaleWidth - space, tempLatitudeSpace + rightScaleHeight / 2, rightTextPaint);
                            }
                        } else {
                            if (showType == TIME_VOLUME || i == (latitudeNums - 1) / 2) {
                                //中间的坐标都将进行偏移
                                if (!isHorizontal || leftTextDrawIn) {
                                    canvas.drawText(leftScale, space, tempLatitudeSpace + leftScaleHeight / 2, leftTextPaint);
                                    //画纬线刻度(右)
                                    canvas.drawText(rightScale, effectiveWeight - rightScaleWidth - space, tempLatitudeSpace + rightScaleHeight / 2, rightTextPaint);
                                } else {
                                    //画纬线刻度(左)
                                    canvas.drawText(leftScale, space + (paddingLeft - leftScaleWidth - 10), tempLatitudeSpace + leftScaleHeight / 2, leftTextPaint);
                                    //画纬线刻度(右)
                                    canvas.drawText(rightScale, effectiveWeight - rightScaleWidth - space, tempLatitudeSpace + rightScaleHeight / 2, rightTextPaint);
                                }
                            }
                        }
                    }
                } else {
                    if (!isHorizontal || leftTextDrawIn) {
                        //画纬线刻度(右) ,但是使用的 左刻度值的数据
                        canvas.drawText(leftScale, space, tempLatitudeSpace + leftScaleHeight / 2, leftTextPaint);
                    } else {
                        //画纬线刻度(左)
                        canvas.drawText(leftScale, space + (paddingLeft - leftScaleWidth - 10), tempLatitudeSpace + leftScaleHeight / 2, leftTextPaint);
                    }
                }
            }
        }
    }


    /**
     * 绘制部分需要展示的信息
     *
     * @param canvas - 画布
     */
    public void drawSomethingInfo(Canvas canvas, int index) {
        if (showType == TIME_DAY || showType == TIME_GGT_DAY || showType == TIME_FIVE_DAY || showType == TIME_HK_DAY) {
            return;
        }

        float textSizeSum = paddingLeft + 3;
        int infoSpaceLeft = (int) dpToPx(mContext, 8);
        int infoSpaceTop = (int) dpToPx(mContext, 8);
        boolean isShow = (info != null && info.size() > 0) && (colors != null && colors.size() > 0);
        if (isShow) {
            for (int i = 0; i < info.size(); i++) {
                String str = info.get(i);
                //默认给个值为颜色的第一个
                int color = colors.get(0);
                if (colors.size() > i) {
                    color = colors.get(i);
                }
                infoPaint.setColor(color);
                infoPaint.setTextSize(coordinateTextSize);
                infoPaint.setAntiAlias(true);
                canvas.drawText(str, textSizeSum, infoSpaceTop, infoPaint);
                textSizeSum = textSizeSum + infoPaint.measureText(str) + infoSpaceLeft;
            }
        }
    }


    /**
     * 设置需要展示的信息和展示信息的颜色
     *
     * @param info  　需要展示的信息
     * @param color 　展示信息的颜色
     */
    public void setSomethingInfo(List<String> info, List<Integer> color) {

        if (info != null) {
            this.info = info;
        }

        if (color != null) {
            this.colors = color;
        }
    }


}
