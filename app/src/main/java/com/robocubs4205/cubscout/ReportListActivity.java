package com.robocubs4205.cubscout;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ReportListActivity extends Activity {

    private final List<ListIndividualReportFragment> reports = new ArrayList<>();

    @SuppressWarnings("UnusedParameters")
    public void onSortButtonClick(View view)
    {
        new GetResultTask().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.sortable_fields,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner firstSortSpinner = (Spinner)findViewById(R.id.first_sort_spinner);
        firstSortSpinner.setAdapter(adapter);
        Spinner secondSortSpinner = (Spinner)findViewById(R.id.second_sort_spinner);
        secondSortSpinner.setAdapter(adapter);
        Spinner thirdSortSpinner = (Spinner)findViewById(R.id.third_sort_spinner);
        thirdSortSpinner.setAdapter(adapter);
    }

    private class GetResultTask extends AsyncTask<Void,Void,TaskResult> {
        @Override
        protected TaskResult doInBackground(Void... voids) {
            List<RobotData> results = new ArrayList<>();
            try {
                URL url = new URL("http://data.robocubs4205.com/retrieve");
                URLConnection connection = url.openConnection();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(connection.getInputStream());
                NodeList errors = doc.getElementsByTagName("error");
                for (int i = 0; i < errors.getLength(); i++) {
                    Log.e("bob", ((Element) errors.item(i)).getAttribute("type") + " " + errors.item(i).getTextContent());
                }
                if (errors.getLength() == 1) {
                    return new TaskResult(false,results,"There was an error of the following type: " + ((Element) errors.item(1)).getAttribute("type") + ". Please try again later");
                } else if (errors.getLength() > 1) {
                    return new TaskResult(false,results,"There were errors processing your request. Please try again later.");
                }
                NodeList robots = doc.getElementsByTagName("robot");
                for (int i = 0; i < robots.getLength(); i++) {
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

                        results.add(data);
                }
            }
            catch (UnknownHostException e)
            {
                Log.e("bob","UnknownHostException");
                return  new TaskResult(false,results,"");
            }
            catch (Exception e) {
                Log.e("bob", "error", e);
                e.printStackTrace();
            }
            return new TaskResult(true,results,"");
        }

        @Override
        protected void onPostExecute(TaskResult result) {
            if(!result.isServerReachable)
            {
                Toast.makeText(getApplicationContext(), "Unable to connect to server. Please check your internet connection", Toast.LENGTH_LONG).show();
                return;
            }
            if(!result.errorString.isEmpty())
            {
                Toast.makeText(getApplicationContext(),result.errorString,Toast.LENGTH_LONG).show();
            }
            List<RobotData> robotDatas = result.robotDatas;
            for(ListIndividualReportFragment report : reports)
            {
                getFragmentManager().beginTransaction().remove(report).commit();
                Log.i("bob", "removing existing report");
            }
            Collections.sort(robotDatas,new ReportComparator());
            for(RobotData robotData : robotDatas)
            {
                ListIndividualReportFragment report = new ListIndividualReportFragment();
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

                    transaction.add(R.id.report_list_container, report, "INDIVIDUAL_REPORT");
                    transaction.commit();
                    reports.add(report);
            }
        }
    }
    private class TaskResult
    {
        final Boolean isServerReachable;
        final String errorString;
        final List<RobotData> robotDatas;
        TaskResult(Boolean isServerReachable, List<RobotData> robotDatas, String errorString)
        {
            this.isServerReachable = isServerReachable;
            this.robotDatas = robotDatas;
            this.errorString = errorString;
        }
    }
    private class ReportComparator implements Comparator<RobotData>
    {
        @Override
        public int compare(RobotData t1, RobotData t2) {
            CompareToBuilder builder = new CompareToBuilder();
            int firstSortType = ((Spinner)findViewById(R.id.first_sort_spinner)).getSelectedItemPosition();
            int secondSortType = ((Spinner)findViewById(R.id.second_sort_spinner)).getSelectedItemPosition();
            int thirdSortType = ((Spinner)findViewById(R.id.third_sort_spinner)).getSelectedItemPosition();
            if(firstSortType == 1)
            {
                builder.append(t1.avgHighGoal,t2.avgHighGoal);
            }
            else if(firstSortType == 2)
            {
                builder.append(t1.avgLowGoal,t2.avgLowGoal);
            }
            else if(firstSortType == 3)
            {
                builder.append(t1.avgTotalCross,t2.avgTotalCross);
            }
            else if(firstSortType == 4)
            {
                builder.append(t1.avgDefense,t2.avgDefense);
            }
            else if(firstSortType == 5)
            {
                float t1Accuracy = 0;
                if(t1.avgHighGoal != 0 || t1.avgHighMiss != 0)
                    t1Accuracy = t1.avgHighGoal/(t1.avgHighGoal+t1.avgHighMiss);
                float t2Accuracy = 0;
                if(t2.avgHighGoal != 0 || t2.avgHighMiss != 0)
                    t2Accuracy = t2.avgHighGoal/(t2.avgHighGoal+t2.avgHighMiss);
                builder.append(t1Accuracy,t2Accuracy);
            }
            else if(firstSortType == 6)
            {
                float t1Accuracy = 0;
                if(t1.avgLowGoal != 0 || t1.avgLowMiss != 0)
                    t1Accuracy = t1.avgLowGoal/(t1.avgLowGoal+t1.avgLowMiss);
                float t2Accuracy = 0;
                if(t2.avgLowGoal != 0 || t2.avgLowMiss != 0)
                    t2Accuracy = t2.avgLowGoal/(t2.avgLowGoal+t2.avgLowMiss);
                builder.append(t1Accuracy,t2Accuracy);
            }

            if(secondSortType == 1)
            {
                builder.append(t1.avgHighGoal,t2.avgHighGoal);
            }
            else if(secondSortType == 2)
            {
                builder.append(t1.avgLowGoal,t2.avgLowGoal);
            }
            else if(secondSortType == 3)
            {
                builder.append(t1.avgTotalCross,t2.avgTotalCross);
            }
            else if(secondSortType == 4)
            {
                builder.append(t1.avgDefense,t2.avgDefense);
            }
            else if(secondSortType == 5)
            {
                float t1Accuracy = 0;
                if(t1.avgHighGoal != 0 || t1.avgHighMiss != 0)
                    t1Accuracy = t1.avgHighGoal/(t1.avgHighGoal+t1.avgHighMiss);
                float t2Accuracy = 0;
                if(t2.avgHighGoal != 0 || t2.avgHighMiss != 0)
                    t2Accuracy = t2.avgHighGoal/(t2.avgHighGoal+t2.avgHighMiss);
                builder.append(t1Accuracy,t2Accuracy);
            }
            else if(secondSortType == 6)
            {
                float t1Accuracy = 0;
                if(t1.avgLowGoal != 0 || t1.avgLowMiss != 0)
                    t1Accuracy = t1.avgLowGoal/(t1.avgLowGoal+t1.avgLowMiss);
                float t2Accuracy = 0;
                if(t2.avgLowGoal != 0 || t2.avgLowMiss != 0)
                    t2Accuracy = t2.avgLowGoal/(t2.avgLowGoal+t2.avgLowMiss);
                builder.append(t1Accuracy,t2Accuracy);
            }

            if(thirdSortType == 1)
            {
                builder.append(t1.avgHighGoal,t2.avgHighGoal);
            }
            else if(thirdSortType == 2)
            {
                builder.append(t1.avgLowGoal,t2.avgLowGoal);
            }
            else if(thirdSortType == 3)
            {
                builder.append(t1.avgTotalCross,t2.avgTotalCross);
            }
            else if(thirdSortType == 4)
            {
                builder.append(t1.avgDefense,t2.avgDefense);
            }
            else if(thirdSortType == 5)
            {
                float t1Accuracy = 0;
                if(t1.avgHighGoal != 0 || t1.avgHighMiss != 0)
                    t1Accuracy = t1.avgHighGoal/(t1.avgHighGoal+t1.avgHighMiss);
                float t2Accuracy = 0;
                if(t2.avgHighGoal != 0 || t2.avgHighMiss != 0)
                    t2Accuracy = t2.avgHighGoal/(t2.avgHighGoal+t2.avgHighMiss);
                builder.append(t1Accuracy,t2Accuracy);
            }
            else if(thirdSortType == 6)
            {
                float t1Accuracy = 0;
                if(t1.avgLowGoal != 0 || t1.avgLowMiss != 0)
                    t1Accuracy = t1.avgLowGoal/(t1.avgLowGoal+t1.avgLowMiss);
                float t2Accuracy = 0;
                if(t2.avgLowGoal != 0 || t2.avgLowMiss != 0)
                    t2Accuracy = t2.avgLowGoal/(t2.avgLowGoal+t2.avgLowMiss);
                builder.append(t1Accuracy,t2Accuracy);
            }
            return -builder.toComparison();
        }
    }

}
