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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CancelledTrainFragment#newInstance} factory method t+o
 * create an instance of this fragment.
 */
public class CancelledTrainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public CancelledTrainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CancelledTrainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CancelledTrainFragment newInstance(String param1, String param2) {
        CancelledTrainFragment fragment = new CancelledTrainFragment();
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

    EditText dateEditText4;
    Button cancelledTrainButton;
    ListView listView2;
    ArrayList<String> arrayList1 = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter1;
    TextView totalCancelled;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_cancelled_train, container, false);


        dateEditText4 = (EditText) view.findViewById(R.id.dateEditText4);
        cancelledTrainButton = (Button) view.findViewById(R.id.cancelledTrainButton);
        listView2 = (ListView) view.findViewById(R.id.listView2);
        arrayAdapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList1);
        totalCancelled = (TextView) view.findViewById(R.id.totalCancelled);
        listView2.setAdapter(arrayAdapter1);

        arrayList1.clear();

        final ConstraintLayout constraintLayout = (ConstraintLayout) view.findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(constraintLayout.getWindowToken(),0);
            }
        });


        cancelledTrainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateEditText4.getText().toString();
                if(date.isEmpty())
                {
                    dateEditText4.setError("Date is invalid!");
                    dateEditText4.requestFocus();
                }
                else
                {
                    date = URLEncoder.encode(date);
                    ApiKey apiKey =new ApiKey();
                    String trainCancelledUrl = "https://api.railwayapi.com/v2/cancelled/date/" + date + "/apikey/" + apiKey.myApiKey + "/";
                    DownloadTask downloadTask = new DownloadTask();
                    try
                    {
                        String result = downloadTask.execute(trainCancelledUrl).get();
                        //Log.i("Result: ",result);
                        JSONObject jsonObject = new JSONObject(result);
                        String total = jsonObject.getString("total");
                        //Log.i("Total" ,total);
                        totalCancelled.setText(total+" trains are cancelled on given date.");
                        JSONArray jsonArray = jsonObject.getJSONArray("trains");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String trainName = jsonObject1.getString("name");
                            String trainNumber = jsonObject1.getString("number");
                            arrayList1.add(trainNumber + "  " + trainName);
                            arrayAdapter1.notifyDataSetChanged();
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
