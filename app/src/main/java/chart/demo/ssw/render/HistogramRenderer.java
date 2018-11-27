package chart.demo.ssw.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * 柱状图渲染器 （成交量，MACD）
 *
 * @author saisai
 * @date 2018/6/11
 */
public class HistogramRenderer extends BaseRenderer {

    /**
     * 实心画笔
     */
    private Paint fillPaint = null;
    /**
     * 外壳画笔
     */
    private Paint strokePaint = null;
    /**
     * 文字颜色
     */
    private Paint textPaint = null;
    /**
     * 是否填充
     */
    private boolean isFill = true;
    /**
     * 涨时颜色
     */
    private int upColor = Color.parseColor("#ff322e");
    /**
     * 跌时颜色
     */
    private int downColor = Color.parseColor("#2eff2e");
    /**
     * 不涨不跌颜色
     */
    private int evenColor = Color.parseColor("#656565");
    /**
     * 文字颜色
     */
    private int textColor = Color.parseColor("#666666");
    /**
     *
     */
    private RectF rect;

    /**
     * 绘制柱状图是否和正常的相反是翻转的
     */
    private boolean isFlipOver = false;

    public HistogramRenderer(boolean isFlipOver, boolean isSubRender, int showType, @NonNull Context mContext) {
        super(isSubRender, showType, mContext);
        this.isFlipOver = isFlipOver;
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

        float up = data.getTurnoverUpByIndex(position);
        //设置蜡烛的颜色
        if (up >= 0) {
            fillPaint.setColor(upColor);
        } else {
            fillPaint.setColor(downColor);
        }

    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        //绘制矩形
        if (isFlipOver) {
            canvas.drawRect(rectFSetByOver(drawScreenIndex, this.cellWidth), fillPaint);
        } else {
            canvas.drawRect(rectFSet(drawScreenIndex, this.cellWidth, data.getTurnoverUpByIndex(drawIndex)), fillPaint);
        }
    }

    @Override
    public void onDraw(int drawStatIndex, int drawEndIndex, @NonNull Canvas canvas) {
        drawScreenIndex = drawIndex - drawStatIndex;
        super.onDraw(drawStatIndex, drawEndIndex, canvas);
    }

    @Override
    public void init() {
        //初始化线画笔
        this.fillPaint = new Paint();
        this.fillPaint.setAntiAlias(true);
        this.fillPaint.setStrokeWidth(1.0f);
        this.fillPaint.setColor(Color.GRAY);
        this.fillPaint.setStyle(Paint.Style.FILL);

        this.strokePaint = new Paint();
        this.strokePaint.setAntiAlias(true);
        this.strokePaint.setStrokeWidth(0.3f);
        this.strokePaint.setColor(Color.GRAY);
        this.strokePaint.setStyle(Paint.Style.STROKE);


        this.textPaint = new Paint();
        this.textPaint.setStrokeWidth(2f);
        this.textPaint.setTextSize(26);
        this.textPaint.setColor(textColor);

        this.rect = new RectF();
    }


    /**
     * 获取到绘制的矩形
     *
     * @param index          -
     * @param histogramWidth -
     * @return -
     */
    private RectF rectFSet(int index, float histogramWidth, double isUp) {

        float turnover = data.getTurnoverByIndex(drawIndex);

        float leftX;

        //如果是第一条矩形
        if (index == 0) {
            leftX = paddingLeft;
        } else {
            leftX = index * histogramWidth + space + paddingLeft;
        }

        float leftY;

        if (showType == MACD) {
            leftY = (1f - (turnover - yMin) / yPriceDifference) * effectiveHeight;
        } else {
            //如果显示MA数据，需要在顶部流出MA的高度
            leftY = (1f - (turnover - yMin) / yPriceDifference) * (effectiveHeight - maHeight) + maHeight;
        }

        float rightX = (index + 1) * histogramWidth - space + paddingLeft;

        float rightY;

        if (showType == MACD) {
            rightY = effectiveHeight / 2;
        } else {
            rightY = effectiveHeight;
        }

        //当绘制MACD时需要进行坐标转换，讲左右两点的横坐标进行互换，
        //因为在绘制MACD时，当进行绿色矩形绘制时，放进rect 里面的坐标不是标准的坐标，需要进行转换
        if (showType == MACD) {
            if (isUp < 0) {
                float temp = leftY;
                leftY = rightY;
                rightY = temp;
            }

            //添加一个最小像素，如果柱子的像素太小
            if (Math.abs(rightY - leftY) <= 2) {
                if (isUp < 0) {
                    rightY = leftY + 2;
                } else {
                    leftY = rightY - 2;
                }
            }
        }

        if (showType != MACD) {
            if (turnover > 0) {
                //添加一个最小像素，如果柱子的像素太小
                if ((rightY - leftY) < 2) {
                    leftY = rightY - 2;
                }
            }
        }

        rect.set(leftX + startX, leftY + mSubRect.top, rightX + startX, rightY + mSubRect.top);

        return rect;
    }

    /**
     * 获取到绘制的矩形
     *
     * @param index          -
     * @param histogramWidth -
     * @return -
     */
    private RectF rectFSetByOver(int index, float histogramWidth) {

        float turnover = data.getTurnoverByIndex(drawIndex);

        float leftX;

        //如果是第一条矩形
        if (index == 0) {
            leftX = paddingLeft;
        } else {
            leftX = index * histogramWidth + space + paddingLeft;
        }

        float leftY = 0f;

        float rightX;

        rightX = (index + 1) * histogramWidth - space + paddingLeft;

        float rightY = (turnover - yMax) / yPriceDifference * effectiveHeight;

        //添加一个最小像素，如果柱子的像素太小
        if ((rightY - leftY) < 4) {
            rightY = 4;
        }

        rect.set(leftX + startX, leftY, rightX + startX, rightY);

        return rect;
    }


}
