package edu.ufl.cicero.sample;

/**
 * Created by antonello on 17/06/2015.
 */

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


public class AwareActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ArrayAdapter<String> listAdapter ;
    private boolean tap=false;
    private boolean allFinished=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aware);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listView = (ListView)findViewById(R.id.awareListView);
        listAdapter = new ArrayAdapter<String>(this, R.layout.snippet_item1, new ArrayList<String>());
        listAdapter.add("Top 10 health benefits");
        listAdapter.add("FAQ about Walking");
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);

        if (!CyberManager.getInstance().isStarted()) {
            // Walking more than before
            CyberManager.getInstance().addBehaviorAwarePNode(
                    this,
                    new Device(DeviceName.SmartPhone, DeviceActionName.DetectUserMotion),
                    new Activity(ActivityName.MoreWalking),
                    new Context(ContextName.Duration),
                    0, //not relevant
                    null, //not relevant
                    null, //not relevant
                    true // add also the dual
            );
        }

    }

    @Override
    public void onResume(){
        super.onResume();

        if(tap) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want proceed with goal setting?")
                    .setCancelable(false)
                    .setTitle("Bravo! Are you ready?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            allFinished=true;
                            finish();

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String url = null;
        switch (position) {
            case 0:
                tap = true;
                url = "http://www.tescoliving.com/health-and-wellbeing/fitness/2013/october/top-10-health-benefits-of-walking-everyday";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                break;
            case 1:
                tap = true;
                url = "http://www.medicinenet.com/walking/article.htm";
                Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent2);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {

        CyberManager.getInstance().notifyAwareActivityFinished(allFinished); // I use the application context because this Activity is going to be closed

        super.onPause();
    }


}
