package chart.demo.ssw.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import chart.demo.ssw.data.BaseCharData;


/**
 * 渲染器基础类
 *
 * @author saisai
 * @date 2018/6/11
 */
public abstract class BaseRenderer<T extends BaseCharData> {

    /**
     * 绘制分时
     */
    public static final int TIME_DAY = 1;
    /**
     * 绘制K线
     */
    public static final int K_DAY = 2;
    /**
     * 绘制五日
     */
    public static final int TIME_FIVE_DAY = 3;
    /**
     * 绘制港股分时
     */
    public static final int TIME_HK_DAY = 4;

    /**
     * 绘制港股通分时
     */
    public static final int TIME_GGT_DAY = 5;

    /**
     * 绘制分时的成交量
     */
    public static final int TIME_VOLUME = 6;
    /**
     * 对比线类型
     */
    public static final int CONTRAST_VIEW = 7;
    /**
     * 融资融券类型
     */
    public static final int RONG_VIEW = 8;
    /**
     * 限售解禁类型
     */
    public static final int RESTRICTED_VIEW = 9;
    /**
     * 成家量
     */
    public static final int VOLUME = 10;
    /**
     * KDJ指标
     */
    public static final int KDJ = 11;
    /**
     * MACD指标
     */
    public static final int MACD = 12;
    /**
     * DMI指标
     */
    public static final int DMI = 13;
    /**
     * WR指标
     */
    public static final int WR = 14;
    /**
     * OBV指标
     */
    public static final int OBV = 15;
    /**
     * BOLL指标
     */
    public static final int BOLL = 16;
    /**
     * RSI指标
     */
    public static final int RSI = 17;
    /**
     * ROC指标
     */
    public static final int ROC = 18;
    /**
     * VR指标
     */
    public static final int VR = 19;
    /**
     * CR指标
     */
    public static final int CR = 20;
    /**
     * BIAS指标
     */
    public static final int BIAS = 21;
    /**
     * ASI指标
     */
    public static final int ASI = 22;
    /**
     * CCI指标
     */
    public static final int CCI = 23;
    /**
     * PSY指标
     */
    public static final int PSY = 24;

    /**
     * 上下文
     */
    protected Context mContext;

    /**
     * 需要绘制的数据
     */
    protected T data;

    /**
     * 主视图所在的矩形
     */
    protected RectF mMainRect;

    /**
     * 副视图所在的矩形
     */
    protected RectF mSubRect;

    /**
     * 每个单位的宽度
     */
    protected float cellWidth = 0;

    /**
     * 每个单位的空隙
     */
    protected float space = 0;

    /**
     * 对左边边框间距
     */
    protected float paddingLeft = 0;

    /**
     * 绘画的起始坐标，比如最左边的K线坐标
     */
    protected float startX = 0.0f;

    /**
     * 坐标系主图的最大值
     */
    protected float mainYMax = 0;

    /**
     * 坐标系主图的最小值
     */
    protected float mainYMin = 0;

    /**
     * 坐标系的最大值
     */
    protected float subYMax = 0;

    /**
     * 坐标系的最小值
     */
    protected float subYMin = 0;

    /**
     * 给MA或者其他预留的高度
     */
    protected float maHeight = 0f;

    /**
     * 默认显示个数
     */
    protected int showNumbers = 0;

    /**
     * 坐标系线条颜色
     */
    protected int mCoordinatesLineColor;
    /**
     * 十字线线条色
     */
    protected int mCrossLineColor;
    /**
     * 十字线标签背景色
     */
    protected int mCrossBgColor;
    /**
     * 十字线标签文字色
     */
    protected int mCrossTvColor;

    /**
     * 价格最大最下差值
     */
    protected float yPriceDifference;

    /**
     * 有效绘制区域高低直间差值
     */
    protected float yRectDifference;

    /**
     * Y轴绘制的高度
     */
    protected float effectiveHeight;

    /**
     * X轴绘制的宽度
     */
    protected float effectiveWeight;

    /**
     * X轴有效的绘制宽度
     */
    protected float xRectDifference;

    /**
     * 设置坐标系最大值真实
     */
    protected float yMaxFlow = 0;

