package chart.demo.ssw.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.OverScroller;

import chart.demo.ssw.gestures.Zoomer;
import chart.demo.ssw.listener.BaseChartTouchListener;
import chart.demo.ssw.listener.LineChartTouchListener;
import chart.demo.ssw.render.BaseRenderer;


/**
 * 行情新图表基础容器界面，包含手势控制，数据控制，绘制控制
 *
 * @author saisai
 * @date 2018/6/11
 */
public class CharView extends BaseCharView {

    /**
     * 缩放过程中，最多和最小的放大后的个数和缩小后的个数
     */
    public static final int MIN_KLINE_NUMBER = 10;
    /**
     * 当前界面显示的最大数据
     */
    public static final int MAX_KLINE_NUMBER = 200;

    /**
     * 当前主图显示的类型
     */
    private int showMainType = BaseRenderer.TIME_DAY;

    /**
     * 当前副图显示的类型
     */
    private int showSubType = BaseRenderer.TIME_VOLUME;


    /**
     * 主视图占整个图大小比例
     */
    private final float mainAccounting = 0.66f;

    /**
     * 副视图占整个图的大小比例
     */
    private final float subAccounting = 0.34f;

    /**
     * 当前可见区域的矩形
     */
    private RectF mContentRect = new RectF();

    /**
     * 宽度
     */
    private int width = 0;

    /**
     * 高度
     */
    private int height = 0;

    /**
     * 主视图和副视图之间的距离
     */
    private float paddingHeight = 100;

    /**
     * 主副视图相加有效的高度
     */
    private float effectiveHeight = 100f;

    /**
     * 是否显示副视图
     */
    private boolean showSub = true;

    /**
     * 当前是否进行了加载数据
     */
    private boolean isPageLoading = false;

    /**
     * 是否需要滑动到边界后有反弹效果
     */
    private boolean isHasLrRecover = false;

    private float preAnimatedValue = 0.0f;

    /**
     * 单元格的宽度
     */
    private float cellWidth = 0f;

    /**
     * 单元格的宽度最小
     */
    private float cellWidthMin = 0f;

    /**
     * 单元格的宽度最小
     */
    private float cellWidthMax = 0f;

    /**
     * 绘画的起始坐标，比如最左边的K线坐标
     */
    protected float startX = 0.0f;

    /**
     * 固定参照点单元格数据索引
     */
    protected int fixedIndex;

    /**
     * 固定参照点单元格 X 起始坐标
     */
    protected float fixedCellX;

    /**
     * 左边的个数
     */
    protected int leftCount;

    /**
     * 右边的个数;
     */
    protected int rightCount;

    /**
     * 固定参照点的类型
     */
    protected BaseChartTouchListener.ChartDrawIndex charDrawIndex;

    /**
     * 两手指第一次触屏的间距
     */
    private float firstTowPDistance = 0.0f;

    /**
     * 第一次的单元格的宽度
     */
    private float firstCellWidth = 0.0f;

    /**
     * 最小手指间距离
     */
    private static final int MIN_FINGER_DISTANCE = 10;


    private float downStartX;

    /**
     * 两指间距离
     */
    private float distance = 0.0f;

    /**
     * 滑动时间
     */
    private long scrollEventTimeMillis = 0;

    /**
     * 双指滑动时间
     */
    private long towPointerScrollTimeMillis = 0;

    /**
     * 当前缩放状态
     */
    private BaseChartTouchListener.ChartZoomState charZoomState = BaseChartTouchListener.ChartZoomState.NORMAL;

    /**
     * 上次缩放状态
     */
    private BaseChartTouchListener.ChartZoomState lastChartZoomState = charZoomState;

    /**
     * 手势管理
     */
    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetectorCompat mGestureDetector;
    private OverScroller mScroller;
    private Zoomer mZoomer;

    private DisplayMetrics mMetrics;

    /**
     * 开始缩放的时候双指距离
     */
    private float lastSpanX;

    private LineChartTouchListener chartTouchListener;

    /**
     * the minimum distance between the pointers that will trigger a zoom gesture
     */
    private float mMinScalePointerDistance;

    /**
     * 各种手势监听
     */
    private final GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            super.onShowPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return super.onDown(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onContextClick(MotionEvent e) {
            return super.onContextClick(e);
        }
    };

