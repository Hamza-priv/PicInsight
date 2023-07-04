package com.example.picinsight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ResultText extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_text);
        TextView text = findViewById(R.id.displayText);
        TextView count = findViewById(R.id.count);
        Intent intent = getIntent();
        String answer = intent.getStringExtra("gptResult");
        String question = intent.getStringExtra("userQuestion");
        text.setText(answer);
        DataBaseHandler db = new DataBaseHandler(ResultText.this);
        PicInsightModel storeQuery = new PicInsightModel();
        storeQuery.setQuestion(question);
        storeQuery.setAnswer(answer);
        db.addQuery(storeQuery);
        count.setText(String.valueOf(db.getCount()));
    }
}
