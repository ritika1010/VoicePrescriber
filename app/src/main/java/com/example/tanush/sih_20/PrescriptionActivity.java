package com.example.tanush.sih_20;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PrescriptionActivity extends AppCompatActivity {

    private RecyclerView symRecyclerView;
    private SymptomsAdapter symAdapter;


    private RecyclerView.LayoutManager symLayoutManager;


    EditText newText;
    SpeechRecognizer speechRecognizer;
    ArrayList<String> resultSpeech = null;
    String str, json, age, sym[], diag[], adv[], patient, dose[], days[], med[];
    public AlertDialog alertDialog;
    public ArrayList<SymptomsRecyclerItem> symptomsRecyclerList = new ArrayList<>();


    public String s[] = {"hello", "hiii", "hey"};
    public int p;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pres_temp);
        // Intent intent =new Intent();
        //json= intent.getStringExtra("mess");

        json = "{\"Advice\":[\"advice take steam.\"],\"Age\":20,\"Days\":[\"5\"],\"Diagnosis\":[\"diagnosis viral fever.\"],\"Dose\":[\"after lunch\"],\"Medicines\":[\"Alpha tablet\",\"Arixtra 2.5mg Injection\",\"Banocide 50mg Syrup\"],\"PatientID\":\"ABC\",\"PatientName\":\"Pratik Patil\",\"Recommendations\":[[\"Arixtra 2.5mg Injection\",\"Arixtra 7.5mg Injection\"],[\"Banocide 120mg Syrup\",\"Banocide 50mg Syrup\",\"Banocide Pead 50mg Syrup\"]],\"Symptom\":[\"symptom weakness.\"]}";
        convert_json();





        for (int i = 1; i < sym.length; i++) {

            symptomsRecyclerList.add(new SymptomsRecyclerItem(sym[i]));
        }
        for (int i = 1; i < diag.length; i++) {

            symptomsRecyclerList.add(new SymptomsRecyclerItem(diag[i]));
        }
        //for (int i = 0; i < med.length; i++) {

        // Log.e("j",med[i]+days[i]+dose[i]);
        //
        //}
//        symptomsRecyclerList.add(new SymptomsRecyclerItem();
//        Log.e("success", Integer.toString(med.length));

        Log.e("j", String.valueOf(med));
        for (int i = 1; i < adv.length; i++) {

            symptomsRecyclerList.add(new SymptomsRecyclerItem(adv[i]));
        }


        symRecyclerView = findViewById(R.id.recyclerSymptoms);

        symRecyclerView.setHasFixedSize(false);
        symLayoutManager = new LinearLayoutManager(this);
        symAdapter = new SymptomsAdapter(symptomsRecyclerList);
        symRecyclerView.setLayoutManager(symLayoutManager);
        symRecyclerView.setAdapter(symAdapter);

        symAdapter.setOnItemClickListener(new SymptomsAdapter.onItemClickListener1() {
            @Override
            public void onItemClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PrescriptionActivity.this);
                final View mview = getLayoutInflater().inflate(R.layout.edit_prescription_dialog, null);
                builder.setView(mview);
                ImageButton startRecording = mview.findViewById(R.id.StartRecordingEdit);

                p = position;
                EditText editText = mview.findViewById(R.id.editText);
                Button cancelDialog = mview.findViewById(R.id.cancelDialogEdit);
                Button okDialog = mview.findViewById(R.id.okDialogEdit);

                newText = mview.findViewById(R.id.editText);
                builder.setCancelable(false);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();

                speechRecognizer = speechRecognizer.createSpeechRecognizer(mview.getContext());
                Intent intent;
                intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                startRecording.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        alertDialog.dismiss();
                        Toast.makeText(getBaseContext(), "Starting Prescription", Toast.LENGTH_SHORT).show();
                        speech(p);


                    }
                });
                okDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        symptomsRecyclerList.get(p).changeSymptom(newText.getText().toString());
                        // symptomsRecyclerList.get(p).changeHead()
                        symAdapter.notifyItemChanged(p);
                        alertDialog.dismiss();
                    }
                });
                cancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });


            }

        });

        Button sendmail = findViewById(R.id.mail);
        sendmail.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Resources res = getResources();

                String email1 = "ritika_gd@outlook.com";
                String subject = "Voice Based Prescription";
