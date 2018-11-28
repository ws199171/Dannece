package com.ssw.stockchart.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * k线数据bean
 *
 * @author saisai
 * @date 2018/6/11
 */
public class KlineChartData extends BaseCharData {

    /**
     * 最低价
     */
    private float[] lowPrice;

    /**
     * 最低价的最大值
     */
    private float lowPriceMax;

    /**
     * 最低价的最小值
     */
    private float lowPriceMin;

    /**
     * 开盘价
     */
    private float[] openPrice;

    /**
     * 开盘价的最大值
     */
    private float openPriceMax;

    /**
     * 开盘价的最小值
     */
    private float openPriceMin;

    /**
     * 收盘价
     */
    private float[] closePrice;

    /**
     * 收盘价的最大值
     */
    private float closePriceMax;

    /**
     * 收盘价的最小值
     */
    private float closePriceMin;

    /**
     * 最高价
     */
    private float[] heightPrice;

    /**
     * 最高价的最大值
     */
    private float heightPriceMax;

    /**
     * 最高价的最小值
     */
    private float heightPriceMin;

    /**
     * 涨跌幅
     */
    private float[] zdf;

    /**
     * 涨跌幅最大值
     */
    private float zdfMax;

    /**
     * 涨跌幅最小值
     */
    private float zdfMin;

    /**
     * 振幅
     */
    private float[] flux;

    /**
     * 振幅最大值
     */
    private float fluxMax;

    /**
     * 振幅最小值
     */
    private float fluxMin;

    /**
     * 涨跌
     */
    private float[] zd;

    /**
     * 涨跌最大值
     */
    private float zdMax;

    /**
     * 涨跌最小值
     */
    private float zdMin;

    /**
     * 换手率
     */
    private float[] hsl;

    /**
     * 换手率最大值
     */
    private float hslMax;

    /**
     * 换手率最小值
     */
    private float hslMin;

    /**
     * 成交额
     */
    private float[] amount;

    /**
     * 成交额最大值
     */
    private float amountMax;
    /**
     * 成交额最小值
     */
    private float amountMin;

    /**
     * 昨收价格
     */
    private float[] lastClose;

    /**
     * 昨收价格最大值
     */
    private float lastCloseMax;

    /**
     * 昨收价格最小值
     */
    private float lastCloseMin;

    /**
     * 分钟K线的时间，默认值是0
     */
    private String[] candleTime;


    /**
     * 分钟K线的时间，目前只用于买卖点的日期比对 时间格式为yyyyMMdd;
     */
    private String[] candleTimeStype2;

    /**
     * 蜡烛线展示买卖点类型 0为买、1为卖
     */
    private String[] buySellType;

    /**
     * 第一条MA数据
     */
    private float[] ma1;

    /**
     * 第一条MA数据最大值
     */
    private float ma1Max;

    /**
     * 第一条MA数据最小值
     */
    private float ma1Min;

    /**
     * 第二条MA数据
     */
    private float[] ma2;

    /**
     * 第二条MA数据最大值
     */
    private float ma2Max;

    /**
     * 第二条MA数据最小值
     */
    private float ma2Min;

    /**
     * 第三条MA数据
     */
    private float[] ma3;

    /**
     * 第三条MA数据最大值
     */
    private float ma3Max;

    /**
     * 第三条MA数据最小值
     */
    private float ma3Min;

    /**
     * 第四条MA数据
     */
    private float[] ma4;

    /**
     * 第四条MA数据最大值
     */
    private float ma4Max;

    /**
     * 第四条MA数据最小值
     */
    private float ma4Min;

    /**
     * 第五条MA数据
     */
    private float[] ma5;

    /**
     * 第五条MA数据最大值
     */
    private float ma5Max;

    /**
     * 第五条MA数据最小值
     */
    private float ma5Min;

    /**
     * 取6日MA均线
     */
    private float[] ma6;

    /**
     * 取6日MA均线最大值
     */
    private float ma6Max;

    /**
     * 取6日MA均线最小值
     */
    private float ma6Min;

    /**
     * 取18日MA均线
     */
    private float[] ma18;

    /**
     * 取18日MA均线最大值
     */
    private float ma18Max;

    /**
     * 取18日MA均线最小值
     */
    private float ma18Min;

    public KlineChartData() {

    }

