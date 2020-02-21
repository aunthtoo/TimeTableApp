package com.aunthtoo.timetableforcupyay;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aunthtoo.timetableforcupyay.model.Student;
import com.aunthtoo.timetableforcupyay.prefmanager.PrefManager;
import com.aunthtoo.timetableforcupyay.volleyapp.VolleyApplication;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.MMButtonView;
import me.myatminsoe.mdetect.MMTextView;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private Spinner year, section;
    private TextInputLayout textInputName, textInputPhone;
    private EditText edtRoll, edtName, edtPhone;
    private Button btnEnter;

    private TextView titletext, choosesection;
    private ImageView logo;


    private RelativeLayout centercontent;

    private String URL;

    private String DATA_URL;

    int checkforfirst = 0;

    //for firebase
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResentToken;

    private String TAG = "Error";
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private boolean mVerificationInProgress = false;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    private ProgressDialog progressDialog;

    private final int READ_STAT = 0;

    private AlertDialog codeenterdialog, accesskeydialog;
    private View codeenterview;
    private EditText edtCode;
    private MMButtonView enterbtn;

    private ProgressDialog waitingProgress;

    List<String> retVal = new ArrayList<>();
    private String yearText;

    private PrefManager prefManager;

    private TextView loginhelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //startService(new Intent(this, MyService.class));

        prefManager = new PrefManager(this);

        URL = getString(R.string.url);
        DATA_URL = getString(R.string.dataurl);

        MDetect.INSTANCE.init(this);


        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() == null) {
            setContentView(R.layout.activity_login);

            if (!isNetworkConnected())
                showInternetDialog();


            // [START initialize_auth]
            mAuth = FirebaseAuth.getInstance();
            // [END initialize_auth]

            //for database
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference("user");

            titletext = findViewById(R.id.titletxt);
            logo = findViewById(R.id.titlelogo);

            centercontent = findViewById(R.id.centercontent);

            waitingProgress = new ProgressDialog(this);
            waitingProgress.setMessage(MDetect.INSTANCE.getText(getString(R.string.plswait)));
            waitingProgress.setCancelable(false);


            //initializing view
            year = findViewById(R.id.spnYear);


            if (isNetworkConnected()) {

                loadDataForSpinner();

            }

            section = findViewById(R.id.spnSection);

            textInputName = findViewById(R.id.textinputforname);
            textInputName.setHint(MDetect.INSTANCE.getText(getString(R.string.name)));

            textInputPhone = findViewById(R.id.textinputforphno);
            textInputPhone.setHint(MDetect.INSTANCE.getText(getString(R.string.phno)));

            edtRoll = findViewById(R.id.rollnumber);
            edtRoll.setHint(MDetect.INSTANCE.getText(getString(R.string.urroll)));

            edtName = findViewById(R.id.name);
            edtPhone = findViewById(R.id.phno);


            btnEnter = findViewById(R.id.enter);
            btnEnter.setText(MDetect.INSTANCE.getText(getString(R.string.enter)));

            choosesection = findViewById(R.id.choosesection);

            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Sign In");
            progressDialog.setMessage("Waiting for code");
            progressDialog.setCancelable(false);


            loginhelp = findViewById(R.id.loginhelp);
            loginhelp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:09761130828"));
                    startActivity(callIntent);
                }
            });

            //end of initializing view


            if (checkForPhoneReadState())
                setPhoneno();

            //for event regisration
            btnEnter.setOnClickListener(this);

            year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                    if (year.getSelectedItem().toString().contains("CS")) {
                        if (year.getSelectedItem().toString().toLowerCase().startsWith("5"))
                            setSection(false);
                        else
                            setSection(true);
                    } else {
                        setSection(false);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //for phone number verification firebase
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    Log.w(TAG, "Verification is completed");

                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Log.w(TAG, "Verification failed");
                    progressDialog.dismiss();


                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                        // ...

                        Log.e(TAG, "Invalid request");
                        edtPhone.setError("Invalid Phone Number");

                        Toast.makeText(LogIn.this, "Invalid request !", Toast.LENGTH_LONG).show();

                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                        // ...
                        Log.e(TAG, "The SMS quota for the project has been exceeded");

                        Toast.makeText(LogIn.this, "The SMS quota for the project has been exceeded", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    Log.w(TAG, "Code is sent " + s);
                    progressDialog.setMessage("Code is sent to your phone " + edtPhone.getText().toString());

                    progressDialog.dismiss();

                    showCodeEnterDialog();

                    //Toast.makeText(LogIn.this, "Code is sent", Toast.LENGTH_LONG).show();

                    mVerificationId = s;
                    mResentToken = forceResendingToken;
                }
            };
