package com.example.user.railenquiry;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class Main2Activity extends AppCompatActivity
{

    EditText emailId2,password2,confirmPassword;
    Button signinButton;
    TextView loginText;
    FirebaseAuth firebaseAuth;
    LinearLayout linearLayout;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        emailId2 = (EditText) findViewById(R.id.emailId2);
        password2 = (EditText) findViewById(R.id.password2);
        signinButton = (Button) findViewById(R.id.signinButton);
        loginText = (TextView) findViewById(R.id.loginText);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(linearLayout.getWindowToken(),0);
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();

        signinButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String id = emailId2.getText().toString();
                String pd = password2.getText().toString();
                String pd2 = confirmPassword.getText().toString();

                if(id.isEmpty())
                {
                    emailId2.setError("Email is required!");
                    emailId2.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(id).matches())
                {
                    emailId2.setError("Enter a valid email!");
                    emailId2.requestFocus();
                    return;
                }
                if(pd.isEmpty())
                {
                    password2.setError("Password is required!");
                    password2.requestFocus();
                    return;
                }
                if(pd.length()<6)
                {
                    password2.setError("Must contain 6 characters!");
                    password2.requestFocus();
                    return;
                }
                if(!pd2.equals(pd))
                {
                    confirmPassword.setError("Password do not match!");
                    confirmPassword.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.createUserWithEmailAndPassword(id,pd).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        progressBar.setVisibility(View.INVISIBLE);
                        if(task.isSuccessful())
                        {
                            finish();
                            startActivity(new Intent(getApplicationContext(),Main3Activity.class));
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                        else
                        {
                            Toast.makeText(Main2Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });



        loginText.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);

                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });


    }
}
