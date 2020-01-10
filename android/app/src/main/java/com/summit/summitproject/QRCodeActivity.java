package com.summit.summitproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCodeActivity extends AppCompatActivity {

    TextView countdownTimer;
    ImageView qr_view;
    Button cancelButton;
    String numAmount, transactionID, dOrW;

    private SharedPreferences sharedPreferences;
    private static final String PREF_USERNAME = "USERNAME";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_activity);

        qr_view = findViewById(R.id.qr_image);
        cancelButton = findViewById(R.id.cancel_button);
        countdownTimer = findViewById(R.id.timer);

        dOrW = getIntent().getStringExtra("DepositOrWithdraw");
        numAmount = getIntent().getStringExtra("Amount");
        transactionID = getIntent().getStringExtra("TransactionID");

        setupCancelListener();

        // Create QR Code and send to database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString(PREF_USERNAME, "");

        DatabaseReference user = database.getReference("Usernames").child(username);

        user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String account = dataSnapshot.getValue().toString().substring(9, 17);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference bankAccounts= database.getReference("Bank Accounts");
                DatabaseReference balance = bankAccounts.child(account).child("Balance");

                JSONObject json = new JSONObject();

                try {
                    json.put("acct", account);
                    json.put("transId", transactionID);
                    json.put("amt", numAmount);
                    json.put("type", dOrW);

                    TimeZone tz = TimeZone.getTimeZone("UTC");
                    DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                    date.setTimeZone(tz);
                    String nowAsISO = date.format(new Date());

                    json.put("time", nowAsISO);

                    DatabaseReference transactions = database.getReference("Transactions");

                    class Transac implements Serializable {
                        public String account;
                        public String type;
                        public String amt;
                        public String timestamp;
                        public Date time;

                        Transac() {}

                        Transac(String acct, String ty, String a, String tm) {
                            account = acct;
                            type = ty;
                            amt = a;
                            timestamp = tm;
                        }
                    }

                    transactions.child(transactionID).setValue(new Transac(account, dOrW, numAmount, nowAsISO));

                    String text=json.toString();
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try {
                        BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        qr_view.setImageBitmap(bitmap);

                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        new CountDownTimer(900000, 1000) {

            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                String mins = seconds / 60 > 0 ? "" + seconds / 60 : "0";
                String secs = seconds % 60 > 9 ? "" + seconds % 60 : "0" + seconds % 60;
                String time = mins + ":" + secs;
                countdownTimer.setText(time);
            }

            public void onFinish() {
                Intent intent = new Intent(QRCodeActivity.this, AtmScheduleActivity.class);
                startActivity(intent);
            }
        }.start();
    }

    private void setupCancelListener() {
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QRCodeActivity.this, AtmScheduleActivity.class);
                startActivity(intent);
            }

        });
    }
}
