package com.hancockalc.andrewhancock.loancalculator10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class amoritization_table extends AppCompatActivity {

    //global variables
    public double interestRate = 0.0;
    public double principal = 0.0;
    public int term = 0;
    public double monthlyPayment = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amoritization_table);

        Intent intent = getIntent();
        interestRate = intent.getDoubleExtra("interestRate", interestRate);
        term = intent.getIntExtra("term", term);
        principal = intent.getDoubleExtra("principal", principal);
        monthlyPayment = intent.getDoubleExtra("monthlyPayment", monthlyPayment);

        addFragment();

        //final Intent intent2 = new Intent(this, LoanCalculationPage.class);

        final Button back = (Button)findViewById(R.id.back_button);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(intent2);
            }
        });
    }

    void addFragment(){
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.amoritization_holder, AmoritizationFragment.newInstance(term,
                        principal, interestRate, monthlyPayment)).commit();
    }

}
