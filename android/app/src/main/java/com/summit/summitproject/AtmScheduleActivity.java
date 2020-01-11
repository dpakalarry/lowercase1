package com.summit.summitproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Random;

public class AtmScheduleActivity extends AppCompatActivity {
    Button generate_QRCode;
    ImageView qrCode;
    RadioGroup depoWithButtons;
    EditText amountField;
    TextView currBalance;

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
        qrCode=findViewById(R.id.qr_image);
        depoWithButtons = findViewById(R.id.depositOrWithdraw);
        amountField = findViewById(R.id.atm_amount);
        currBalance = findViewById(R.id.balance_amount);

        amountField.setHint("00.00");

        Spinner spinner = findViewById(R.id.accountSelect);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.accounts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        setupDepositListener();
        displayBalance();

        generate_QRCode.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Determine withdraw or deposit
                int selectedId = depoWithButtons.getCheckedRadioButtonId();
                // find selected button by returned id
                RadioButton radioButton = findViewById(selectedId);
                String depositOrWithdraw = (String) radioButton.getText();
                String dOrW = depositOrWithdraw.equals("Deposit") ? "d" : "w";

                // Get amount user entered
                EditText amount = findViewById(R.id.atm_amount);
                String numAmount = dOrW.equals("w") ? amount.getText().toString() : "0.00";

                if (dOrW.equals("w") && numAmount.equals("")) {
                    Toast.makeText(AtmScheduleActivity.this, "Please input amount", Toast.LENGTH_LONG).show();
                } else if (dOrW.equals("w") && Double.parseDouble(numAmount) > Double.parseDouble(currBalance.getText().toString().substring(1))) {
                    Toast.makeText(AtmScheduleActivity.this, "Insufficient funds", Toast.LENGTH_LONG).show();
                } else {
                    // Generate random 8-digit transaction ID
                    Random gen = new Random();
                    int randVal = gen.nextInt(100000000);
                    String transactionID = Integer.toString(randVal);

                    // Go to QR Code View
                    Intent intent = new Intent(AtmScheduleActivity.this, QRCodeActivity.class);
                    intent.putExtra("DepositOrWithdraw", dOrW);
                    intent.putExtra("Amount", numAmount);
                    intent.putExtra("TransactionID", transactionID);
                    startActivity(intent);
                }
            }
        });
    }

    private void displayBalance() {
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
                DatabaseReference bankAccounts = database.getReference("Bank Accounts");
                DatabaseReference balance = bankAccounts.child(account).child("Balance");

                balance.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String currentBalance = dataSnapshot.getValue().toString();

                        currBalance.setText("$" + currentBalance);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
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

    @Override
    public void onBackPressed() {

    }
}
