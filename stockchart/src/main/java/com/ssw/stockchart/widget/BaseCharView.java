package com.ssw.stockchart.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewParent;

import com.ssw.stockchart.data.BaseCharData;
import com.ssw.stockchart.listener.BaseChartTouchListener;
import com.ssw.stockchart.render.BaseRenderer;
import com.ssw.stockchart.render.XAxisRenderer;
import com.ssw.stockchart.render.YAxisRenderer;

import java.util.ArrayList;
import java.util.List;


/**
 * 图表容器基础类
 *
 * @author saisai
 * @date 2018/6/11
 */
public abstract class BaseCharView<T extends BaseCharData> extends View {

    /**
     * 上下文
     */
    protected Context mContext;

    /**
     * 当前的手势状态
     */
    protected BaseChartTouchListener.ChartGesture chartGesture = BaseChartTouchListener.ChartGesture.NONE;

    /**
     * 是否可以相应触摸事件
     */
    protected boolean mTouchEnabled = true;

    /**
     * 行情数据
     */
    protected T data;

    /**
     * 开始绘制的位置
     */
    protected float drawStatIndex = 0;

    /**
     * 结束绘制的位置
     */
    protected float drawEndIndex = 0;

    /**
     * 当前界面需要绘制的条目数
     */
    protected float showNumbers = 100;

    /**
     * X轴的渲染器
     */
    protected XAxisRenderer mMainXAxisRenderer;

    /**
     * Y轴的渲染器
     */
    protected YAxisRenderer mMainYAxisRenderer;


    /**
     * X轴的渲染器
     */
    protected XAxisRenderer mSubXAxisRenderer;

    /**
     * Y轴的渲染器
     */
    protected YAxisRenderer mSubYAxisRenderer;

    /**
     * 主视图渲染器集合
     */
    protected List<BaseRenderer<T>> mainRendererList = new ArrayList<>();

    /**
     * 副视图渲染器集合
     */
    protected List<BaseRenderer<T>> subRendererList = new ArrayList<>();

    /**
     * 当前可以绘制的实际宽度
     */
    protected float coordinateWidth;

    /**
     * 对左边边框间距
     */
    protected float paddingLeft = 0f;

    /**
     * 当前主视图所在的矩形
     */
    protected RectF mainRect = new RectF();

    /**
     * 当前副视图所在的矩形
     */
    protected RectF subRect = new RectF();

    /**
     * 画笔
     */
    private Paint mBackgroundPaint;

    /**
     * 当前股票的类型，根据此值来进行判断显示的数据保留小数位数
     */
    protected int stockType = 0;

    private DisplayMetrics mMetrics;


    public BaseCharView(@NonNull Context context) {
        this(context, null);
    }

