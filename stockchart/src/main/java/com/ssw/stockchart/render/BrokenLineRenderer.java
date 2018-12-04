package com.ssw.stockchart.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.NonNull;

/**
 * 折线图渲染器
 *
 * @author saisai
 * @date 2018/6/11
 */
public class BrokenLineRenderer extends BaseRenderer {


    /**
     * 线画笔
     */
    private Paint linePaint = null;
    /**
     * 背景色画笔
     */
    private Paint backgroundPaint = null;
    /**
     * 是否填充
     */
    private boolean isFill = false;


    /**
     * 渐变开始颜色
     */
    private int startColor = Color.WHITE;
    /**
     * 渐变结束颜色
     */
    private int endColor = Color.BLACK;
    /**
     * 线颜色
     */
    private int lineColor = Color.BLACK;

    /**
     * 带阴影的path路径
     */
    private Path backgroundPath;

    /**
     * 线段路径
     */
    private Path linePath;

    /**
     * 路径上的某个点
     */
    private PointF point;

    /**
     * 当前是否是第一次绘制
     */
    private boolean isFirstDraw = true;

    /**
     * 获取当前需要绘制的数据接口
     */
    private BindData bindData;

    /**
     * 当前绘制的最后一个点
     */
    private int drawEndIndex = -1;

    public BrokenLineRenderer(@NonNull BindData bindData, int lineColor, boolean isFill, boolean isSubRender, int showType, @NonNull Context mContext) {
        super(isSubRender, showType, mContext);
        this.isFill = isFill;
        this.bindData = bindData;
        this.lineColor = lineColor;
        init();
    }

    @Override
    public void makeDrawData(int position) {
        drawIndex = position;

        if (isSubRender) {
            yMin = subYMin;
            yMax = subYMax;
            yPriceDifference = subYMax - subYMin;
            effectiveHeight = mSubRect.bottom - mSubRect.top;
            yRectDifference = (effectiveHeight - maHeight);
            effectiveWeight = mSubRect.right - mSubRect.left;
            xRectDifference = effectiveWeight - paddingLeft;
        } else {
            yMin = mainYMin;
            yMax = mainYMax;
            yPriceDifference = mainYMax - mainYMin;
            effectiveHeight = mMainRect.bottom - mMainRect.top;
            yRectDifference = (effectiveHeight - maHeight);
            effectiveWeight = mMainRect.right - mMainRect.left;
            xRectDifference = effectiveWeight - paddingLeft;
        }

    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {

        if (isFirstDraw) {
            backgroundPath.reset();
            linePath.reset();
            if (isFill) {
                backgroundPath.moveTo(paddingLeft, effectiveWeight);
            }
        }

        float drawData = bindData == null ? 0 : bindData.getDrawDataByIndex(drawIndex);

        if (showType == TIME_DAY || showType == TIME_FIVE_DAY || showType == TIME_HK_DAY || showType == TIME_GGT_DAY || showType == CONTRAST_VIEW) {
            //分时，五日，股票对比，港股、港股通分时，不需要绘制0
            if (Float.isNaN(drawData) || drawData == 0 || drawData == Float.MIN_VALUE) {
                return;
            }
        } else {
            //指标均线需要绘制0数据
            if (Float.isNaN(drawData) || drawData == Float.MIN_VALUE) {
                return;
            }
        }

        getCoordinatePointFloat(point, drawData, drawScreenIndex);

        //如果第一次画,就把第一次作为起始点
        if (isFirstDraw) {
            linePath.moveTo(point.x, point.y);
        } else {
            linePath.lineTo(point.x, point.y);
        }
        if (isFill) {
            backgroundPath.lineTo(point.x, point.y);
        }

        //背景
        if (isFill) {
            backgroundPath.lineTo(point.x, effectiveWeight);
            backgroundPath.close();
            canvas.drawPath(backgroundPath, backgroundPaint);
        }

        //画线
        if (this.drawEndIndex != -1 && (this.drawEndIndex - 1) == drawIndex) {
            canvas.drawPath(linePath, linePaint);
        }

        if (isFirstDraw) {
            isFirstDraw = false;
        }
    }

    public void resetFirstDraw() {
        this.isFirstDraw = true;
    }

    @Override
    public void onDraw(int drawStatIndex, int drawEndIndex, @NonNull Canvas canvas) {
        isFirstDraw = drawStatIndex == drawIndex;
        drawScreenIndex = drawIndex - drawStatIndex;
        this.drawEndIndex = drawEndIndex;
        super.onDraw(drawStatIndex, drawEndIndex, canvas);
    }

    @Override
    public void init() {

        this.point = new PointF();
        this.backgroundPath = new Path();
        this.linePath = new Path();
        //初始化线画笔
        this.linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.linePaint.setAntiAlias(true);
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.linePaint.setStrokeWidth(1);
        this.linePaint.setColor(lineColor);
        //初始化背景画笔
        this.backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.backgroundPaint.setColor(Color.WHITE);
        this.backgroundPaint.setStyle(Paint.Style.FILL);
    }

    /**
     * 得到坐标系中点
     *
     * @param i 屏幕上的点的个数下标
     */
    private void getCoordinatePointFloat(PointF pointF, float str, int i) {

        float x;
        float y;
        if (showType == TIME_DAY || showType == TIME_FIVE_DAY || showType == TIME_HK_DAY || showType == TIME_GGT_DAY) {
            x = i * cellWidth + cellWidth / 2 + paddingLeft;
            y = (1f - (str - yMin) / yPriceDifference) * effectiveHeight;
        } else if (showType == CONTRAST_VIEW) {
            x = i * cellWidth + paddingLeft;
            y = (1f - (str - yMin) / yPriceDifference) * effectiveHeight;
        } else {
            x = i * cellWidth + cellWidth / 2 + paddingLeft;
            //如果是指标类型的折线
            y = (1f - (str - yMin) / yPriceDifference) * (effectiveHeight - maHeight) + maHeight;
        }

        if (isSubRender) {
            pointF.set(x + startX, y + mSubRect.top);
        } else {
            pointF.set(x + startX, y);
        }
    }
}
