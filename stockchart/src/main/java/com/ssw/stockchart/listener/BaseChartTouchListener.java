package com.ssw.stockchart.listener;

import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.OverScroller;

import com.ssw.stockchart.widget.BaseCharView;


/**
 * @author saisai
 */
public abstract class BaseChartTouchListener<T extends BaseCharView<?>> extends GestureDetector.SimpleOnGestureListener {

    /**
     * 先关图表
     */
    protected BaseCharView charView;

    /**
     * 处理手势
     */
    protected GestureDetector gestureDetector;

    /**
     * 滑动计算
     */
    protected OverScroller mScroller;

    /**
     * 是否初始化相关参数
     */
    private boolean initialData = false;

    public BaseChartTouchListener(@NonNull BaseCharView charView) {
        this.charView = charView;
        this.mScroller = new OverScroller(this.charView.getContext());
        this.gestureDetector = new GestureDetector(this.charView.getContext(), this);
    }

    /**
     * 当前手势状态
     */
    public enum ChartGesture {
        //普通状态
        NONE,
        //拖动
        DRAG,
        //普通缩放状态
        ZOOM_NONE,
        //缩小
        ZOOM_IN,
        //放大
        ZOOM_OUT,
        //单击
        SINGLE_TAP,
        //双击
        DOUBLE_TAP,
        //长按
        LONG_PRESS,
        //惯性滑动
        FLING
    }

    /**
     * 当前图表缩放状态
     */
    public enum ChartZoomState {
        //普通缩放状态
        NORMAL,
        //缩放到最大状态
        ZOOM_MAX,
        //缩放到最小状态
        ZOOM_MIN
    }

    /**
     * 当前绘制参照
     */
    public enum ChartDrawIndex {
        //最左边
        LEFT,
        //最右边
        RIGHT
    }


    /**
     * 处理触摸事件
     *
     * @param event -
     * @return -
     */
    public abstract boolean onTouchEvent(MotionEvent event);


    /**
     * 初始化触屏相关操作数据
     *
     * @param info -
     */
    public void resetTouchData(float... info) {
        initialData = true;
    }

    /**
     * 是否初始化
     *
     * @return -
     */
    public boolean isInitialData() {
        return initialData;
    }

    /**
     * 用于滑动计算
     */
    public abstract void computeScroll();


    /**
     * 用于计算惯性滑动
     *
     * @param velocityX - X轴速度
     * @param velocityY - Y轴速度
     */
    public abstract void fling(int velocityX, int velocityY);


    /**
     * 是否当前scroll有效
     *
     * @param distanceX - X轴变化的距离
     * @return -
     */
    public abstract boolean scroll(float distanceX);

}
