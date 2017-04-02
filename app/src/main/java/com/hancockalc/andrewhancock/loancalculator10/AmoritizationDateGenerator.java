package com.hancockalc.andrewhancock.loancalculator10;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andrew Hancock on 2/28/2017.
 */

public class AmoritizationDateGenerator {

    //global variable to remember the balance left
    public double balanceLeft;

    /*
    will generate and return the array of date objects. needs the
    parameters of term, principal and interest rate.
     */
    public List<AmoritizationDate> AssignData(int term,
                                              double interest_rate,
                                              double principal,
                                              double monthly_payment){

        //new amoritization list
        List<AmoritizationDate> dates = new ArrayList<AmoritizationDate>();

        //loop that goes through all the dates
        for(int i = 0;i <= term;i++){

            //new object that'll be added to the list
            AmoritizationDate ad = new AmoritizationDate();

            //sets first month because its a special case
            if(i == 0){
                ad.balance = principal;
                ad.principal = 0.0;
                ad.interest = 0.0;
                ad.equity = 0.0;
                ad.month = i;
                ad.monthly_payment = 0.0;
                //adds new object to array list
                dates.add(ad);
            }
            //sets rest of dates
            else if(i != 0){
                if(i == 1) {
                    ad.balance = principal;
                    balanceLeft = principal;
                }
                ad.interest = ((interest_rate/12.00) * balanceLeft);
                ad.monthly_payment = monthly_payment;
                ad.equity = (monthly_payment - ad.interest);
                ad.balance= (balanceLeft - ad.equity);
                balanceLeft = ad.balance;
                if((i == term) && (balanceLeft > -0.00000001) && (balanceLeft < 0.00000001)){
                    balanceLeft = 0.0;
                    ad.balance = 0.0;
                }
                ad.month = i;
                dates.add(ad);
            }
        }
        return dates;
    }
}
