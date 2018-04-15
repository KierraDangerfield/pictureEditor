package com.kierradangerfield.pictureeditor;
/*sources:
* https://stackoverflow.com/questions/15704205/how-to-draw-line-on-imageview-along-with-finger-in-android
* https://stackoverflow.com/questions/21864863/android-canvas-clear-with-transparency
* https://stackoverflow.com/questions/6956838/how-to-erase-previous-drawing-on-canvas*/

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kierra on 3/25/2018.
 */

public class DrawView extends View {
    int tool = MainActivity.tools;

    /*private static int lineSize = 100;
    private static int lineDIP = 5;*/
   int drawingSize; // = MainActivity.size;
    private static int androidDIP = 50;

    private static int rectangleSizeDip = 50;
    private static int rectangleStepDip = 5;

    private Paint linePaint;
    private Paint bitmapPaint;
    private Random random;

    private int currentWidth;
    private int currentHeight;

    private Paint recPaint;
    private ArrayList<Rectangle> rectangles;
    private ArrayList<Path> paths = new ArrayList<Path>();

   public static Bitmap bitmap;
    private Canvas canvas;
    Path path;

    float x, y;
    float width = 100.0f;
    float height = 50.0f;

    int color = MainActivity.color;

    int color2 = MainActivity.color;

  //  private ImageView imageView;

    public DrawView(Context context) {
        super(context);
         setup();
        rectangleSetUp();

    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
       rectangleSetUp();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setup();
        rectangleSetUp();
    }

    public void setup(){
      //  ImageView iv = findViewById(R.id.picture_imageView);

            linePaint = new Paint();
            linePaint.setAntiAlias(true);
            linePaint.setColor(color2);
            linePaint.setStyle(Paint.Style.STROKE);
            drawingSize = 50;

            path = new Path();
           /* bitmapPaint = new Paint();
            bitmapPaint.setColor(Color.GREEN);*/

            loadBitmap();

     /* /*RECTANGLE *
        random = new Random();
        recPaint = new Paint();
        recPaint.setStyle(Paint.Style.FILL);
        rectangles = new ArrayList<>();
        setUpBitmap();*/
       // setBackground(getBackground(R.id.picture_imageView));

      /*  random = new Random();

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.FILL);

        rectangles = new ArrayList<>();

        setUpBitmap();*/
      /*  linePaint = new Paint();
        linePaint.setColor(Color.CYAN);
        linePaint.setStyle(Paint.Style.STROKE);

        int dpSize = 2; // want the line to be 2 dp wide
        DisplayMetrics dm = getResources().getDisplayMetrics();
        float strokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
        linePaint.setStrokeWidth(strokeWidth);
        linePaint.setAntiAlias(true);*/
    }

    public void rectangleSetUp(){
        /*recPaint = new Paint();
        recPaint.setStyle(Paint.Style.FILL);

        random = new Random();
        rectangles = new ArrayList<>();

        loadBitmap();*/
        x = y = 0;

        recPaint = new Paint();
        recPaint.setColor(color2);
        recPaint.setStyle(Paint.Style.STROKE);

    }

    private void loadBitmap(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int pixelSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, drawingSize, dm);
        bitmap = Bitmap.createBitmap(pixelSize, pixelSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

    }

    private void setUpBitmap(){

       // imageView = findViewById(R.id.picture_imageView);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int pixelSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, androidDIP, dm);



        //make the drawable
       /*Canvas canvas = new Canvas(bitmap);
        imageView.setAdjustViewBounds(true);
        imageView.getAdjustViewBounds();
        imageView.draw(canvas);*/
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        /*FOR RECTANGLE */
        currentWidth = w;
        currentHeight = h;

        /****FOR LINES*****************************************************************/
        bitmap = Bitmap.createBitmap(currentWidth, currentHeight, Bitmap.Config.ARGB_8888);
      canvas = new Canvas(bitmap);

    }

    public void addRectangle(){
        Rectangle r = new Rectangle(rectangleSizeDip, rectangleStepDip);
        rectangles.add(r);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //while (tool == 1) {
        //Path[] paths;
        //linePaint.setColor(color);
        //bitmapPaint.setColor(Color.WHITE);

        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);

