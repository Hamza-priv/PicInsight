package com.example.picinsight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {
    private RecylerViewAdaptor recylerViewAdaptor;
    private RecyclerView recyclerView;
    private ArrayList<PicInsightModel> picInsightModelArrayList;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DataBaseHandler db = new DataBaseHandler(History.this);
        List<PicInsightModel> picInsightModelList = db.getAllQuery();

        // Initialize the picInsightModelArrayList
        picInsightModelArrayList = new ArrayList<>();

        for (PicInsightModel picInsightModel : picInsightModelList) {
            picInsightModelArrayList.add(picInsightModel);
        }

        recylerViewAdaptor = new RecylerViewAdaptor(History.this, picInsightModelArrayList);
        recyclerView.setAdapter(recylerViewAdaptor);
    }
}
