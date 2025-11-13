cmake_minimum_required(VERSION 3.22.1)

project("edgeapp")

# IMPORTANT: Download OpenCV Android SDK from https://opencv.org/releases/
# Extract the ZIP file and update the path below to point to:
# opencv-android-sdk/sdk/native/jni
set(OpenCV_DIR "C:/Users/adity/Downloads/OpenCV-android-sdk/sdk/native/jni")

find_package(OpenCV REQUIRED)

add_library(native-lib SHARED
        native-lib.cpp
        opencv_processing.cpp)

find_library(log-lib log)

# Force the same STL that OpenCV was built with
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -lc++_shared")
# Ensure we link in the correct order and include all necessary STL components
target_link_libraries(native-lib
        ${log-lib}
        ${OpenCV_LIBS}
        -lc++_shared
        -lc
        -lm
        -ldl
        android)


# Include OpenCV headers
include_directories(${OpenCV_INCLUDE_DIRS})
