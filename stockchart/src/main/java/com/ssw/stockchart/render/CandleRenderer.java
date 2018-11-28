package com.ssw.stockchart.render;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.ssw.stockchart.R;
import com.ssw.stockchart.data.KlineChartData;


/**
 * 蜡烛图渲染器
 *
 * @author saisai
 * @date 2018/6/11
 */
public class CandleRenderer extends BaseRenderer {

    /**
     * 文字画笔
     */
    private Paint textPaint = null;

    /**
     * 缺口画笔
     */
    private Paint qkPaint = null;
    /**
     * 蜡烛画笔
     */
    private Paint candlePaint = null;
    /**
     * 是否填充
     */
    private boolean isFill = true;

    /**
     * 涨时颜色
     */
    private int upColor;
    /**
     * 跌时颜色
     */
    private int downColor;
    /**
     * 不涨不跌颜色
     */
    private int evenColor;
    /**
     * 是否已经绘制最高价
     */
    private boolean isHadDrawHeightPrice = false;
    /**
     * 是否已经绘制最低价
     */
    private boolean isHadDrawLowPrice = false;
    /**
     * 绘制最高最低点的偏移量
     */
    private int mPriceOffset;
    /**
     * 默认缺口显示个数
     */
    private int queKou = 3;

    /**
     * 用来绘制蜡烛矩形
     */
    private RectF candleRectF;

    /**
     * 用来绘制缺口矩形
     */
    private RectF qkRect;

    /**
     * 当前显示个数一半
     */
    private int showMidNumber = 2;

    /**
     * queKou
     */
    private Paint.FontMetrics fm;

    /**
     * 向左的最高最低图
     */
    private Bitmap mKChartIconLeft;

    /**
     * 向右的最高最低图
     */
    private Bitmap mKChartIconRight;

    private int queKouPadding = 1;

    public CandleRenderer(boolean isFill, boolean isSubRender, int showType, @NonNull Context mContext) {
        super(isSubRender, showType, mContext);
        this.isFill = isFill;
        init();
    }

    @Override
    public void makeDrawData(int position) {
        drawIndex = position;
        showMidNumber = showNumbers / 2;

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

        //每次重绘重置最高最低点
        if (isHadDrawHeightPrice) {
            isHadDrawHeightPrice = false;
        }

        if (isHadDrawLowPrice) {
            isHadDrawLowPrice = false;
        }

    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        drawCandle(drawScreenIndex, canvas);
    }

    @Override
    public void onDraw(int drawStatIndex, int drawEndIndex, @NonNull Canvas canvas) {
        drawScreenIndex = drawIndex - drawStatIndex;
        //在这里绘制缺口
        //  calculationQk(drawStatIndex, drawEndIndex, canvas);
        super.onDraw(drawStatIndex, drawEndIndex, canvas);
    }

