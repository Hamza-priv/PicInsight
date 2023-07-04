package com.example.picinsight;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class displayQuery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_query);
        Toolbar toolbar = findViewById(R.id.infoToolbar);
        Intent intent = getIntent();
        String answer = intent.getStringExtra("answer");
        String question = intent.getStringExtra("question");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        TextView nameTextView = findViewById(R.id.displayAnswer);
        nameTextView.setText(answer);
        TextView phoneTextView = findViewById(R.id.displayQuestion);
        phoneTextView.setText(question);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            Intent intent = new Intent(this, History.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}