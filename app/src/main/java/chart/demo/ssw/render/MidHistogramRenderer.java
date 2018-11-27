package chart.demo.ssw.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

/**
 * 从图中间开始画的柱状图
 *
 * @author saisai
 * @date 2018/6/11
 */
public class MidHistogramRenderer extends BaseRenderer {

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
     * 柱之间间隙
     */
    private float space = 0;

    /**
     *
     */
    private RectF rect;

    /**
     * 当前在屏幕上的位置
     */
    private int drawScreenIndex = 0;

    public MidHistogramRenderer(boolean isSubRender, int showType, @NonNull Context mContext) {
        super(isSubRender, showType, mContext);
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

        float isUp = data.getTurnoverUpByIndex(drawIndex);
        //设置蜡烛的颜色
        if (isUp >= 0) {
            fillPaint.setColor(upColor);
        } else if (isUp < 0) {
            fillPaint.setColor(downColor);
        } else {
            fillPaint.setColor(evenColor);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas) {
        //绘制矩形
        canvas.drawRect(rectFSet(drawScreenIndex, this.cellWidth, data.getTurnoverUpByIndex(drawIndex)), fillPaint);
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

        float leftX;

        //如果是第一条矩形
        if (index == 0) {
            leftX = paddingLeft;
        } else {
            leftX = index * histogramWidth + space + paddingLeft;
        }
        float rightX = (index + 1) * histogramWidth - space + paddingLeft;

        float leftY;
        float rightY;

        if (isUp > 0) {
            leftY = effectiveHeight / 2 - (data.getTurnoverByIndex(drawIndex) / yMax) * (effectiveHeight / 6);
            rightY = effectiveHeight / 2;

            if (Math.abs(leftY - rightY) < 2) {
                leftY = leftY - 2;
            }

        } else {
            leftY = effectiveHeight / 2;
            rightY = effectiveHeight / 2 + (data.getTurnoverByIndex(drawIndex) / yMax) * (effectiveHeight / 6);

            if (Math.abs(leftY - rightY) < 2) {
                leftY = leftY + 2;
            }
        }
        rect.set(leftX + startX, leftY, rightX, rightY);

        return rect;
    }
}
