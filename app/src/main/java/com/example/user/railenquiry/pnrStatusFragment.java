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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link pnrStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pnrStatusFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public pnrStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment pnrStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static pnrStatusFragment newInstance(String param1, String param2) {
        pnrStatusFragment fragment = new pnrStatusFragment();
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


    EditText pnrNumberEditText2;
    TextView statusTextView2;
    Button pnrStatusButton2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_pnr_status, container, false);

        pnrNumberEditText2  = (EditText) view.findViewById(R.id.pnrNumberEditText2);
        statusTextView2 = (TextView) view.findViewById(R.id.statusTextView2);
        pnrStatusButton2 = (Button) view.findViewById(R.id.pnrStatusButton2);

        final ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(constraintLayout.getWindowToken(),0);
            }
        });

        pnrStatusButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pnrNum = pnrNumberEditText2.getText().toString();
                if(pnrNum.isEmpty())
                {
                    pnrNumberEditText2.setError("PNR number is invalid! Please check!");
                    pnrNumberEditText2.requestFocus();
                }
                else if(pnrNum.length()<10)
                {
                    pnrNumberEditText2.setError("PNR number is invalid! Please check!");
                    pnrNumberEditText2.requestFocus();
                }
                else
                {
                    pnrNum = URLEncoder.encode(pnrNum);
                    ApiKey apiKey = new ApiKey();
                    String pnrStatusUrl = "https://api.railwayapi.com/v2/pnr-status/pnr/" + pnrNum + "/apikey/"+ apiKey.myApiKey + "/";
                    DownloadTask downloadTask = new DownloadTask();
                    try
                    {
                        String result = downloadTask.execute(pnrStatusUrl).get();
                        //Log.i("Result:",result);

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("passengers");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String bookingStatus = jsonObject1.getString("booking_status");
                        String currentStatus = jsonObject1.getString("current_status");
                        statusTextView2.setText("BookingStatus: " + bookingStatus + "\nCurrentStatus: " + currentStatus);


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
