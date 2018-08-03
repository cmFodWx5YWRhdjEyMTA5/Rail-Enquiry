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

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.R.layout.simple_list_item_1;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrainRouteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainRouteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public TrainRouteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrainRouteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrainRouteFragment newInstance(String param1, String param2) {
        TrainRouteFragment fragment = new TrainRouteFragment();
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

    EditText trainNumberTextView3;
    Button getRouteButton3;
    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;
    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_train_route, container, false);

        arrayList.clear();

        trainNumberTextView3 = (EditText) view.findViewById(R.id.trainNumberEditText3);
        getRouteButton3 = (Button) view.findViewById(R.id.getRouteButton3);
        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);

        final ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(constraintLayout.getWindowToken(),0);
            }
        });



        getRouteButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String trainNumber = trainNumberTextView3.getText().toString();

                if(trainNumber.isEmpty()||trainNumber.length()<5)
                {
                    trainNumberTextView3.setError("Train number is invalid!");
                    trainNumberTextView3.requestFocus();
                }
                else
                {
                    trainNumber = URLEncoder.encode(trainNumber);
                    ApiKey apiKey = new ApiKey();
                    String trainRouteUrl = "https://api.railwayapi.com/v2/route/train/" + trainNumber + "/apikey/" + apiKey.myApiKey + "/";
                    DownloadTask downloadTask = new DownloadTask();
                    try
                    {
                        String result = downloadTask.execute(trainRouteUrl).get();
                        //Log.i("Train route: ",result);
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("route");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            JSONObject station = jsonObject1.getJSONObject("station");
                            arrayList.add(station.getString("name"));
                            arrayAdapter.notifyDataSetChanged();
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
