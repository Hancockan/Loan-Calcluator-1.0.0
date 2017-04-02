package com.hancockalc.andrewhancock.loancalculator10;

import java.text.DecimalFormat;

/**
 * Created by Andrew Hancock on 2/28/2017.
 */

public class AmoritizationDate {
    int month;
    double interest;
    double monthly_payment;
    double equity;
    double principal;
    double balance;

    DecimalFormat df = new DecimalFormat("$#,##0.00");

    public String getBalanceLeft(){
        String tmp = "" + df.format(balance);
        return tmp;
    }

    public String getMonth(){
        String tmp = "" + month;
        return tmp;
    }

    public String getPrincipal(){
        String tmp = "" + df.format(principal);
        return tmp;
    }

    public String getInterest(){
        String tmp = "" + df.format(interest);
        return tmp;
    }

    public String getMP(){
        String tmp = "" + df.format(monthly_payment);
        return tmp;
    }

}
