package com.abhishek.onlineshop.Helper;

import android.content.Context;
import android.util.Patterns;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class InputValidation {
    private Context context;

    public InputValidation(Context context){
        this.context = context;
    }

    public boolean IsInputValid(EditText editText , TextInputLayout textInputLayout , String message){
        String value = editText.getText().toString().trim();
        if(value.isEmpty()){
            textInputLayout.setError(message);
            return false;
        }else{
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }
    public boolean IsInputEmailValid(EditText editText , TextInputLayout textInputLayout , String message){
        String value = editText.getText().toString().trim();
        if(value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            textInputLayout.setError(message);
            return false;
        }else{
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean IsInputPasswordMatches(EditText editText ,EditText editText1, TextInputLayout textInputLayout , String message){
        String value = editText.getText().toString().trim();
        String value2 = editText1.getText().toString().trim();
        if(!value2.contentEquals(value)){
            textInputLayout.setError(message);
            return false;
        }else{
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }


}
