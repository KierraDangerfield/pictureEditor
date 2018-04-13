package com.kierradangerfield.pictureeditor;
/*SOURCES: https://stackoverflow.com/questions/15704205/how-to-draw-line-on-imageview-along-with-finger-in-android*/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Random;

/*FEATURES
1. CHANGE CANVAS BACKGROUND
2. CHANGE BACKGROUND LAYOUT-> DAY/LIGHT MODE
3.
*
* */

public class MainActivity extends Activity {
    private int reqCode = 1;
    static final int TAKE_PICTURE = 2;
    ImageView imageView;

    private static int IMAGE = 1;
    private Bitmap bitmap;
    private Canvas canvas;
    private Paint linePaint;

    float xDown = 0, yDown = 0;
    float xUp, yUp;

    public static int tools = 1;
   public static int size = 50;
   public static int color = Color.BLACK;

   public static RadioButton recButton;
    boolean isDayLight = true;
    boolean canvasLight = true;

    public static Paint.Style style = Paint.Style.STROKE;

    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //DrawView drawView = new DrawView(this);

        final DrawView dv = findViewById(R.id.drawView);
        dv.setup();



        RadioButton rb;
        recButton = findViewById(R.id.rec_radioButton);

        rb = findViewById(R.id.line_radioButton);
        rb.setChecked(true);


