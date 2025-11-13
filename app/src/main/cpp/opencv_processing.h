#ifndef OPENCV_PROCESSING_H
#define OPENCV_PROCESSING_H

#include <jni.h>

jbyteArray processFrameNative(JNIEnv* env, jbyte* inputBytes, int width, int height);

#endif // OPENCV_PROCESSING_H
