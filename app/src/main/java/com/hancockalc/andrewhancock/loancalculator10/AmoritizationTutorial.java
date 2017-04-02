package com.hancockalc.andrewhancock.loancalculator10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AmoritizationTutorial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amoritization_tutorial);

        final Intent intent4 = new Intent(this, LoanCalculationPage.class);

        final Button back_button = (Button)findViewById(R.id.back_button2);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(intent4);
            }
        });

    }
}
