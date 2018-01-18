package pdb.cm.fc.ul.pt.pdb.activities.medico.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import pdb.cm.fc.ul.pt.pdb.R;
import pdb.cm.fc.ul.pt.pdb.models.BallScore;
import pdb.cm.fc.ul.pt.pdb.models.Doente;
import pdb.cm.fc.ul.pt.pdb.models.Shake;
import pdb.cm.fc.ul.pt.pdb.models.WordScore;
import pdb.cm.fc.ul.pt.pdb.utilities.Utilities;

import static android.content.ContentValues.TAG;
import static pdb.cm.fc.ul.pt.pdb.activities.medico.MedicoDashboardMainActivity.EXTRA_DOENTE;

/**
 * Created by nunonelas on 24/12/17.
 */

public class MedicoDashboardFragment extends Fragment implements OnChartValueSelectedListener {

    private LineChart mChartWords;
    private LineChart mChartBall;
    private LineChart mChartShake;

    private static final String TBL_SHAKES = "shake";
    private static final String TBL_WORDSSCORES = "wordscores";
    private static final String TBL_BALLSCORES = "ballscores";

    private static final String ATR_DATE = "date";

    private ArrayList<Entry> shakes;
    private ArrayList<Entry> timeBall;
    private ArrayList<Entry> scoreBall;

    private ArrayList<Entry> timeWords;
    private ArrayList<Entry> scoreWords;
    private ArrayList<Entry> faultsWords;

    private ArrayList<Integer> hoursList;

    private HashMap<Integer, String> nameOfDayOfWeek;

    private Doente doente;

    public static MedicoDashboardFragment newInstance() {
        MedicoDashboardFragment fragment = new MedicoDashboardFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medico_dashboard, container, false);

        doente = (Doente) getActivity().getIntent().getExtras().getSerializable(EXTRA_DOENTE);

        ((TextView) view.findViewById(R.id.name)).setText(doente.getName());
        ((TextView) view.findViewById(R.id.age)).setText(doente.getAge());

        mChartWords = (LineChart) view.findViewById(R.id.chartWords);
        mChartBall = (LineChart) view.findViewById(R.id.chartBall);
        mChartShake = (LineChart) view.findViewById(R.id.chartAcell);

        getAxisLabel();
        fetchAllWordsGameData();
        fetchAllBallGameData();
        fetchAllShakeData();

