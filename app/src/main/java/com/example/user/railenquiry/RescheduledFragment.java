package com.example.user.railenquiry;


import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RescheduledFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RescheduledFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public RescheduledFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RescheduledFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RescheduledFragment newInstance(String param1, String param2) {
        RescheduledFragment fragment = new RescheduledFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    EditText dateEditText5;
    Button getRescheduledButton;
    ListView listView3;
    ArrayList<String> arrayList3 = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       View view =  inflater.inflate(R.layout.fragment_rescheduled, container, false);

       dateEditText5 = (EditText) view.findViewById(R.id.dateEditText5);
       getRescheduledButton = (Button) view.findViewById(R.id.getRescheduledButton);
       listView3 =(ListView) view.findViewById(R.id.listView3);

       arrayList3.clear();
       arrayAdapter2 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList3);
       listView3.setAdapter(arrayAdapter2);

        final ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(constraintLayout.getWindowToken(),0);
            }
        });


       getRescheduledButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String date = dateEditText5.getText().toString();
               if(date.isEmpty())
               {
                   dateEditText5.setError("Date is invalid!");
                   dateEditText5.requestFocus();
               }
               else
               {
                   date = URLEncoder.encode(date);
                   ApiKey apiKey = new ApiKey();
                   String rescheduledUrl = "https://api.railwayapi.com/v2/rescheduled/date/" + date + "/apikey/" + apiKey.myApiKey + "/";

                   DownloadTask downloadTask = new DownloadTask();
                   try
                   {
                       String result = downloadTask.execute(rescheduledUrl).get();
                       //Log.i("Result:",result);
                       JSONObject jsonObject = new JSONObject(result);
                       JSONArray jsonArray = jsonObject.getJSONArray("trains");
                       for(int i=0;i<jsonArray.length();i++)
                       {
                           JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                           JSONObject from_station = jsonObject1.getJSONObject("from_station");
                           //Log.i("Data ", jsonObject1.getString("name")+jsonObject1.get("rescheduled_time")+jsonObject1.getString("number"));
                           //Log.i("Data2",from_station.getString("name"));
                           arrayList3.add(jsonObject1.getString("number")+"   "+jsonObject1.getString("name"));
                           arrayList3.add("New Time: "+jsonObject1.getString("rescheduled_time")+"  From: "+from_station.getString("name"));
                           arrayList3.add("");
                           arrayAdapter2.notifyDataSetChanged();
                       }
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
