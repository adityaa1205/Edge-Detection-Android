#include <jni.h>
#include <string>
#include <opencv2/opencv.hpp>
#include "opencv_processing.h"

extern "C" JNIEXPORT jbyteArray JNICALL
Java_com_example_edgeapp_CameraActivity_processFrame(
        JNIEnv* env,
        jobject /* this */,
        jbyteArray input,
        jint width,
        jint height) {
    jbyte* inputBytes = env->GetByteArrayElements(input, nullptr);
    jsize length = env->GetArrayLength(input);

    // Process the frame using OpenCV
    jbyteArray result = processFrameNative(env, inputBytes, width, height);

    env->ReleaseByteArrayElements(input, inputBytes, JNI_ABORT);

    return result;
}
