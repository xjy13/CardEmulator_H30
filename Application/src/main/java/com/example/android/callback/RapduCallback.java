package com.example.android.callback;

public interface RapduCallback {
    void onDone(byte[] result);
    void onError(Exception e);
}
