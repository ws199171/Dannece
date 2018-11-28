package com.ssw.stockchart.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import java.util.List;


/**
 * X轴方向的渲染器
 *
 * @author saisai
 * @date 2018/6/11
 */
public class XAxisRenderer extends BaseRenderer {

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
    public static int marginBottom = 30;
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

    public XAxisRenderer(boolean isSubRender, int showType, @NonNull Context mContext) {
        super(isSubRender, showType, mContext);
        init();
    }

    @Override
    public void makeDrawData(int position) {
        if (isSubRender) {
            yPriceDifference = subYMax - subYMin;
            effectiveHeight = mSubRect.bottom - mSubRect.top;
            yRectDifference = (effectiveHeight - maHeight);
            effectiveWeight = mSubRect.right - mSubRect.left;
            xRectDifference = effectiveWeight - paddingLeft;
        } else {
            yPriceDifference = mainYMax - mainYMin;
            effectiveHeight = mMainRect.bottom - mMainRect.top;
            yRectDifference = (effectiveHeight - maHeight);
            effectiveWeight = mMainRect.right - mMainRect.left;
            xRectDifference = effectiveWeight - paddingLeft;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        if (showType == TIME_FIVE_DAY
                || showType == TIME_HK_DAY
                || showType == TIME_GGT_DAY
                || showType == CONTRAST_VIEW
                || showType == TIME_DAY
                || showType == RONG_VIEW) {
            drawTimeLongitude(canvas);
        } else {
            drawKLinesLongitude(canvas);
        }
    }

    @Override
    public void init() {

        marginBottom = (int) dpToPx(mContext, 15);
        backgroundRect = new RectF();

        //初始化经线画笔
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(mCoordinatesLineColor == 0 ? 0xFFCCCCCC : mCoordinatesLineColor);
        mLinePaint.setStrokeWidth(1f);

        //初始化虚线画笔
        mDashLinePaint = new Paint();
        mDashLinePaint.setStyle(Paint.Style.FILL);
        mDashLinePaint.setColor(mCoordinatesLineColor == 0 ? 0xFFCCCCCC : mCoordinatesLineColor);
        mDashLinePaint.setStrokeWidth(1f);
        mDashLinePaint.setPathEffect(new DashPathEffect(new float[]{4, 4, 4, 4}, 4));

        //初始化左边文字画笔
        leftTextPaint = new Paint();
        leftTextPaint.setTextSize(coordinateTextSize);
        leftTextPaint.setAntiAlias(true);
        leftTextPaint.setColor(0xFF666666);
        //初始化右边文字画笔
        rightTextPaint = new Paint();
        rightTextPaint.setTextSize(coordinateTextSize);
        rightTextPaint.setAntiAlias(true);
        rightTextPaint.setColor(0xFF666666);
        //初始化底部文字画笔
        bottomTextPaint = new Paint();
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
     * 绘制分时，五日经线时使用
     *
     * @param canvas -
     */
    private void drawTimeLongitude(Canvas canvas) {
        mLinePaint.setStrokeWidth(1f);
        //经线总条数
        int longitudeNumber = data.getBottomScaleTextArraySize();
        //经线间的宽度
        float longitudeSpace = xRectDifference / (longitudeNumber - 1);
        //这里的0.0588的计算公式 = 0.5 - 15 / 34
        float ggtSpace = xRectDifference * 0.0588f;
        Paint.FontMetrics fm = new Paint.FontMetrics();
        for (int i = 0; i < longitudeNumber; i++) {
            //底部刻度文字
            String bottomScale = data.getBottomScaleTextArrayByIndexByIndex(i);
            //文字宽度
            float scaleWidth = bottomTextPaint.measureText(bottomScale);
            bottomTextPaint.getFontMetrics(fm);
            //文字高度
            float scaleHeight = Math.abs(fm.ascent);
            //第一条经线
            if (i == 0) {
                //五日分时
                if (showType == TIME_FIVE_DAY) {
                    float tempLeft = longitudeSpace * i + longitudeSpace / 2 - scaleWidth / 2 + paddingLeft;
                    canvas.drawText(bottomScale, tempLeft, effectiveHeight + marginBottom / 2 + scaleHeight / 2, bottomTextPaint);
                } else {
                    canvas.drawText(bottomScale, space + paddingLeft, effectiveHeight + marginBottom / 2 + scaleHeight / 2, bottomTextPaint);
                }
            }
            //最后一条经线
            else if (i == longitudeNumber - 1) {
                //五日分时
                if (showType == TIME_FIVE_DAY) {
                    float tempLeft = longitudeSpace * i + longitudeSpace / 2 - scaleWidth / 2 + paddingLeft;
                    canvas.drawText(bottomScale, tempLeft, effectiveHeight + marginBottom / 2 + scaleHeight / 2, bottomTextPaint);
                } else {
                    canvas.drawText(bottomScale, effectiveWeight - scaleWidth - space, effectiveHeight + marginBottom / 2 + scaleHeight / 2, bottomTextPaint);
                }
            } else {//其中所有的经线
                //画经线
                //经线间隙,i此时应从1开始,因为第一个if屏蔽了
                float tempLongitudeSpace = i * longitudeSpace;
                if (showType == TIME_GGT_DAY || showType == TIME_HK_DAY) {
                    //如果是港股通
                    if (i == (longitudeNumber - 1) / 2) {
                        tempLongitudeSpace = i * longitudeSpace - ggtSpace;
                    } else {
                        tempLongitudeSpace = i * longitudeSpace - ggtSpace * 0.5f;
                    }
                    canvas.drawLine(tempLongitudeSpace - mLinePaint.getStrokeWidth() + paddingLeft, 0, tempLongitudeSpace - mLinePaint.getStrokeWidth() + paddingLeft, effectiveHeight, mLinePaint);
                } else {
                    canvas.drawLine(tempLongitudeSpace - mLinePaint.getStrokeWidth() + paddingLeft, 0, tempLongitudeSpace - mLinePaint.getStrokeWidth() + paddingLeft, effectiveHeight, mLinePaint);
                }

                //画经线刻度
                //五日分时
                if (showType == TIME_FIVE_DAY) {
                    float tempLeft = longitudeSpace * i + longitudeSpace / 2 - scaleWidth / 2 + paddingLeft;
                    canvas.drawText(bottomScale, tempLeft, effectiveHeight + marginBottom / 2 + scaleHeight / 2, bottomTextPaint);
                } else {
                    canvas.drawText(bottomScale, tempLongitudeSpace - scaleWidth / 2 + paddingLeft, effectiveHeight + marginBottom / 2 + scaleHeight / 2, bottomTextPaint);
                }
            }
        }
    }


    /**
     * 绘制K线时使用
     *
     * @param canvas -
     */
    private void drawKLinesLongitude(Canvas canvas) {

        //如果下部的坐标数组为空，那么返回
        if (data.getBottomScaleTextIndexArraySize() == 0) {
            return;
        }

        if (showType == RESTRICTED_VIEW) {
            if (data.getBottomScaleTextArraySize() > 0) {
                drawOthersLongitudeWithText(canvas, false);
            }
            return;
        }

        boolean isDrawText = true;

        if (showType != K_DAY) {
            isDrawText = false;
        }

        if (data.getBottomScaleTextArraySize() > 0) {
            drawKLinesLongitudeWithText(canvas, isDrawText);
        }

    }


    /**
     * 绘制K线时使用
     *
     * @param canvas -
     */
    private void drawOthersLongitudeWithText(Canvas canvas, boolean isDrawLine) {
        mLinePaint.setStrokeWidth(1f);
        Paint.FontMetrics fm = new Paint.FontMetrics();
        bottomTextPaint.getFontMetrics(fm);
        //获取需要绘制的原型的文字高度
        float scaleHeight = Math.abs(fm.ascent);
        //获取需要绘制的原型的文字宽度
        float scaleWidth = bottomTextPaint.measureText(data.getBottomScaleTextArrayByIndexByIndex(0));
        //最后一个绘制点的位置
        float lastDraw = 0f;
        //每条K线所占有的长度
        float argWith = cellWidth;
        for (int i = 0; i < data.getBottomScaleTextArraySize(); i++) {
            float distance = data.getBottomScaleTextIndexArrayByIndexByIndex(i) * argWith + argWith / 2 + startX;
            if (((distance - scaleWidth / 2) >= 0) && ((effectiveWeight - distance - paddingLeft - scaleWidth / 2) >= 0)) {
                //如果当前点的坐标的位置，距离上次绘制的距离大于 ，绘制文字的宽度，那么这个点就可以绘制
                canvas.drawText(data.getBottomScaleTextArrayByIndexByIndex(i), distance - scaleWidth / 2 + paddingLeft, effectiveWeight + marginBottom / 2 + scaleHeight / 2, bottomTextPaint);
                if (isDrawLine) {
                    canvas.drawLine(distance + paddingLeft, 0, distance + paddingLeft, effectiveHeight, mLinePaint);
                }
                //更新最后绘制点位置
                lastDraw = distance;
            }
        }
    }


    /**
     * 绘制经线，并且带文字
     *
     * @param canvas     -
     * @param isDrawText -
     */
    private void drawKLinesLongitudeWithText(Canvas canvas, boolean isDrawText) {
        mLinePaint.setStrokeWidth(1f);
        Paint.FontMetrics fm = new Paint.FontMetrics();
        bottomTextPaint.getFontMetrics(fm);
        //获取需要绘制的原型的文字高度
        float scaleHeight = Math.abs(fm.ascent);
        //获取需要绘制的原型的文字宽度
        float scaleWidth = bottomTextPaint.measureText(data.getBottomScaleTextArrayByIndexByIndex(0));
        //最后一个绘制点的位置
        float lastDraw = 0f;
        //每条K线所占有的长度
        float echoWith = cellWidth;
        int size = data.getBottomScaleTextArraySize();
        //是否已经绘制了最后一条
        boolean isDrawLast = false;
        for (int i = 0; i < data.getBottomScaleTextIndexArraySize(); i++) {
            if (!isDrawLast && data.getBottomScaleTextIndexArrayByIndexByIndex(size - 1) * echoWith < scaleWidth) {
                if (isDrawText) {
                    canvas.drawText(data.getBottomScaleTextArrayByIndexByIndex(i), paddingLeft + bottomTextPaint.getStrokeWidth(), effectiveHeight + marginBottom / 2 + scaleHeight / 2, bottomTextPaint);
                    isDrawLast = true;
                }
                lastDraw = data.getBottomScaleTextIndexArrayByIndexByIndex(size - 1) * echoWith;
                continue;
            }
            float distance = data.getBottomScaleTextIndexArrayByIndexByIndex(i) * echoWith + echoWith / 2 + startX;
            if (((distance - scaleWidth / 2) >= 0) && ((effectiveWeight - distance - paddingLeft - scaleWidth / 2) >= 0)) {
                if (lastDraw == 0) {
                    if ((distance - lastDraw) >= scaleWidth * 0.5) {
                        //如果当前点的坐标的位置，距离上次绘制的距离大于 ，绘制文字的宽度，那么这个点就可以绘制
                        canvas.drawLine(distance + paddingLeft, 0, distance + paddingLeft, effectiveHeight, mLinePaint);

                        if (isDrawText) {
                            canvas.drawText(data.getBottomScaleTextArrayByIndexByIndex(i), distance - scaleWidth / 2 + paddingLeft, effectiveHeight + marginBottom / 2 + scaleHeight / 2, bottomTextPaint);
                        }
                        //更新最后绘制点位置
                        lastDraw = distance;
                    }
                } else {
                    if ((distance - lastDraw) >= scaleWidth * 1.5) {
                        //如果当前点的坐标的位置，距离上次绘制的距离大于 ，绘制文字的宽度，那么这个点就可以绘制
                        canvas.drawLine(distance + paddingLeft, 0, distance + paddingLeft, effectiveHeight, mLinePaint);
                        if (isDrawText) {
                            canvas.drawText(data.getBottomScaleTextArrayByIndexByIndex(i), distance - scaleWidth / 2 + paddingLeft, effectiveHeight + marginBottom / 2 + scaleHeight / 2, bottomTextPaint);
                        }
                        //更新最后绘制点位置
                        lastDraw = distance;
                    }
                }
            }
        }
    }

}