/*
            } else {

            }*/

        } else {
            startActivity(new Intent(this, MainPage.class));
            finish();
        }
    }


    //on click

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.enter:

                if (isNetworkConnected()) {


                    if (loginValidation()) {
                        progressDialog.show();
                        String phoneNumber = "+95" + (edtPhone.getText().toString()).substring(1);

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,        // Phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                this,               // Activity (for callback binding)
                                mCallbacks);


                    } else {
                        Toast.makeText(this, "You cannot empty data !! ", Toast.LENGTH_LONG).show();

                        if (TextUtils.isEmpty(edtRoll.getText())) {
                            edtRoll.setError("Cannot empty!");
                        }

                        if (TextUtils.isEmpty(edtPhone.getText())) {
                            edtPhone.setError("Cannot empty!");
                        }

                        if (TextUtils.isEmpty(edtName.getText()))
                            edtName.setError("Cannot empty!");
                    }

                } else {

                    Snackbar.make(getCurrentFocus(), MDetect.INSTANCE.getText(getString(R.string.logintxt)), BaseTransientBottomBar.LENGTH_LONG).show();


                }

                break;
        }
    }


    //for validation login
    public boolean loginValidation() {
        boolean retVal = true;

        if (TextUtils.isEmpty(edtRoll.getText()) || TextUtils.isEmpty(edtName.getText()) || TextUtils.isEmpty(edtPhone.getText()))
            retVal = false;

        return retVal;
    }

    void setSection(boolean secVal) {
        if (secVal) {
            choosesection.setTextColor(getResources().getColor(R.color.colorAccent));
            section.setEnabled(true);
        } else {
            choosesection.setTextColor(getResources().getColor(R.color.shadowcolor));
            section.setEnabled(false);
        }


    }

    //for animation

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        logo.setVisibility(View.VISIBLE);
        titletext.setVisibility(View.VISIBLE);
        centercontent.setVisibility(View.VISIBLE);

        if (hasFocus && checkforfirst == 0) {

            YoYo.with(Techniques.BounceIn)
                    .duration(1500)
                    .playOn(logo);

            YoYo.with(Techniques.BounceInDown)
                    .duration(1500)
                    .playOn(titletext);

            YoYo.with(Techniques.SlideInUp)
                    .duration(1500)
                    .playOn(centercontent);


            checkforfirst = 1;
        }


    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {


        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            progressDialog.dismiss();

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("PhoneVERifier", "signInWithCredential:success");
                            if (year.getSelectedItem().toString().contains("CS")) {
                                Student studentModel = new Student(year.getSelectedItem().toString(), edtRoll.getText().toString(), section.getSelectedItem().toString(), edtName.getText().toString(), edtPhone.getText().toString());
                                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(studentModel);

                                Intent intent = new Intent(LogIn.this, MainPage.class);
                                if (!year.getSelectedItem().toString().startsWith("5"))
                                    intent.putExtra("url_linkfortimetable", DATA_URL + year.getSelectedItem().toString().toLowerCase() + "_" + section.getSelectedItem().toString().toLowerCase() + ".json");
                                else
                                    intent.putExtra("url_linkfortimetable", DATA_URL + year.getSelectedItem().toString().toLowerCase() + "_cs.json");

                                startActivity(intent);
                                finish();

                            } else {
                                Student studentModel = new Student(year.getSelectedItem().toString(), edtRoll.getText().toString(), "CT", edtName.getText().toString(), edtPhone.getText().toString());
                                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(studentModel);
                                Intent intent = new Intent(LogIn.this, MainPage.class);
                                intent.putExtra("url_linkfortimetable", DATA_URL + year.getSelectedItem().toString().toLowerCase() + "_ct.json");
                                startActivity(intent);
                                finish();
                            }


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("PhoneVERifier", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
// [END sign_in_with_phone]


    public boolean checkForPhoneReadState() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_PHONE_STATE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, READ_STAT);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case READ_STAT: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    setPhoneno();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void setPhoneno() {

        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String mPhoneNumber = null;
        try {
            mPhoneNumber = tMgr.getLine1Number();

            //Toast.makeText(this, mPhoneNumber, Toast.LENGTH_LONG).show();

        } catch (SecurityException e) {
            Log.e(TAG, e.getMessage());
        }


        if (mPhoneNumber != null && !mPhoneNumber.startsWith("+"))
            edtPhone.setText(mPhoneNumber);
        else if (mPhoneNumber.startsWith("+")) {
            edtPhone.setText("0" + mPhoneNumber.substring(3));
        }
    }

    public void showCodeEnterDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        codeenterview = inflater.inflate(R.layout.code_input_dialog, null);
        codeenterdialog = builder.create();

        codeenterdialog.setView(codeenterview);
        codeenterdialog.setCancelable(false);

        edtCode = codeenterview.findViewById(R.id.edtCode);
        edtCode.setHint(MDetect.INSTANCE.getText("ကုဒ်ထည့်ရန်"));
        enterbtn = codeenterview.findViewById(R.id.enterbtn);

        enterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtCode.getText().toString()) && edtCode.getText().toString().length() == 6) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, edtCode.getText().toString());

                    codeenterdialog.dismiss();
                    progressDialog.show();
                    progressDialog.setMessage("Verifying");
                    signInWithPhoneAuthCredential(credential);
                } else {
                    Toast.makeText(LogIn.this, "Wrong code", Toast.LENGTH_LONG).show();
                    YoYo.with(Techniques.Shake)
                            .duration(2000)
                            .playOn(edtCode);

                }
            }
        });

        codeenterdialog.show();


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void showInternetDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(MDetect.INSTANCE.getText(getString(R.string.warning)));
        builder.setMessage(MDetect.INSTANCE.getText(getString(R.string.logintxt)));
        builder.setPositiveButton(MDetect.INSTANCE.getText(getString(R.string.yes)), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setCancelable(false);
        builder.show();

    }

    public void loadDataForSpinner() {

        waitingProgress.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {


                    yearText = response.getString("year");
                    setData(yearText);


                    if (!response.getString("check").equals("0")) {
                        showAccessKeyDialog(response.getString("pass"));
                    }

                    String validmonth = response.getString("validmonth");
                    prefManager.setVALID_MONTH(validmonth);
                    waitingProgress.dismiss();

                } catch (JSONException e) {

                    e.printStackTrace();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                        R.layout.item_spinner, retVal);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                year.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                waitingProgress.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        });

        VolleyApplication.getInstance().addToReqQueue(jsonObjReq, "jreq");

    }

    public void setData(String str) {
        for (int i = 0; i < str.split("_").length; i++) {
            retVal.add(str.split("_")[i].toUpperCase());
        }

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Warning!!");
        builder.setMessage("Are u sure to exit??");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }


    public void showAccessKeyDialog(final String key) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View accessView = inflater.inflate(R.layout.code_input_dialog, null);
        accesskeydialog = builder.create();

        accesskeydialog.setView(accessView);
        accesskeydialog.setCancelable(false);

        ((MMTextView) accessView.findViewById(R.id.inputtitle)).setMMText(getString(R.string.enteraccesskey));

        final EditText edtAccessCode = accessView.findViewById(R.id.edtCode);
        edtAccessCode.setHint(MDetect.INSTANCE.getText(getString(R.string.accesskey)));
        edtAccessCode.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);

        MMButtonView accessBtn = accessView.findViewById(R.id.enterbtn);

        accessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtAccessCode.getText().toString()) && edtAccessCode.getText().toString().equals(key)) {

                    accesskeydialog.dismiss();


                } else {
                    Toast.makeText(LogIn.this, "Wrong code", Toast.LENGTH_LONG).show();
                    YoYo.with(Techniques.Shake)
                            .duration(2000)
                            .playOn(edtAccessCode);

                }
            }
        });

        accesskeydialog.show();


    }


}