//                String message = "Dear " + par1.getText().toString() + "," + "\nGreetings from PASC!! \n\nThank you for registering for Pulzion'19. \n" + "Your details have been recorded and corresponding payment received. \n\nPlease find below your Registration ID.\n" + "\nRegistration ID: " + rnd + "\nThe above ID is unique to you." + "\n\nYou have participated in the following Event/s:-\n\n" + event + "\n Total Amount Paid:  Rs. " + amount + "\n\nPlease feel free to reach out to us in case of doubts or difficulty.\nHimani Gwalani: 7387664241\nRitik Manghani: 8208641527" + "\n\nAll the Best!!\n\nRegards,\nPICT ACM Student Chapter\n\n\n\n" + dontreply;
                String message = json;
                //Creating SendMail object
                SendMail sm = new SendMail(PrescriptionActivity.this, email1, subject, message);

                //Executing sendmail to send email
                sm.execute();
//                Toast.makeText(PrescriptionActivity.this, "E-Mail Sent!!", Toast.LENGTH_LONG).show();

            }
        });




    }

    public void fun(int position) {
        symptomsRecyclerList.get(position).changeSymptom(str);
        symAdapter.notifyItemChanged(position);
    }

    public void speech(int po) {
        p = po;
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 5);
        } else {
            Toast.makeText(getBaseContext(), "Your Device Doesn't Support Speech Intent", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 5) {
            if (resultCode == RESULT_OK && data != null) {
                resultSpeech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                str = resultSpeech.get(0);
                Log.e("string", str);
                fun(p);

            }
        }
    }


    public void convert_json() {
        try {
            JSONObject j = new JSONObject(json);
            sym = j.getString("Symptom").split("]|,|\\[");
            med = j.getString("Medicines").split("]|,|\\[");
            adv = j.getString("Advice").split("]|,|\\[");
            dose = j.getString("Dose").split("]|,|\\[");
            diag = j.getString("Diagnosis").split("]|,|\\[");
            days = j.getString("Days").split("]|,|\\[");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


//    @SuppressLint("NewApi")
//    public void generatePDF() {
//         PdfDocument pdfDocument = new PdfDocument();
//        View rellayout = findViewById(R.id.prescriptionLayout);
//        PdfDocument.PageInfo pageInfo;
//        int height = rellayout.getHeight();
//        int width = rellayout.getWidth();
//        Log.v("width * height", width + "  * " + height);
//
//        pageInfo = new PdfDocument.PageInfo.Builder(595, height, 1).create();
//        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
//        Canvas pres = page.getCanvas();
//        // units are in points (1/72 of an inch)
//        int titleBaseLine = 20;
//        int leftMargin = 30;
//        int leftmargindetails = 154;
//
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        //Letter Head
//        paint.setTextSize(10);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setStrokeWidth(1);
//
//        pres.drawText("Voice Prescriber", leftmargindetails + 58, titleBaseLine, paint);
//        paint.setTextSize(6);
//        pres.drawText("Smart India Hackathon 2020", leftmargindetails + 88, titleBaseLine = titleBaseLine + 7, paint);
//        paint.setTextSize(7);
//        pres.drawRect(leftMargin, titleBaseLine = titleBaseLine + 9, 565, titleBaseLine = titleBaseLine + 1, paint); //Line
//
//        paint.setTextSize(10);
//        pres.drawText("PRESCRIPTION", leftmargindetails + 106, titleBaseLine = titleBaseLine + 12, paint);
//        paint.setTextSize(7);
//
//
//        pdfDocument.finishPage(page);
//        SimpleDateFormat trail = new SimpleDateFormat("ddMMyyyyhhmmss");
//        String pdfName = "Prescription - " + trail.format(Calendar.getInstance().getTime()) +".pdf";
//
//        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//        File file = new File(path, "VoicePrescriber");
//        file.mkdirs();
//        File outputFile = new File(file, pdfName);
//
//        try {
//            outputFile.createNewFile();
//            OutputStream out = new FileOutputStream(outputFile);
//            pdfDocument.writeTo(out);
//            pdfDocument.close();
//            out.close();
//            String outPut = Uri.fromFile(outputFile).getPath().toString();
//            outPut = outPut.replaceAll("/storage/emulated/0/", "Internal Storage: ");
//            Toast.makeText(this, "Prescription Saved in " + outPut, Toast.LENGTH_LONG).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }

}
