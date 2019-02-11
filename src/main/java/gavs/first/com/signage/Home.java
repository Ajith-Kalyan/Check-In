package gavs.first.com.signage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    EditText et;
    TextView tv;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        et = findViewById(R.id.et1);
        tv = findViewById(R.id.tv1);

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            tag = et.getText().toString();
            tv.setText(tag);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }
}
