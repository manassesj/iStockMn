package bsi.mpoo.istock.gui.fragments;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

import bsi.mpoo.istock.R;
import bsi.mpoo.istock.domain.Session;
import bsi.mpoo.istock.services.order.OrderServices;

public class Home extends AppCompatActivity {

    BarChart barChart;
    OrderServices orderServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        orderServices = new OrderServices(getApplicationContext());
        ArrayList<Integer> months = orderServices.dateForMonth(Session.getInstance().getAdministrator());

        barChart = findViewById(R.id.grahpRec);


        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1f,months.get(0)));
        entries.add(new BarEntry(2f,months.get(1)));
        entries.add(new BarEntry(3f,months.get(2)));
        entries.add(new BarEntry(4f,months.get(3)));
        entries.add(new BarEntry(5f,months.get(4)));
        entries.add(new BarEntry(6f,months.get(5)));
        entries.add(new BarEntry(7f,months.get(6)));
        entries.add(new BarEntry(8f,months.get(7)));
        entries.add(new BarEntry(9f,months.get(8)));
        entries.add(new BarEntry(10f,months.get(9)));
        entries.add(new BarEntry(11f,months.get(10)));
        entries.add(new BarEntry(12f,months.get(11)));
        BarDataSet barDataSet = new BarDataSet(entries,"Meses");


        BarData barData = new BarData(barDataSet);
        barDataSet.setColor(getColor(R.color.colorAccent));
        barData.setBarWidth(1f);
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.invalidate();

    }


}
