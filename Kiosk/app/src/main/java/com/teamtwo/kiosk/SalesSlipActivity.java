// 통계 그래프 클래스
package com.teamtwo.kiosk;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SalesSlipActivity extends AppCompatActivity {

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_slip);

        // PieChart 위젯을 레이아웃에서 찾아서 참조합니다.
        pieChart = (PieChart) findViewById(R.id.piechart);

        // PieChart의 여러 속성을 설정합니다.
        setupPieChart();

        // 오늘 년도+월 초기값 설정
        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR); // 현재 년도
        int curMonth = calendar.get(Calendar.MONTH) + 1;

        // Node.js 서버로부터 메뉴 데이터를 받아옵니다.
        getMenuDataAndRefreshChart(curYear, curMonth);

    }

    private void setupPieChart() {
        // PieChart의 여러 속성을 설정합니다.
        pieChart.setUsePercentValues(true); // 차트의 값을 백분율로 표시
        pieChart.getDescription().setEnabled(false); // 불필요한 텍스트 표시 x
        pieChart.setExtraOffsets(150, 10, 5, 5);

        // 드래그 감속을 위한 계수를 설정합니다.
        pieChart.setDragDecelerationFrictionCoef(0.50f);

        // PieChart (파이 조각) 레이블 텍스트 크기 설정
        float entryLabelTextSize = 20f; // 원하는 크기로 설정
        pieChart.setEntryLabelTextSize(entryLabelTextSize);

        pieChart.setDrawHoleEnabled(false); // 중앙 홀
        pieChart.setHoleColor(Color.WHITE); // 차트 텍스트 색상
        pieChart.setTransparentCircleRadius(61f); // 홀 주변 투명 원형 경계 반지름
    }

    // 파이 차트 업데이트
    private void updatePieChart(ArrayList<MenuData> menuData) {
        ArrayList<PieEntry> yValues = new ArrayList<>(); // 파이 차트에 표시될 데이터 리스트
        for (MenuData menu : menuData) {
            yValues.add(new PieEntry(menu.getSalesPercentage(), menu.getMenuName()));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f); // 파이 조각 사이의 간격
        dataSet.setSelectionShift(20f); // 파이 조각 클릭시 이동하는 양

        // 파이애 색상 적용
        int[] colors = new int[]{
            Color.parseColor("#C0392B"), // 빨강
            Color.parseColor("#D35400"), // 주황
            Color.parseColor("#F1C40F"), // 노랑
            Color.parseColor("#27AE60"), // 초록
            Color.parseColor("#2980B9"), // 파랑
            Color.parseColor("#4A148C"), // 남색
            Color.parseColor("#9B59B6"), // 보라
        };

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet); // 차트에 표시될 데이터 정보

        // 값의 비율의 크기 + 색
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.YELLOW);

        // 차트 애니메이션 적용
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        pieChart.setData(data);

        // 차트의 범례 적용
        Legend legend = pieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // 세로 방향
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM); // 정렬(아래)
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT); // 전체 위치
        legend.setFormSize(20f);
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(50f);
        legend.setYOffset(80f);
        legend.setXOffset(90f);
    }


    // 파이 차트에 표시되는 데이터를 업데이트
    private void getMenuDataAndRefreshChart(int year, int month) {
        ArrayList<MenuData> menuDataList = getMenuDataFromServer(year, month); // db 데이터를 보냄
        updatePieChart(menuDataList); // 업데이트 차트에 인수로 넣음
    }

    // 서버 요청
    private ArrayList<MenuData> getMenuDataFromServer(int year, int month) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id", LoginActivity.cur_id);
            jsonBody.put("password", LoginActivity.cur_pw);
            jsonBody.put("year", year);
            jsonBody.put("month", month);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<MenuData> menuDataList = new ArrayList<>();

        ServerCommunicationHelper.sendRequest(
                "https://sm-kiosk.kro.kr/order/monthOrder",
                ServerCommunicationHelper.HttpMethod.POST,
                jsonBody,
                new ServerCommunicationHelper.ResultCallback() {
                    @Override
                    public void onSuccess(String responseBody) { // json 데이터 파싱
                        try {
                            JSONObject responseJson = new JSONObject(responseBody);
                            JSONObject data = responseJson.getJSONObject("data");
                            JSONObject count = data.getJSONObject("count");
                            String totalPriceValue = data.getString("totalPrice");

                            // 서버로부터 가져온 totalPrice 값을 텍스트뷰에 업데이트합니다.
                            TextView textTotalPrice = findViewById(R.id.textTotalPrice);
                            textTotalPrice.setText("총 매출 : " + totalPriceValue + "원");

                            Iterator<String> keysIterator = count.keys();

                            while (keysIterator.hasNext()) {
                                String menu = keysIterator.next();
                                float value = (float) count.getDouble(menu);
                                menuDataList.add(new MenuData(menu, value)); // 추가
                            }

                            // 메뉴 데이터 리스트를 7개로 제한
                            if(menuDataList.size() > 7){
                                // 7부터 끝까지 부분 리스트를 지움
                                menuDataList.subList(7, menuDataList.size()).clear();
                            }

                            // 서버로부터 데이터를 가져온 후에 PieChart를 업데이트합니다.
                            updatePieChart(menuDataList);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("xxx");
                    }
                }
        );
        return menuDataList;
    }
}

// 메뉴 데이터의 정보를 저장하는 역할
class MenuData {
    private String menuName;
    private float salesPercentage;

    public MenuData(String menuName, float salesPercentage) {
        this.menuName = menuName;
        this.salesPercentage = salesPercentage;
    }
    public String getMenuName() {
        return menuName;
    }
    public float getSalesPercentage() {
        return salesPercentage;
    }
}

