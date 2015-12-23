package edu.ufl.cicero.sample;

import android.app.PendingIntent;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;

import edu.ufl.cicero.cyber.CyberManager;
import edu.ufl.cicero.cyber.assess.behavior.Activity;
import edu.ufl.cicero.cyber.assess.behavior.ActivityName;
import edu.ufl.cicero.cyber.assess.behavior.Context;
import edu.ufl.cicero.cyber.assess.behavior.ContextName;
import edu.ufl.cicero.cyber.assess.behavior.Device;
import edu.ufl.cicero.cyber.assess.behavior.DeviceActionName;
import edu.ufl.cicero.cyber.assess.behavior.DeviceName;
import edu.ufl.cicero.sentience.context.ContextDeadline;
import edu.ufl.cicero.sentience.context.clock.info.DurationAdditionalInfo;
import edu.ufl.cicero.sentience.context.clock.info.FrequencyAdditionalInfo;
import edu.ufl.cicero.sentience.detect.location.LocationAdditionalInfo;

public class PlanActivity extends AppCompatActivity {
    private boolean allPlanned=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button = (Button)findViewById(R.id.finishedPlanButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // setting "be near gym at a fixed time"
                EditText latET = (EditText)findViewById(R.id.latEditText);
                EditText lonET = (EditText)findViewById(R.id.lonEditText);
                EditText altET = (EditText)findViewById(R.id.altEditText);

                AssessOverviewActivity.location_of_gym_lat=Double.parseDouble(latET.getText().toString());
                AssessOverviewActivity.location_of_gym_lon=Double.parseDouble(lonET.getText().toString());
                AssessOverviewActivity.location_of_gym_alt=Double.parseDouble(altET.getText().toString());

                Location loc = new Location("PlanActivity");
                loc.setLatitude(AssessOverviewActivity.location_of_gym_lat);
                loc.setLongitude(AssessOverviewActivity.location_of_gym_lon);
                loc.setAltitude(AssessOverviewActivity.location_of_gym_alt);

                if (!CyberManager.getInstance().isStarted()) {
                    // setting "be at gym 3 times in a week"
                    CyberManager.getInstance().addGoalRelatedActivityPNode(
                            getApplicationContext(), // I use the application context because maybe this Activity will be closed
                            new Device(DeviceName.SmartPhone, DeviceActionName.DetectUserLocation),
                            new Activity(ActivityName.BeingAt),
                            new Context(ContextName.Frequency),
                            3, //3 times
                            new LocationAdditionalInfo(loc, AssessOverviewActivity.location_tolerance),
                            new FrequencyAdditionalInfo(new ContextDeadline(
                                    Calendar.getInstance().getTimeInMillis() + 1000 * 60 * 60 * 24 * 7, //TODO every week from midnight?
                                    1000 * 60 * 60 * 24 * 7 //every week from now
                            )),
                            true //add also dual activities
                    );

                    // setting "walking for 20 minutes every day"
                    CyberManager.getInstance().addGoalRelatedActivityPNode(
                            getApplicationContext(), // I use the application context because maybe this Activity will be closed
                            new Device(DeviceName.SmartPhone, DeviceActionName.DetectUserMotion),
                            new Activity(ActivityName.Walking),
                            new Context(ContextName.Duration),
                            AssessOverviewActivity.MILLIS_TO_WALK,
                            null,
                            new DurationAdditionalInfo(new ContextDeadline(
                                    Calendar.getInstance().getTimeInMillis() + 1000 * 60 * 60 * 24, //TODO every day from midnight?
                                    1000 * 60 * 60 * 24 //every day from now
                            )),
                            true //add also dual activities
                    );

                    //adding notification
                    Intent notificationIntent = new Intent(CyberManager.getInstance().getNotificationContext(), CyberManager.getInstance().getAssessActivityAdapter().getAssessActivityClass());
                    PendingIntent pendingIntent = PendingIntent.getActivity(CyberManager.getInstance().getNotificationContext(),1,notificationIntent,0);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(CyberManager.getInstance().getNotificationContext())
                                    .setSmallIcon(R.drawable.ic_stat_name)
                                    .setContentTitle("Hey Champion!")
                                    .setContentText("Do you remember to walking, don't you? Have a good running today!")
                                    .setContentIntent(pendingIntent)
                                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)); //Required on Gingerbread and below
                    CyberManager.getInstance().addPeriodicalNotification(mBuilder.build(), 1000*60*60*24); //a notification per day
                }

                allPlanned=true;
                finish();

            }
        });

        CyberManager.getInstance().addGoalSettingStatus(
                getApplicationContext(),
                DeviceName.SmartPhone
        );


    }

    @Override
    protected void onPause() {




        CyberManager.getInstance().notifyPlanActivityFinished(allPlanned); // I use the application context because this Activity is going to be closed

        super.onPause();
    }


}
