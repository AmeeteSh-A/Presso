package com.example.attendanceappdraft0;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.attendanceappdraft0.utils.BaseActivity;
import com.example.attendanceappdraft0.utils.UIUtils;

public class SupportPageActivity extends BaseActivity {

    Spinner spinnersupportpage;
    EditText textfieldsupportpage;
    Button submitbuttonSupportpage, helpmainback3, button3, button5;
    TextView welcometextsupportpage; CheckBox versioncheckboxsupportpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        themePreserver();

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_support_page);

        submitbuttonSupportpage=findViewById(R.id.submitbuttonSupportpage);
        helpmainback3=findViewById(R.id.helpmainback3);
        spinnersupportpage=findViewById(R.id.spinnersupportpage);
        textfieldsupportpage=findViewById(R.id.textfieldsupportpage);
        versioncheckboxsupportpage=findViewById(R.id.versioncheckboxsupportpage);
        welcometextsupportpage= findViewById(R.id.welcometextsupportpage);

        UIUtils.applyShrinkEffect(submitbuttonSupportpage);
        UIUtils.applyShrinkEffect(helpmainback3);


        button3= findViewById(R.id.button3);
        button5= findViewById(R.id.button5);

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                themeChanger();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SharedPreferences supportpref = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String role = supportpref.getString("role", "unknown");
        String name="unknown";
        if (role.equals("student")){
            name = supportpref.getString("username", "student");
        }

        else if (role.equals("professor")){
            name = supportpref.getString("profName", "professor");
        }

        else {
            name = "unknown";
        }

        final String namefinal = name;


        String [] subject = {"Feedback", "Bug-Report", "Query", "Re-Validate"};
//        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(this, R.layout.spinnerlayout, subject);
//        spinnersupportpage.setAdapter(subjectAdapter);
//        spinnersupportpage.setPopupBackgroundResource(R.drawable.textfield);
        ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                subject
        );
        subjectAdapter.setDropDownViewResource(R.layout.spinnerlayout);
        spinnersupportpage.setAdapter(subjectAdapter);

        spinnersupportpage.setPopupBackgroundResource(R.drawable.spinnerbg);

        subjectAdapter.setDropDownViewResource(R.layout.spinnerlayout);
        spinnersupportpage.setAdapter(subjectAdapter);


        helpmainback3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SupportPageActivity.this, loadingscreenActivity.class));
            }
        });


        submitbuttonSupportpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (textfieldsupportpage.getText().toString().isEmpty()) {
                    Toast.makeText(SupportPageActivity.this, "Please fill in the fields before continuing", Toast.LENGTH_SHORT).show();
                } else {


                    String emailsubject = spinnersupportpage.getSelectedItem().toString();


                    if (versioncheckboxsupportpage.isChecked()) {

                        String deviceInfo = "Device: " + Build.MANUFACTURER + " " + Build.MODEL +
                                "\nAndroid Version: " + Build.VERSION.RELEASE;

                        String version = "Unknown";
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.PackageInfoFlags.of(0));
                                version = pInfo.versionName;
                            } else {
                                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                                version = pInfo.versionName;
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }


                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// The intent does not have a URI, so declare the "text/plain" MIME type
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"example@support.com"}); // recipients
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailsubject);
                        emailIntent.putExtra(Intent.EXTRA_TEXT, textfieldsupportpage.getText().toString() + "\nSent by: " + namefinal + "\n" + deviceInfo + "\n" + version);
//                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
// You can also attach multiple items by passing an ArrayList of Uris

                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    } else {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// The intent does not have a URI, so declare the "text/plain" MIME type
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"ameeteshawadh@gmail.com"});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailsubject);
                        emailIntent.putExtra(Intent.EXTRA_TEXT, textfieldsupportpage.getText().toString() + "\nSent by: " + namefinal);
//                emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
// You can also attach multiple items by passing an ArrayList of Uris

                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }

                }
            }
        });

        String fulltext= "Before submitting a query or a feedback, you might want to check the FAQ section under the assistance menu — your answer might already be there!\\n\\nStill got something to share? Feel free to drop your feedback or shoot a question out — I'll get back to you ASAP.";
        SpannableString sssp= new SpannableString(fulltext);

        ClickableSpan cssp= new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent faqopener = new Intent(SupportPageActivity.this, FaqPageActivity.class);
                startActivity(faqopener);
            }


            @Override
            public void updateDrawState(@NonNull TextPaint dssp) {
                super.updateDrawState(dssp);
                dssp.setColor(Color.parseColor("#0D5FE4"));
                dssp.setUnderlineText(false); // removes the underline (kuch device pe aa sakta hai, ab nhi aayega)
            }


        };


        int start = fulltext.indexOf("FAQ section");
        int end = start +"FAQ section".length();

        sssp.setSpan(cssp, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        welcometextsupportpage.setText(sssp);
        welcometextsupportpage.setMovementMethod(LinkMovementMethod.getInstance());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}