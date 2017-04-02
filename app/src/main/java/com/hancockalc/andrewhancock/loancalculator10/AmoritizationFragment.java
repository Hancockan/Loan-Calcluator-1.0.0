package com.hancockalc.andrewhancock.loancalculator10;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Hancock on 2/28/2017.
 */

public class AmoritizationFragment extends Fragment {

    //global variable
    int term;
    double principal;
    double interest_rate;
    double monthly_payment;

    //creates a new listview for the amoritization dates
    ListView amoritizationList;
    /*
    creates a new array adapter for the object array
    of amoritization dates. This will help display them
     */
    ArrayAdapter<AmoritizationDate> adapter;
    //creates list of amoritization date objects
    List<AmoritizationDate> dates;
    //generate new object of amoritization date class
    AmoritizationDateGenerator AmoritizationHolder;
    //need new handler
    Handler h;

    //constructor
    public AmoritizationFragment(){
        //initializes the new list of date objects
        h = new Handler();
        dates = new ArrayList<AmoritizationDate>();
    }

    public static Fragment newInstance(int term,
                                       double principal,
                                       double interest_rate,
                                       double monthly_payment){

        //creates new instance of this class
        AmoritizationFragment af = new AmoritizationFragment();

        //set global variables
        af.term = term;
        af.principal = principal;
        af.interest_rate = interest_rate;
        af.monthly_payment = monthly_payment;

        // (new instance of this class).(amoritizationGenerationClass object) = new instance of amoritization generation class
        af.AmoritizationHolder = new AmoritizationDateGenerator();
        return af;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.dates, container, false);
        //sets new list view equal to dates list view
        amoritizationList = (ListView)v.findViewById(R.id.dates_list);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        initialize();
    }

    public void initialize(){
        if(dates.size() == 0){
            new Thread(){
                public void run(){
                    dates.addAll(AmoritizationHolder.AssignData(term, interest_rate, principal, monthly_payment));

                    h.post(new Runnable(){
                        public void run(){
                            createAdapter();
                        }
                    });
                }
            }.start();
        }
    }

    public void createAdapter(){

        if(getActivity() == null){
            return;
        }

        adapter = new ArrayAdapter<AmoritizationDate>(getActivity(), R.layout.date_item, dates){
            public View getView(int position, View convertView, ViewGroup parent){
                if(convertView == null){
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.date_item, null);
                }

                TextView month;
                month = (TextView)convertView.findViewById(R.id.month);

                TextView principal;
                principal = (TextView)convertView.findViewById(R.id.balance_remaining);

                TextView interest;
                interest = (TextView)convertView.findViewById(R.id.interest);

                TextView monthly_payment;
                monthly_payment = (TextView)convertView.findViewById(R.id.monthly_payment);

                monthly_payment.setText(dates.get(position).getMP());
                interest.setText(dates.get(position).getInterest());
                principal.setText(dates.get(position).getBalanceLeft());
                month.setText(dates.get(position).getMonth());
                return convertView;
            }
        };
        amoritizationList.setAdapter(adapter);

    }

}