        return view;
    }

    /*
     * PARA CONFIGURAR O GRÁFICO
     *
     *      CHART WORDS
     */

    private void createChartWords(){
        mChartWords.setOnChartValueSelectedListener(this);

        // no description text
        mChartWords.getDescription().setEnabled(false);

        // enable touch gestures
        mChartWords.setTouchEnabled(true);

        mChartWords.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChartWords.setDragEnabled(true);
        mChartWords.setScaleEnabled(false);
        mChartWords.setDrawGridBackground(false);
        mChartWords.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChartWords.setPinchZoom(true);

        // set an alternative background color
        //mChart.setBackgroundColor(Color.WHITE);

        // add data
        setDataWords();

        mChartWords.animateX(2500);

        configureLegendWords();
        configureAxisWords();
    }

    private void configureLegendWords(){
        // get the legend (only possible after setting data)
        Legend l = mChartWords.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);
    }

    private void configureAxisWords(){
        XAxis xAxis = mChartWords.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setAxisMinimum(1f);
        xAxis.setAxisMaximum(7f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return nameOfDayOfWeek.get((int)value);
            }
        });

        YAxis leftAxis = mChartWords.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        //leftAxis.setAxisMaximum(200f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(false);
        //leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = mChartWords.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void setDataWords() {

        LineDataSet set1, set2, set3;

        if (mChartWords.getData() != null &&
                mChartWords.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChartWords.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) mChartWords.getData().getDataSetByIndex(1);
            set3 = (LineDataSet) mChartWords.getData().getDataSetByIndex(2);
            set1.setValues(timeWords);
            set2.setValues(scoreWords);
            set3.setValues(faultsWords);
            mChartWords.getData().notifyDataChanged();
            mChartWords.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(timeWords, "Time (s)");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);
            set1.setDrawCircles(false);
            set1.setDrawValues(false);
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            // create a dataset and give it a type
            set2 = new LineDataSet(scoreWords, "Score");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.RED);
            set2.setCircleColor(Color.BLACK);
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(Color.RED);
            set2.setDrawCircleHole(false);
            set2.setDrawCircles(false);
            set2.setDrawValues(false);
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            //set2.setFillFormatter(new MyFillFormatter(900f));

            set3 = new LineDataSet(faultsWords, "Faults");
            set3.setAxisDependency(YAxis.AxisDependency.LEFT);
            set3.setColor(Color.YELLOW);
            set3.setCircleColor(Color.BLACK);
            set3.setLineWidth(2f);
            set3.setCircleRadius(3f);
            set3.setFillAlpha(65);
            set3.setFillColor(ColorTemplate.colorWithAlpha(Color.YELLOW, 200));
            set3.setDrawCircleHole(false);
            set3.setDrawCircles(false);
            set3.setDrawValues(false);
            set3.setHighLightColor(Color.rgb(244, 117, 117));

            // create a data object with the datasets
            LineData data = new LineData(set1, set2, set3);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);

            // set data
            mChartWords.setData(data);
        }
    }

     /*
     * PARA CONFIGURAR O GRÁFICO
     *
     *      CHART BALL
     */

    private void createChartBall(){
        mChartBall.setOnChartValueSelectedListener(this);

        // no description text
        mChartBall.getDescription().setEnabled(false);

        // enable touch gestures
        mChartBall.setTouchEnabled(true);

        mChartBall.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChartBall.setDragEnabled(true);
        mChartBall.setScaleEnabled(false);
        mChartBall.setDrawGridBackground(false);
        mChartBall.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChartBall.setPinchZoom(true);

        // set an alternative background color
        //mChart.setBackgroundColor(Color.WHITE);

        // add data
        setDataBall();

        mChartBall.animateX(2500);

        configureLegendBall();
        configureAxisBall();
    }

    private void configureLegendBall(){
        // get the legend (only possible after setting data)
        Legend l = mChartBall.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);
    }

    private void configureAxisBall(){
        XAxis xAxis = mChartBall.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setAxisMinimum(1f);
        xAxis.setAxisMaximum(7f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return nameOfDayOfWeek.get((int)value);
            }
        });

        YAxis leftAxis = mChartBall.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        //leftAxis.setAxisMaximum(200f);
        //leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(false);
        //leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = mChartBall.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void setDataBall() {

        LineDataSet set1, set2;

        if (mChartBall.getData() != null &&
                mChartBall.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChartBall.getData().getDataSetByIndex(0);
            set2 = (LineDataSet) mChartBall.getData().getDataSetByIndex(1);
            set1.setValues(timeBall);
            set2.setValues(scoreBall);
            mChartBall.getData().notifyDataChanged();
            mChartBall.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(timeBall, "Time (s)");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);
            set1.setDrawCircles(false);
            set1.setDrawValues(false);
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);

            // create a dataset and give it a type
            set2 = new LineDataSet(scoreBall, "Score");
            set2.setAxisDependency(YAxis.AxisDependency.LEFT);
            set2.setColor(Color.RED);
            set2.setCircleColor(Color.BLACK);
            set2.setLineWidth(2f);
            set2.setCircleRadius(3f);
            set2.setFillAlpha(65);
            set2.setFillColor(Color.RED);
            set2.setDrawCircleHole(false);
            set2.setDrawCircles(false);
            set2.setDrawValues(false);
            set2.setHighLightColor(Color.rgb(244, 117, 117));
            //set2.setFillFormatter(new MyFillFormatter(900f));

            // create a data object with the datasets
            LineData data = new LineData(set1, set2);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);

            // set data
            mChartBall.setData(data);
        }
    }

     /*
     * PARA CONFIGURAR O GRÁFICO
     *
     *      CHART SHAKE
     */

    private void createChartShake(){
        mChartShake.setOnChartValueSelectedListener(this);

        // no description text
        mChartShake.getDescription().setEnabled(false);

        // enable touch gestures
        mChartShake.setTouchEnabled(true);

        mChartShake.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChartShake.setDragEnabled(true);
        mChartShake.setScaleEnabled(false);
        mChartShake.setDrawGridBackground(false);
        mChartShake.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChartShake.setPinchZoom(true);

        // set an alternative background color
        //mChart.setBackgroundColor(Color.WHITE);

        // add data
        setDataShake();

        mChartShake.animateX(2500);

        configureLegendShake();
        configureAxisShake();
    }

    private void configureLegendShake(){
        // get the legend (only possible after setting data)
        Legend l = mChartShake.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);
    }

    private void configureAxisShake(){
        XAxis xAxis = mChartShake.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(false);
        xAxis.setAxisMinimum(Utilities.getHourRightNow()-2f);
        xAxis.setAxisMaximum(Utilities.getHourRightNow()+1f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //HH.MM
                String hold = String.format("%02.02f", value);
                hold = hold.replace(',', ':');
                return hold+"h";
            }
        });

        YAxis leftAxis = mChartShake.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setDrawGridLines(true);
        leftAxis.setDrawAxisLine(false);
        //leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = mChartShake.getAxisRight();
        rightAxis.setEnabled(false);
    }

    private void setDataShake() {

        LineDataSet set1;

        if (mChartShake.getData() != null &&
                mChartShake.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChartShake.getData().getDataSetByIndex(0);
            set1.setValues(shakes);
            mChartShake.getData().notifyDataChanged();
            mChartShake.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(shakes, "Usage");

            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(ColorTemplate.getHoloBlue());
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(2f);
            set1.setCircleRadius(1f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);
            set1.setDrawCircles(false);
            set1.setDrawValues(false);
            //set1.setFillFormatter(new MyFillFormatter(0f));
            //set1.setDrawHorizontalHighlightIndicator(false);
            //set1.setVisible(false);
            //set1.setCircleHoleColor(Color.WHITE);


            // create a data object with the datasets
            LineData data = new LineData(set1);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(9f);

            // set data
            mChartShake.setData(data);
        }
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());

        //mChartWords.centerViewToAnimated(e.getX(), e.getY(), mChartWords.getData().getDataSetByIndex(h.getDataSetIndex())
        //        .getAxisDependency(), 500);
        //mChart.zoomAndCenterAnimated(2.5f, 2.5f, e.getX(), e.getY(), mChart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
        //mChart.zoomAndCenterAnimated(1.8f, 1.8f, e.getX(), e.getY(), mChart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
    }

    @Override
    public void onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.");
    }

    private void fetchAllWordsGameData() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_WORDSSCORES + "/" + doente.getId());
        Query query = databaseReference.orderByChild(ATR_DATE).startAt(Utilities.getFirstDayOfCurrentWeek());
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scoreWords = new ArrayList<>();
                timeWords = new ArrayList<>();
                faultsWords = new ArrayList<>();
                for (DataSnapshot scoresSnapshot: dataSnapshot.getChildren()) {
                    WordScore wordScore = scoresSnapshot.getValue(WordScore.class);
                    try {
                        Calendar c = Calendar.getInstance();
                        c.setTime(new SimpleDateFormat("dd/M/yyyy HH:mm:ss").parse(wordScore.getDate()));
                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                        int hours = c.get(Calendar.HOUR_OF_DAY);
                        int minutes = c.get(Calendar.MINUTE);

                        float time = convertDate(dayOfWeek, hours, minutes);

                        scoreWords.add(new Entry(time, (float) wordScore.getScore()));
                        timeWords.add(new Entry(time, (float) wordScore.getTime()));
                        faultsWords.add(new Entry(time, (float) wordScore.getFaults()));

                    } catch (java.text.ParseException e){

                    }
                }
                createChartWords();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    private void fetchAllBallGameData() {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_BALLSCORES + "/" + doente.getId());
        Query query = databaseReference.orderByChild(ATR_DATE).startAt(Utilities.getFirstDayOfCurrentWeek());
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                scoreBall = new ArrayList<>();
                timeBall = new ArrayList<>();
                for (DataSnapshot scoresSnapshot: dataSnapshot.getChildren()) {
                    BallScore ballScore = scoresSnapshot.getValue(BallScore.class);
                    try {
                        Calendar c = Calendar.getInstance();
                        c.setTime(new SimpleDateFormat("dd/M/yyyy HH:mm:ss").parse(ballScore.getDate()));
                        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                        int hours = c.get(Calendar.HOUR_OF_DAY);
                        int minutes = c.get(Calendar.MINUTE);

                        float time = convertDate(dayOfWeek, hours, minutes);

                        scoreBall.add(new Entry(time, (float) ballScore.getScore()));
                        timeBall.add(new Entry(time, (float) ballScore.getTime()));

                    } catch (java.text.ParseException e){

                    }
                }
                createChartBall();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    private void fetchAllShakeData() {
        hoursList = new ArrayList<>();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(TBL_SHAKES + "/" + doente.getId());
        Query query = databaseReference.orderByChild(ATR_DATE).startAt(Utilities.getLastDay());
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shakes = new ArrayList<>();
                for (DataSnapshot shakesSnapshot: dataSnapshot.getChildren()) {
                    Shake shake = shakesSnapshot.getValue(Shake.class);
                    try {
                        Calendar c = Calendar.getInstance();
                        c.setTime(new SimpleDateFormat("dd/M/yyyy HH:mm:ss").parse(shake.getDate()));
                        int hours = c.get(Calendar.HOUR_OF_DAY);
                        int minutes = c.get(Calendar.MINUTE);

                        String hold = String.format("%02d.%02d", hours, minutes);

                        float time = Float.valueOf(hold);

                        shakes.add(new Entry(time, (float) shake.getShake()));

                        hoursList.add(hours);
                    } catch (java.text.ParseException e){

                    }

                }
                createChartShake();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e(TAG, "Failed to read value. ", error.toException());
            }
        });
    }

    private float convertDate (int dayOfWeek, int hour, int minute){
        int maxHourOfDay = 2359;
        int mHour = Integer.valueOf(String.valueOf(hour) + String.valueOf(minute));

        double convHour = (mHour*0.99)/maxHourOfDay;
        double hold = dayOfWeek + convHour;
        return (float) hold;
    }

    private void getAxisLabel(){
        nameOfDayOfWeek = new HashMap<>();
        nameOfDayOfWeek.put(1, "Sunday");
        nameOfDayOfWeek.put(2, "Monday");
        nameOfDayOfWeek.put(3, "Tuesday");
        nameOfDayOfWeek.put(4, "Wednesday");
        nameOfDayOfWeek.put(5, "Thursday");
        nameOfDayOfWeek.put(6, "Friday");
        nameOfDayOfWeek.put(7, "Saturday");
    }
}
