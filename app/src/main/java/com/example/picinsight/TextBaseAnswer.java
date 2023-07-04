package com.example.picinsight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
public class TextBaseAnswer extends AppCompatActivity implements ChatGptCallback {
    EditText userInput;
    String aiResponse, userText;
    Button send;
    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_base_answer);
        userInput = findViewById(R.id.inputText);
        send = findViewById(R.id.textButton);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        userText = intent.getStringExtra("picResult");
        if(!TextUtils.isEmpty(userText)){
            userInput.setText(userText);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userText = userInput.getText().toString();
                showLoadingPrompt();
                ChatGpt chatGpt = new ChatGpt(TextBaseAnswer.this);
                chatGpt.ChatGptAnswer(userText, TextBaseAnswer.this);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // Handle the back button click
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onSuccess(String response) {
        hideLoadingPrompt();
        aiResponse = response;
        Intent intent = new Intent(TextBaseAnswer.this, ResultText.class);
        intent.putExtra("gptResult", aiResponse.toString());
        intent.putExtra("userQuestion", userText.toString());
        Log.d("S", aiResponse.toString());
        startActivity(intent);
    }

    @Override
    public void onError(String error) {
            hideLoadingPrompt();
            Intent intent = new Intent(TextBaseAnswer.this, ResultText.class);
            intent.putExtra("gptResult", error);
            startActivity(intent);
        Log.d("S", aiResponse.toString() + error.toString());
    }


        private void showLoadingPrompt() {
        loadingDialog = ProgressDialog.show(this, "", "Generating response...", true);
    }

    private void hideLoadingPrompt() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }
}
