package com.example.reinhardstaveaux.sudoku;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jbuy519 on 19/08/2014.
 */
public class PuzzleView extends View {

    private static final String TAG = "Sudoku.view";

    private final Game game;

    private int tileWidth;
    private int tileHeight;
    private int selX;
    private int selY;

    private Paint background;
    private Paint hilite;
    private Paint light;
    private Paint dark;
    private Paint foreground;

    private Paint selected;


    private final Rect selRect = new Rect();

    public PuzzleView(Context context) {
        super(context);
        this.game = (Game)context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        init();
    }

    public PuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.game = (Game)context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        init();
    }

    public PuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.game = (Game)context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        init();
    }

    public void init(){
        background = new Paint();
        background.setColor(getResources().getColor(R.color.puzzle_background));

        this.requestFocus();
        this.setFocusableInTouchMode(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG,"widthMeasureSpec : "+widthMeasureSpec+" , heightMeasureSpec : "+heightMeasureSpec);
        Log.d(TAG,"MeasureWidth : "+measureWidth(widthMeasureSpec)+" , MeasureHeight : "+measureHeight(heightMeasureSpec));
        //use rect cursorX * measureHight/tileheight idem y

        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);

        getRect((int)(selX * height/tileWidth),(int)(selY * width/tileHeight) ,selRect);
        setMeasuredDimension(width,height);
    }

    private void getRect(int x, int y, Rect rect){
        rect.set((int) (x*tileWidth),(int)y*tileHeight,(int)(x*tileWidth+tileHeight),(int)(y * tileWidth + tileHeight));
    }

    private int measureHeight(int measureSpec){
        //getMode
        int measureMode = MeasureSpec.getMode(measureSpec);
        //getsize
        int measureSize = MeasureSpec.getSize(measureSpec);
        //unspecified == 500
        int result;
        if(measureMode == MeasureSpec.UNSPECIFIED)
        {

            result = 500;
        }
        else
        {

            result = measureSize;
        }
        tileHeight = result/9;
        return result;
    }

    private int measureWidth(int measureSpec){
        //getMode
        int measureMode = MeasureSpec.getMode(measureSpec);
        //getsize
        int measureSize = MeasureSpec.getSize(measureSpec);
        //unspecified == 500
        int result;
        if(measureMode == MeasureSpec.UNSPECIFIED)
        {
            result = 500;
        }
        else
        {
            result = measureSize;
        }
        //tileHeight / 9
        tileWidth = result/9;
        Log.d(TAG,"TileWidth : "+tileWidth);
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Draw the background...
        Paint background = new Paint();
        background.setColor(getResources().getColor(
                R.color.puzzle_background));
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);


        // Draw the board...

        // Define colors for the grid lines
        Paint dark = new Paint();
        dark.setColor(getResources().getColor(R.color.puzzle_dark));

        Paint hilite = new Paint();
        hilite.setColor(getResources().getColor(R.color.puzzle_hilite));

        Paint light = new Paint();
        light.setColor(getResources().getColor(R.color.puzzle_light));


        //Tekent minor grid lines
        for (int i=0; i<9; i++){
            canvas.drawLine(0, i*tileHeight, getWidth(), i*tileHeight, light);
            canvas.drawLine(0, i*tileHeight + 1, getWidth(), i*tileHeight+1, hilite);
            canvas.drawLine(i*tileWidth, 0, i*tileWidth, getHeight(), light);
            canvas.drawLine(i*tileWidth+1, 0, i*tileWidth+1,getHeight(),hilite);
        }

        //tekent de major grid lines
        for (int i=0; i<9; i++){
            if (i%3 != 0)
                continue;
            canvas.drawLine(0, i*tileHeight, getWidth(), i*tileHeight, dark);
            canvas.drawLine(0, i*tileHeight+1, getWidth(), i*tileHeight+1, hilite);
            canvas.drawLine(i*tileWidth, 0, i*tileWidth, getHeight(), dark);
            canvas.drawLine(i*tileWidth+1, 0, i*tileWidth+1, getHeight(), hilite);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyDown: keycode=" + keyCode + ", event="
                + event);
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                select(selX, selY - 1);
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                select(selX, selY + 1);
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                select(selX - 1, selY);
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                select(selX + 1, selY);
                break;


            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE: setSelectedTile(0); break;
            case KeyEvent.KEYCODE_1:     setSelectedTile(1); break;
            case KeyEvent.KEYCODE_2:     setSelectedTile(2); break;
            case KeyEvent.KEYCODE_3:     setSelectedTile(3); break;
            case KeyEvent.KEYCODE_4:     setSelectedTile(4); break;
            case KeyEvent.KEYCODE_5:     setSelectedTile(5); break;
            case KeyEvent.KEYCODE_6:     setSelectedTile(6); break;
            case KeyEvent.KEYCODE_7:     setSelectedTile(7); break;
            case KeyEvent.KEYCODE_8:     setSelectedTile(8); break;
            case KeyEvent.KEYCODE_9:     setSelectedTile(9); break;
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_DPAD_CENTER:
                game.showKeypadOrError(selX, selY);
                break;


            default:
                return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    public void setSelectedTile(int tile) {
        if (game.setTileIfValid(selX, selY, tile)){
            invalidate();// may change hints
        } else {
            // Number is not valid for this tile
            Log.d(TAG, "setSelectedTile: invalid: " + tile);
        }
    }

    private void select(int x, int y){
        //Is heel belangrijk!
        //TODO vul de code aan om te nieuwe x en y coordinaten te
        //bepalen om de nieuw geselecteerde rechthoek te bepalen
        // GEBRUIK INVALIDATE
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);

        select((int) (event.getX() / tileWidth),
                (int) (event.getY() / tileHeight));
        game.showKeypadOrError(selX, selY);
        Log.d(TAG, "onTouchEvent: x " + selX + ", y " + selY);
        return true;
    }


}
