package com.example.edgeapp;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class GLView extends GLSurfaceView {
    private GLRenderer renderer;

    public GLView(Context context) {
        super(context);
        init();
    }

    public GLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        renderer = new GLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void updateTexture(byte[] data, int width, int height) {
        renderer.updateTexture(data, width, height);
        requestRender();
    }
}