    @Override
    public void init() {
        mPriceOffset = (int)  dpToPx(mContext, 3);
        queKouPadding = 2;
        candleRectF = new RectF();
        qkRect = new RectF();

        mKChartIconLeft = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.hq_kchart_height_icon_left);
        mKChartIconRight = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.hq_kchart_height_icon_right);

        upColor = Color.RED;
        downColor = Color.GREEN;
        evenColor = Color.GRAY;

        candlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        candlePaint.setAntiAlias(true);
        candlePaint.setStrokeWidth(1f);
        candlePaint.setStyle(Paint.Style.FILL);
        candlePaint.setColor(Color.BLACK);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(1.5f);
        textPaint.setTextSize(dpToPx(mContext, 10));
        textPaint.setColor(Color.parseColor("#ff666666"));
        fm = new Paint.FontMetrics();
        textPaint.getFontMetrics(fm);

        qkPaint = new Paint();
        qkPaint.setAntiAlias(true);
        qkPaint.setStrokeWidth(1.5f);
        qkPaint.setColor(Color.parseColor("#ffdddddd"));
    }

    /**
     * 绘制蜡烛一根
     *
     * @param i      -
     * @param canvas -
     */
    private void drawCandle(int i, Canvas canvas) {

        KlineChartData vLineChartData = (KlineChartData) data;
        float closePrice = vLineChartData.getClosePriceByIndex(drawIndex);
        float openPrice = vLineChartData.getOpenPriceByIndex(drawIndex);
        float heightPrice = vLineChartData.getHeightPriceByIndex(drawIndex);
        float lowPrice = vLineChartData.getLowPriceByIndex(drawIndex);
        float zdf = vLineChartData.getZdfByIndex(drawIndex);

        //从收盘价与开盘价之间比较出最大值
        float maxPrice = closePrice >= openPrice ? closePrice : openPrice;
        //从收盘价与开盘价之间比较出最小值
        float minPrice = closePrice <= openPrice ? closePrice : openPrice;
        //防止画出坐标系外
        if (minPrice <= mainYMin) {
            minPrice = mainYMin;
        }
        //计算出蜡烛顶端尖尖的Y轴坐标
        float y1 = (1f - (heightPrice - mainYMin) / yPriceDifference) * yRectDifference;
        //计算出蜡烛顶端横线的Y轴坐标
        float y2 = (1f - (maxPrice - mainYMin) / yPriceDifference) * yRectDifference;
        //计算出蜡烛底端横线的Y轴坐标
        float y3 = (1f - (minPrice - mainYMin) / yPriceDifference) * yRectDifference;
        //计算出蜡烛底端尖尖的Y轴坐标
        float y4 = (1f - (lowPrice - mainYMin) / yPriceDifference) * yRectDifference;

        if (closePrice == openPrice) {
            if (zdf >= 0f) {
                candlePaint.setColor(upColor);
            } else {
                candlePaint.setColor(downColor);
            }
        } else if (closePrice > openPrice) {
            candlePaint.setColor(upColor);
        } else {
            candlePaint.setColor(downColor);
        }

        if (i == 0) {
            if (y2 == y3) {
                candleRectF.set(startX + paddingLeft, y2, startX + (i + 1) * cellWidth - space + paddingLeft, y3 + candlePaint.getStrokeWidth());
            } else {
                candleRectF.set(startX + paddingLeft, y2, startX + (i + 1) * cellWidth - space + paddingLeft, y3);
            }
        } else {
            if (y2 == y3) {
                candleRectF.set(startX + (i * cellWidth + space) + paddingLeft, y2, startX + (i + 1) * cellWidth - space + paddingLeft, y3 + candlePaint.getStrokeWidth());
            } else {
                candleRectF.set(startX + (i * cellWidth + space) + paddingLeft, y2, startX + (i + 1) * cellWidth - space + paddingLeft, y3);
            }
        }

        //画蜡烛的方块主干
        canvas.drawRect(candleRectF, candlePaint);
        //画蜡烛的上尖尖
        canvas.drawLine(startX + (i * cellWidth) + cellWidth / 2 + paddingLeft, y1, startX + (i * cellWidth) + cellWidth / 2 + paddingLeft, y4, candlePaint);

        //当前是最大数据
        if (heightPrice == yMaxFlow && !isHadDrawHeightPrice) {
            isHadDrawHeightPrice = true;
            drawMaxRect(canvas, i, heightPrice, startX + (i * cellWidth) + cellWidth / 2 + paddingLeft, y1);
        }

        //当前价格为最小价格
        if (lowPrice == yMinFlow && !isHadDrawLowPrice) {
            isHadDrawLowPrice = true;
            drawMinRect(canvas, i, lowPrice, startX + (i * cellWidth) + cellWidth / 2 + paddingLeft, y4);
        }
    }


    /**
     * 计算缺口
     *
     * @param drawCandleIndex -当前开始绘制的index
     * @param drawEnd         -当前屏幕绘制结束的index
     * @param canvas          -画布
     */
    private void calculationQk(int drawCandleIndex, int drawEnd, Canvas canvas) {

        if (data.dataSize() <= 1) {
            return;
        }

        int drawQkNum = 0;

        //这个记录图标的第几个触发缺口
        for (int i = drawEnd - 1; i >= drawCandleIndex; i--) {

            if ((i - 1) < 0) {
                return;
            }
            KlineChartData lineChartData = ((KlineChartData) data);
            float lastLowPrice = lineChartData.getLowPriceByIndex(i - 1);
            float nowLowPrice = lineChartData.getLowPriceByIndex(i);
            float lastHeightPrice = lineChartData.getHeightPriceByIndex(i - 1);
            float nowHeightPrice = lineChartData.getHeightPriceByIndex(i);

            //如果当前绘制的缺口数量大于3个，那么不再进行绘制
            if (drawQkNum >= queKou) {
                break;
            }

            //判断是否符合缺口条件 , 是否是做多缺口
            if (lastLowPrice > nowHeightPrice) {
                //当前的index位置符合缺口的条件，判断后面的所有蜡烛是否满足缺口条件
                boolean isLowQK = true;
                float qkMaxPrice = nowHeightPrice;
                for (int j = i; j < drawEnd; j++) {
                    //在取得当前的J序列下的K线bean
                    float nowJHeightPrice = lineChartData.getHeightPriceByIndex(j);
                    qkMaxPrice = qkMaxPrice > nowJHeightPrice ? qkMaxPrice : nowJHeightPrice;
                    if (lastLowPrice <= nowJHeightPrice) {
                        isLowQK = false;
                        break;
                    }
                }

                if (isLowQK) {
                    //证明当前的是缺口，否知则不满足
                    int drawIndex = i - drawCandleIndex;
                    float leftX = drawIndex * cellWidth + space + paddingLeft;
                    float rightX = xRectDifference;
                    float leftY = (1f - (lastLowPrice - mainYMin) / yPriceDifference) * xRectDifference;
                    float rightY = (1f - (qkMaxPrice - mainYMin) / yPriceDifference) * xRectDifference;
                    qkRect.set(leftX + startX, leftY + queKouPadding, rightX + startX, rightY - queKouPadding);
                    canvas.drawRect(qkRect, qkPaint);
                    drawQkNum++;
                }
                //是否是做空缺口
            } else if (lastHeightPrice < nowLowPrice) {

                boolean isLowQK = true;
                float qkMinPrice = nowLowPrice;
                for (int j = i; j < drawEnd; j++) {
                    //在取得当前的J序列下的K线bean
                    float nowJLowPrice = ((KlineChartData) data).getLowPriceByIndex(j);
                    qkMinPrice = qkMinPrice < nowJLowPrice ? qkMinPrice : nowJLowPrice;
                    if (lastHeightPrice >= nowJLowPrice) {
                        isLowQK = false;
                        break;
                    }
                }

                if (isLowQK) {
                    //证明当前的是缺口，否知则不满足
                    int drawIndex = i - drawCandleIndex;
                    float leftX = drawIndex * cellWidth + space + paddingLeft;
                    float rightX = xRectDifference;
                    float rightY = (1f - (lastHeightPrice - mainYMin) / yPriceDifference) * xRectDifference;
                    float leftY = (1f - (qkMinPrice - mainYMin) / yPriceDifference) * xRectDifference;
                    qkRect.set(leftX + startX, leftY + queKouPadding, rightX + startX, rightY - queKouPadding);
                    canvas.drawRect(qkRect, qkPaint);
                    drawQkNum++;
                }
            }
        }

    }

    /**
     * 画最大的点的指示
     *
     * @param index -当前的下标位置
     * @param price -当前价格
     * @param x     -X轴起始位置
     * @param y     -Y轴结束位置
     */
    @SuppressLint("DefaultLocale")
    private void drawMaxRect(Canvas canvas, int index, float price, float x, float y) {
        float textWidth = textPaint.measureText(price + "");
        float textHeight = Math.abs(fm.ascent);
        if (showMidNumber >= index) {
            //在左边
            canvas.drawBitmap(mKChartIconLeft, x, y - mKChartIconRight.getHeight() * 1.5f, textPaint);
            canvas.drawText(String.format("%.2f", price), x + mPriceOffset + mKChartIconRight.getWidth(), y - mKChartIconRight.getHeight() / 2, textPaint);
        } else {
            //在右边
            canvas.drawBitmap(mKChartIconRight, x - mKChartIconRight.getWidth(), y - mKChartIconRight.getHeight() * 1.5f, textPaint);
            canvas.drawText(String.format("%.2f", price), x - mKChartIconRight.getWidth() - mPriceOffset - textWidth, y - mKChartIconRight.getHeight() / 2, textPaint);
        }
    }

    /**
     * 画最小的点的指示
     *
     * @param index -当前的下标位置
     * @param price -当前价格
     * @param x     -X轴起始位置
     * @param y     -Y轴结束位置
     */
    @SuppressLint("DefaultLocale")
    private void drawMinRect(Canvas canvas, int index, float price, float x, float y) {
        float textWidth = textPaint.measureText(price + "");
        float textHeight = Math.abs(fm.ascent);

        if (showMidNumber >= index) {
            //在左边
            canvas.drawBitmap(mKChartIconLeft, x, y + mKChartIconRight.getHeight() / 2, textPaint);
            canvas.drawText(String.format("%.2f", price), x + mPriceOffset + mKChartIconLeft.getWidth(), y + textHeight * 1.0f, textPaint);
        } else {
            //在右边
            canvas.drawBitmap(mKChartIconRight, x - mKChartIconRight.getWidth(), y + mKChartIconRight.getHeight() / 2, textPaint);
            canvas.drawText(String.format("%.2f", price), x - mKChartIconRight.getWidth() - mPriceOffset - textWidth, y + textHeight, textPaint);
        }
    }

    public void setQueKouColor(int queKouColor) {
        this.qkPaint.setColor(queKouColor);
    }
}
