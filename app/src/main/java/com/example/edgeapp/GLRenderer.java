package com.example.edgeapp;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "GLRenderer";

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private int mProgram;
    private int mPositionHandle;
    private int mTexCoordHandle;
    private int mTextureHandle;
    private int mTextureId;

    private FloatBuffer vertexBuffer;
    private FloatBuffer texCoordBuffer;
    private ShortBuffer drawListBuffer;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static final int TEXCOORDS_PER_VERTEX = 2;

    static float squareCoords[] = {
            -1.0f, 1.0f, 0.0f, // top left
            -1.0f, -1.0f, 0.0f, // bottom left
            1.0f, -1.0f, 0.0f, // bottom right
            1.0f, 1.0f, 0.0f }; // top right

    static float texCoords[] = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f };

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    private volatile byte[] frameData;
    private volatile int frameWidth;
    private volatile int frameHeight;

    public GLRenderer() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // initialize texture coordinate buffer
        ByteBuffer tcb = ByteBuffer.allocateDirect(texCoords.length * 4);
        tcb.order(ByteOrder.nativeOrder());
        texCoordBuffer = tcb.asFloatBuffer();
        texCoordBuffer.put(texCoords);
        texCoordBuffer.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        // Load shaders
        String vertexShaderCode = loadShaderFromAssets("shader_vertex.glsl");
        String fragmentShaderCode = loadShaderFromAssets("shader_fragment.glsl");

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);

        // Create texture
        int[] textures = new int[1];
        GLES20.glGenTextures(1, textures, 0);
        mTextureId = textures[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        if (frameData != null) {
            updateTexture();
        }

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                COORDS_PER_VERTEX * 4, vertexBuffer);

        // get handle to texture coordinates
        mTexCoordHandle = GLES20.glGetAttribLocation(mProgram, "vTexCoord");
        GLES20.glEnableVertexAttribArray(mTexCoordHandle);
        GLES20.glVertexAttribPointer(mTexCoordHandle, TEXCOORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                TEXCOORDS_PER_VERTEX * 4, texCoordBuffer);

        // get handle to texture
        mTextureHandle = GLES20.glGetUniformLocation(mProgram, "texture");
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
        GLES20.glUniform1i(mTextureHandle, 0);

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordHandle);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    public void updateTexture(byte[] data, int width, int height) {
        frameData = data;
        frameWidth = width;
        frameHeight = height;
    }

    private void updateTexture() {
        if (frameData != null) {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureId);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, frameWidth, frameHeight, 0,
                    GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, ByteBuffer.wrap(frameData));
            frameData = null; // Clear after update
        }
    }

    public static int loadShader(int type, String shaderCode) {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    private String loadShaderFromAssets(String filename) {
        // For simplicity, hardcode the shader code here
        if (filename.equals("shader_vertex.glsl")) {
            return "attribute vec4 vPosition;\n" +
                    "attribute vec2 vTexCoord;\n" +
                    "varying vec2 texCoord;\n" +
                    "void main() {\n" +
                    "    texCoord = vTexCoord;\n" +
                    "    gl_Position = vPosition;\n" +
                    "}";
        } else if (filename.equals("shader_fragment.glsl")) {
            return "precision mediump float;\n" +
                    "varying vec2 texCoord;\n" +
                    "uniform sampler2D texture;\n" +
                    "void main() {\n" +
                    "    gl_FragColor = texture2D(texture, texCoord);\n" +
                    "}";
        }
        return "";
    }
}
