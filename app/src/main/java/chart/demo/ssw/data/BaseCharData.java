package chart.demo.ssw.data;

import android.os.Parcelable;

/**
 * 图表数据基类
 *
 * @author saisai
 * @date 2018/6/11
 */
public abstract class BaseCharData implements Parcelable {

    /**
     * 主视图左边刻度数值
     */
    protected String[] leftScaleTextArray;

    /**
     * 主视图右边刻度数值
     */
    protected String[] rightScaleTextArray;

    /**
     * 底下刻度数值
     */
    protected String[] bottomScaleTextArray;

    /**
     * 底下刻度数值坐标
     */
    protected float[] bottomScaleTextIndexArray;

    /**
     * 成交量颜色
     */
    protected float[] turnoverUp;

    /**
     * 成交量
     */
    protected float[] turnover;

    /**
     * 成交量最大值
     */
    protected float turnoverMax;

    /**
     * 成交量最小值
     */
    protected float turnoverMin;

    /**
     * 获取当前数据的长度
     *
     * @return -
     */
    public abstract int dataSize();

    /**
     * 检查数据是否越界
     *
     * @param size  - 数组长度
     * @param index - 目标位置
     * @return - 是否越界
     */
    protected boolean checkParameter(int size, int index) {
        return index < size;
    }


    public float getTurnoverUpByIndex(int index) {
        if (!checkParameter(turnoverUp == null ? 0 : turnoverUp.length, index)) {
            return 0f;
        }
        return turnoverUp[index];
    }

    public float getTurnoverByIndex(int index) {
        if (!checkParameter(turnover == null ? 0 : turnover.length, index)) {
            return 0f;
        }
        return turnover[index];
    }

    public String getLeftScaleTextArrayByIndex(int index) {
        if (!checkParameter(leftScaleTextArray == null ? 0 : leftScaleTextArray.length, index)) {
            return "";
        }

        return leftScaleTextArray[index];
    }

    public String getRightScaleTextArrayByIndex(int index) {
        if (!checkParameter(rightScaleTextArray == null ? 0 : rightScaleTextArray.length, index)) {
            return "";
        }
        return rightScaleTextArray[index];
    }

    public String getBottomScaleTextArrayByIndexByIndex(int index) {
        if (!checkParameter(bottomScaleTextArray == null ? 0 : bottomScaleTextArray.length, index)) {
            return "";
        }
        return bottomScaleTextArray[index];
    }

    public float getBottomScaleTextIndexArrayByIndexByIndex(int index) {
        if (!checkParameter(bottomScaleTextIndexArray == null ? 0 : bottomScaleTextIndexArray.length, index)) {
            return 0;
        }
        return bottomScaleTextIndexArray[index];
    }

    public void setLeftScaleTextArray(String[] leftScaleTextArray) {
        this.leftScaleTextArray = leftScaleTextArray;
    }

    public void setRightScaleTextArray(String[] rightScaleTextArray) {
        this.rightScaleTextArray = rightScaleTextArray;
    }

    public void setBottomScaleTextArray(String[] bottomScaleTextArray) {
        this.bottomScaleTextArray = bottomScaleTextArray;
    }

    public void setBottomScaleTextIndexArray(float[] bottomScaleTextIndexArray) {
        this.bottomScaleTextIndexArray = bottomScaleTextIndexArray;
    }

    public int getLeftScaleTextArraySize() {
        return leftScaleTextArray == null ? 0 : leftScaleTextArray.length;
    }

    public int getRightScaleTextArraySize() {
        return rightScaleTextArray == null ? 0 : rightScaleTextArray.length;
    }

    public int getBottomScaleTextArraySize() {
        return bottomScaleTextArray == null ? 0 : bottomScaleTextArray.length;
    }

    public int getBottomScaleTextIndexArraySize() {
        return bottomScaleTextIndexArray == null ? 0 : bottomScaleTextIndexArray.length;
    }

    public void setTurnoverUp(float[] turnoverUp) {
        this.turnoverUp = turnoverUp;
    }

    public void setTurnover(float[] turnover) {
        this.turnover = turnover;
    }

    public float getTurnoverMax() {
        return turnoverMax;
    }

    public void setTurnoverMax(float turnoverMax) {
        this.turnoverMax = turnoverMax;
    }

    public float getTurnoverMin() {
        return turnoverMin;
    }

    public void setTurnoverMin(float turnoverMin) {
        this.turnoverMin = turnoverMin;
    }
}
