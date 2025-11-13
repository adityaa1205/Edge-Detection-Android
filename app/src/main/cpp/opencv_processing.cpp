#include "opencv_processing.h"
#include <opencv2/opencv.hpp>

jbyteArray processFrameNative(JNIEnv* env, jbyte* inputBytes, int width, int height) {
    // Create OpenCV Mat from input bytes (assuming grayscale)
    cv::Mat input(height, width, CV_8UC1, (unsigned char*)inputBytes);

    // Apply Canny edge detection
    cv::Mat edges;
    cv::Canny(input, edges, 50, 150);

    // Convert back to byte array
    jbyteArray result = env->NewByteArray(width * height);
    env->SetByteArrayRegion(result, 0, width * height, (jbyte*)edges.data);

    return result;
}
