package com.example.edgeapp;

public class JNIUtils {
    static {
        System.loadLibrary("native-lib");
    }

    public native byte[] processFrame(byte[] input, int width, int height);
}
