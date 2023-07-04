package com.example.picinsight;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {
    public DataBaseHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create = "CREATE TABLE " + Params.TABLE_NAME + "("
                + Params.KEY_ID + " INTEGER PRIMARY KEY," + Params.KEY_ANSWER
                + " TEXT, " + Params.KEY_QUESTION + " TEXT" + ")";
        sqLiteDatabase.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addQuery(PicInsightModel picInsightModel){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Params.KEY_ANSWER, picInsightModel.getAnswer());
        values.put(Params.KEY_QUESTION, picInsightModel.getQuestion());

        db.insert(Params.TABLE_NAME,null, values);
        db.close();
        Log.d("db", "Successfully inserted");
    }

    public List<PicInsightModel> getAllQuery(){
        List<PicInsightModel> picInsightModelList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT * FROM " + Params.TABLE_NAME;
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                PicInsightModel picInsightModel = new PicInsightModel();
                picInsightModel.setId(Integer.parseInt(cursor.getString(0)));
                picInsightModel.setAnswer(cursor.getString(1));
                picInsightModel.setQuestion(cursor.getString(2));

                // Add the picInsightModel to the list
                picInsightModelList.add(picInsightModel);
            } while (cursor.moveToNext());
        }
        cursor.close(); // Close the cursor
        db.close(); // Close the database

        return picInsightModelList;
    }

    public int getCount(){
        String query = "SELECT * FROM " + Params.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount();
    }
}
