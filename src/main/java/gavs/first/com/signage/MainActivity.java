package gavs.first.com.signage;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gavs.first.com.signage.Utils.print_card;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    String TAG="";

    public Uri imageUri;

    public ContentValues values;

    static final int PICTURE_RESULT = 1;


    ImageView ib;
    ImageView im;

    Button button;
    //Button test;

    EditText etFirstName;
    EditText etLastName;
    EditText empId;
    EditText etFrom;
    EditText etContact;
    EditText etOrganization;
    EditText etbloodGroup;

    AutoCompleteTextView etPurpose;

    Uri image;

//    Bitmap photo;
    Bitmap emptyPhoto;

    TextView tvCapture;
    TextView tvmarquee;

    Intent cameraIntent;
    private Spinner categorySpinner;

    boolean imageFlag = false;
//    boolean serverR = false;

    //TextView tv;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 11;
    List<String> categoryList;

    //String firstName, mFirstName;
    String first_Name, last_Name, from,  purpose, emp_Id, organization,   blood_Group;
    String imgString="";
    Editable contact_No;
    int user_Type = 0;
    private String[] categories = new String[]{
            "Select Category",
            "New Employee",
            "Guest",
            "Vendor",
            "Client"
    };

//    byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBarSetup();



        ib = findViewById(R.id.camera_image);
        im = (ImageView) findViewById(R.id.imageView);
        etFirstName = (EditText) findViewById(R.id.etfirstName_txt);
        etLastName = (EditText) findViewById(R.id.etsecondName_txt);
        etFrom = findViewById(R.id.from_where);
        etContact = findViewById(R.id.contact_no);
        etPurpose = findViewById(R.id.etpurpose_txt);
        empId = findViewById(R.id.emp_id_txt);
        etOrganization = findViewById(R.id.etOrganization_txt);
        categorySpinner = (Spinner) findViewById(R.id.usertype_spinner);
        etbloodGroup = findViewById(R.id.etbloodGroup_txt);
        tvCapture = findViewById(R.id.tvCapture);
        tvmarquee = findViewById(R.id.MarqueeText);

        etPurpose.setAdapter(new SuggestionAdapter(this,etPurpose.getText().toString()));

        //test= findViewById(R.id.sample);

        tvmarquee.setSelected(true);


