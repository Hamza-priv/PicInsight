package com.example.picinsight;

public interface ChatGptCallback {
    void onSuccess(String aiResponse);

    void onError(String errorMessage);
}