    public BaseCharView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseCharView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setLayerType(LAYER_TYPE_HARDWARE, null);
        mMetrics = context.getResources().getDisplayMetrics();
        this.mBackgroundPaint = new Paint();
        this.mBackgroundPaint.setAntiAlias(true);
        this.mBackgroundPaint.setColor(Color.GRAY);
        this.mBackgroundPaint.setStrokeWidth(1.5f);
        this.mBackgroundPaint.setStyle(Paint.Style.STROKE);
        setLayerType(LAYER_TYPE_SOFTWARE, mBackgroundPaint);
    }

    public float dp2Px(float dp) {
        if (mMetrics == null) {
            return dp;
        }
        return dp * mMetrics.density;
    }


    public T getData() {
        return data;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(mainRect, this.mBackgroundPaint);
        canvas.drawRect(subRect, this.mBackgroundPaint);
    }

    /**
     * 重新设置数据
     */
    public abstract void resetBaseValues();

    @SuppressWarnings("unchecked")
    public void addMainRenderer(BaseRenderer renderer) {
        if (!mainRendererList.contains(renderer)) {
            mainRendererList.add(renderer);
        }
    }

    @SuppressWarnings("unchecked")
    public void addSubRenderer(BaseRenderer renderer) {
        if (!subRendererList.contains(renderer)) {
            subRendererList.add(renderer);
        }
    }

    public void cleanMainRenderer() {
        mainRendererList.clear();
    }


    public void cleanSubRenderer() {
        subRendererList.clear();
    }

    @SuppressWarnings("unchecked")
    public void setData(@NonNull T t) {
        setRect();

        this.data = t;

        //设置XY轴渲染器数据
        if (this.mMainYAxisRenderer != null) {
            this.mMainYAxisRenderer.setData(t);
        }
        if (mMainXAxisRenderer != null) {
            this.mMainXAxisRenderer.setData(t);
        }
        if (mSubXAxisRenderer != null) {
            this.mSubXAxisRenderer.setData(t);
        }
        if (mSubYAxisRenderer != null) {
            this.mSubYAxisRenderer.setData(t);
        }

        //设置主视图渲染器数据
        for (BaseRenderer renderer : mainRendererList) {
            renderer.setData(t);
        }

        //设置副视图渲染器数据
        for (BaseRenderer renderer : subRendererList) {
            renderer.setData(t);
        }

        resetBaseValues();
    }


    /**
     * 设置主副视图矩形
     */
    protected void setRect() {
        setRect(mainRect, subRect);
    }

    /**
     * 设置主副视图矩形
     *
     * @param mainRect - 主视图所在的矩形
     * @param subRect  - 俯视图所在的矩形
     */
    protected void setRect(@NonNull RectF mainRect, @NonNull RectF subRect) {

        if (mMainXAxisRenderer != null) {
            mMainXAxisRenderer.setMainRect(mainRect);
            mMainXAxisRenderer.setSubRect(subRect);
        }

        if (mMainYAxisRenderer != null) {
            mMainYAxisRenderer.setMainRect(mainRect);
            mMainYAxisRenderer.setSubRect(subRect);
        }

        if (mSubXAxisRenderer != null) {
            mSubXAxisRenderer.setMainRect(mainRect);
            mSubXAxisRenderer.setSubRect(subRect);
        }

        if (mSubYAxisRenderer != null) {
            mSubYAxisRenderer.setMainRect(mainRect);
            mSubYAxisRenderer.setSubRect(subRect);
        }

        for (BaseRenderer renderer : mainRendererList) {
            renderer.setMainRect(mainRect);
            renderer.setSubRect(subRect);
        }

        for (BaseRenderer renderer : subRendererList) {
            renderer.setMainRect(mainRect);
            renderer.setSubRect(subRect);
        }
    }

    /**
     * 计算当前的图表高度
     */
    protected abstract void measure();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measure();
    }


    /**
     * 设置主视图副视图相关的Y轴最大值最小值
     *
     * @param mainYMax -主视图Y轴的最大值
     * @param mainYMin -主视图Y轴最小值
     * @param subYMax  -副视图Y轴最大值
     * @param subYMin  -副视图Y轴最小值
     */
    public void setMaxMin(float mainYMax, float mainYMin, float subYMax, float subYMin) {
        for (BaseRenderer renderer : mainRendererList) {
            if (mainYMax > 0) {
                renderer.setMainYMax(mainYMax);
            }

            if (mainYMin > 0) {
                renderer.setMainYMin(mainYMin);
            }
        }

        for (BaseRenderer renderer : subRendererList) {
            if (subYMax > 0) {
                renderer.setSubYMax(subYMax);
            }

            if (subYMin > 0) {
                renderer.setSubYMin(subYMin);
            }
        }
    }

    /**
     * 设置当前绘制区域的真实Y轴最大最小值
     *
     * @param yMaxMainFlow - 主视图真实最大值
     * @param yMinMainFlow - 主视图真实最小值
     * @param yMaxSubFlow  - 副视图真实最大值
     * @param yMinSubFlow  - 副视图真实最小值
     */
    public void setMaxMinFlow(float yMaxMainFlow, float yMinMainFlow, float yMaxSubFlow, float yMinSubFlow) {
        for (BaseRenderer renderer : mainRendererList) {
            if (yMaxMainFlow > 0) {
                renderer.setyMaxFlow(yMaxMainFlow);
            }

            if (yMinMainFlow > 0) {
                renderer.setyMinFlow(yMinMainFlow);
            }
        }

        for (BaseRenderer renderer : subRendererList) {
            if (yMaxSubFlow > 0) {
                renderer.setyMaxFlow(yMaxSubFlow);
            }

            if (yMinSubFlow > 0) {
                renderer.setyMinFlow(yMinSubFlow);
            }
        }
    }

    /**
     * 重置绘图数据
     *
     * @param invalidate - 是否重绘
     * @param info       - 相关数据
     */
    public abstract void resetDrawData(boolean invalidate, float... info);

    public void disableScroll() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    /**
     * 设置股票类型
     *
     * @param stockType -股票类型
     */
    public void setStockType(int stockType) {
        this.stockType = stockType;

        for (BaseRenderer renderer : mainRendererList) {
            renderer.setStockType(stockType);
        }

        for (BaseRenderer renderer : subRendererList) {
            renderer.setStockType(stockType);
        }
    }

    public void setMainXAxisRenderer(XAxisRenderer mMainXAxisRenderer) {
        this.mMainXAxisRenderer = mMainXAxisRenderer;
    }

    public void setMainYAxisRenderer(YAxisRenderer mMainYAxisRenderer) {
        this.mMainYAxisRenderer = mMainYAxisRenderer;
    }

    public void setSubXAxisRenderer(XAxisRenderer mSubXAxisRenderer) {
        this.mSubXAxisRenderer = mSubXAxisRenderer;
    }

    public void setSubYAxisRenderer(YAxisRenderer mSubYAxisRenderer) {
        this.mSubYAxisRenderer = mSubYAxisRenderer;
    }

}
