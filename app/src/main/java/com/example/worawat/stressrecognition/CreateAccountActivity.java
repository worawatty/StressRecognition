package com.example.worawat.stressrecognition;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.models.nosql.UserDO;
import com.database.CreateUserAWS;

import java.util.Calendar;

public class CreateAccountActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, CreateUserAWS.AsyncResponseCreateUser {
    EditText email;
    EditText username;
    EditText dateOfBirth;
    RadioGroup genderGroup;
    RadioButton male;
    RadioButton female;
    RadioButton gender;
    Button submitButton;
    UserDO userDO;
    Calendar dob;
    CreateUserAWS userAccount ;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        if(isRegisterd()){
            Intent i = new Intent(getApplicationContext(),StartService.class);
            startActivity(i);
        }
        userAccount = new CreateUserAWS(this);
        //get all layout resources
        email=(EditText)findViewById(R.id.email_create_account);
        username=(EditText)findViewById(R.id.name_create_account);
        dateOfBirth=(EditText)findViewById(R.id.dob_create_account);
        dateOfBirth.setShowSoftInputOnFocus(false);
        genderGroup =(RadioGroup)findViewById(R.id.gender_group);
        male=(RadioButton)findViewById(R.id.male_radie_create_account);
        female=(RadioButton)findViewById(R.id.female_radie_create_account);
        submitButton=(Button)findViewById(R.id.summit_create_account);
        genderGroup.check(R.id.male_radie_create_account);
        userDO=new UserDO();
        dateOfBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showDatePickerDialog(v);
                }
            }

        });

        dateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String result = formValidation();
                if(result.equals("Success")){
                    Calendar c=Calendar.getInstance();
                    int age = c.get(Calendar.YEAR)-dob.get(Calendar.YEAR);

                    int selectedGender = genderGroup.getCheckedRadioButtonId();
                    gender = (RadioButton)findViewById(selectedGender);
                    userDO.setGender(gender.getText()+"");
                    userDO.setPhoneMAC(email.getText()+"");
                    userDO.setAge((double)age);
                    userDO.setUsername(username.getText()+"");

                    Log.i("User Infomation: ", "Email: "+userDO.getPhoneMAC()+
                                                "\nUsername: "+userDO.getUsername()+
                                                "\nGender: "+userDO.getGender()+
                                                "\nAge: "+userDO.getAge());

                    AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());

                    userAccount.execute(userDO);
                }else {
                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
                }

            }
        });




    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.male_radie_create_account:
                if (checked)
                    genderGroup.check(R.id.male_radie_create_account);
                    break;
            case R.id.female_radie_create_account:
                if (checked)
                    genderGroup.check(R.id.female_radie_create_account);
                    break;
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onResume(){
        super.onResume();
        if(isRegisterd()){
            Intent i = new Intent(getApplicationContext(),StartService.class);
            startActivity(i);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


            dob=Calendar.getInstance();
            dob.set(year,monthOfYear,dayOfMonth);
            String dOBString=dob.get(Calendar.YEAR)+"/"+dob.get(Calendar.MONTH)+"/"+dob.get(Calendar.DAY_OF_MONTH);

            dateOfBirth.setText(dOBString);

    }

    public String formValidation(){
        String result="";
        if(!isValidEmail(email.getText())){
            result="Please Enter Email Correctly";
            return result;
        }else if(username==null){
            result="Please Enter Username";
            return result;
        }else if (dateOfBirth==null){
            result="Please Enter Date of Birth";
            return result;
        }else{
            result = "Success";
            return result;
        }

    }

    public  boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public boolean isRegisterd(){
        sharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.share_pref_name_users), Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean(getString(R.string.registration_statis),false)) {

            return true;
        }else return false;
    }

    @Override
    public void processFinish() {
        sharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.share_pref_name_users), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(getString(R.string.user_email),userDO.getPhoneMAC()+"");
        editor.putBoolean(getString(R.string.registration_statis),true);
        editor.commit();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }

    public static class DatePickerFragment extends DialogFragment
            {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = 1995;
            int month = 1;
            int day = 1;

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), (CreateAccountActivity)getActivity(), year, month, day);
        }


    }
}