    /**
     * 滑动监听
     */
    private final ScaleGestureDetector.SimpleOnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }


        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
        }
    };


    @Override
    public void computeScroll() {
        super.computeScroll();

    }

    public CharView(@NonNull Context context) {
        this(context, null);
    }

    public CharView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CharView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Sets up interactions
        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestureListener);
        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);
        mScroller = new OverScroller(context);
        mZoomer = new Zoomer(context);

        Resources resource = context.getResources();
        mMetrics = resource.getDisplayMetrics();
        mMinScalePointerDistance = convertDpToPixel(3.5f);

        chartTouchListener = new LineChartTouchListener(this);
    }

    public float convertDpToPixel(float dp) {
        if (mMetrics == null) {
            return dp;
        }
        return dp * mMetrics.density;
    }


    @Override
    protected void measure() {
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        effectiveHeight = height - paddingHeight;
        coordinateWidth = width - paddingLeft;
        mContentRect.set(0, 0, width, height);
        if (showSub) {
            mainRect.set(0, 0, width, effectiveHeight * mainAccounting);
            subRect.set(0, mainRect.bottom + paddingHeight, width, height);
        } else {
            mainRect.set(mContentRect);
            subRect.set(0, 0, 0, 0);
        }

        setRect(mainRect, subRect);

        cellWidthMin = coordinateWidth / MAX_KLINE_NUMBER;
        cellWidthMax = coordinateWidth / MIN_KLINE_NUMBER;

        if (chartTouchListener != null) {
            chartTouchListener.measure(coordinateWidth, cellWidthMin, cellWidthMax);
        }
    }

    @Override
    public void resetDrawData(boolean invalidate, float... info) {
        if (invalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        this.cellWidth = info[0];
        this.drawStatIndex = info[1];
        this.drawEndIndex = info[2];
        this.showNumbers = info[3];
        this.startX = info[4];
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onDraw(Canvas canvas) {
        long begin = System.currentTimeMillis();
        super.onDraw(canvas);

        if (mMainXAxisRenderer != null) {
            mMainXAxisRenderer.makeDrawData(0);
            mMainXAxisRenderer.onDraw(canvas);
        }

        if (mMainYAxisRenderer != null) {
            mMainYAxisRenderer.makeDrawData(0);
            mMainYAxisRenderer.onDraw(canvas);
        }

        if (mSubXAxisRenderer != null) {
            mSubXAxisRenderer.makeDrawData(0);
            mSubXAxisRenderer.onDraw(canvas);
        }

        if (mSubYAxisRenderer != null) {
            mSubYAxisRenderer.makeDrawData(0);
            mSubYAxisRenderer.onDraw(canvas);
        }
        // 第一次设置单元格的宽度
        setCellWidth(cellWidth);
        int clipRestoreCount = canvas.save();
        canvas.clipRect(mContentRect);
        if (this.chartTouchListener != null) {
            if (!this.chartTouchListener.isInitialData()) {
                this.chartTouchListener.resetTouchData(this.cellWidth, this.drawStatIndex, this.drawEndIndex, this.showNumbers, this.startX);
            }
        }
        for (int i = (int) drawStatIndex; i < drawEndIndex; i++) {
            //主视图的渲染器进行绘制
            for (int j = 0; j < mainRendererList.size(); j++) {
                BaseRenderer renderer = (BaseRenderer) mainRendererList.get(j);
                if (renderer != null) {
                    renderer.setShowNumbers((int) showNumbers);
                    renderer.setStartX(startX);
                    renderer.setCellWidth(this.cellWidth);
                    renderer.setSpace(1);
                    renderer.makeDrawData(i);
                    renderer.onDraw((int) drawStatIndex, (int) drawEndIndex, canvas);
                }
            }

            //俯视图的渲染器进行绘制
            for (int k = 0; k < subRendererList.size(); k++) {
                BaseRenderer renderer = (BaseRenderer) subRendererList.get(k);
                if (renderer != null) {
                    renderer.setShowNumbers((int) showNumbers);
                    renderer.setStartX(startX);
                    renderer.setCellWidth(this.cellWidth);
                    renderer.setSpace(1);
                    renderer.makeDrawData(i);
                    renderer.onDraw((int) drawStatIndex, (int) drawEndIndex, canvas);
                }
            }
        }
        canvas.restoreToCount(clipRestoreCount);

        long end = System.currentTimeMillis();

        Log.i("@@@", "*------chart---draw-----end---" + (end - begin) + " drawStatIndex " + drawStatIndex + " drawEndIndex " + drawEndIndex + "  startX " + startX);
    }

    /**
     * 设置单元格的宽度
     *
     * @param cellWidth -
     */
    public void setCellWidth(float cellWidth) {
        if (cellWidth == 0 && coordinateWidth > 0 && showNumbers != 0) {
            this.cellWidth = (coordinateWidth - paddingLeft) / showNumbers;
        } else {
            this.cellWidth = cellWidth;
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        boolean retVal = mScaleGestureDetector.onTouchEvent(event);
//        retVal = mGestureDetector.onTouchEvent(event) || retVal;
//        return retVal || super.onTouchEvent(event);

        super.onTouchEvent(event);

        if (chartTouchListener == null || getData() == null) {
            return false;
        }

        if (!mTouchEnabled) {
            return false;
        }

        return chartTouchListener.onTouchEvent(event);
    }

    /**
     * 设置初始数据，用来处理最新的缩放功能
     */
    @Override
    public void resetBaseValues() {

        this.startX = 0;
        if (data.dataSize() > showNumbers) {
            this.drawStatIndex = data.dataSize() - showNumbers;
            this.drawEndIndex = data.dataSize();
        } else {
            this.drawStatIndex = 0;
            this.drawEndIndex = data.dataSize();
        }
        this.cellWidth = coordinateWidth / showNumbers;
    }

}
