package Contracts;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by cunfe on 2016-01-19.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context){
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        mRenderer = new MyGLRenderer();
        this.setRenderer(mRenderer);
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }
}
