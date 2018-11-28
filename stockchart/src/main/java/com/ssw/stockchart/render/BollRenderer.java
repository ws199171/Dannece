package com.ssw.stockchart.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.NonNull;

import com.ssw.stockchart.data.KlineChartData;


/**
 * 副视图布尔线渲染器
 *
 * @author saisai
 * @date 2018/6/12
 */
public class BollRenderer extends BaseRenderer {

    /**
     * 实心画笔
     */
    private Paint fillPaint = null;

    /**
     * 外壳画笔
     */
    private Paint strokePaint = null;

    /**
     * 最低价位置
     */
    private PointF lowRightBottomPoint;

    /**
     * 最高价位置
     */
    private PointF highLeftTopPoint;

    /**
     * 开盘价位置
     */
    private PointF openPoint;

    /**
     * 计算收盘价位置
     */
    private PointF closePoint;


    public BollRenderer(boolean isSubRender, int showType, @NonNull Context mContext) {
        super(isSubRender, showType, mContext);
        init();
    }

    @Override
    public void makeDrawData(int position) {
        yPriceDifference = subYMax - subYMin;
        if (isSubRender) {
            effectiveHeight = mSubRect.bottom - mSubRect.top;
            yRectDifference = (effectiveHeight - maHeight);
        } else {
            effectiveHeight = mMainRect.bottom - mMainRect.top;
            yRectDifference = (effectiveHeight - maHeight);
        }

        getOpenLeftTopPoint(openPoint, position);
        getCloseRightBottomPoint(closePoint, position);
        getHighLeftTopPoint(highLeftTopPoint, position);
        getLowRightBottomPoint(lowRightBottomPoint, position);
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        //画最高最低线
        canvas.drawLine(highLeftTopPoint.x, highLeftTopPoint.y, lowRightBottomPoint.x, lowRightBottomPoint.y, fillPaint);
        //画开盘价线
        canvas.drawLine(openPoint.x, openPoint.y, openPoint.x + cellWidth / 4, openPoint.y, fillPaint);
        //画收盘价线
        canvas.drawLine(closePoint.x - cellWidth / 4, closePoint.y, closePoint.x, closePoint.y, fillPaint);
    }

    @Override
    public void init() {

        //初始化线画笔
        this.fillPaint = new Paint();
        this.fillPaint.setAntiAlias(true);
        this.fillPaint.setStrokeWidth(2f);
        this.fillPaint.setColor(Color.parseColor("#1199EE"));

        this.strokePaint = new Paint();
        this.strokePaint.setAntiAlias(true);
        this.strokePaint.setStrokeWidth(0.3f);
        this.strokePaint.setColor(Color.parseColor("#1199EE"));
        this.strokePaint.setStyle(Paint.Style.STROKE);

        lowRightBottomPoint = new PointF();
        highLeftTopPoint = new PointF();
        openPoint = new PointF();
        closePoint = new PointF();

    }


    private void getOpenLeftTopPoint(PointF pointF, int index) {
        float x = index * cellWidth + cellWidth / 4 + paddingLeft;
        float y = (1f - (((KlineChartData) data).getOpenPriceByIndex(index) - subYMin) / yPriceDifference) * yRectDifference + maHeight;
        if (isSubRender) {
            pointF.set(x + startX, y + mSubRect.top);
        } else {
            pointF.set(x + startX, y);
        }
    }

    private void getCloseRightBottomPoint(PointF pointF, int index) {
        float x = index * cellWidth + cellWidth / 4 * 3 + paddingLeft;
        float y = (1f - ((((KlineChartData) data).getClosePriceByIndex(index) - subYMin) / yPriceDifference)) * yRectDifference + maHeight;
        if (isSubRender) {
            pointF.set(x + startX, y + mSubRect.top);
        } else {
            pointF.set(x + startX, y);
        }
    }

    private void getHighLeftTopPoint(PointF pointF, int index) {
        float x = index * cellWidth + cellWidth / 2 + paddingLeft;
        float y = (1f - ((((KlineChartData) data).getHeightPriceByIndex(index) - subYMin) / yPriceDifference)) * yRectDifference + maHeight;
        if (isSubRender) {
            pointF.set(x + startX, y + mSubRect.top);
        } else {
            pointF.set(x + startX, y);
        }

    }

    private void getLowRightBottomPoint(PointF pointF, int index) {
        float x = index * cellWidth + cellWidth / 2 + paddingLeft;
        float y = (1f - ((((KlineChartData) data).getLowPriceByIndex(index) - subYMin) / yPriceDifference)) * yRectDifference + maHeight;
        if (isSubRender) {
            pointF.set(x + startX, y + mSubRect.top);
        } else {
            pointF.set(x + startX, y);
        }
    }


}
