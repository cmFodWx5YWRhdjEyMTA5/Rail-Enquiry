package com.example.user.railenquiry;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrainStatusFragment extends Fragment {


    public TrainStatusFragment() {
        // Required empty public constructor
    }


    EditText trainNumberEditText,dateEditText;
    Button getStatusButton;
    TextView statusTextView;
    ConstraintLayout constraintLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_train_status, container, false);


        trainNumberEditText = (EditText) view.findViewById(R.id.trainNumberEditText1);
        dateEditText = (EditText) view.findViewById(R.id.dateEditText1);
        getStatusButton = (Button) view.findViewById(R.id.getStatusButton1);
        statusTextView = (TextView) view.findViewById(R.id.statusTextView1);
        constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(constraintLayout.getWindowToken(),0);
            }
        });

        getStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String trainNumber = URLEncoder.encode(trainNumberEditText.getText().toString());
                if(trainNumber.isEmpty()||trainNumber.length()<5)
                {
                    trainNumberEditText.setError("Train number is invalid! Please check!");
                    trainNumberEditText.requestFocus();
                    return;
                }
                else
                {
                    String date = URLEncoder.encode(dateEditText.getText().toString());
                    if(date.isEmpty())
                    {
                        dateEditText.setError("Train number is invalid! Please check!");
                        dateEditText.requestFocus();
                        return;
                    }
                    //https://api.railwayapi.com/v2/live/train/12925/date/29-07-2018/apikey/w9ffblxh5n/
                    ApiKey apiKey = new ApiKey();
                    String trainStatusUrl = "https://api.railwayapi.com/v2/live/train/"+ trainNumber + "/date/" + date + "/apikey/" + apiKey.myApiKey + "/";
                    DownloadTask task = new DownloadTask();
                    try
                    {
                        String result = task.execute(trainStatusUrl).get();
                        //Log.i("Result: ",result);
                        JSONObject jsonObject  = new JSONObject(result);
                        String response_code = jsonObject.getString("response_code");
                        String position = jsonObject.getString("position");
                        statusTextView.setText(position);

                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    catch (ExecutionException e)
                    {
                        e.printStackTrace();
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }

            }
        });



        return view;
    }

}
