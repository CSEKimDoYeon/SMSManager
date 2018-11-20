package com.example.kimdoyeon.smsmanager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity {
    TextView tv_Address;
    TextView tv_Body;
    EditText et_sendMessage;
    Button btn_send;

    public String message_Address;
    public String message_Body;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        tv_Address = (TextView) findViewById(R.id.tv_message_address);
        tv_Body = (TextView) findViewById(R.id.tv_message_body);
        et_sendMessage = (EditText) findViewById(R.id.et_send);
        btn_send = (Button) findViewById(R.id.btn_sendMessage);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String send_Address = message_Address;
                String message_contents = et_sendMessage.getText().toString();

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(send_Address, null, message_contents, null, null);
                    Toast.makeText(getApplicationContext(), "전송이 완료되었습니다.", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "전송을 실패하였습니다.", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });

        message_Address = getIntent().getStringExtra("key_Address");
        message_Body = getIntent().getStringExtra("key_Body");

        tv_Address.setText(message_Address);
        tv_Body.setText(message_Body);
    }
}