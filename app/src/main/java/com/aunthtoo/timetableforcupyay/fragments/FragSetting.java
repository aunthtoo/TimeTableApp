package com.aunthtoo.timetableforcupyay.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.aunthtoo.timetableforcupyay.R;
import com.aunthtoo.timetableforcupyay.dbhandler.TimeTableDBHandler;
import com.aunthtoo.timetableforcupyay.model.Interval;
import com.aunthtoo.timetableforcupyay.model.Student;
import com.aunthtoo.timetableforcupyay.model.TeacherAndSubject;
import com.aunthtoo.timetableforcupyay.model.TimeTable;
import com.aunthtoo.timetableforcupyay.prefmanager.PrefManager;
import com.aunthtoo.timetableforcupyay.volleyapp.VolleyApplication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.marcoscg.easylicensesdialog.EasyLicensesDialogCompat;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import me.myatminsoe.mdetect.MDetect;
import me.myatminsoe.mdetect.MMButtonView;
import me.myatminsoe.mdetect.MMEditText;
import me.myatminsoe.mdetect.MMTextView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragSetting extends Fragment implements View.OnClickListener {

    private View view, dialogView;

    //for database
    private TimeTableDBHandler handler;


    //for profile
    private MMTextView txtRollno, txtName, txtPh, txtSection;

    //for update
    private LinearLayout btnUpdate, btnSignout;
    private AlertDialog updateDialog;

    //for dialog component
    private Spinner spnYear;
    private MMTextView section;
    private MMEditText edtRoll, edtName, edtSection;
    private MMButtonView btnUpdateDialog;
    private Button dialogClose;

    private ProgressDialog waitingProgress;


    private String URL;
    private String DATA_URL;

    private List<String> spnValue = new ArrayList<>();


    private PrefManager prefManager;

    private String url;

    //for backup and restore
    private RelativeLayout backup, restore;

    private int WRITE_STATE = 1;
    String folderName = "MMT RC";
    private String dbname = "TimeTable.db";

    String filePath;

    private LinearLayout credit, feedback, help, contact;

    public FragSetting() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        URL = getString(R.string.url);
        DATA_URL = getString(R.string.dataurl);

        handler = new TimeTableDBHandler(getContext());

        MDetect.INSTANCE.init(getContext());

        waitingProgress = new ProgressDialog(getContext());
        waitingProgress.setMessage(MDetect.INSTANCE.getText(getString(R.string.plswait)));
        waitingProgress.setCancelable(false);

        prefManager = new PrefManager(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.frag_setting, container, false);

        checkForStorageRead();
        checkForStoragepermission();

        //initializing view
        txtRollno = view.findViewById(R.id.yourroll);
        txtName = view.findViewById(R.id.yourname);
        txtPh = view.findViewById(R.id.yourph);
        txtSection = view.findViewById(R.id.yoursection);

        //for update
        btnUpdate = view.findViewById(R.id.updatebtn);
        btnUpdate.setOnClickListener(this);

        //for backup and restor
        backup = view.findViewById(R.id.tobackup);
        restore = view.findViewById(R.id.torestore);

        backup.setOnClickListener(this);
        restore.setOnClickListener(this);

        //for about app
        credit = view.findViewById(R.id.credit);
        feedback = view.findViewById(R.id.feedback);
        help = view.findViewById(R.id.help);
        contact = view.findViewById(R.id.contact);

        credit.setOnClickListener(this);
        feedback.setOnClickListener(this);
        help.setOnClickListener(this);
        contact.setOnClickListener(this);

        //for about app

        //setting up data
        profileDataSetup();

        //setting up dialog
        setupDialog();

        return view;
    }

    public void profileDataSetup() {
        Student student = handler.getStudent();

        txtRollno.setMMText(student.getYear().toUpperCase() + "-" + student.getRollno());
        txtName.setMMText(student.getStName());
        txtPh.setMMText(student.getPhonenumber());
        txtSection.setMMText(student.getSection());
    }

    public void profileDataUpdate(Student student) {

        handler.updateStudent(student);
        FirebaseDatabase.getInstance().getReference("user").child(FirebaseAuth.getInstance().getUid()).setValue(student);
        profileDataSetup();

    }


    public void setupDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        updateDialog = builder.create();

        LayoutInflater inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.profile_update, null, false);

        updateDialog.setView(dialogView);
        updateDialog.setCancelable(false);

        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        updateDialog.getWindow()
                .getAttributes().windowAnimations = R.style.DialogAnimation;

        //initializing component
        section = dialogView.findViewById(R.id.txtsection);

        dialogClose = dialogView.findViewById(R.id.close);
        dialogClose.setOnClickListener(this);

        spnYear = dialogView.findViewById(R.id.updateSpn);
        edtRoll = dialogView.findViewById(R.id.updateRoll);
        edtName = dialogView.findViewById(R.id.updatename);
        //edtPh = dialogView.findViewById(R.id.updateph);
        edtSection = dialogView.findViewById(R.id.updatesection);

        btnUpdateDialog = dialogView.findViewById(R.id.btnupdatefordialog);
        btnUpdateDialog.setOnClickListener(this);

        spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnYear.getSelectedItem().toString().contains("CS")) {
                    if (spnYear.getSelectedItem().toString().toLowerCase().startsWith("5"))
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


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.close:

                updateDialog.dismiss();

                break;


            case R.id.updatebtn:

                if (isNetworkConnected()) {

                    waitingProgress.show();

                    loadDataForSpinner();
                    //adding data
                    edtRoll.setMMText(txtRollno.getText().toString().split("-")[1]);
                    edtName.setMMText(txtName.getText().toString());
                    // edtPh.setMMText(txtPh.getText().toString());
                    edtSection.setMMText(txtSection.getText().toString());


                } else {
                    Snackbar.make(getView(), MDetect.INSTANCE.getText(getString(R.string.openinternet)), Snackbar.LENGTH_SHORT).show();
                }

                break;

            case R.id.btnupdatefordialog:

                if (isNetworkConnected()) {

                    updateDialog.dismiss();
                    Student student = new Student();
                    student.setRollno(edtRoll.getText().toString());
                    student.setStName(edtName.getText().toString());
                    student.setSection(edtSection.getText().toString());
                    student.setPhonenumber(txtPh.getText().toString());
                    student.setYear(spnYear.getSelectedItem().toString());


                    if (spnYear.getSelectedItem().toString().toLowerCase().contains("cs"))
                        if (!spnYear.getSelectedItem().toString().toLowerCase().startsWith("5"))
                            prefManager.setURL_LINK(DATA_URL + spnYear.getSelectedItem().toString().toLowerCase() + "_" + edtSection.getText().toString().toLowerCase() + ".json");
                        else
                            prefManager.setURL_LINK(DATA_URL + spnYear.getSelectedItem().toString().toLowerCase() + "_cs.json");
                    else
                        prefManager.setURL_LINK(DATA_URL + spnYear.getSelectedItem().toString().toLowerCase() + "_ct.json");

                    profileDataUpdate(student);

                    insertDatatoDb(prefManager.getURL_LINK());

                } else {
                    Snackbar.make(getView(), MDetect.INSTANCE.getText(getString(R.string.openinternet)), Snackbar.LENGTH_SHORT).show();
                }

                break;


            case R.id.tobackup:

                if (checkForStoragepermission()) {

                    waitingProgress.setMessage(MDetect.INSTANCE.getText(getString(R.string.backuping)));
                    waitingProgress.show();
                    folderCreation(folderName);
                    exportDatabse(dbname);

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            waitingProgress.dismiss();
                            Toast.makeText(getContext(), MDetect.INSTANCE.getText(getString(R.string.backupdone)), Toast.LENGTH_SHORT).show();
                        }
                    }, 1000);
                }

                break;

            case R.id.torestore:

                File sd = new File(Environment.getExternalStorageDirectory(), folderName);
                String backupDBPath = "TimeTable.db";
                File backupDB = new File(sd, backupDBPath);

                if (checkForStorageRead()) {
                    if (backupDB.exists()) {
                        waitingProgress.setMessage(MDetect.INSTANCE.getText(getString(R.string.restoring)));
                        waitingProgress.show();
                        importDatabase(dbname);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                waitingProgress.dismiss();
                                Toast.makeText(getContext(), MDetect.INSTANCE.getText(getString(R.string.restoredone)), Toast.LENGTH_SHORT).show();

                            }
                        }, 1000);


                    } else {

                        Toast.makeText(getContext(), MDetect.INSTANCE.getText(getString(R.string.filepickerstr)), Toast.LENGTH_SHORT).show();


                        new MaterialFilePicker()
                                .withSupportFragment(FragSetting.this)
                                .withRequestCode(1)
                                .withHiddenFiles(true) // Show hidden files and folders
                                .start();


                    }
                }

                break;

            //for about app

            case R.id.credit:
                //for credit click

                new EasyLicensesDialogCompat(getContext())
                        .setTitle("Credit")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();

                break;

            case R.id.feedback:
                //for feedback

                final Intent _Intent = new Intent(Intent.ACTION_SEND);
                _Intent.setType("text/html");
                _Intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.feedback_email)});
                _Intent.putExtra(Intent.EXTRA_SUBJECT, "MMT RC version 1.0");
                _Intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(_Intent, getString(R.string.feedback)));


                break;


            case R.id.help:
                //for help with phone call
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:09761130828"));
                startActivity(callIntent);


                break;


            case R.id.contact:
                //for contact

                startActivity(getOpenFacebookIntent(getContext(), "fb://profile/100008563238909", "https://www.facebook.com/aunthtooaung.mmt"));


                break;


        }
    }

    //data updating
    public void insertDatatoDb(String timeTableLink) {

        waitingProgress.show();

        //loading time table
        loadTimeTable(timeTableLink);


    }

    //data updating

    //for time table

    public void loadTimeTable(String link) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, link,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    //load timetable
                    List<TimeTable> timeTableList = new ArrayList<>();

                    //for monday
                    TimeTable monday = new TimeTable();
                    monday.setDay("mon");
                    monday.setSubject(response.getString("mon"));
                    timeTableList.add(monday);


                    //for tuesday
                    TimeTable tuesday = new TimeTable();
                    tuesday.setDay("tue");
                    tuesday.setSubject(response.getString("tue"));
                    timeTableList.add(tuesday);

                    //for wednesday
                    TimeTable wednesday = new TimeTable();
                    wednesday.setDay("wed");
                    wednesday.setSubject(response.getString("wed"));
                    timeTableList.add(wednesday);

                    //for thursday
                    TimeTable thursday = new TimeTable();
                    thursday.setDay("thu");
                    thursday.setSubject(response.getString("thu"));
                    timeTableList.add(thursday);

                    //for friday
                    TimeTable friday = new TimeTable();
                    friday.setDay("fri");
                    friday.setSubject(response.getString("fri"));
                    timeTableList.add(friday);

                    handler.insertTimeTableData(timeTableList);

                    //end of load time table

                    //start of load interval

                    List<Interval> intervalList = new ArrayList<>();

                    String intervalStr = response.getString("interval");


                    //end of load interval

                    for (int j = 0; j < intervalStr.split("/").length; j++) {
                        Interval intervalObj = new Interval();

                        intervalObj.setInterval(intervalStr.split("/")[j]);
                        intervalObj.setStartTime(intervalStr.split("/")[j].split(" - ")[0]);
                        intervalObj.setEndTime(intervalStr.split("/")[j].split(" - ")[1]);

                        intervalList.add(intervalObj);
                    }

                    handler.insertIntervalData(intervalList);

                    //for teacher and subject start

                    String subShort = response.getString("subjectshort");
                    List<TeacherAndSubject> teacherAndSubjects = new ArrayList<>();


                    for (int j = 0; j < subShort.split("_").length; j++) {
                        String longandteacher = response.getString(subShort.split("_")[j]);

                        TeacherAndSubject obj = new TeacherAndSubject(subShort.split("_")[j], longandteacher.split("/")[0], longandteacher.split("/")[1]);
                        teacherAndSubjects.add(obj);

                    }


                    if (teacherAndSubjects != null)
                        handler.insertToTeacherAndSubTable(teacherAndSubjects);


                    //for teacher and subject end

                    waitingProgress.dismiss();


                    Toast.makeText(getContext(), MDetect.INSTANCE.getText(getString(R.string.refreshdone)), Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {

                    e.printStackTrace();
                }


                //to perform data insert

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                waitingProgress.dismiss();

                Toast.makeText(getContext(), error.getMessage() + "Error", Toast.LENGTH_LONG).show();

            }
        });

        VolleyApplication.getInstance().addToReqQueue(jsonObjReq, "jreq");
    }


    //for time table


    //data adding to spinner

    public void loadDataForSpinner() {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, URL,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {


                    String yearText = response.getString("year");
                    if (spnValue.size() != 0) {
                        spnValue.clear();
                    }

                    for (int i = 0; i < yearText.split("_").length; i++) {
                        spnValue.add(yearText.split("_")[i].toUpperCase());
                    }


                } catch (JSONException e) {

                    e.printStackTrace();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        R.layout.item_spinner, spnValue);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spnYear.setAdapter(adapter);
                adapter.notifyDataSetChanged();

                int selectedPos = 0;

                lbl:
                for (int i = 0; i < spnValue.size(); i++) {
                    if (spnValue.get(i).equals(txtRollno.getText().toString().split("-")[0].toUpperCase())) {
                        selectedPos = i;
                        break lbl;
                    }
                }

                spnYear.setSelection(selectedPos);

                waitingProgress.dismiss();
                updateDialog.show();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                waitingProgress.dismiss();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                updateDialog.dismiss();
            }
        });

        VolleyApplication.getInstance().addToReqQueue(jsonObjReq, "jreq");

    }


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    void setSection(boolean secVal) {
        if (secVal) {
            section.setTextColor(getResources().getColor(R.color.detailtextcolor));
            edtSection.setEnabled(true);
        } else {
            section.setTextColor(getResources().getColor(R.color.shadowcolor));
            edtSection.setEnabled(false);
        }


    }


    //for backup and restore
    public void folderCreation(String fName) {
        File f = new File(Environment.getExternalStorageDirectory(), fName);
        if (!f.exists()) {
            f.mkdirs();
            Log.e("SettingFrag", "Folder Creating complete");
        }
    }


    //for runtime permission

    public boolean checkForStoragepermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STATE);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    public boolean checkForStorageRead() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
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

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    folderCreation(folderName);
                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }


            case 2: {


                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getContext(), "Permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //file copy

    public void exportDatabse(String databaseName) {
        try {
            File sd = new File(Environment.getExternalStorageDirectory(), folderName);
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + getActivity().getPackageName() + "//databases//" + databaseName + "";
                String backupDBPath = "TimeTable.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

    public void importDatabase(String databaseName) {
        try {
            File sd = new File(Environment.getExternalStorageDirectory(), folderName);
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + getActivity().getPackageName() + "//databases//" + databaseName + "";
                String backupDBPath = "TimeTable.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(backupDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

    public void importDbWithFilePath(String databaseName, String filepath) {
        try {
            File sd = new File(filepath);
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + getActivity().getPackageName() + "//databases//" + databaseName + "";
                // String backupDBPath = "TimeTable.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = sd;

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(backupDB).getChannel();
                    FileChannel dst = new FileOutputStream(currentDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:

                if (resultCode == RESULT_OK) {
                    String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                    // Do anything with file


                    importDbWithFilePath(dbname, filePath);
                    waitingProgress.setMessage(MDetect.INSTANCE.getText(getString(R.string.restoring)));
                    waitingProgress.show();

                    Handler han = new Handler();
                    han.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            waitingProgress.dismiss();
                            Toast.makeText(getContext(), MDetect.INSTANCE.getText(getString(R.string.restoredone)), Toast.LENGTH_SHORT).show();


                        }
                    }, 1000);

                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    //for facebook intent
    public static Intent getOpenFacebookIntent(Context context, String id, String link) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse(id));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        }
    }

}
