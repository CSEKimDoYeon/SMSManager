package com.example.kimdoyeon.smsmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kimdoyeon.smsmanager.DeleteKeywordDB.DeleteKeywordDbOpenHelper;

public class AddDeleteKeywordActivity extends Activity {

    EditText et_input;
    Button btn_ok;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delete_keyword_popup);

        //requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀바 삭제


        et_input = (EditText) findViewById(R.id.et_input_add_delete_keyword);
        btn_ok = (Button) findViewById(R.id.btn_ok_add_delete_keyword);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input_Delete_Keyword = et_input.getText().toString();

                Intent intent = new Intent();
                intent.putExtra("data", input_Delete_Keyword);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }

}
