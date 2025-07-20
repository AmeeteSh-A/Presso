package com.example.attendanceappdraft0;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.attendanceappdraft0.utils.BaseActivity;
import com.example.attendanceappdraft0.utils.UIUtils;

public class FaqPageActivity extends BaseActivity {


    Button question1, question2, question3, question4, question5, question6, question7, question8, question9, question10, helpmainback4, button3, button5;
    TextView answer1, answer2, answer3, answer4, answer5, answer6, answer7, answer8, answer9, answer10;
    TextView contactlinktext;
    int answer1opened=0;int answer2opened=0;int answer3opened=0;int answer4opened=0;int answer5opened=0;int answer6opened=0;int answer7opened=0;int answer8opened=0;int answer9opened=0;int answer10opened=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themePreserver();
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_faq_page);

        question1= findViewById(R.id.question1);
        question2=findViewById(R.id.question2);
        question3 = findViewById(R.id.question3);
        question4= findViewById(R.id.question4);
        question5=findViewById(R.id.question5);
        question6=findViewById(R.id.question6);
        question7=findViewById(R.id.question7);
        question8=findViewById(R.id.question8);
        question9 = findViewById(R.id.question9);
        question10=findViewById(R.id.question10);
        answer1=findViewById(R.id.answer1);
        answer2=findViewById(R.id.answer2);
        answer3=findViewById(R.id.answer3);
        answer4=findViewById(R.id.answer4);
        answer5= findViewById(R.id.answer5);
        answer6= findViewById(R.id.answer6);
        answer7= findViewById(R.id.answer7);
        answer8=findViewById(R.id.answer8);
        answer8= findViewById(R.id.answer8);
        answer9=findViewById(R.id.answer9);
        answer10=findViewById(R.id.answer10);
        helpmainback4= findViewById(R.id.helpmainback4);
        button3= findViewById(R.id.button3);
        button5= findViewById(R.id.button5);

        UIUtils.applyShrinkEffect(question1);
        UIUtils.applyShrinkEffect(question2);
        UIUtils.applyShrinkEffect(question3);
        UIUtils.applyShrinkEffect(question4);
        UIUtils.applyShrinkEffect(question5);
        UIUtils.applyShrinkEffect(question6);
        UIUtils.applyShrinkEffect(question7);
        UIUtils.applyShrinkEffect(question8);
        UIUtils.applyShrinkEffect(question9);
        UIUtils.applyShrinkEffect(question10);
        UIUtils.applyShrinkEffect(helpmainback4);

        contactlinktext=findViewById(R.id.contactlinktext);


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

        helpmainback4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FaqPageActivity.this, loadingscreenActivity.class));
            }
        });


        question1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (answer1opened == 0) {
                    answer1.setVisibility(VISIBLE);
                    answer1.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_in));
                    answer1opened=1;
                }
                else{
                    answer1.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_out));

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            answer1.setVisibility(INVISIBLE);  invisible nhi because it still remains just becomes invisible. gone mtlb bhag gaya, the @id becomes irrelevant and the barrier comes into play.
                            answer1.setVisibility(GONE);
                        }
                    },300);

                    answer1opened=0;
                }

            }
        });




        question2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (answer2opened == 0) {
                    answer2.setVisibility(VISIBLE);
                    answer2.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_in));
                    answer2opened=1;
                }
                else{
                    answer2.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_out));

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            answer1.setVisibility(INVISIBLE);  invisible nhi because it still remains just becomes invisible. gone mtlb bhag gaya, the @id becomes irrelevant and the barrier comes into play.
                            answer2.setVisibility(GONE);
                        }
                    },300);

                    answer2opened=0;
                }

            }
        });







        question3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (answer3opened == 0) {
                    answer3.setVisibility(VISIBLE);
                    answer3.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_in));
                    answer3opened=1;
                }
                else{
                    answer3.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_out));

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            answer1.setVisibility(INVISIBLE);  invisible nhi because it still remains just becomes invisible. gone mtlb bhag gaya, the @id becomes irrelevant and the barrier comes into play.
                            answer3.setVisibility(GONE);
                        }
                    },300);

                    answer3opened=0;
                }

            }
        });







        question4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (answer4opened == 0) {
                    answer4.setVisibility(VISIBLE);
                    answer4.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_in));
                    answer4opened=1;
                }
                else{
                    answer4.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_out));

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            answer1.setVisibility(INVISIBLE);  invisible nhi because it still remains just becomes invisible. gone mtlb bhag gaya, the @id becomes irrelevant and the barrier comes into play.
                            answer4.setVisibility(GONE);
                        }
                    },300);

                    answer4opened=0;
                }

            }
        });









        question5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (answer5opened == 0) {
                    answer5.setVisibility(VISIBLE);
                    answer5.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_in));
                    answer5opened=1;
                }
                else{
                    answer5.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_out));

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            answer1.setVisibility(INVISIBLE);  invisible nhi because it still remains just becomes invisible. gone mtlb bhag gaya, the @id becomes irrelevant and the barrier comes into play.
                            answer5.setVisibility(GONE);
                        }
                    },300);

                    answer5opened=0;
                }

            }
        });








        question6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (answer6opened == 0) {
                    answer6.setVisibility(VISIBLE);
                    answer6.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_in));
                    answer6opened=1;
                }
                else{
                    answer6.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_out));

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            answer1.setVisibility(INVISIBLE);  invisible nhi because it still remains just becomes invisible. gone mtlb bhag gaya, the @id becomes irrelevant and the barrier comes into play.
                            answer6.setVisibility(GONE);
                        }
                    },300);

                    answer6opened=0;
                }

            }
        });









        question7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (answer7opened == 0) {
                    answer7.setVisibility(VISIBLE);
                    answer7.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_in));
                    answer7opened=1;
                }
                else{
                    answer7.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_out));

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            answer1.setVisibility(INVISIBLE);  invisible nhi because it still remains just becomes invisible. gone mtlb bhag gaya, the @id becomes irrelevant and the barrier comes into play.
                            answer7.setVisibility(GONE);
                        }
                    },300);

                    answer7opened=0;
                }

            }
        });









        question8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (answer8opened == 0) {
                    answer8.setVisibility(VISIBLE);
                    answer8.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_in));
                    answer8opened=1;
                }
                else{
                    answer8.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_out));

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            answer1.setVisibility(INVISIBLE);  invisible nhi because it still remains just becomes invisible. gone mtlb bhag gaya, the @id becomes irrelevant and the barrier comes into play.
                            answer8.setVisibility(GONE);
                        }
                    },300);

                    answer8opened=0;
                }

            }
        });






        question9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (answer9opened == 0) {
                    answer9.setVisibility(VISIBLE);
                    answer9.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_in));
                    answer9opened=1;
                }
                else{
                    answer9.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_out));

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            answer1.setVisibility(INVISIBLE);  invisible nhi because it still remains just becomes invisible. gone mtlb bhag gaya, the @id becomes irrelevant and the barrier comes into play.
                            answer9.setVisibility(GONE);
                        }
                    },300);

                    answer9opened=0;
                }

            }
        });






        question10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (answer10opened == 0) {
                    answer10.setVisibility(VISIBLE);
                    answer10.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_in));
                    answer10opened=1;
                }
                else{
                    answer10.startAnimation(AnimationUtils.loadAnimation(FaqPageActivity.this, R.anim.fade_out));

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                            answer1.setVisibility(INVISIBLE);  invisible nhi because it still remains just becomes invisible. gone mtlb bhag gaya, the @id becomes irrelevant and the barrier comes into play.
                            answer10.setVisibility(GONE);
                        }
                    },300);

                    answer10opened=0;
                }

            }
        });



        String fulltext= "Not helpful? Contact the developer.";
        SpannableString ss= new SpannableString(fulltext);

        ClickableSpan cs= new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent supportopener = new Intent(FaqPageActivity.this, SupportPageActivity.class);
                startActivity(supportopener);
            }


            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#0D5FE4"));
                ds.setUnderlineText(false); // removes the underline (kuch device pe aa sakta hai, ab nhi aayega)
            }


        };


        int start = fulltext.indexOf("Contact the developer.");
        int end = start +"Contact the developer.".length();

        ss.setSpan(cs, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        contactlinktext.setText(ss);
        contactlinktext.setMovementMethod(LinkMovementMethod.getInstance());




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

//    public void toggleAnswer(View view) {
//        LinearLayout card = (LinearLayout) view;
//
//        // Try all possible IDs inside the clicked card
//        int[] ids = {R.id.a1, R.id.a2, R.id.a3, R.id.a4, R.id.a5};
//        TextView answer = null;
//        for (int id : ids) {
//            TextView txt = card.findViewById(id);
//            if (txt != null) {
//                answer = txt;
//                break;
//            }
//        }
//
//        if (answer != null) {
//            if (answer.getVisibility() == View.GONE) {
//                answer.setVisibility(View.VISIBLE);
//                answer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
//            } else {
//                answer.setVisibility(View.GONE);
//            }
//        }
//    }
}