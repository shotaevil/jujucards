package juju.android.jujucards;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

import org.androidannotations.annotations.EBean;

/**
 * Created by Milos on 7/16/2017.
 */

@EBean
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public String getRotation(Context context) {
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return "portrait";
            case Surface.ROTATION_90:
                return "landscape";
            case Surface.ROTATION_180:
                return "reverse portrait";
            default:
                return "reverse landscape";
        }
    }

    public int getWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Log.v("Screen width", "" + size.x);
        return size.x;
    }

    public int getHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public void displayFragment(final Fragment fragment, final @IdRes int fragmentContainer) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();

        // If the fragment is already in the queue, we pop it back and introduce the new one
        String fragmentTag =  fragment.getClass().getSimpleName();
        if (isThereFragment(fragmentTag)) {
            popFragmentInclusive(fragmentTag);
        }

        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        transaction.replace(fragmentContainer, fragment, fragmentTag);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
    }

    private boolean isThereFragment(final String fragmentTag) {
        return getSupportFragmentManager().findFragmentByTag(fragmentTag) != null;
    }

    protected void popFragmentInclusive(String fragmentTag) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.popBackStackImmediate(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

}
