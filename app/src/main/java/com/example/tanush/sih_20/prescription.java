package com.example.tanush.sih_20;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class prescription extends AppCompatActivity {
    EditText patient, medicines, diagnosis, advice, symptoms;
    String s, Date;
    static final int REQUEST = 112;
    private Context mContext=prescription.this;
    private SharedPreferences sharedPreferences;



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

        s = "patient name is Pratik Patil. patient ID is 1. take Alpha tablet after lunch for 5 days. diagnosis viral fever. symptom weakness . advice take steam.";
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

        Button generatePdf = findViewById(R.id.bt_pdfGenerate);
        generatePdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 23) {
                    String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    if (!hasPermissions(mContext, PERMISSIONS)) {
                        ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
                    } else {
                        Toast.makeText(mContext, "Error Occured1", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, REQUEST );
                    }
                } else {
                    Toast.makeText(mContext, "Error Occured2", Toast.LENGTH_SHORT).show();
                }



            }
        });


    }




    public void generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();
        View rellayout = findViewById(R.id.prescriptionLayout);
        PdfDocument.PageInfo pageInfo;
        int height = rellayout.getHeight();
        int width = rellayout.getWidth();
        Log.v("width * height", width + "  * " + height);

        pageInfo = new PdfDocument.PageInfo.Builder(595, height, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas pres = page.getCanvas();
        // units are in points (1/72 of an inch)
        int titleBaseLine = 20;
        int leftMargin = 30;
        int leftmargindetails = 154;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        //Letter Head
        paint.setTextSize(10);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);

        pres.drawText("Voice Prescriber", leftmargindetails + 58, titleBaseLine, paint);
        paint.setTextSize(6);
        pres.drawText("Smart India Hackathon 2020", leftmargindetails + 88, titleBaseLine = titleBaseLine + 7, paint);
        paint.setTextSize(7);
        pres.drawRect(leftMargin, titleBaseLine = titleBaseLine + 9, 565, titleBaseLine = titleBaseLine + 1, paint); //Line

        paint.setTextSize(10);
        pres.drawText("PRESCRIPTION", leftmargindetails + 106, titleBaseLine = titleBaseLine + 12, paint);
        paint.setTextSize(7);


        pdfDocument.finishPage(page);
        SimpleDateFormat trail = new SimpleDateFormat("ddMMyyyyhhmmss");
        String pdfName = "Prescription - " + trail.format(Calendar.getInstance().getTime()) +".pdf";

        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Documents");
        boolean isPresent = true;
        if (!docsFolder.exists()) {
            isPresent = docsFolder.mkdir();
        }
        if (isPresent) {
            File file = new File(docsFolder.getAbsolutePath(),"VP");
        } else {
            Toast.makeText(mContext, "Error Occured while creating folder", Toast.LENGTH_SHORT).show();
        }

        File outputFile = new File(docsFolder, pdfName);

        try {
            outputFile.createNewFile();
            OutputStream out = new FileOutputStream(outputFile);
            pdfDocument.writeTo(out);
            pdfDocument.close();
            out.close();
            String outPut = Uri.fromFile(outputFile).getPath().toString();
            outPut = outPut.replaceAll("/storage/emulated/0/", "Internal Storage: ");
            Toast.makeText(this, "Prescription Saved in " + outPut, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void convert_json() {
        String s = "{\"Advice\":[\"advice take steam.\"],\"Age\":20,\"Days\":[\"5\"],\"Diagnosis\":[\"diagnosis viral fever.\"],\"Dose\":[\"after lunch\"],\"Medicines\":[\"Alpha tablet\"],\"PatientID\":\"1\",\"PatientName\":\"Pratik Patil\",\"Recommendations\":[[\"Arixtra 2.5mg Injection\",\"Arixtra 7.5mg Injection\"],[\"Banocide 120mg Syrup\",\"Banocide 50mg Syrup\",\"Banocide Pead 50mg Syrup\"]],\"Symptom\":[\"symptom weakness.\"]}";
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


    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    Date = dateFormat.format(Calendar.getInstance().getTime());
                    // get default values from sharedPreference to set in pdf
                    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                   generatePDF();
                } else {
                    Toast.makeText(mContext, "The app was not allowed to read your store.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }







}
