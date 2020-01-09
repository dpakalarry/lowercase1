package com.summit.summitproject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class AtmScheduleActivity extends AppCompatActivity {
    Button generate_QRCode;
    ImageView qrCode;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atm_scheduler);

        generate_QRCode=findViewById(R.id.generate_qr);
        qrCode=findViewById(R.id.imageView);

        Spinner spinner = findViewById(R.id.accountSelect);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.accounts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        generate_QRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json = new JSONObject();

                // Get amount user entered
                EditText amount = findViewById(R.id.atm_amount);
                String numAmount = amount.getText().toString();

                // Determine withdraw or deposit
                RadioGroup radioGroup = findViewById(R.id.depositOrWithdraw);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                // find selected button by returned id
                RadioButton radioButton = findViewById(selectedId);
                String depositOrWithdraw = (String)radioButton.getText();
                String dOrW = depositOrWithdraw == "Deposit" ? "d" : "w";

                // Generate random 8-digit transaction ID
                Random gen = new Random();
                int randVal = gen.nextInt(100000000);
                String transactionID = Integer.toString(randVal);

                // Debugging
                Log.d("Input, Amount:", numAmount);
                Log.d("Input, Type:", dOrW);
                Log.d("Input, TransID:", transactionID);

                try {
                    json.put("transactionId", transactionID);
                    json.put("amount", numAmount);

                    json.put("type", dOrW);

                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                    date.setTimeZone(tz);
                    String nowAsISO = date.format(new Date());

                    json.put("timestamp", nowAsISO);

                    String text=json.toString();
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,1000,1000);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qrCode.setImageBitmap(bitmap);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
