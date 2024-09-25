package com.abhishek.onlineshop.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abhishek.onlineshop.Domain.UserDomain;
import com.abhishek.onlineshop.Helper.InputValidation;
import com.abhishek.onlineshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends BaseActivity {
    private EditText nameEditText, emailEditText,createPasswordEditText, confirmPasswordEditText, mobileEditText;
    private Button registerButton;
    private TextView loginClickButton;
    private InputValidation inputValidation;
    TextInputLayout reg_name,reg_email,reg_cr_password,reg_con_password,reg_mob;

    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_name = (TextInputLayout) findViewById(R.id.TextInputName);
        reg_email = (TextInputLayout) findViewById(R.id.TextInputEmail);
        reg_cr_password = (TextInputLayout) findViewById(R.id.TextInputPassword);
        reg_con_password = (TextInputLayout) findViewById(R.id.TextInputConfirmpassword);
        reg_mob = (TextInputLayout) findViewById(R.id.TextInputMobileNumber);

        inputValidation = new InputValidation(this);


        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        createPasswordEditText = findViewById(R.id.create_password);
        confirmPasswordEditText = findViewById(R.id.confirm_password);
        mobileEditText = findViewById(R.id.mobileEditText);
        registerButton = findViewById(R.id.registerButton);
        loginClickButton = findViewById(R.id.login_click_button);

        progressBar  = findViewById(R.id.progress_bar_r);
        progressBar.setVisibility(View.GONE);

        auth  = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        loginClickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                CreateUser();

            }
        });
    }
    private void CreateUser() {
        String name  = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String mobileNumber = mobileEditText.getText().toString().trim();
        String password = createPasswordEditText.getText().toString().trim();

        if(!inputValidation.IsInputValid(nameEditText,reg_name,"Enter Full Name") ){
            progressBar.setVisibility(View.GONE);
            return;
        }
        if(!inputValidation.IsInputEmailValid(emailEditText,reg_email,"Enter Email") ){
            progressBar.setVisibility(View.GONE);
            return;
        }
        if(!inputValidation.IsInputValid(createPasswordEditText,reg_cr_password,"Enter Password") ){
            progressBar.setVisibility(View.GONE);
            return;
        }
        if(!inputValidation.IsInputPasswordMatches(confirmPasswordEditText,createPasswordEditText,reg_con_password,"Password not Matches") ){
            progressBar.setVisibility(View.GONE);
            return;
        }
        if(!inputValidation.IsInputValid(mobileEditText,reg_mob,"Enter Mobile Number") ){
            progressBar.setVisibility(View.GONE);
            return;
        }


        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            UserDomain userModel = new UserDomain(name,email,password,mobileNumber);
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("users").child(id).setValue(userModel);
                            editor.putBoolean("isLoggedIn", false);
                            editor.apply();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                            MoveToLoginPage();
                        }
                        else{
                            Toast.makeText(RegisterActivity.this,"Error:"+task.getException(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void MoveToLoginPage() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}