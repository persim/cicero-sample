package edu.ufl.cicero.sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import edu.ufl.cicero.cyber.CyberManager;
import edu.ufl.cicero.cyber.assess.behavior.Activity;
import edu.ufl.cicero.cyber.assess.behavior.ActivityName;
import edu.ufl.cicero.cyber.assess.behavior.Context;
import edu.ufl.cicero.cyber.assess.behavior.ContextName;
import edu.ufl.cicero.cyber.assess.behavior.Device;
import edu.ufl.cicero.cyber.assess.behavior.DeviceActionName;
import edu.ufl.cicero.cyber.assess.behavior.DeviceName;
import edu.ufl.cicero.sentience.context.sensors.SensorAdditionalInfo;

/**
 * Created by antonello on 09/06/2015.
 */
public class LearnActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayAdapter<String> listAdapter ;
    private boolean tap=false;
    private boolean allFinished=false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView)findViewById(R.id.learnListView);
        listAdapter = new ArrayAdapter<String>(this, R.layout.snippet_item1, new ArrayList<String>());
        listAdapter.add("Right way to walk");
        listAdapter.add("Step by step guide");
        listAdapter.add("A therapist shows how to walk");
        listAdapter.add("Beginning a fitness program");
        listAdapter.add("Best time of day to walk");

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);

        if (!CyberManager.getInstance().isStarted()) {
            // Walking with too light, maybe is not a good idea... too UVs!
            CyberManager.getInstance().addBehaviorKnowledgeNNode(
                    this,
                    new Device(DeviceName.SmartPhone, DeviceActionName.DetectUserMotion),
                    new Activity(ActivityName.Walking),
                    new Context(ContextName.Light),
                    0, //not relevant
                    null, //not relevant
                    new SensorAdditionalInfo(50000.0, true), //more then 50000 lux (sunlight at midday)
                    true // add also the dual
            );
        }

    }

    @Override
    public void onResume(){
        super.onResume();

        if(tap) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want read another suggestion about how walk correctly?")
                    .setCancelable(false)
                    .setTitle("Bravo! Do you want to continue reading?")
                    .setNegativeButton("No, thanks.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            allFinished=true;
                            finish();
                        }
                    })
                    .setPositiveButton("Yes!", null);
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String url = null;
        switch (position) {
            case 0:
                tap=true;
                url = "http://www.realsimple.com/health/fitness-exercise/how-to-walk";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                break;
            case 1:
                tap=true;
                url = "http://www.telegraph.co.uk/lifestyle/wellbeing/7935172/How-to-walk-A-step-by-step-guide.html";
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent2);
                break;
            case 2:
                tap=true;
                url = "https://www.youtube.com/watch?v=-fD2TSL2s7I";
                Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent3);
                break;
            case 3:
                tap=true;
                url = "http://www.thewalkingsite.com/beginner.html";
                Intent intent4 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent4);
                break;
            case 4:
                tap=true;
                url = "http://www.prevention.com/fitness/fitness-tips/best-time-day-walk";
                Intent intent5 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent5);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {

        CyberManager.getInstance().notifyLearnActivityFinished(allFinished); // I use the application context because this Activity is going to be closed

        super.onPause();
    }
}