        //tools radio group
        //RadioGroup radioGroup = findViewById(R.id.tools
       RadioGroup rg =  findViewById(R.id.tools_radioGroup);
       rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(RadioGroup radioGroup, int id) {
               DrawView dv = findViewById(R.id.drawView);
               if (id == R.id.rec_radioButton){
                   tools = 0;
                   /*dv.setOnTouchListener(new View.OnTouchListener() {
                      /* @Override
                       public boolean onTouch(View view, MotionEvent motionEvent) {
                           if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                               float x = motionEvent.getX();
                               float y = motionEvent.getY();


                           }
                           return false;
                       }*
                   });*/

                  dv.drawRectangle();
               }else if (id == R.id.line_radioButton){
                   tools = 1;
               }

           }
       });

       findViewById(R.id.small_radioButton);
       findViewById(R.id.Medium_radioButton);
       findViewById(R.id.large_radioButton);

     rg =  findViewById(R.id.size_radioGroup);
     rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(RadioGroup radioGroup, int id) {
            // RadioButton rb = id;
             DrawView dv = findViewById(R.id.drawView);

             if (id == R.id.small_radioButton){
                 size = 25;
             }else if (id == R.id.Medium_radioButton){
                 size = 50;
             }else if (id == R.id.large_radioButton){
                 size = 75;
             }

             dv.changeSize(size);
         }
     });

    RadioButton color_rb = findViewById(R.id.black_radioButton);
    color_rb.setChecked(true);
   // color_rb = findViewById(R.id.cyan_radioButton);
        findViewById(R.id.cyan_radioButton);
       // color_rb.setChecked(true);
    findViewById(R.id.red_radioButton);

    RadioGroup color_radioGroup = findViewById(R.id.color_radioGroup);
    color_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
           int selected = radioGroup.getCheckedRadioButtonId();
           DrawView dv = findViewById(R.id.drawView);

            if (selected == R.id.black_radioButton){

                color = Color.BLACK;
            }else if (selected == R.id.cyan_radioButton){
                color = Color.CYAN;
            }else if (selected == R.id.red_radioButton){
                color = Color.RED;
            }
            dv.changeColor(color);
        }
    });

        //change canvas color----------->>>>FEATURE**********************************************************************
  final Button cb = findViewById(R.id.canvasBackgroundColor_button);

  cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawView dv = findViewById(R.id.drawView);
                int canvasColor;

                if (canvasLight){
                    canvasColor = Color.BLACK;
                    dv.setBackgroundColor(canvasColor);
                    dv.invalidate();
                    cb.setText("White Background");
                    canvasLight = false;
                }else if (canvasLight == false){
                    canvasColor = Color.WHITE;
                    dv.setBackgroundColor(canvasColor);
                    dv.invalidate();
                    cb.setText("Black Background");
                    canvasLight = true;
                }

            }
        });

        //day or night mode--->>>>>FEATURE**********************************************************************
      final Button b = findViewById(R.id.layoutMode_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ConstraintLayout layout = findViewById(R.id.layout);

                if (isDayLight){

                    layout.setBackgroundColor(Color.rgb(105,105,105));
                    changeTextColorDay();
                    b.setText("Day Mode");
                    isDayLight = false;

                }else if(isDayLight == false){

                    layout.setBackgroundColor(Color.rgb(255,255,240));
                    changeTextColorNight();
                    b.setText("Night Mode");
                    isDayLight = true;
                }
            }
        });

        //Clear Canvas---------->FEATURE**********************************************************************
        findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Bitmap imBitmap;

                dv.clearCanvas();
            }
        });

        rb =  findViewById(R.id.stroke_radioButton);
        rb.setChecked(true);
        findViewById(R.id.fill_radioButton);
        findViewById(R.id.strokeFill_radioButton);
        rg = findViewById(R.id.style_radioGroup);
        //change style---------->FEATURE**********************************************************************
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (i == R.id.stroke_radioButton){
                    style = Paint.Style.STROKE;
                }else if (i == R.id.fill_radioButton){
                    style = Paint.Style.FILL;
                }else if (i == R.id.strokeFill_radioButton){
                    style = Paint.Style.FILL_AND_STROKE;
                }

                DrawView dv = findViewById(R.id.drawView);
                dv.changeStyle(style);
            }
        });


        //open gallery
        /*findViewById(R.id.openGallery_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImage();
            }
        });*/

        //image
       /* this.findViewById(R.id.picture_imageView).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

             / ImageView iv = findViewById(R.id.picture_imageView);
                Drawable drawable = iv.getDrawable();
                drawable.setBounds(20,30, drawable.getIntrinsicWidth() + 20, drawable.getIntrinsicHeight() +30);
                drawable.draw(canvas);*

                DrawView dv = findViewById(R.id.drawView);
                dv.setup();

                return false;
            }
        });*/


       /* findViewById(R.id.picture_imageView).setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        //DrawView drawView = findViewById(R.id.picture_imageView);
                        //Canvas canvas = new Canvas();
                        // DrawView drawView = DrawView;
                        float actionDown_x, actionDown_y;
                        float actionUp_x, actionUp_y;
                        int action = motionEvent.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_DOWN:
                                actionDown_x = motionEvent.getX();
                                actionDown_y = motionEvent.getY();
                                break;
                            case MotionEvent.ACTION_UP:
                                actionUp_x = motionEvent.getX();
                                actionUp_y = motionEvent.getY();
                               //canvas.drawLine(actionDown_x, actionDown_y, actionUp_x, actionUp_y);
                                break;
                            default:
                                 break;
                        }
                        return false;
                    }
                });*/
 
        /*
        * source
        * https://stackoverflow.com/questions/15704205/how-to-draw-line-on-imageview-along-with-finger-in-android*/




        /****************** SAVE BUTTON***************************************************************/
       findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DrawView dv = new DrawView(MainActivity.this);
                //dv.setDrawingCacheEnabled(true);
                save();
            }
        });


    }
    int[] ids = {R.id.rec_radioButton, R.id.line_radioButton, R.id.small_radioButton,
    R.id.Medium_radioButton, R.id.large_radioButton, R.id.black_radioButton, R.id.cyan_radioButton,
    R.id.red_radioButton, R.id.stroke_radioButton, R.id.fill_radioButton, R.id.strokeFill_radioButton};

    public void changeTextColorDay(){
        TextView tv;

        tv = findViewById(R.id.tools_textview);
        tv.setTextColor(Color.WHITE);
        tv = findViewById(R.id.size_textView);
        tv.setTextColor(Color.WHITE);
        tv = findViewById(R.id.color_textview);
        tv.setTextColor(Color.WHITE);
        tv = findViewById(R.id.style_textView);
        tv.setTextColor(Color.WHITE);


        for (int i = 0; i < ids.length; i++){
         //   int b = i;
            RadioButton rb = findViewById(ids[i]);
            rb.setTextColor(Color.WHITE);
        }
    }

    public void changeTextColorNight(){
        TextView tv;

        tv = findViewById(R.id.tools_textview);
        tv.setTextColor(Color.BLACK);
        tv = findViewById(R.id.size_textView);
        tv.setTextColor(Color.BLACK);
        tv = findViewById(R.id.color_textview);
        tv.setTextColor(Color.BLACK);
        tv = findViewById(R.id.style_textView);
        tv.setTextColor(Color.BLACK);

        for (int i = 0; i < ids.length; i++){
            //   int b = i;
            RadioButton rb = findViewById(ids[i]);
            rb.setTextColor(Color.BLACK);
        }
    }



