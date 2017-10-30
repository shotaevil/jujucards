package juju.android.jujucards;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;

/**
 * Created by tzimonjic on 5/9/17.
 */

public class Utils {
    public void SlideUP(View view, Context context)
    {
        view.startAnimation(AnimationUtils.loadAnimation(context,
                R.anim.slide_down));
    }

    public void SlideLeft(View view, Context context, String speed)
    {
        if(speed.equals("fast")){
            view.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.slide_left_fast));
        }else {
            view.startAnimation(AnimationUtils.loadAnimation(context,
                    R.anim.slide_left));
        }
    }
}