    /**
     * 设置坐标系最小值真实值
     */
    protected float yMinFlow = 0;

    /**
     * 股票类型
     */
    protected int stockType = 0;

    /**
     * 当前绘制的位置
     */
    protected int drawIndex = 0;

    /**
     * 当前是主视图还是副视图，默认主视图
     */
    protected boolean isSubRender = false;

    /**
     * 当前展示的图表类型
     */
    protected int showType = TIME_DAY;

    /**
     * 当前是横屏还是竖屏
     */
    protected boolean isHorizontal;

    /**
     * 当前有效的最小数值
     */
    protected float yMin = 0;

    /**
     * 当前有效的最大数值
     */
    protected float yMax = 0;

    /**
     * 当前在屏幕上的位置
     */
    protected int drawScreenIndex = 0;


    public interface BindData {

        /**
         * 获取当前绑定的下标数据
         *
         * @param index - 下标
         * @return - 数据
         */
        float getDrawDataByIndex(int index);
    }


    public BaseRenderer(boolean isSubRender, int showType, @NonNull Context mContext) {
        this.isSubRender = isSubRender;
        this.showType = showType;
        this.mContext = mContext;
    }

    /**
     * 获取到当前需要需要的数据
     *
     * @param position -
     */
    public abstract void makeDrawData(int position);

    /**
     * 进行绘制当前的逻辑
     *
     * @param canvas - 画布
     */
    public abstract void onDraw(@NonNull Canvas canvas);


    /**
     * 进行绘制当前的逻辑
     *
     * @param drawStatIndex - 当前屏幕绘制的开始索引
     * @param drawEndIndex  - 当前屏幕绘制的结束索引
     * @param canvas        - 画布
     */
    public void onDraw(int drawStatIndex, int drawEndIndex, @NonNull Canvas canvas) {
        onDraw(canvas);
    }

    /**
     * 初始化画笔
     */
    public abstract void init();


    public void setData(@NonNull T t) {
        this.data = t;
    }

    public RectF getMainRect() {
        return mMainRect;
    }

    public void setMainRect(@NonNull RectF mMainRect) {
        this.mMainRect = mMainRect;
    }

    public RectF getSubRect() {
        return mSubRect;
    }

    public void setSubRect(@NonNull RectF mSubRect) {
        this.mSubRect = mSubRect;
    }

    public void setCellWidth(float cellWidth) {
        this.cellWidth = cellWidth;
    }

    public void setSpace(float space) {
        this.space = space;
    }

    public void setStartX(float startX) {
        this.startX = startX;
    }

    public void setMainYMax(float mainYMax) {
        this.mainYMax = mainYMax;
    }

    public void setMainYMin(float mainYMin) {
        this.mainYMin = mainYMin;
    }

    public void setSubYMax(float subYMax) {
        this.subYMax = subYMax;
    }

    public void setSubYMin(float subYMin) {
        this.subYMin = subYMin;
    }

    public void setMaHeight(float maHeight) {
        this.maHeight = maHeight;
    }

    public int getShowNumbers() {
        return showNumbers;
    }

    public void setShowNumbers(int showNumbers) {
        this.showNumbers = showNumbers;
    }

    public float getyMaxFlow() {
        return yMaxFlow;
    }

    public void setyMaxFlow(float yMaxFlow) {
        this.yMaxFlow = yMaxFlow;
    }

    public float getyMinFlow() {
        return yMinFlow;
    }

    public void setyMinFlow(float yMinFlow) {
        this.yMinFlow = yMinFlow;
    }

    public void setStockType(int stockType) {
        this.stockType = stockType;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }


    public static float dpToPx(Context context, float dp) {
        return context == null ? -1.0F : dp * context.getResources().getDisplayMetrics().density;
    }

    public static float pxToDp(Context context, float px) {
        return context == null ? -1.0F : px / context.getResources().getDisplayMetrics().density;
    }

    public static float pxToSp(Context context, float pxValue) {
        if (context == null) {
            return -1.0F;
        } else {
            float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return pxValue / fontScale + 0.5F;
        }
    }

    public static float spToPx(Context context, float spValue) {
        if (context == null) {
            return -1.0F;
        } else {
            float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
            return spValue * fontScale + 0.5F;
        }
    }



}
