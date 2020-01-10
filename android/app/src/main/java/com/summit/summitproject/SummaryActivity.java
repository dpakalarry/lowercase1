package com.summit.summitproject;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Displays a user's name, the last 4 numbers of a credit card, and their recent transactions
 * for that card.
 * <br>
 * Expects the following pieces of data to be supplied via the {@link android.content.Intent}:
 * <ul>
 *     <li>User's name -- via {@link SummaryActivity#KEY_NAME}</li>
 *     <li>The last four numbers of a credit card -- via {@link SummaryActivity#KEY_CARD_NUM}</li>
 * </ul>
 */
public class SummaryActivity extends AppCompatActivity {

    /**
     * Used to extract the user's name from the launch {@link android.content.Intent}
     */
    public static final String KEY_NAME = "NAME";

    /**
     * Used to extract a credit card last 4 from the launch {@link android.content.Intent}
     */
    public static final String KEY_CARD_NUM = "CARD_NUM";

    // Data passed in via the Intent

    private String name;

    private String cardNum;

    // UI Widgets

    private TextView title;

    private TextView subtitle;

    private Button scheduleAtmVisitButton;

    View.OnClickListener handler = new View.OnClickListener() {
        public void onClick(View v) {
            // Start the SummaryActivity and also pass the user's name,
            // card number, and list of transactions in the launch intent.
            Intent intent = new Intent(SummaryActivity.this, AtmScheduleActivity.class);
            startActivity(intent);
        }
    };

    /**
     * Called the first time an Activity is created, but before any UI is shown to the user.
     * Prepares the layout and assigns UI widget variables.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        name = getIntent().getStringExtra(KEY_NAME);
        cardNum = getIntent().getStringExtra(KEY_CARD_NUM);

        title = findViewById(R.id.summary_title);
        subtitle = findViewById(R.id.summary_subtitle);
        scheduleAtmVisitButton = findViewById(R.id.scheduleAtmVisitButton);
        scheduleAtmVisitButton.setOnClickListener(handler);

                // Substitute in the user's name and card last 4 in the text widgets
        title.setText(getString(R.string.summary_title, name));
        subtitle.setText(getString(R.string.summary_subtitle, cardNum));
    }

}