//        test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getApplicationContext(),sampleActivity.class);
//                startActivity(i);
//            }
//        });

        categoryList = new ArrayList<>(Arrays.asList(categories));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, categoryList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }



            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        categorySpinner.setAdapter(spinnerArrayAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                employee
//                guest
//                vendor
//                client

                InputMethodManager imm;

                switch (position) {

                    case 0:

                        etPurpose.setVisibility(View.GONE);
                        empId.setVisibility(View.GONE);
                        etFrom.setVisibility(View.GONE);
                        etContact.setVisibility(View.GONE);
                        etOrganization.setVisibility(View.GONE);
                        etbloodGroup.setVisibility(View.GONE);

                        //ib.setVisibility(View.GONE);

                        etbloodGroup.setText("");
                        //etPurpose.setText("");
                        etContact.setText("");
                        etFrom.setText("");
                        etOrganization.setText("");
                        empId.setText("");


                        break;

                    case 1:
                       // etPurpose.setText("");
                        etContact.setText("");
                        etFrom.setText("");
                        etOrganization.setText("");


                        etPurpose.setVisibility(View.GONE);
                        empId.setVisibility(View.VISIBLE);
                        etbloodGroup.setVisibility(View.VISIBLE);
                        etFrom.setVisibility(View.GONE);
                        etContact.setVisibility(View.GONE);
                        etOrganization.setVisibility(View.GONE);
                        im.setVisibility(View.VISIBLE);
                        //ib.setVisibility(View.GONE);
                        tvCapture.setVisibility(View.GONE);

                        empId.requestFocus();
                        user_Type =1;
                        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(empId, InputMethodManager.SHOW_IMPLICIT);

                        break;

                    case 2:
                        etFrom.setVisibility(View.VISIBLE);
                        etContact.setVisibility(View.VISIBLE);
                        etPurpose.setVisibility(View.VISIBLE);
                        empId.setVisibility(View.GONE);
                        etOrganization.setVisibility(View.GONE);
                        etbloodGroup.setVisibility(View.GONE);
                        im.setVisibility(View.VISIBLE);
                        //ib.setVisibility(View.GONE);
                        tvCapture.setVisibility(View.GONE);

                        etOrganization.setText("");
                        empId.setText("");
                        etbloodGroup.setText("");

                        etFrom.requestFocus();
                        user_Type =3;
                        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etFrom, InputMethodManager.SHOW_IMPLICIT);

                        break;

                    case 3:
                        etFrom.setVisibility(View.VISIBLE);
                        etContact.setVisibility(View.VISIBLE);
                        etPurpose.setVisibility(View.VISIBLE);
                        empId.setVisibility(View.GONE);
                        etOrganization.setVisibility(View.GONE);
                        etbloodGroup.setVisibility(View.GONE);
                        im.setVisibility(View.VISIBLE);
                        //ib.setVisibility(View.GONE);
                        tvCapture.setVisibility(View.GONE);

                        empId.setText("");
                        etOrganization.setText("");
                        etbloodGroup.setText("");
                        user_Type =4;
                        etFrom.requestFocus();
                        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etFrom, InputMethodManager.SHOW_IMPLICIT);

                        break;

                    case 4:
                        etFrom.setVisibility(View.GONE);
                        etContact.setVisibility(View.GONE);
                        etPurpose.setVisibility(View.GONE);
                        empId.setVisibility(View.GONE);
                        etOrganization.setVisibility(View.VISIBLE);
                        etbloodGroup.setVisibility(View.GONE);
                        im.setVisibility(View.VISIBLE);
                        //ib.setVisibility(View.GONE);
                        tvCapture.setVisibility(View.GONE);

                        //etPurpose.setText("");
                        etbloodGroup.setText("");
                        etContact.setText("");
                        etFrom.setText("");
                        empId.setText("");
                        user_Type =2;
                        etOrganization.requestFocus();
                        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(etOrganization, InputMethodManager.SHOW_IMPLICIT);

                        break;


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//camera button
        ib.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



                if (checkPermission()) {
                    //main logic or main code
//                    cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, receiptUri);
//                    startActivityForResult(cameraIntent, 1);
                    // . write your main code to execute, It will execute if the permission is already given.

                    values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "New Picture");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");

                    imageUri = getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, PICTURE_RESULT);

                } else {

                    requestPermission();
                }
            }

        });

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                first_Name = etFirstName.getText().toString();
                last_Name = etLastName.getText().toString();
                purpose = etPurpose.getText().toString();
                from = etFrom.getText().toString();
                contact_No = etContact.getText();
                emp_Id = empId.getText().toString();
                organization = etOrganization.getText().toString();
                blood_Group = etbloodGroup.getText().toString();

                if (categorySpinner.getSelectedItemPosition() == 0){
                    Toast.makeText(getApplicationContext(), "Please select category", Toast.LENGTH_SHORT).show();

                }

                if (TextUtils.isEmpty(first_Name)) {
                    etFirstName.setError("Please Enter First_name");
                }
                if (TextUtils.isEmpty(last_Name)) {
                    etLastName.setError("Please Enter Last_name");
                }
                if(imgString.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please Capture Image", Toast.LENGTH_SHORT).show();
                }


                if (categorySpinner.getSelectedItemPosition() == 1) {
                    if (!TextUtils.isEmpty(first_Name) && !TextUtils.isEmpty(last_Name) && !TextUtils.isEmpty(emp_Id) && !TextUtils.isEmpty(blood_Group)) {
//                        Toast.makeText(getApplicationContext(), "Processing",
//                                Toast.LENGTH_LONG).show();

//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                finish();
//                                startActivity(getIntent());
//                            }
//                        }, 5000);


                        if (imgString.isEmpty()){
                            Toast.makeText(getApplicationContext(), "Please capture image", Toast.LENGTH_SHORT).show();

                        }
                       {

                            ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                            if (netInfo == null){

                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle(getResources().getString(R.string.app_name))
                                        .setMessage("Please connect to network")
                                        .setPositiveButton("OK", null).show();
                            }else{
                                if(!imgString.isEmpty()) {
                                    new PostMethodDemo().execute();
                                }
                            }


                        }

                    }
                    /**/
                    else {
                        if (TextUtils.isEmpty(emp_Id)) {

                            empId.setError("Enter Employee Id");
                        }
                        if (TextUtils.isEmpty(blood_Group)) {
                            etbloodGroup.setError("Enter your Blood Group");
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Please capture Image",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                } else if (categorySpinner.getSelectedItemPosition() == 2)

                {
                    if (!TextUtils.isEmpty(first_Name) && !TextUtils.isEmpty(last_Name) && !TextUtils.isEmpty(from) && !TextUtils.isEmpty(contact_No)&& !TextUtils.isEmpty(purpose)) {
//                        Toast.makeText(getApplicationContext(), "Processing",
//                                Toast.LENGTH_LONG).show();
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                finish();
//                                startActivity(getIntent());
//                            }
//                        }, 5000);
                        if(contact_No.length()<10)
                        {
                            etContact.setError("Enter a valid Contact No");
                        }else{

                          /*  if (!imageFlag){
                                Toast.makeText(getApplicationContext(), "Please capture image", Toast.LENGTH_SHORT).show();

                            }*/
                             {
                                ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                                if (netInfo == null){

                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle(getResources().getString(R.string.app_name))
                                            .setMessage("Please connect to network")
                                            .setPositiveButton("OK", null).show();
                                }else{
                                    if(!imgString.isEmpty()) {
                                        new PostMethodDemo().execute();
                                    }
                                }
                            }
                        }}
                    /**/
                    else {
                        if (TextUtils.isEmpty(emp_Id)) {
                            empId.setError("Enter Employee Id");
                        } if (TextUtils.isEmpty(from)) {
                            etFrom.setError("Enter where you are From");
                        } if (TextUtils.isEmpty(contact_No)) {
                            etContact.setError("Enter Valid Contact No");
                        }
//                        if (TextUtils.isEmpty(purpose)) {
//                            etPurpose.setError("Enter Purpose of Visit");
//                        }

                    }


                } else if (categorySpinner.getSelectedItemPosition() == 3) {
                    if (TextUtils.isEmpty(from)) {
                        etFrom.setError("Enter where you are From");
                    }if (TextUtils.isEmpty(contact_No)) {
                        etContact.setError("Enter Contact No");
                    }
//                    if (TextUtils.isEmpty(purpose)) {
//                        etPurpose.setError("Enter Purpose of Visit");
//                    }
                    else {
//                        Toast.makeText(getApplicationContext(), "Processing",
//                                Toast.LENGTH_LONG).show();
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                finish();
//                                startActivity(getIntent());
//                            }
//                        }, 5000);
                        if(contact_No.length()<10) {

                            etContact.setError("Enter Valid Contact No");
                        }
                        else{
                          /*  if (!imageFlag) {
                                Toast.makeText(getApplicationContext(), "Please capture image", Toast.LENGTH_SHORT).show();

                            } */
                            {
                                ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                                if (netInfo == null){

                                    new AlertDialog.Builder(MainActivity.this)
                                            .setTitle(getResources().getString(R.string.app_name))
                                            .setMessage("Please connect to network")
                                            .setPositiveButton("OK", null).show();
                                }else{
                                    if(!imgString.isEmpty()) {
                                        new PostMethodDemo().execute();
                                    }
                                }
                            }
                        }
                    }
                } else if (categorySpinner.getSelectedItemPosition() == 4) {
                    if (TextUtils.isEmpty(organization)) {
                        etOrganization.setError("Enter Name of Your Company");
                    } else {
                            {
                            ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                            if (netInfo == null){

                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle(getResources().getString(R.string.app_name))
                                        .setMessage("Please connect to network")
                                        .setPositiveButton("OK", null).show();
                            }else{
                                if(!imgString.isEmpty()) {
                                    new PostMethodDemo().execute();
                                }
                            }

                        }
                    }
                }


            }

        });

        etFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int c = etLastName.getText().toString().length() + etFirstName.getText().toString().length();
                //Toast.makeText(getApplicationContext(), "c  "+c, Toast.LENGTH_SHORT).show();
                if (count > 18) {
                    etFirstName.setError("Max characters reached for first name");

                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int c = etLastName.getText().toString().length() + etFirstName.getText().toString().length();
                if (c > 20) {
                    etLastName.setError("you should enter only 20 characters in name");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_CAMERA);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                    // main logic
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MainActivity.this, "You need to allow access permissions", Toast.LENGTH_LONG).show();

                        }
                    }

                }
                break;
        }

    }


    public class PostMethodDemo extends AsyncTask<String, Void, Boolean> {
//        String server_response;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

//            Toast.makeText(getApplicationContext(), "error",
//            Toast.LENGTH_LONG).show();

            dialog = new ProgressDialog(MainActivity.this);

            dialog.setMessage("Processing your request, please wait.");
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;

            try {

//                 url = new URL("http://10.0.14.22:8080/gavs/api/print/card");
               //url = new URL("http://192.168.1.217:8081/gavs/api/print/card");
              // url = new URL("http://10.0.8.27:8081/gavs/api/print/card");
               url = new URL("http://10.0.100.223:8081/gavs/api/print/card");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());


                //uploadBitmap(photo);
                try {


                    JSONObject obj = new JSONObject();

                    obj.put("type", user_Type);
                    obj.put("first_Name", first_Name);
                    obj.put("last_Name", last_Name);
                    obj.put("blood_Group", blood_Group);
                    obj.put("from", from);
                    obj.put("organization", organization);
                    obj.put("contact_No", contact_No);
                    obj.put("purpose", purpose);
                    obj.put("emp_Id", emp_Id);
                    obj.put("photo", imgString);


                    wr.writeBytes(obj.toString());
                    Log.e("JSON Input", obj.toString());
                    wr.flush();
                    wr.close();


                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();


                if (responseCode == HttpURLConnection.HTTP_OK) {

                    return true;
                  /*  Toast.makeText(getApplicationContext(), "Server connected",
                            Toast.LENGTH_LONG).show();*/
//                    server_response = readStream(urlConnection.getInputStream());
                }else {

                    Toast.makeText(getApplicationContext(),"Server not connected",Toast.LENGTH_LONG).show();
                    return false;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.e("connect", "Error : "+e.getLocalizedMessage(), e);
                Toast.makeText(getApplicationContext(), "Error : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                return false;

            } catch (IOException e) {
                e.printStackTrace();
//                Toast.makeText(getApplicationContext(), "Error : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            if(s) {

                Toast.makeText(getApplicationContext(), "Process completed", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        finish();
                        startActivity(getIntent());
                    }
                }, 3000);
            }

        }
    }

    public static String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case PICTURE_RESULT:
                if (requestCode == PICTURE_RESULT)
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            Bitmap thumbnail = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), imageUri);


