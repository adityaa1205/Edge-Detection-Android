# Android Edge Detection App

A real-time edge detection Android application that captures camera frames, processes them using OpenCV Canny edge detection via JNI, and renders the results using OpenGL ES 2.0. Includes a TypeScript web viewer for demonstration.

## Architecture

```
CameraX (Java) → JNI → OpenCV C++ (Canny) → OpenGL ES (Render) → Display
```

## Components

### Android App (`/app`)
- **CameraActivity.java**: Main activity handling CameraX capture and analysis
- **JNIUtils.java**: JNI bridge utilities
- **GLRenderer.java**: OpenGL ES 2.0 renderer for displaying processed frames
- **GLView.java**: GLSurfaceView wrapper
- **native-lib.cpp**: JNI entry point
- **opencv_processing.cpp/h**: OpenCV Canny edge detection implementation
- **CMakeLists.txt**: NDK build configuration
- **Shaders**: Vertex and fragment shaders for OpenGL rendering

### Web Viewer (`/web`)
- **index.html**: Simple HTML page displaying processed frames and stats
- **main.ts**: TypeScript logic for frame viewer
- **dist/main.js**: Compiled JavaScript

## Setup Instructions

### Prerequisites
- Android Studio with NDK support
- OpenCV Android SDK
- Node.js and TypeScript (for web viewer)

### 1. Install OpenCV for Android:
1. Download OpenCV Android SDK from https://opencv.org/releases/
2. Extract to a directory (e.g., `C:/OpenCV-android-sdk`)
3. Update `CMakeLists.txt` with the correct path:
   ```
   set(OpenCV_DIR "C:/path/to/OpenCV-android-sdk/sdk/native/jni")
   ```

### 2. Build the Android App:
1. Open the project in Android Studio
2. Ensure NDK is installed via SDK Manager
3. Sync Gradle files
4. Build and run on device/emulator

### 3. Build the Web Viewer:
1. Navigate to `/web` directory
2. Run `tsc --init` (if not already done)
3. Run `tsc` to compile TypeScript to JavaScript
4. Open `index.html` in a browser

## Frame Pipeline:

1. **Capture**: CameraX captures real-time frames as YUV_420_888
2. **Conversion**: Convert to grayscale byte array
3. **JNI Call**: Pass bytes to native C++ via `processFrame()`
4. **Processing**: OpenCV applies Canny edge detection (thresholds 50, 150)
5. **Return**: Processed grayscale bytes returned to Java
6. **Rendering**: OpenGL ES renders the texture on GLSurfaceView
7. **Display**: Real-time edge detection at 10-15 FPS

## Key Features:

- Real-time camera capture using CameraX
- Efficient JNI bridge to C++ OpenCV
- OpenGL ES 2.0 hardware-accelerated rendering
- Grayscale edge detection output
- TypeScript web viewer for demonstration
- Maintains 10-15 FPS performance

## Troubleshooting

### CMake/OpenCV Issues
- Ensure OpenCV_DIR path is correct in CMakeLists.txt
- Verify OpenCV Android SDK is properly extracted

### Gradle Build Issues
- Check Java version compatibility (requires Java 11+)
- Ensure NDK is installed and configured

### OpenGL Rendering Issues
- Verify device supports OpenGL ES 2.0
- Check shader compilation logs

## Performance Notes

- Frame processing is optimized for real-time performance
- OpenGL rendering uses efficient texture updates
- CameraX analysis runs on background executor
- JNI calls are minimized for low latency
