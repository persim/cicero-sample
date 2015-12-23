package edu.ufl.cicero.sample;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.ufl.cicero.cyber.CyberManager;
import edu.ufl.cicero.cyber.assess.AssessActivityAdapter;
import edu.ufl.cicero.cyber.aware.AwareActivityAdapter;
import edu.ufl.cicero.cyber.learn.LearnActivityAdapter;
import edu.ufl.cicero.cyber.plan.PlanActivityAdapter;

public class AssessOverviewActivity extends AppCompatActivity {

    // This value are assigned in this example.
    // Please, feel free to ask them to a user in the Plan Activity
    public static final double MILLIS_TO_WALK = 1000*60*60; //1h
    public static final int HOUR_TIME_TO_BE_NEAR_GYM = 18;
    public static final int MINUTE_TIME_TO_BE_NEAR_GYM = 30;
    public static final int SECOND_TIME_TO_BE_NEAR_GYM = 00;
    public static final int TOLERANCE_MILLIS_TIME_TO_BE_NEAR_GYM = 2*60*60*1000; //2h (+/- 1h)

    // User will insert them in the Plan Activity
    public static double location_of_gym_lat;
    public static double location_of_gym_lon;
    public static double location_of_gym_alt;
    public static long location_tolerance=1000; //1km


    private Button startButton;
    private TextView contentTextView;
    private TextView assessDebugTextView;
    private TextView assessValueTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final CyberManager cyberManager = CyberManager.getInstance();

        // setting activities
        cyberManager.setAssessActivityAdapter(new AssessActivityAdapter(AssessOverviewActivity.class));
        cyberManager.setAwareActivityAdapter(new AwareActivityAdapter(AwareActivity.class));
        cyberManager.setPlanActivityAdapter(new PlanActivityAdapter(PlanActivity.class));
        cyberManager.setLearnActivityAdapter(new LearnActivityAdapter(LearnActivity.class));

        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!cyberManager.isStarted()) {
                    // don't forget this!
                    cyberManager.start(AssessOverviewActivity.this);
                    refreshData();
                } else {
                    cyberManager.stop(AssessOverviewActivity.this);
                    refreshData();
                }
            }
        });

        contentTextView = (TextView)findViewById(R.id.assessOverviewContentTextView);
        assessDebugTextView = (TextView)findViewById(R.id.assessDebugTextView);
        assessValueTextView = (TextView)findViewById(R.id.assessTreeValueTextView);


    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshData();
    }

    @Override
    protected void onPause() {
        CyberManager.getInstance().notifyAssessActivityFinished(); // I use the application context because this Activity is going to be closed
        super.onPause();
    }

    private void refreshData() {
        //TODO better...
        //CyberManager.getInstance().getAssessTree().assess();
        if (CyberManager.getInstance().isStarted()) {
            startButton.setText("Stop");
            contentTextView.setText(R.string.assess_overview_running);
            assessDebugTextView.setText(CyberManager.getInstance().getAssessTree().toString());
            //assessValueTextView.setText(""+CyberManager.getInstance().getAssessTree().root.value);
        } else {
            startButton.setText("Start");
            contentTextView.setText(R.string.assess_overview_stopped);
            assessDebugTextView.setText("");
            assessValueTextView.setText("-");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CyberManager.MY_PERMISSIONS_REQUEST_ACCESS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    System.out.println("permission ok");
                    CyberManager.getInstance().start(AssessOverviewActivity.this);
                    refreshData();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    System.out.println("permission denied!");

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