            //if (MainActivity.recButton.isChecked()){

              //  canvas.drawRect(downX, downY, x, y, recPaint);
           // }else  {
                canvas.drawPath(path, linePaint);
            //}




        //}

       /* while (tool == 0){
            canvas.drawRect(x,y,x + width, y + height, recPaint);
        }*/
        /*FOR RECTANGLE*/

   /*  int size = rectangles.size();

        for (int i = 0; i < size; i++){
           Rectangle r = rectangles.get(i);


            canvas.drawBitmap(bitmap, r.x, r.y, recPaint);
        }*/


       /* if (touch){
            canvas.drawRect(x,y,x + width, y + height, recPaint);
        }*/



      //  canvas.drawPaint(backgroundPaint);
      /*  canvas.drawLine(0, 0, currentWidth - 1, currentHeight - 1, linePaint);
        canvas.drawLine(currentWidth - 1, 0, 0, currentHeight - 1, linePaint);*/
    }

    public void changeColor(int color){

        linePaint.setColor(color);
        recPaint.setColor(color);

    }

    public void changeSize(int size){
        linePaint.setStrokeWidth((float) size);
        recPaint.setStrokeWidth((float) size);
    }

    public void clearCanvas(){
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void drawRectangle(){

        /*while(tool == 0){
            canvas.drawRect(x, y, x2, y2.getY(), drawPaint);
        }*/
        canvas.drawRect(downX, downY, x, y, recPaint);
    }

    public void changeStyle(Paint.Style style){
        linePaint.setStyle(style);
        recPaint.setStyle(style);
    }

    public void save(){

    }

    private float downX, downY;
    private static final float TOUCH_TOL = 4;
    private void touchDown(float x, float y){

        path.moveTo(x,y);

        downX = x;
        downY = y;
    }

    private void touchMove(float x, float y){
        float moveX = Math.abs(x - downX);
        float moveY = Math.abs(y - downY);

        if (moveX >= TOUCH_TOL || moveY >= TOUCH_TOL){
            path.quadTo(downX, downY, (x + downX)/2, (y + downY)/2);
            downX = x;
            downY = y;
        }
    }

    private void touchUp(){
        int tool = MainActivity.tools;

        path.lineTo(downX, downY);

        if (tool == 1) {
            canvas.drawPath(path, linePaint);
        }else if (tool == 0) {
            canvas.drawRect(downX, downY, x, y, recPaint);
        }
        path.reset();
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();


        if (MainActivity.recButton.isChecked()){
            switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:
                   /* downX = x;
                    downY = y;*/
                   touchDown(x, y);
                   invalidate();
                    return true;

                case MotionEvent.ACTION_MOVE:
                    touchMove(x, y);
                    invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    touchUp();
                    invalidate();
                    break;

                default:
                    return false;

            }
            postInvalidate();
            return true;

        }else  {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touchDown(x, y);
                    invalidate();
                    break;

                case MotionEvent.ACTION_MOVE:
                    touchMove(x, y);
                    invalidate();
                    break;

                case MotionEvent.ACTION_UP:
                    touchUp();
                    invalidate();
                    break;

                default:
                    break;
            }
        }

        return true;
    }



    /**
     * Source and explanation
     * http://stackoverflow.com/questions/12266899/onmeasure-custom-view-explanation
     */
  /*  @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
    }*/

   /* public void start() {
        if (timer == null) {
            Random r = new Random();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (backgroundPaint.getColor() == Color.RED) {
                        backgroundPaint.setColor(Color.BLUE);
                    } else {
                        backgroundPaint.setColor(Color.RED);
                    }
                    postInvalidate();
                }
            };

            timer = new Timer();
            timer.schedule(task, 0, r.nextInt(2000) + 1000);
        }
    }*/

  /*  public void stop() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }*/

  class Rectangle{

      int x, y;
      float pixelSize;
      //float pixelSize, xStep

      Rectangle(int rectangleSizeDip, int rectangleStepDip){

          DisplayMetrics dm = getResources().getDisplayMetrics();
          pixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rectangleSizeDip, dm);

          x = random.nextInt((int) (currentWidth - pixelSize));
          y = random.nextInt((int) (currentHeight - pixelSize));
      }
  }
}
