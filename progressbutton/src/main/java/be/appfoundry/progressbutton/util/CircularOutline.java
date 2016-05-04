package be.appfoundry.progressbutton.util;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

/** Outline provider for the elevation shadow of the ProgressButton */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CircularOutline extends ViewOutlineProvider {

    int width;
    int height;

    public CircularOutline(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void getOutline(View view, Outline outline) {
        Rect r = new Rect(0, 0, width, height);
        outline.setOval(r);
    }

}