//                            imgView.setImageBitmap(thumbnail);
                            String imageurl = getRealPathFromURI(imageUri);

                            Matrix matrix = new Matrix();
                            matrix.postRotate(-90);
                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(thumbnail, thumbnail.getWidth(), thumbnail.getHeight(), true);
                            Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

                            Bitmap newImg = BITMAP_RESIZER(rotatedBitmap, 186, 217);

//                            im.setImageBitmap(thumbnail);
                            imageFlag = true;
                            tvCapture.setVisibility(View.GONE);
                            etFirstName.requestFocus();

                            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                            ByteArrayOutputStream stream = new ByteArrayOutputStream();

                            newImg.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            im.setImageBitmap(rotatedBitmap);

                            char[] hexArray = "0123456789ABCDEF".toCharArray();

                            char[] hexChars = new char[byteArray.length * 2];
                            for (int j = 0; j < byteArray.length; j++) {
                                int v = byteArray[j] & 0xFF;
                                hexChars[j * 2] = hexArray[v >>> 4];
                                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
                            }
                            imgString = new String(hexChars);
                            Log.e(TAG, "onActivityResult: "+imgString);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
        }

        //todo removed for testing camera quality
        /*if (data == null) {
            onResume();
        } else {



            photo = (Bitmap) data.getExtras().get("data");
            Bitmap newImg = BITMAP_RESIZER(photo, 186, 217);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            *//*int size = photo.getRowBytes() * photo.getHeight();
            ByteBuffer byteBuffer = ByteBuffer.allocate(size);
            photo.copyPixelsToBuffer(byteBuffer);
            byteArray = byteBuffer.array();*//*
            newImg.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byteArray = stream.toByteArray();
            im.setImageBitmap(photo);

            imageFlag = true;
            tvCapture.setVisibility(View.GONE);
            etFirstName.requestFocus();




            *//*BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            Bitmap source = BitmapFactory.decodeResource(a.getResources(), path, options);*//*

            // imgString = Base64.encodeToString(byteArray, Base64.DEFAULT);

            char[] hexArray = "0123456789ABCDEF".toCharArray();


            char[] hexChars = new char[byteArray.length * 2];
            for (int j = 0; j < byteArray.length; j++) {
                int v = byteArray[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            imgString = new String(hexChars);
//
            // Toast.makeText(MainActivity.this,imgString, Toast.LENGTH_LONG).show();
////            tv.setText(imgString);

            //String imageString = Base64.encodeToString(byteArray, Base64.NO_WRAP);


        }*/


    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public Bitmap BITMAP_RESIZER(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }

    @Override
    public void onResume() {  // After a pause OR at startup
        super.onResume();
        //Refresh your stuff here
    }


    /**
     * Sets the Action Bar for new Android versions.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void actionBarSetup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar ab = getSupportActionBar();
            ab.setTitle("Welcome to GAVS Technologies Private Limited");
        }
    }

    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
   /* public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        //getting the tag from the edittext
        final String tags = "dummy tag";

        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, "http://10.0.8.139:8081/gavs/api/print/card",
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            *//*
     * If you want to add more parameters with the image
     * you can do it here
     * here we have only one parameter with the image
     * which is tags
     * *//*
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("type", user_Type);
                params.put("first_Name" , first_Name);
                params.put("last_Name" , last_Name);
                params.put("blood_Group", blood_Group);
                params.put("organization" ,organization);
                params.put("contact_No" ,contact_No);
                params.put("purpose" , purpose);
                params.put("emp_Id" , emp_Id);
                //params.put("photo",imgString);

                return params;
            }

            *//*
     * Here we are passing image by renaming it with a unique name
     * *//*
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("photo", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }*/

}


