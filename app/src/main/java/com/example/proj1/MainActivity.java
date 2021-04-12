package com.example.proj1;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLConnection;
import java.util.Scanner;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editText;
    private TextView textView;
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        textView.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                predict(s.toString());
            }
        });
    }

    void predict(final String text) {
        new Thread() {
            public void run() {
                String request0 = "https://predictor.yandex.net/api/v1/predict.json/complete?key=pdct.1.1.20210412T141130Z.7fe24c3464fb114c.a91bd86212d96a2ed24c9e0a44fc721cd4e118ea&q=hello+";
                String request1 = text;
                String request2 = "&lang=ru";
                String request = request0 + request1 + request2;
                URLConnection connection = null;
                try {
                    connection = new URL(request).openConnection();
                    Scanner in = new Scanner(connection.getInputStream());
                    final String response = in.nextLine();
                    runOnUiThread(new Thread() {
                        public void run() {
                            String finalResponse = "";
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("text");
                                for (int i = 0; i < jsonArray.length() && i < 1; i++) {
                                    finalResponse = jsonArray.get(i).toString();
                                }
                                pos = jsonObject.getInt("pos");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            textView.setText(finalResponse);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    @Override
    public void onClick(View v) {
        if (v == textView) {
            String text = textView.getText().toString();
            try {
                text = text.substring(-pos);
                editText.setText(editText.getText() + text);
            } catch (Exception e) {
                editText.setText(editText.getText() + " " + text);
            }
            editText.setSelection(editText.getText().length());
        }
    }


}
