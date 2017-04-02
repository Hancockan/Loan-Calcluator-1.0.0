package com.hancockalc.andrewhancock.loancalculator10;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LoanCalculationPage extends AppCompatActivity {

    //global variables
    public double interestRate = 0.0;
    public double principal = 0.0;
    public int term = 0;
    public double monthlyPayment = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_calculation_page);

        final EditText principal_edittext = (EditText)findViewById(R.id.loan_principal);
        final EditText interest_rate_edittext = (EditText)findViewById(R.id.interest_rate);
        final EditText term_edittext = (EditText)findViewById(R.id.term);

        final Intent intent1 = new Intent(this, amoritization_table.class);
        final Intent intent3 = new Intent(this, AmoritizationTutorial.class);
        final Intent intent4 = new Intent(this, AboutPage.class);

        //calculation button onclick method
        final Button calcButton = (Button)findViewById(R.id.calc_button);
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(principal_edittext.getText().toString().matches("") == false
                        && containsNumbersOnly(principal_edittext.getText().toString())
                        && interest_rate_edittext.getText().toString().matches("") == false
                        && containsNumbersOnly(interest_rate_edittext.getText().toString())
                        && term_edittext.getText().toString().matches("") == false
                        && containsNumbersOnly(term_edittext.getText().toString())) {
                    calculate();
                }
            }
        });

        //reset button onclick method
        final Button resetButton = (Button)findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });

        //amortization schedule button
        final Button amortButton = (Button)findViewById(R.id.amort_schedule_button);
        amortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent1.putExtra("term", term);
                intent1.putExtra("interestRate", interestRate);
                intent1.putExtra("principal", principal);
                intent1.putExtra("monthlyPayment", monthlyPayment);

                startActivity(intent1);
            }
        });

        final Button aboutAmort = (Button)findViewById(R.id.about_amort);
        aboutAmort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent3);
            }
        });

        final Button aboutButton = (Button)findViewById(R.id.about_page_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent4);
            }
        });

    }



    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt("term", term);
        savedInstanceState.putDouble("interestRate", interestRate);
        savedInstanceState.putDouble("principal", principal);
        savedInstanceState.putDouble("monthlyPayment", monthlyPayment);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        term = savedInstanceState.getInt("term");
        interestRate = savedInstanceState.getDouble("interestRate");
        principal = savedInstanceState.getDouble("principal");
        monthlyPayment = savedInstanceState.getDouble("monthlyPayment");

    }




    //method to reset
    public void reset(){
        EditText principal_edittext = (EditText)findViewById(R.id.loan_principal);
        EditText interest_rate_edittext = (EditText)findViewById(R.id.interest_rate);
        EditText term_edittext = (EditText)findViewById(R.id.term);
        TextView total_amount = (TextView)findViewById(R.id.total_loan_amount);
        TextView total_interest = (TextView)findViewById(R.id.total_interest_amount);
        TextView monthly_payment = (TextView)findViewById(R.id.monthly_payment);

        principal_edittext.setText("");
        interest_rate_edittext.setText("");
        term_edittext.setText("");
        total_amount.setText("");
        total_interest.setText("");
        monthly_payment.setText("");
    }

    //method to calculate the total
    public void calculate(){
        //initializing the edit texts and text views
        EditText principal_edittext = (EditText)findViewById(R.id.loan_principal);
        EditText interest_rate_edittext = (EditText)findViewById(R.id.interest_rate);
        EditText term_edittext = (EditText)findViewById(R.id.term);
        TextView total_amount = (TextView)findViewById(R.id.total_loan_amount);
        TextView total_interest = (TextView)findViewById(R.id.total_interest_amount);
        TextView monthly_payment = (TextView)findViewById(R.id.monthly_payment);
        RadioButton months_rb = (RadioButton)findViewById(R.id.months_radio_button);
        RadioButton years_rb = (RadioButton)findViewById(R.id.years_radio_button);


        //getting value from edit texts
        String principal_string = principal_edittext.getText().toString();
        String interest_rate_string = interest_rate_edittext.getText().toString();
        String term_string = term_edittext.getText().toString();

        //converting values to double and integer
        String principal_string_formatted = "";
        //if there are commas in the string and converts to double
        double principal;
        if(principal_string.contains(",")) {
            principal_string_formatted = removeCommas(principal_string);
            principal = Double.valueOf(principal_string_formatted);
        }else{
            principal = Double.valueOf(principal_string);
        }

        //term converted next
        int term = Integer.valueOf(term_string);

        //fix interest rate if it has a percentage sign and conver to double from string
        double interest_rate;
        String interest_rate_formatted;
        if(interest_rate_string.contains("%")){
            interest_rate_formatted = removePercentageSign(interest_rate_string);
            interest_rate = Double.valueOf(interest_rate_formatted);
        }else{
            interest_rate = Double.valueOf(interest_rate_string);
        }

        //convert the rate to the correct form by timesing it by 10^-2
        interest_rate = convertRate(interest_rate);

        //set global interest rate
        interestRate = interest_rate;

        //variable in equation for months or years
        double i = 12.00;

        //choose whether the term is in months or years - radio buttons
        if(months_rb.isChecked()){
            i = 1.00;
        }else{
            i = 12.00;
        }

        //sets global term variable
        if(years_rb.isChecked()){
            this.term = (term * 12);
        }else{
            this.term =term;
        }

        //calculation section for total cost and monthly payment
        double monthlyPayment = ((principal * (interest_rate/12.00)) /
                (1.00 - Math.pow((1.00 + (interest_rate/12.00)),(term * -i))));
        double totalLoanAmount = ((principal * (interest_rate/12.00)) /
                (1.00 - Math.pow((1.00 + (interest_rate/12.00)),(term * -i)))) * (term * i);

        //set global variable
        this.monthlyPayment = monthlyPayment;
        this.principal = principal;

        //formatter for any dollar amount
        DecimalFormat df = new DecimalFormat("$#,###.00");

        //calculate total interest
        double totalInterest = totalLoanAmount - principal;

        //format amounts and convert to string for displaying
        String totalInterestFormatted = df.format(totalInterest);
        String monthlyPaymentString = df.format(monthlyPayment);
        String totalLoanAmountFormatted = df.format(totalLoanAmount);

        //set amounts to be displayed
        total_amount.setText("  " + totalLoanAmountFormatted);
        total_interest.setText("  " + totalInterestFormatted);
        monthly_payment.setText("  " + monthlyPaymentString);

        //decalre colors that will be used
        int colorOne = getResources().getColor(R.color.pieChartColor1);
        int colorTwo = getResources().getColor(R.color.pieChartColor2);
        int colorThree = getResources().getColor(R.color.background);

        //declaring a new pie chart and assigning it the xml piechart
        PieChart interestPrincipalPieChart = (PieChart)findViewById(R.id.principal_interest_piechart);

        //creates a new list of entries for the pie chart
        List<PieEntry> entries = new ArrayList<>();

        //entries for the pie chart
        entries.add(new PieEntry((float)(totalInterest/totalLoanAmount * 100), "Interest"));
        entries.add(new PieEntry((float)(principal/totalLoanAmount * 100), "Principal"));

        //sets the data for the pie chart to the entries list
        PieDataSet set = new PieDataSet(entries, "Interest/Principal Percentage");

        //sets the colors of the pie chart slices
        set.setColors(new int [] {colorOne, colorTwo});

        //gets data object from the pie data set
        PieData data = new PieData(set);

        //formats pie chart data
        data.setValueTextSize(18.7f);
        data.setValueFormatter(new PercentFormatter());

        //formats pie chart
        interestPrincipalPieChart.setData(data);
        interestPrincipalPieChart.setHoleRadius(35);
        interestPrincipalPieChart.setTransparentCircleRadius(39);
        interestPrincipalPieChart.setEntryLabelTextSize(20);
        interestPrincipalPieChart.setEntryLabelColor(Color.WHITE);
        interestPrincipalPieChart.setCenterText("Total Loan Distribution");
        interestPrincipalPieChart.setCenterTextSize(16.5f);
        interestPrincipalPieChart.setHoleColor(colorThree);
        interestPrincipalPieChart.setDescription(null);
        interestPrincipalPieChart.invalidate();

        //gets legend object and then disables it
        Legend legend = interestPrincipalPieChart.getLegend();
        legend.setEnabled(false);
    }

    //removes commas in the string if there are any.
    public String removeCommas(String principal){
        //break the string apart at the commas
        String[] principal_seperated = principal.split(",");
        String tmp = "";
        //put it back together without the commas
        for(int i = 0;i < principal_seperated.length;i++){
            tmp = tmp + principal_seperated[i];
        }
        System.out.println("fixed string is: " + tmp);
        return tmp;
    }

    //removes the percentage sign from the interest rate if there is one
    public String removePercentageSign(String rate){
        //break string apart
        String[] rate_seperated = rate.split("%");
        String tmp = "";
        //put it back together
        for(int i = 0;i < rate_seperated.length;i++){
            tmp = tmp + rate_seperated[i];
        }
        System.out.println("fixed rate is: " + tmp);
        return tmp;
    }

    //converts interest rate for equation
    public double convertRate(double rate){
        double newRate = (rate) * (Math.pow(10, -2));
        return newRate;
    }



    public boolean containsNumbersOnly(String s){

        boolean doesNotContainsLetters = true;
        String characters = "0123456789,.";

        for(int t = 0;t < s.length();t++){

            char i = s.charAt(t);

            boolean a = false;

            for(int w = 0;w < characters.length();w++){
                char y = characters.charAt(w);
                if(y == i){
                    a = true;
                }
            }

            if(a == false){

                Toast.makeText(getApplicationContext(), "Your entries can't contain letters or symbols!", Toast.LENGTH_SHORT).show();
                doesNotContainsLetters = false;
            }
        }

        System.out.println("This string: " + s + " does not contain letters: " + doesNotContainsLetters);

        return doesNotContainsLetters;

    }


}
