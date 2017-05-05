package com.gyj.colock_demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * data:2017/5/5
 * author:郭彦君(Administrator)
 * function:
 */
public class TickingClockView extends View {

    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    private Paint strokePaint;

    private int radius;

    private int innerPadding;

    private int borderSize = 15;

    private int borderColor = 0xFF9f0100;

    private int hourPiontColor = 0xFF000000;

    private int minutePiontColor = 0xFF00f100;

    private int secondPointColor = 0xFF0000f3;

    private boolean stopDraw = false;

    private SimpleDateFormat format;
    private Date date;

    public TickingClockView(Context context) {
        this(context,null);
    }

    public TickingClockView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }


    public TickingClockView(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCanvas(context);
    }

    private void initCanvas(Context c) {

        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        radius = Math.min(getWidth(), getHeight())/2-borderSize;
        canvas.setDrawFilter(paintFlagsDrawFilter);

        Rect rect = new Rect(radius, radius, radius, radius);
        strokePaint.setColor(Color.BLUE);
        canvas.drawRect(rect, strokePaint);


        long timeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String format = sdf.format(new Date(timeMillis));

        canvas.save();
        canvas.translate(getWidth()/2, getHeight()/2);
        strokePaint.setColor(Color.RED);
        strokePaint.setStyle(Style.STROKE);
        strokePaint.setStrokeWidth(borderSize);
        canvas.drawCircle(0, 0, radius, strokePaint);


        canvas.save();

        strokePaint.setTextSize(50);
        canvas.rotate(0);

        for (int i = 0; i<12; i++)
        {
            strokePaint.setColor(Color.BLUE);
            strokePaint.setStrokeWidth(borderSize);

            int startX = (int) (Math.cos(Math.toRadians(-i*30))*(radius-20));
            int startY = (int) (Math.sin(Math.toRadians(-i*30))*(radius-20));
            int stopX = (int) (Math.cos(Math.toRadians(-i*30))*(radius-5));
            int stopY = (int) (Math.sin(Math.toRadians(-i*30))*(radius-5));

            String text = ""+(i+1);
            float textWidth = strokePaint.measureText(text);
            int clockHourPosX =  (int) ((Math.cos(Math.toRadians(i*30+270+30))*(radius-40))-textWidth/3);
            int clockHourPosY = (int) ((Math.sin(Math.toRadians(i*30+270+30))*(radius-40))+textWidth/3);
            canvas.drawLine(startX, startY, stopX, stopY, strokePaint);

            strokePaint.setStrokeWidth(borderSize/2);
            strokePaint.setColor(Color.BLACK);
            strokePaint.setStyle(Style.STROKE.FILL);
            canvas.drawText(text, clockHourPosX, clockHourPosY, strokePaint);
            for (int j = 1; j < 5; j++)
            {

                int minStartX = (int) (Math.cos(Math.toRadians(-i*30-j*6))*(radius-15));
                int minStartY = (int) (Math.sin(Math.toRadians(-i*30-j*6))*(radius-15));
                int minStopX = (int) (Math.cos(Math.toRadians(-i*30-j*6))*(radius-5));
                int minStopY = (int) (Math.sin(Math.toRadians(-i*30-j*6))*(radius-5));

                canvas.drawLine(minStartX, minStartY, minStopX, minStopY, strokePaint);
            }

        }

        String[] strFormatTimes = format.split(":");
        int secondText = Integer.parseInt(strFormatTimes[2]);
        int minuteText = Integer.parseInt(strFormatTimes[1]);
        int hourText = Integer.parseInt(strFormatTimes[0])%12;

        strokePaint.setStyle(Style.STROKE);
        strokePaint.setStrokeCap(Cap.ROUND);

        //秒钟
        int startX = (int) (Math.cos(Math.toRadians(-90+6*secondText))*(-50));
        int startY = (int) (Math.sin(Math.toRadians(-90+6*secondText))*(-50));
        int stopX = (int) (Math.cos(Math.toRadians(-90+6*secondText))*(radius-50));
        int stopY = (int) (Math.sin(Math.toRadians(-90+6*secondText))*(radius-50));
        strokePaint.setStrokeWidth(4);
        strokePaint.setColor(Color.DKGRAY);
        canvas.drawLine(startX, startY, stopX, stopY, strokePaint);

        //分针
        startX = (int) (Math.cos(Math.toRadians(-90+minuteText*6+6*secondText/60f))*(-30));
        startY = (int) (Math.sin(Math.toRadians(-90+minuteText*6+6*secondText/60f))*(-30));
        stopX = (int) (Math.cos(Math.toRadians(-90+minuteText*6+6*secondText/60f))*(radius-100));
        stopY = (int) (Math.sin(Math.toRadians(-90+minuteText*6+6*secondText/60f))*(radius-100));
        strokePaint.setStrokeWidth(6);
        strokePaint.setColor(Color.MAGENTA);
        canvas.drawLine(startX, startY, stopX, stopY, strokePaint);

        //时针
        startX = (int) (Math.cos(Math.toRadians(-90+hourText*30+30*minuteText/60f+30*secondText/3600f)));
        startY = (int) (Math.sin(Math.toRadians(-90+hourText*30+30*minuteText/60f+30*secondText/3600f)));
        stopX = (int) (Math.cos(Math.toRadians(-90+hourText*30+30*minuteText/60f+30*secondText/3600f))*(radius-150));
        stopY = (int) (Math.sin(Math.toRadians(-90+hourText*30+30*minuteText/60f+30*secondText/3600f))*(radius-150));
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStrokeWidth(10);
        canvas.drawLine(startX, startY, stopX, stopY, strokePaint);

        strokePaint.setStrokeWidth(1);
        strokePaint.setStyle(Style.STROKE.FILL);
        strokePaint.setTextSize(50f);
        float measureText = strokePaint.measureText(format);
        canvas.drawText("星期五", -measureText*3/2,0, strokePaint);


        strokePaint.setColor(Color.CYAN);
        strokePaint.setStrokeWidth(10);
        canvas.drawCircle(0, 0, 10, strokePaint);


        canvas.restore();

        startTicking();
    }

    private void startTicking()
    {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                invalidate();
                if(!stopDraw)
                {
                    postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopDraw = true;
    }
    //触摸点击
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //中心点
        float a = getWidth()/2;//x的二分之一
        float b = getHeight()/2;//y的二分之一

        float c = event.getX();
        float d = event.getY();
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_UP:
                if(c<a  &&   c<a+radius*0.4f  &&   d<b+radius*0.2f     &&   d<b+radius*0.2f*2  ){
                    Toast.makeText(getContext(), "sss", Toast.LENGTH_SHORT).show();
                }else{
                    //  toastMessage("框外");

                }
                break;
        }
        return true;
    }

}
