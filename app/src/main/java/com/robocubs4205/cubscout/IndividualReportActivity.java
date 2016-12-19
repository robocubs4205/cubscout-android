package com.robocubs4205.cubscout;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


@SuppressWarnings("all")
public class IndividualReportActivity extends Activity {

    private IndividualReportFragment report = new IndividualReportFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_report);
    }
    public void onSearchButtonClick(View view)
    {
        try
        {
            new GetResultTask().execute(Integer.parseInt(((EditText)findViewById(R.id.search_text)).getText().toString()));
        }
        catch (NumberFormatException e)
        {
            Log.e("bob","error",e);
        }
    }
    private class GetResultTask extends AsyncTask<Integer,Void,TaskResult> {
        @Override
        protected TaskResult doInBackground(Integer... robotNumbers) {
            Integer robotNumber = robotNumbers[0];
            try {
                URL url = new URL("http://data.robocubs4205.com/retrieve");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                if(connection.getResponseCode() != 200)
                {
                    Log.e("bob","unable to connect to server");
                    return new TaskResult(null,false,"");
                }
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(connection.getInputStream());
                NodeList errors = doc.getElementsByTagName("error");
                for (int i = 0; i < errors.getLength(); i++) {
                    Log.e("bob", ((Element) errors.item(i)).getAttribute("type") + " " + errors.item(i).getTextContent());
                }
                if (errors.getLength() == 1) {
                    return new TaskResult(null,true,"There was an error of the following type: \" + ((Element) errors.item(1)).getAttribute(\"type\") + \". Please try again later");
                } else if (errors.getLength() > 1) {
                    return new TaskResult(null,true,"There were errors processing your request. Please try again later.");
                }
                NodeList robots = doc.getElementsByTagName("robot");
                Log.i("bob", "target:" + robotNumber.toString());
                for (int i = 0; i < robots.getLength(); i++) {
                    if (((Element) (robots.item(i))).getAttribute("team").equals(robotNumber.toString())) {
                        RobotData data = new RobotData();
                        data.teamNumber = Integer.parseInt(((Element) (robots.item(i))).getAttribute("team"));
                        data.avgHighGoal = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_high_goal"));
                        data.avgHighMiss = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_high_goal_miss"));
                        data.avgLowGoal = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_low_goal"));
                        data.avgLowMiss = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_low_goal_miss"));
                        data.avgTotalCross = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_total_cross"));

                        data.avgPortcullis = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_portcullis"));
                        data.avgCheval = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_cheval"));
                        data.avgMoat = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_moat"));
                        data.avgRampart = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_rampart"));
                        data.avgDrawBridge = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_drawbridge"));
                        data.avgSallyPort = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_sally_port"));
                        data.avgRockWall = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_rock_wall"));
                        data.avgRoughTerrain = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_rough_terrain"));
                        data.avgLowBar = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_low_bar"));
                        try {
                            data.avgDefense = Float.parseFloat(((Element) (robots.item(i))).getAttribute("avg_defense"));
                            data.didDefense = true;
                        } catch (NumberFormatException e) {
                            data.avgDefense = 0;
                            data.didDefense = false;
                        }
                        return new TaskResult(data,true,"");
                    }
                }
            }
            catch (UnknownHostException e)
            {
                Log.e("bob","UnknownHostException");
                return new TaskResult(null,false,"");
            }
            catch (Exception e) {
                Log.e("bob", "error", e);
                e.printStackTrace();
            }
            return new TaskResult(null,true,"");
        }

        @Override
        protected void onPostExecute(TaskResult result) {
            //((ViewGroup)findViewById(R.id.report_fragment_container)).removeAllViews();
            RobotData robotData = result.robotData;
            getFragmentManager().beginTransaction().remove(report).commit();
            Log.i("bob", "removing existing report");
            report = new IndividualReportFragment();
            if (robotData != null) {
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                Bundle fragmentData = new Bundle();
                fragmentData.putInt("team", robotData.teamNumber);

                fragmentData.putFloat("avgHighGoal", robotData.avgHighGoal);
                fragmentData.putFloat("avgHighMiss", robotData.avgHighMiss);
                fragmentData.putFloat("avgLowGoal", robotData.avgLowGoal);
                fragmentData.putFloat("avgLowMiss", robotData.avgLowMiss);

                fragmentData.putFloat("avgCheval", robotData.avgCheval);
                fragmentData.putFloat("avgDrawBridge", robotData.avgDrawBridge);
                fragmentData.putFloat("avgLowBar", robotData.avgLowBar);
                fragmentData.putFloat("avgMoat", robotData.avgMoat);
                fragmentData.putFloat("avgPortcullis", robotData.avgPortcullis);
                fragmentData.putFloat("avgRampart", robotData.avgRampart);
                fragmentData.putFloat("avgRockWall", robotData.avgRockWall);
                fragmentData.putFloat("avgRoughTerrain", robotData.avgRoughTerrain);
                fragmentData.putFloat("avgSallyPort", robotData.avgSallyPort);

                fragmentData.putFloat("avgTotalCross", robotData.avgTotalCross);
                fragmentData.putFloat("avgDefense", robotData.avgDefense);
                fragmentData.putBoolean("didDefense", robotData.didDefense);

                report.setArguments(fragmentData);

                transaction.add(R.id.report_fragment_container, report, "INDIVIDUAL_REPORT");
                transaction.commit();
            }
            else if(!result.errorText.isEmpty())
            {
                Toast.makeText(getApplicationContext(), result.errorText, Toast.LENGTH_LONG).show();
            }
            else if (result.isServerReachable)
            {
                Toast.makeText(getApplicationContext(), "no results for that team", Toast.LENGTH_LONG).show();
            }
            else
            {
                Log.e("bob","could not connect to server");
                Toast.makeText(getApplicationContext(), "Could not connect to server. Please try again later", Toast.LENGTH_LONG).show();
            }
        }
    }
    private class TaskResult
    {
        public final RobotData robotData;
        public final Boolean isServerReachable;
        public final String errorText;
        public TaskResult(RobotData robotData, Boolean isServerReachable, String errorText)
        {
            this.robotData = robotData;
            this.isServerReachable = isServerReachable;
            this.errorText = errorText;
        }
    }
}
