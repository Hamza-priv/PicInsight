package com.example.picinsight;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecylerViewAdaptor extends RecyclerView.Adapter<RecylerViewAdaptor.ViewHolder> {
    private Context context;
    private List<PicInsightModel> picInsightModelList;

    public RecylerViewAdaptor(Context context, List<PicInsightModel> picInsightModelList) {
        this.context = context;
        this.picInsightModelList = picInsightModelList;
    }


    @NonNull
    @Override
    public RecylerViewAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecylerViewAdaptor.ViewHolder holder, int position) {
        PicInsightModel picInsightModel = picInsightModelList.get(position);
        holder.queryAnswer.setText(picInsightModel.getAnswer());
        holder.queryQuestion.setText(picInsightModel.getQuestion());
    }

    @Override
    public int getItemCount() {
        return picInsightModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView queryAnswer,queryQuestion;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            queryAnswer = itemView.findViewById(R.id.answer);
            queryQuestion= itemView.findViewById(R.id.question);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, "Position ", Toast.LENGTH_SHORT).show();
            int position = getAdapterPosition();
            PicInsightModel picInsightModel = picInsightModelList.get(position);
            String answer = picInsightModel.getAnswer();
            String question = picInsightModel.getQuestion();
            Intent intent = new Intent(context, displayQuery.class);
            intent.putExtra("answer", answer);
            intent.putExtra("question", question);
            context.startActivity(intent);
        }
    }
}
