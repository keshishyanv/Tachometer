package widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.havayi.tachometer.R;

/**
 * Created by Havayi on 19-Jan-17.
 */

public class TachometerVIew extends View {

    private int color;
    private Paint mPaint;
    private Paint semicirclePaint;
    private float centerX;
    private float centerY;
    private float width;
    private float height;
    private Paint arrowPaint;
    private float lenA;
    private Path arrow;
    private RectF oval;
    Matrix matrixArrow;
    private int angle;

    private Update update;

    public TachometerVIew(Context context) {
        this(context, null);
    }

    public TachometerVIew(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TachometerVIew(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttrs(attrs);
        init();
    }

    private void init()
    {
        update = new Update();

        mPaint = new Paint();
        arrowPaint = new Paint();
        mPaint.setColor(color);

        setAngle(-90);

        arrowPaint.setColor(Color.BLUE);

        semicirclePaint = new Paint();
        semicirclePaint.setStrokeWidth(10f);
        semicirclePaint.setColor(Color.BLACK);
        semicirclePaint.setStyle(Paint.Style.STROKE);
        oval = new RectF();
        arrow = new Path();
        matrixArrow = new Matrix();
    }

    private void readAttrs(AttributeSet attrs)
    {
        TypedArray values = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.Tachometer, 0,0 );
        this.color = values.getColor(R.styleable.Tachometer_color , Color.GRAY);
        values.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(centerX != 0) {
            width = getWidth();
            height = getHeight();
            centerX = width / 2;
            centerY = height - 30f;
            lenA = width / 30;
        }
        oval.set(10, 10 , width - 10, height * 2);

        canvas.drawCircle(centerX, centerY, 30f, mPaint);
        canvas.drawArc(oval, 180, 130, false, semicirclePaint);
        mPaint.setStrokeWidth(10f);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval , 310, 50, false, mPaint);

        arrow.reset();
        arrow.moveTo(centerX, centerY);
        arrow.lineTo(centerX - (lenA ), centerY - (lenA ));
        arrow.lineTo(centerX , height/6);
        arrow.lineTo(centerX + (lenA ), centerY - (lenA ));
        arrow.close();
        arrow.transform(matrixArrow);
        matrixArrow.setRotate(getAngle() , centerX,centerY);
        canvas.drawPath(arrow, arrowPaint);
    }

    public void setAngle(int angle) {
        this.angle = angle;
        invalidate();
    }

    public int getAngle() {
        return angle;
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    private class Update implements Runnable{
        int isClicked = 0;

        @Override
        public void run() {
            if(getAngle() >= -90 && getAngle() <= 90) {
                setAngle(getAngle() + isClicked);
                post(this);
            }
            else {
                setAngle(getAngle() - isClicked);
                isClicked = 0;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(update.isClicked == 0) {
                    update.isClicked = 1;
                    post(update);
                }
                else {
                    update.isClicked = 1;
                }
                return true;
            case MotionEvent.ACTION_UP:
                if(update.isClicked == 0) {
                    update.isClicked = -1;
                    post(update);
                }
                else {
                    update.isClicked = -1;
                }
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        height = h;
        width = w/2;
        centerY = height - 30f;
        centerX = width/2;
        setAngle(getAngle());
    }

}
