package com.example.tanush.sih_20;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class prescription extends AppCompatActivity {
    EditText patient, medicines, diagnosis, advice, symptoms;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        patient = findViewById(R.id.Patient);
        medicines = findViewById(R.id.Medicines);
        diagnosis = findViewById(R.id.Diagnosis);
        advice = findViewById(R.id.Advice);
        symptoms = findViewById(R.id.Symptoms);
        Intent i = new Intent();
        s = i.getStringExtra("message");

        s = "patient name is Pratik Patil. patient ID is ABC. take Alpha tablet after lunch for 5 days. diagnosis viral fever. symptom weakness . advice take steam.";
        // Log.e("message",s);

        convert_json();

        MaterialButton btn = findViewById(R.id.bt_mail);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = getResources();

                String email1 = "ritika_gd@outlook.com";
                String subject = "Voice Based Prescription";
//                String message = "Dear " + par1.getText().toString() + "," + "\nGreetings from PASC!! \n\nThank you for registering for Pulzion'19. \n" + "Your details have been recorded and corresponding payment received. \n\nPlease find below your Registration ID.\n" + "\nRegistration ID: " + rnd + "\nThe above ID is unique to you." + "\n\nYou have participated in the following Event/s:-\n\n" + event + "\n Total Amount Paid:  Rs. " + amount + "\n\nPlease feel free to reach out to us in case of doubts or difficulty.\nHimani Gwalani: 7387664241\nRitik Manghani: 8208641527" + "\n\nAll the Best!!\n\nRegards,\nPICT ACM Student Chapter\n\n\n\n" + dontreply;
                String message = s;
                //Creating SendMail object
                SendMail sm = new SendMail(prescription.this, email1, subject, message);

                //Executing sendmail to send email
                sm.execute();
                Toast.makeText(prescription.this, "E-Mail Sent!!", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void convert_json() {
        String s = "{\"Advice\":[\"advice take steam.\"],\"Age\":20,\"Days\":[\"5\"],\"Diagnosis\":[\"diagnosis viral fever.\"],\"Dose\":[\"after lunch\"],\"Medicines\":[\"Alpha tablet\"],\"PatientID\":\"ABC\",\"PatientName\":\"Pratik Patil\",\"Recommendations\":[[\"Arixtra 2.5mg Injection\",\"Arixtra 7.5mg Injection\"],[\"Banocide 120mg Syrup\",\"Banocide 50mg Syrup\",\"Banocide Pead 50mg Syrup\"]],\"Symptom\":[\"symptom weakness.\"]}";
        try {
            JSONObject j = new JSONObject(s);
            Log.e("success", j.getString("Age"));
            Log.e("success", j.getString("Medicines"));

            //1.

            patient.setText(j.getString("PatientName") + "\n" + j.getString("PatientID") + "\n" + j.getString("Age"));
            String med[] = j.getString("Medicines").split("]|,|\\[");
            String day[] = j.getString("Days").split("]|,|\\[");
            String dose[] = j.getString("Dose").split("]|,|\\[");
            Log.e("success", Integer.toString(med.length));
            String temp = "";
            for (int i = 1; i < med.length; i++) {
                temp = temp + med[i] + " for " + day[i] + " days " + dose[i] + "\n";
            }

            medicines.setText(temp);
            String adv[] = j.getString("Advice").split("]|,|\\[");
            Log.e("a", (temp));
            temp = " ";
            for (int i = 1; i < adv.length; i++) {
                temp = temp + adv[i] + "\n";
            }
            advice.setText(temp);
            String diag[] = j.getString("Diagnosis").split("]|,|\\[");
            Log.e("success", Integer.toString(med.length));
            temp = " ";
            for (int i = 1; i < diag.length; i++) {
                temp = temp + diag[i] + "\n";
            }
            diagnosis.setText(temp);
            String sym[] = j.getString("Symptom").split("]|,|\\[");
            Log.e("success", Integer.toString(med.length));
            temp = " ";
            for (int i = 1; i < sym.length; i++) {
                temp = temp + sym[i] + "\n";
            }
            symptoms.setText(temp);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("fail", "haha");
        }
    }


}
