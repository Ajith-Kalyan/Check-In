package gavs.first.com.signage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class sampleActivity extends AppCompatActivity {
    public static final String PREFERENCES_FILE_NAME = "SIGNAGE";

    ImageView imvEmp;
    ImageView imvClient;
    ImageView imvGuest;
    ImageView imvVendor;

    int usertype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(PREFERENCES_FILE_NAME,0);
        final SharedPreferences.Editor editor = sharedPref.edit();

    imvEmp = findViewById(R.id.imnewemp);
    imvClient = findViewById(R.id.ivclient);
    imvGuest =findViewById(R.id.ivguest);
    imvVendor = findViewById(R.id.ivguest);

    imvEmp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        usertype =1;
            editor.putInt("usertype",usertype);
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);

        }
    });

    imvClient.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            usertype =2;
            editor.putInt("usertype",usertype);
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
    });
    imvGuest.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            usertype =3;
            editor.putInt("usertype",usertype);
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
    });
    imvVendor.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            usertype =4;
            editor.putInt("usertype",usertype);
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
    });

    }
}
