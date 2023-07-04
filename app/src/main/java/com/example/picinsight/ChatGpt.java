package com.example.picinsight;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ChatGpt {
    String aiResponse;
    private Context context;
    public ChatGpt(Context context) {
        this.context = context;
    }
    public void ChatGptAnswer(String result, final ChatGptCallback callback) {

        String url = "https://openai80.p.rapidapi.com/chat/completions";
        String apiKey = "3645c4a726msh2f611d629aba2c7p195c42jsn06ff988e63ab";
        String host = "openai80.p.rapidapi.com";

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("model", "gpt-3.5-turbo");
            Log.d("CP","suc");
        } catch (JSONException e) {
            aiResponse = new RuntimeException(e).toString();
            Log.d("CP",e.toString());
            throw new RuntimeException(e);
        }
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject();
        try {
            message.put("role", "user");
        } catch (JSONException e) {
            Log.d("CP",e.toString());
            throw new RuntimeException(e);
        }
        try {
            message.put("content", result);
        } catch (JSONException e) {
            Log.d("CP",e.toString());
            throw new RuntimeException(e);
        }
        messages.put(message);
        try {
            requestBody.put("messages", messages);
        } catch (JSONException e) {
            Log.d("CP",e.toString());
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                response -> {
                    // Handle the response here
                    try {
                        JSONArray choices = response.getJSONArray("choices");
                        JSONObject firstChoice = choices.getJSONObject(0);
                        JSONObject messageObj = firstChoice.getJSONObject("message");
                        aiResponse = messageObj.getString("content");
                        callback.onSuccess(aiResponse); // Invoke the callback with the aiResponse
                    } catch (JSONException e) {
                        callback.onError("Error: " + e.getMessage());
                    }
                },
                error -> {
                    // Handle errors here
                    if (error instanceof TimeoutError) {
                        callback.onError("Timeout Error: " + error);
                    } else if (error instanceof NetworkError) {
                        callback.onError("Network Error: " + error);
                    } else {
                        callback.onError("Error: " + error);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("content-type", "application/json");
                headers.put("X-RapidAPI-Key", apiKey);
                headers.put("X-RapidAPI-Host", host);
                return headers;
            }

            @Override
            public RetryPolicy getRetryPolicy() {
                // Set the retry policy
                return new DefaultRetryPolicy(
                        30000, // timeout in ms
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                );
            }
        };
// Add the request to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

}

