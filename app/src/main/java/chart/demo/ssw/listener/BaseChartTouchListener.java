package chart.demo.ssw.listener;

import android.view.GestureDetector;
import android.view.MotionEvent;

import chart.demo.ssw.widget.BaseCharView;

/**
 * @author saisai
 */
public abstract class BaseChartTouchListener<T extends BaseCharView<?>> extends GestureDetector.SimpleOnGestureListener {

    private boolean initialData = false;

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
    public  void resetTouchData(float... info){
        initialData = true;
    }

    /**
     * 是否初始化
     * @return -
     */
    public boolean isInitialData(){
        return initialData;
    }

}