public  void save() {
Bitmap bm;
//DrawView view = findViewById(R.id.drawView);
bm = DrawView.bitmap;

File dir = new File(Environment.getExternalStorageDirectory().toString());
boolean success = false;
if (!dir.exists()){
    success = dir.mkdirs();
}

File file = new File(Environment.getExternalStorageDirectory().toString() + "/sample.png");

if (!file.exists()){
    try {
     success = file.createNewFile();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

FileOutputStream fos = null;
try{
    fos = new FileOutputStream(file);

    DrawView view = findViewById(R.id.drawView);

    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
} catch (FileNotFoundException e) {
    e.printStackTrace();
}

    /*String root = Environment.getExternalStorageDirectory().toString();
        File dir = new File(root + "/sdcard/saved_images");
        dir.mkdirs();
        Random random = new Random();
        int n = 1000;
        n = random.nextInt(n);
        String name = "Image-" + n +".jpg";
        File file = new File(dir, name);
        if (file.exists()){
            file.delete();
        }

        try{
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }*/

   /* DrawView dv = new DrawView(this);
    bitmap = dv.getDrawingCache();
    try {
        FileOutputStream fos = openFileOutput("name.png", Context.MODE_PRIVATE);
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, fos);

        fos.close();
    } catch (IOException e) {
        e.printStackTrace();
    }*/
}



    private File getMediaFile(){
        File file = new File(Environment.getExternalStorageDirectory() +
                            "/Android/data" +
                            getApplicationContext().getPackageName() + "/Files");

        if (!file.exists()){
            if (!file.mkdirs()){
                return null;
            }
        }


        File mediaFile;
        //String imageName = "MI_" +

        return file;
    }

   /* public void onCheckedChanged(RadioGroup radioGroup, int clickedId){

    }*/


   /* public void editButtonClicked() {
        //displays width and height
        Display display = getWindowManager().getDefaultDisplay();

        float displayWidth = display.getWidth();
        float displayHeight = display.getHeight();

        //ImageView imageView = findViewById(R.id.picture_imageView);
        bitmap = Bitmap.createBitmap((int) displayWidth, (int) displayHeight, Bitmap.Config.ARGB_8888);

        canvas = new Canvas(bitmap);
        linePaint = new Paint();
        linePaint.setColor(Color.CYAN);
        imageView.setImageBitmap(bitmap);
        imageView.setOnTouchListener((View.OnTouchListener) this);
    }*/






    /*SOURCE TO GET IMAGE FROM GALLERY
    http://programmerguru.com/android-tutorial/how-to-pick-image-from-gallery/8
    * */

    //get picture from gallery
   /* private void getImage(){

        Intent getPicIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(getPicIntent, reqCode );
    }*/
/*SELECT IMAGE*/
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


            //try {

            if (requestCode == reqCode && resultCode == RESULT_OK && null != data) {
                Uri selectPic = data.getData();
                String[] image = {MediaStore.Images.Media.DATA};

                //cursor
                Cursor cursor = getContentResolver().query(selectPic, image, null, null, null);
                //move cursor to 1st row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(image[0]);
                String path = cursor.getString(columnIndex);
                cursor.close();

                // ImageView imageView = findViewById(R.id.picture_imageView);
                //set image
                //imageView.setImageBitmap(BitmapFactory.decodeFile(path));
                Bitmap bitmap2 = BitmapFactory.decodeFile(path);

                // DrawView drawView =findViewById(R.id.drawView);
                // canvas.drawBitmap(imageView.getWidth(), imageView.getHeight());

                //DrawView dv = findViewById(R.id.drawView);
                // drawView.start();
                //  imageView.setOnTouchListener((View.OnTouchListener) this);


                //save image
            /*case 2:
                // if (requestCode == reqCode && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                ImageView iv = findViewById(R.id.picture_imageView);
                iv.setImageBitmap(imageBitmap);
              /*  } else {
                    Toast.makeText(getApplicationContext(), "An image has not been picked!",
                            Toast.LENGTH_LONG).show();
                }*
                //break;

            default:
                break;
       /* }catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }*

        }
    }*
            }*/

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        int touch = event.getAction();

        drawLines(imageView);
        switch (touch){
            case MotionEvent.ACTION_DOWN:
                xDown = event.getX();
                yDown = event.getY();
                break;

            case  MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                xUp = event.getX();
                yUp = event.getY();
                canvas.drawLine(xDown, yDown, xUp, yUp, linePaint);
                imageView.invalidate();
                break;

            default:
                break;
        }

        return super.onTouchEvent(event);

    }*/

    /*public void drawLines(ImageView imageView){
        bitmap = Bitmap.createBitmap((int) imageView.getWidth(), (int) imageView.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        linePaint = new Paint();
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.FILL);
        //ImageView imageView = findViewById(R.id.drawView);
        imageView.setImageBitmap(bitmap);

      //  imageView.setOnTouchListener((View.OnTouchListener) this);
    }*/

    /*@Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int touch = motionEvent.getAction();

        switch (touch){
            case MotionEvent.ACTION_DOWN:
                xDown = motionEvent.getX();
                yDown = motionEvent.getY();
                break;

            case  MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                xUp = motionEvent.getX();
                yUp = motionEvent.getY();
                canvas.drawLine(xDown, yDown, xUp, yUp, linePaint);
                imageView.invalidate();
                break;

            default:
                break;
        }
        return false;
    }*/


   /* @Override
    protected void onResume() {
        super.onResume();

        DrawView dv = findViewById(R.id.drawView);
        dv.setup();
    }*/
/* @Override
    protected void onPause() {
        super.onPause();
        DrawView dv = findViewById(R.id.drawView);
        dv.stop();

    }*/
}

