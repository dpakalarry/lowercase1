package com.summit.summitproject;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
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

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class AtmScheduleActivity extends AppCompatActivity {
    Button generate_QRCode;
    ImageView qrCode;
    RadioGroup depoWithButtons;
    EditText amountField;

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
        depoWithButtons = findViewById(R.id.depositOrWithdraw);
        amountField = findViewById(R.id.atm_amount);

        Spinner spinner = findViewById(R.id.accountSelect);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.accounts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        setupDepositListener();

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

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference bankAccounts= database.getReference("Bank Accounts");
                        DatabaseReference balance = bankAccounts.child(account).child("Balance");

                        JSONObject json = new JSONObject();

                        // Determine withdraw or deposit
                        int selectedId = depoWithButtons.getCheckedRadioButtonId();
                        // find selected button by returned id
                        RadioButton radioButton = findViewById(selectedId);
                        String depositOrWithdraw = (String)radioButton.getText();
                        String dOrW = depositOrWithdraw.equals("Deposit") ? "d" : "w";

                        // Get amount user entered
                        EditText amount = findViewById(R.id.atm_amount);
                        String numAmount = dOrW.equals("w") ? amount.getText().toString() : "0.00";

                        // Generate random 8-digit transaction ID
                        Random gen = new Random();
                        int randVal = gen.nextInt(100000000);
                        String transactionID = Integer.toString(randVal);

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

    private void setupDepositListener() {
        depoWithButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = group.findViewById(checkedId);

                String depositOrWithdraw = checkedRadioButton.getText().toString();
                if (depositOrWithdraw.equals("Deposit")) {
                    // Make amount field void
                    amountField.setText("");
                    amountField.setEnabled(false);
                    amountField.setInputType(InputType.TYPE_NULL);
                    amountField.setHint("N/A");
                } else {
                    amountField.setEnabled(true);
                    amountField.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    amountField.setHint("00.00");
                }

            }
        });
    }
}
