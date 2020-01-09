package com.summit.summitproject;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class AtmScheduleActivity extends AppCompatActivity {
    Button generate_QRCode;
    ImageView qrCode;

    /**
     * Used to persist user credentials if "Remember Me" is checked.
     */
    private SharedPreferences sharedPreferences;

    /**
     * The key under which the <b>username</b> will be stored in {@link SharedPreferences}.
     */
    private static final String PREF_USERNAME = "USERNAME";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.atm_scheduler);

        generate_QRCode=findViewById(R.id.generate_qr);
        qrCode=findViewById(R.id.imageView);

        generate_QRCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
                String username = sharedPreferences.getString(PREF_USERNAME, "");

                DatabaseReference user = database.getReference("Usernames").child(username);

                user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String account = dataSnapshot.getValue().toString().substring(9, 17);
                        Log.d("msg2", account);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference bankAccounts= database.getReference("Bank Accounts");
                        DatabaseReference balance = bankAccounts.child(account).child("Balance");

                        JSONObject json = new JSONObject();

                        try {
                            json.put("acct", account);
                            json.put("transId", "xxxx5678");
                            json.put("amnt", "10.00");
                            json.put("type", "d");

                            TimeZone tz = TimeZone.getTimeZone("UTC");
                            DateFormat date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                            date.setTimeZone(tz);
                            String nowAsISO = date.format(new Date());

                            json.put("time", nowAsISO);

                            String text=json.toString();
                            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                            try {
                                BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,500,500);
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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }
        });
    }
}