    private KlineChartData(Parcel in) {
        leftScaleTextArray = in.createStringArray();
        rightScaleTextArray = in.createStringArray();
        bottomScaleTextArray = in.createStringArray();
        bottomScaleTextIndexArray = in.createFloatArray();
        turnoverUp = in.createFloatArray();
        turnover = in.createFloatArray();
        turnoverMax = in.readFloat();
        turnoverMin = in.readFloat();

        lowPrice = in.createFloatArray();
        lowPriceMax = in.readFloat();
        lowPriceMin = in.readFloat();
        openPrice = in.createFloatArray();
        openPriceMax = in.readFloat();
        openPriceMin = in.readFloat();
        closePrice = in.createFloatArray();
        closePriceMax = in.readFloat();
        closePriceMin = in.readFloat();
        heightPrice = in.createFloatArray();
        heightPriceMax = in.readFloat();
        heightPriceMin = in.readFloat();
        zdf = in.createFloatArray();
        zdfMax = in.readFloat();
        zdfMin = in.readFloat();
        flux = in.createFloatArray();
        fluxMax = in.readFloat();
        fluxMin = in.readFloat();
        zd = in.createFloatArray();
        zdMax = in.readFloat();
        zdMin = in.readFloat();
        hsl = in.createFloatArray();
        hslMax = in.readFloat();
        hslMin = in.readFloat();
        amount = in.createFloatArray();
        amountMax = in.readFloat();
        amountMin = in.readFloat();
        lastClose = in.createFloatArray();
        lastCloseMax = in.readFloat();
        lastCloseMin = in.readFloat();
        candleTime = in.createStringArray();
        candleTimeStype2 = in.createStringArray();
        buySellType = in.createStringArray();
        ma1 = in.createFloatArray();
        ma1Max = in.readFloat();
        ma1Min = in.readFloat();
        ma2 = in.createFloatArray();
        ma2Max = in.readFloat();
        ma2Min = in.readFloat();
        ma3 = in.createFloatArray();
        ma3Max = in.readFloat();
        ma3Min = in.readFloat();
        ma4 = in.createFloatArray();
        ma4Max = in.readFloat();
        ma4Min = in.readFloat();
        ma5 = in.createFloatArray();
        ma5Max = in.readFloat();
        ma5Min = in.readFloat();
        ma6 = in.createFloatArray();
        ma6Max = in.readFloat();
        ma6Min = in.readFloat();
        ma18 = in.createFloatArray();
        ma18Max = in.readFloat();
        ma18Min = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(leftScaleTextArray);
        dest.writeStringArray(rightScaleTextArray);
        dest.writeStringArray(bottomScaleTextArray);
        dest.writeFloatArray(bottomScaleTextIndexArray);
        dest.writeFloatArray(turnoverUp);
        dest.writeFloatArray(turnover);
        dest.writeFloat(turnoverMax);
        dest.writeFloat(turnoverMin);

        dest.writeFloatArray(lowPrice);
        dest.writeFloat(lowPriceMax);
        dest.writeFloat(lowPriceMin);
        dest.writeFloatArray(openPrice);
        dest.writeFloat(openPriceMax);
        dest.writeFloat(openPriceMin);
        dest.writeFloatArray(closePrice);
        dest.writeFloat(closePriceMax);
        dest.writeFloat(closePriceMin);
        dest.writeFloatArray(heightPrice);
        dest.writeFloat(heightPriceMax);
        dest.writeFloat(heightPriceMin);
        dest.writeFloatArray(zdf);
        dest.writeFloat(zdfMax);
        dest.writeFloat(zdfMin);
        dest.writeFloatArray(flux);
        dest.writeFloat(fluxMax);
        dest.writeFloat(fluxMin);
        dest.writeFloatArray(zd);
        dest.writeFloat(zdMax);
        dest.writeFloat(zdMin);
        dest.writeFloatArray(hsl);
        dest.writeFloat(hslMax);
        dest.writeFloat(hslMin);
        dest.writeFloatArray(amount);
        dest.writeFloat(amountMax);
        dest.writeFloat(amountMin);
        dest.writeFloatArray(lastClose);
        dest.writeFloat(lastCloseMax);
        dest.writeFloat(lastCloseMin);
        dest.writeStringArray(candleTime);
        dest.writeStringArray(candleTimeStype2);
        dest.writeStringArray(buySellType);
        dest.writeFloatArray(ma1);
        dest.writeFloat(ma1Max);
        dest.writeFloat(ma1Min);
        dest.writeFloatArray(ma2);
        dest.writeFloat(ma2Max);
        dest.writeFloat(ma2Min);
        dest.writeFloatArray(ma3);
        dest.writeFloat(ma3Max);
        dest.writeFloat(ma3Min);
        dest.writeFloatArray(ma4);
        dest.writeFloat(ma4Max);
        dest.writeFloat(ma4Min);
        dest.writeFloatArray(ma5);
        dest.writeFloat(ma5Max);
        dest.writeFloat(ma5Min);
        dest.writeFloatArray(ma6);
        dest.writeFloat(ma6Max);
        dest.writeFloat(ma6Min);
        dest.writeFloatArray(ma18);
        dest.writeFloat(ma18Max);
        dest.writeFloat(ma18Min);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<KlineChartData> CREATOR = new Parcelable.Creator<KlineChartData>() {
        @Override
        public KlineChartData createFromParcel(Parcel in) {
            return new KlineChartData(in);
        }

        @Override
        public KlineChartData[] newArray(int size) {
            return new KlineChartData[size];
        }
    };

    @Override
    public int dataSize() {
        if (openPrice != null) {
            return openPrice.length;
        } else if (closePrice != null) {
            return closePrice.length;
        } else if (turnover != null) {
            return turnover.length;
        } else if (ma1 != null) {
            return ma1.length;
        } else {
            return lastClose == null ? 0 : lastClose.length;
        }
    }

    public float getLowPriceByIndex(int index) {

        if (!checkParameter(lowPrice.length, index)) {
            return 0f;
        }
        return lowPrice[index];
    }

    public float getOpenPriceByIndex(int index) {
        if (!checkParameter(openPrice == null ? 0 : openPrice.length, index)) {
            return 0f;
        }
        return openPrice[index];
    }

    public float getClosePriceByIndex(int index) {
        if (!checkParameter(closePrice == null ? 0 : closePrice.length, index)) {
            return 0f;
        }
        return closePrice[index];
    }

    public float getHeightPriceByIndex(int index) {
        if (!checkParameter(heightPrice == null ? 0 : heightPrice.length, index)) {
            return 0f;
        }
        return heightPrice[index];
    }


    public float getZdfByIndex(int index) {
        if (!checkParameter(zdf == null ? 0 : zdf.length, index)) {
            return 0f;
        }
        return zdf[index];
    }

    public float getFluxByIndex(int index) {
        if (!checkParameter(flux == null ? 0 : flux.length, index)) {
            return 0f;
        }
        return flux[index];
    }

    public float getZdByIndex(int index) {
        if (!checkParameter(zd == null ? 0 : zd.length, index)) {
            return 0f;
        }
        return zd[index];
    }

    public float getHslByIndex(int index) {
        if (!checkParameter(hsl == null ? 0 : hsl.length, index)) {
            return 0f;
        }
        return hsl[index];
    }

    public float getAmountByIndex(int index) {
        if (!checkParameter(amount == null ? 0 : amount.length, index)) {
            return 0f;
        }
        return amount[index];
    }

    public float getLastCloseByIndex(int index) {
        if (!checkParameter(lastClose == null ? 0 : lastClose.length, index)) {
            return 0f;
        }
        return lastClose[index];
    }

    public String getCandleTimeByIndex(int index) {
        if (!checkParameter(candleTime == null ? 0 : candleTime.length, index)) {
            return "";
        }
        return candleTime[index];
    }

    public String getCandleTimeStype2ByIndex(int index) {
        if (!checkParameter(candleTimeStype2 == null ? 0 : candleTimeStype2.length, index)) {
            return "";
        }
        return candleTimeStype2[index];
    }

    public String getBuySellTypeByIndex(int index) {
        if (!checkParameter(buySellType == null ? 0 : buySellType.length, index)) {
            return "";
        }
        return buySellType[index];
    }

    public void setLowPrice(float[] lowPrice) {
        this.lowPrice = lowPrice;
    }

    public void setOpenPrice(float[] openPrice) {
        this.openPrice = openPrice;
    }

    public void setClosePrice(float[] closePrice) {
        this.closePrice = closePrice;
    }

    public void setHeightPrice(float[] heightPrice) {
        this.heightPrice = heightPrice;
    }

    public void setZdf(float[] zdf) {
        this.zdf = zdf;
    }

    public void setFlux(float[] flux) {
        this.flux = flux;
    }

    public void setZd(float[] zd) {
        this.zd = zd;
    }

    public void setHsl(float[] hsl) {
        this.hsl = hsl;
    }

    public void setAmount(float[] amount) {
        this.amount = amount;
    }

    public void setLastClose(float[] lastClose) {
        this.lastClose = lastClose;
    }

    public void setCandleTime(String[] candleTime) {
        this.candleTime = candleTime;
    }

    public void setCandleTimeStype2(String[] candleTimeStype2) {
        this.candleTimeStype2 = candleTimeStype2;
    }

    public void setBuySellType(String[] buySellType) {
        this.buySellType = buySellType;
    }


    public float getMa1ByIndex(int index) {
        if (!checkParameter(ma1 == null ? 0 : ma1.length, index)) {
            return 0f;
        }
        return ma1[index];
    }

    public void setMa1(float[] ma1) {
        this.ma1 = ma1;
    }

    public float getMa2ByIndex(int index) {
        if (!checkParameter(ma2 == null ? 0 : ma2.length, index)) {
            return 0f;
        }
        return ma2[index];
    }

    public void setMa2(float[] ma2) {
        this.ma2 = ma2;
    }

    public float getMa3ByIndex(int index) {
        if (!checkParameter(ma3 == null ? 0 : ma3.length, index)) {
            return 0f;
        }
        return ma3[index];
    }

    public void setMa3(float[] ma3) {
        this.ma3 = ma3;
    }

    public float getMa4ByIndex(int index) {
        if (!checkParameter(ma4 == null ? 0 : ma4.length, index)) {
            return 0f;
        }
        return ma4[index];
    }

    public void setMa4(float[] ma4) {
        this.ma4 = ma4;
    }

    public float getMa5ByIndex(int index) {
        if (!checkParameter(ma5 == null ? 0 : ma5.length, index)) {
            return 0f;
        }
        return ma5[index];
    }

    public void setMa5(float[] ma5) {
        this.ma5 = ma5;
    }


    public float getMa6ByIndex(int index) {
        if (!checkParameter(ma6 == null ? 0 : ma6.length, index)) {
            return 0f;
        }
        return ma6[index];
    }

    public void setMa6(float[] ma6) {
        this.ma6 = ma6;
    }

    public float getMa18ByIndex(int index) {
        if (!checkParameter(ma18 == null ? 0 : ma18.length, index)) {
            return 0f;
        }
        return ma18[index];
    }

    public void setMa18(float[] ma18) {
        this.ma18 = ma18;
    }


    public float[] getLowPrice() {
        return lowPrice;
    }

    public float getLowPriceMax() {
        return lowPriceMax;
    }

    public void setLowPriceMax(float lowPriceMax) {
        this.lowPriceMax = lowPriceMax;
    }

    public float getLowPriceMin() {
        return lowPriceMin;
    }

    public void setLowPriceMin(float lowPriceMin) {
        this.lowPriceMin = lowPriceMin;
    }

    public float[] getOpenPrice() {
        return openPrice;
    }

    public float getOpenPriceMax() {
        return openPriceMax;
    }

    public void setOpenPriceMax(float openPriceMax) {
        this.openPriceMax = openPriceMax;
    }

    public float getOpenPriceMin() {
        return openPriceMin;
    }

    public void setOpenPriceMin(float openPriceMin) {
        this.openPriceMin = openPriceMin;
    }

    public float[] getClosePrice() {
        return closePrice;
    }

    public float getClosePriceMax() {
        return closePriceMax;
    }

    public void setClosePriceMax(float closePriceMax) {
        this.closePriceMax = closePriceMax;
    }

    public float getClosePriceMin() {
        return closePriceMin;
    }

    public void setClosePriceMin(float closePriceMin) {
        this.closePriceMin = closePriceMin;
    }

    public float[] getHeightPrice() {
        return heightPrice;
    }

    public float getHeightPriceMax() {
        return heightPriceMax;
    }

    public void setHeightPriceMax(float heightPriceMax) {
        this.heightPriceMax = heightPriceMax;
    }

    public float getHeightPriceMin() {
        return heightPriceMin;
    }

    public void setHeightPriceMin(float heightPriceMin) {
        this.heightPriceMin = heightPriceMin;
    }

    public float[] getZdf() {
        return zdf;
    }

    public float getZdfMax() {
        return zdfMax;
    }

    public void setZdfMax(float zdfMax) {
        this.zdfMax = zdfMax;
    }

    public float getZdfMin() {
        return zdfMin;
    }

    public void setZdfMin(float zdfMin) {
        this.zdfMin = zdfMin;
    }

    public float[] getFlux() {
        return flux;
    }

    public float getFluxMax() {
        return fluxMax;
    }

    public void setFluxMax(float fluxMax) {
        this.fluxMax = fluxMax;
    }

    public float getFluxMin() {
        return fluxMin;
    }

    public void setFluxMin(float fluxMin) {
        this.fluxMin = fluxMin;
    }

    public float[] getZd() {
        return zd;
    }

    public float getZdMax() {
        return zdMax;
    }

    public void setZdMax(float zdMax) {
        this.zdMax = zdMax;
    }

    public float getZdMin() {
        return zdMin;
    }

    public void setZdMin(float zdMin) {
        this.zdMin = zdMin;
    }

    public float[] getHsl() {
        return hsl;
    }

    public float getHslMax() {
        return hslMax;
    }

    public void setHslMax(float hslMax) {
        this.hslMax = hslMax;
    }

    public float getHslMin() {
        return hslMin;
    }

    public void setHslMin(float hslMin) {
        this.hslMin = hslMin;
    }

    public float[] getAmount() {
        return amount;
    }

    public float getAmountMax() {
        return amountMax;
    }

    public void setAmountMax(float amountMax) {
        this.amountMax = amountMax;
    }

    public float getAmountMin() {
        return amountMin;
    }

    public void setAmountMin(float amountMin) {
        this.amountMin = amountMin;
    }

    public float[] getLastClose() {
        return lastClose;
    }

    public float getLastCloseMax() {
        return lastCloseMax;
    }

    public void setLastCloseMax(float lastCloseMax) {
        this.lastCloseMax = lastCloseMax;
    }

    public float getLastCloseMin() {
        return lastCloseMin;
    }

    public void setLastCloseMin(float lastCloseMin) {
        this.lastCloseMin = lastCloseMin;
    }

    public String[] getCandleTime() {
        return candleTime;
    }

    public String[] getCandleTimeStype2() {
        return candleTimeStype2;
    }

    public String[] getBuySellType() {
        return buySellType;
    }

    public float[] getMa1() {
        return ma1;
    }

    public float getMa1Max() {
        return ma1Max;
    }

    public void setMa1Max(float ma1Max) {
        this.ma1Max = ma1Max;
    }

    public float getMa1Min() {
        return ma1Min;
    }

    public void setMa1Min(float ma1Min) {
        this.ma1Min = ma1Min;
    }

    public float[] getMa2() {
        return ma2;
    }

    public float getMa2Max() {
        return ma2Max;
    }

    public void setMa2Max(float ma2Max) {
        this.ma2Max = ma2Max;
    }

    public float getMa2Min() {
        return ma2Min;
    }

    public void setMa2Min(float ma2Min) {
        this.ma2Min = ma2Min;
    }

    public float[] getMa3() {
        return ma3;
    }

    public float getMa3Max() {
        return ma3Max;
    }

    public void setMa3Max(float ma3Max) {
        this.ma3Max = ma3Max;
    }

    public float getMa3Min() {
        return ma3Min;
    }

    public void setMa3Min(float ma3Min) {
        this.ma3Min = ma3Min;
    }

    public float[] getMa4() {
        return ma4;
    }

    public float getMa4Max() {
        return ma4Max;
    }

    public void setMa4Max(float ma4Max) {
        this.ma4Max = ma4Max;
    }

    public float getMa4Min() {
        return ma4Min;
    }

    public void setMa4Min(float ma4Min) {
        this.ma4Min = ma4Min;
    }

    public float[] getMa5() {
        return ma5;
    }

    public float getMa5Max() {
        return ma5Max;
    }

    public void setMa5Max(float ma5Max) {
        this.ma5Max = ma5Max;
    }

    public float getMa5Min() {
        return ma5Min;
    }

    public void setMa5Min(float ma5Min) {
        this.ma5Min = ma5Min;
    }

    public float[] getMa6() {
        return ma6;
    }

    public float getMa6Max() {
        return ma6Max;
    }

    public void setMa6Max(float ma6Max) {
        this.ma6Max = ma6Max;
    }

    public float getMa6Min() {
        return ma6Min;
    }

    public void setMa6Min(float ma6Min) {
        this.ma6Min = ma6Min;
    }

    public float[] getMa18() {
        return ma18;
    }

    public float getMa18Max() {
        return ma18Max;
    }

    public void setMa18Max(float ma18Max) {
        this.ma18Max = ma18Max;
    }

    public float getMa18Min() {
        return ma18Min;
    }

    public void setMa18Min(float ma18Min) {
        this.ma18Min = ma18Min;
    }
}
