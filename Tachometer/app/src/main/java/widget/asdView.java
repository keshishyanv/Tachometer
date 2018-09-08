package widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.havayi.tachometer.R;

/**
 * Created by Havayi on 19-Jan-17.
 */

public class asdView extends View {



    public asdView(Context context) {
        this(context, null);
    }

    public asdView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public asdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        updateThread = new UpdateThread();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
    private UpdateThread updateThread;
    private class UpdateThread implements Runnable{
        boolean isClicked = false;

        @Override
        public void run() {
            if(isClicked)
            {
                // inch vor mi ban
                postDelayed(this , 100);
            }
            }
        }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                updateThread.isClicked = true;
                postDelayed(updateThread , 100);
                // u eli inch vor qayler
                return true;
            case MotionEvent.ACTION_UP:
                updateThread.isClicked = false;
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }


}
