package chart.demo.ssw;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.ssw.stockchart.data.KlineChartData;
import com.ssw.stockchart.render.BaseRenderer;
import com.ssw.stockchart.render.BrokenLineRenderer;
import com.ssw.stockchart.render.CandleRenderer;
import com.ssw.stockchart.render.HistogramRenderer;
import com.ssw.stockchart.render.XAxisRenderer;
import com.ssw.stockchart.render.YAxisRenderer;
import com.ssw.stockchart.widget.CharView;

import java.util.Arrays;

import chart.demo.ssw.com.interchart.R;

public class MainActivity extends AppCompatActivity {

    CharView charView;
    KlineChartData data;

    Context context;

    float mainMax;

    float mainMin;

    float subMax;

    float subMin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        findView();
        initData();
        initView();
    }

    private void findView() {
        charView = findViewById(R.id.charView);
    }

    private void initView() {
        charView.setMainXAxisRenderer(new XAxisRenderer(false, BaseRenderer.K_DAY, this.context));
        charView.setMainYAxisRenderer(new YAxisRenderer(false, BaseRenderer.K_DAY, this.context));
        charView.setSubXAxisRenderer(new XAxisRenderer(true, BaseRenderer.VOLUME, this.context));
        charView.setSubYAxisRenderer(new YAxisRenderer(true, BaseRenderer.VOLUME, this.context));

        charView.addMainRenderer(new CandleRenderer(false, false, BaseRenderer.K_DAY, this.context));
        charView.addMainRenderer(new BrokenLineRenderer(index -> data.getMa2ByIndex(index), Color.parseColor("#00ff00"), false, false, BaseRenderer.K_DAY, this.context));

        charView.addSubRenderer(new HistogramRenderer(false, true, BaseRenderer.VOLUME, this.context));
        charView.addSubRenderer(new BrokenLineRenderer(index -> data.getMa1ByIndex(index), Color.parseColor("#ff00ff"), false, true, BaseRenderer.ASI, this.context));

        //  charView.addSubRenderer(new BollRenderer(true, BaseRenderer.BOLL, this.context));

        charView.setMaxMin(mainMax, mainMin, subMax, subMin);
        charView.setMaxMinFlow(mainMax, mainMin, subMax, subMin);

        charView.setData(data);

        charView.invalidate();
    }

    private void initData() {

        int size = 300;

        float[] yes = new float[size];
        float[] open = new float[size];
        float[] close = new float[size];
        float[] height = new float[size];
        float[] low = new float[size];
        float[] turnover = new float[size];
        float[] turnoverUp = new float[size];

        float[] ma1 = new float[size];
        float[] ma2 = new float[size];
        float[] ma3 = new float[size];
        float[] ma4 = new float[size];
        float[] ma5 = new float[size];

        float openMax = 0;
        float openMin = 0;
        float closeMax = 0;
        float closeMin = 0;
        float heightMax = 0;
        float heightMin = 0;
        float lowMax = 0;
        float lowMin = 0;
        float turnoverMax = 0;
        float turnoverMin = 0;

        float ma1Max = 0;
        float ma1Min = 0;
        float ma2Max = 0;
        float ma2Min = 0;
        float ma3Max = 0;
        float ma3Min = 0;
        float ma4Max = 0;
        float ma4Min = 0;
        float ma5Max = 0;
        float ma5Min = 0;

        for (int i = 0; i < size; i++) {
            open[i] = (float) (20f * Math.random());
            if (openMax == 0) {
                openMax = open[i];
            } else {
                openMax = openMax > open[i] ? openMax : open[i];
            }
            if (openMin == 0) {
                openMin = open[i];
            } else {
                openMin = openMin < open[i] ? openMin : open[i];
            }
            close[i] = (float) (20f * Math.random());
            if (closeMax == 0) {
                closeMax = close[i];
            } else {
                closeMax = closeMax > close[i] ? closeMax : open[i];
            }
            if (closeMin == 0) {
                closeMin = close[i];
            } else {
                closeMin = closeMin < close[i] ? closeMin : close[i];
            }
            height[i] = Math.max(open[i], close[i]) + 10;
            if (heightMax == 0) {
                heightMax = height[i];
            } else {
                heightMax = heightMax > height[i] ? heightMax : height[i];
            }
            if (heightMin == 0) {
                heightMin = height[i];
            } else {
                heightMin = heightMin < height[i] ? heightMin : height[i];
            }
            low[i] = Math.min(open[i], close[i]) / 3 * 2;

            if (lowMax == 0) {
                lowMax = low[i];
            } else {
                lowMax = lowMax > low[i] ? lowMax : low[i];
            }
            if (lowMin == 0) {
                lowMin = low[i];
            } else {
                lowMin = lowMin < low[i] ? lowMin : low[i];
            }

            turnover[i] = (float) (20f * Math.random());
            if (turnoverMax == 0) {
                turnoverMax = turnover[i];
            } else {
                turnoverMax = turnoverMax > turnover[i] ? turnoverMax : turnover[i];
            }
            if (turnoverMin == 0) {
                turnoverMin = turnover[i];
            } else {
                turnoverMin = turnoverMin < turnover[i] ? turnoverMin : turnover[i];
            }

            if (i == 0) {
                yes[i] = (float) (20f * Math.random());
            } else {
                yes[i] = close[i - 1];
            }

            turnoverUp[i] = (open[i] - close[i]) > 0 ? -1f : 1f;

            ma1[i] = (float) (20f * Math.random());
            if (ma1Max == 0) {
                ma1Max = ma1[i];
            } else {
                ma1Max = ma1Max > ma1[i] ? ma1Max : ma1[i];
            }
            if (ma1Min == 0) {
                ma1Min = ma1[i];
            } else {
                ma1Min = ma1Min < ma1[i] ? ma1Min : ma1[i];
            }
            ma2[i] = (float) (20f * Math.random());
            if (ma2Max == 0) {
                ma2Max = ma2[i];
            } else {
                ma2Max = ma2Max > ma2[i] ? ma2Max : ma2[i];
            }
            if (ma2Min == 0) {
                ma2Min = ma2[i];
            } else {
                ma2Min = ma2Min < ma2[i] ? ma2Min : ma2[i];
            }
            ma3[i] = (float) (20f * Math.random());
            if (ma3Max == 0) {
                ma3Max = ma3[i];
            } else {
                ma3Max = ma3Max > ma3[i] ? ma3Max : ma3[i];
            }
            if (ma3Min == 0) {
                ma3Min = ma3[i];
            } else {
                ma3Min = ma3Min < ma3[i] ? ma3Min : ma3[i];
            }
            ma4[i] = (float) (20f * Math.random());
            if (ma4Max == 0) {
                ma4Max = ma4[i];
            } else {
                ma4Max = ma4Max > ma4[i] ? ma4Max : ma4[i];
            }
            if (ma4Min == 0) {
                ma4Min = ma4[i];
            } else {
                ma4Min = ma4Min < ma4[i] ? ma4Min : ma4[i];
            }
            ma5[i] = (float) (20f * Math.random());
            if (ma5Max == 0) {
                ma5Max = ma5[i];
            } else {
                ma5Max = ma5Max > ma5[i] ? ma5Max : ma5[i];
            }
            if (ma5Min == 0) {
                ma5Min = ma5[i];
            } else {
                ma5Min = ma5Min < ma5[i] ? ma5Min : ma5[i];
            }
        }

        data = new KlineChartData();

        data.setTurnover(turnover);
        data.setTurnoverMax(turnoverMax);
        data.setTurnoverMin(turnoverMin);
        data.setTurnoverUp(turnoverUp);
        data.setOpenPrice(open);
        data.setOpenPriceMax(openMax);
        data.setOpenPriceMin(openMin);
        data.setClosePrice(close);
        data.setClosePriceMax(closeMax);
        data.setClosePriceMin(closeMin);
        data.setHeightPrice(height);
        data.setHeightPriceMax(heightMax);
        data.setHeightPriceMin(heightMin);
        data.setLowPrice(low);
        data.setLowPriceMax(lowMax);
        data.setLowPriceMin(lowMin);
        data.setLastClose(yes);

        data.setMa1(ma1);
        data.setMa1Max(ma1Max);
        data.setMa1Min(ma1Min);
        data.setMa2(ma2);
        data.setMa2Max(ma2Max);
        data.setMa2Min(ma2Min);
        data.setMa3(ma3);
        data.setMa3Max(ma3Max);
        data.setMa3Min(ma3Min);
        data.setMa4(ma4);
        data.setMa4Max(ma4Max);
        data.setMa4Min(ma4Min);
        data.setMa5(ma5);
        data.setMa5Max(ma5Max);
        data.setMa5Min(ma5Min);

        float[] max = new float[]{openMax, closeMax, lowMax, heightMax, ma1Max, ma2Max, ma3Max, ma4Max, ma5Max};
        float[] min = new float[]{openMin, closeMin, lowMin, heightMin, ma1Min, ma2Min, ma3Min, ma4Min, ma5Min};
        Arrays.sort(max);
        Arrays.sort(min);
        mainMax = max[max.length - 1];
        mainMin = min[0];

        subMax = turnoverMax;
        subMin = turnoverMin;

    }
}
