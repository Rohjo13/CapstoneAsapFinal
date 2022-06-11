package co.mjc.capstoneasapfinal.note;

import static co.mjc.capstoneasapfinal.NoteFolderActivity.stringToBitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class WriteNote extends View {

    private String TAG = "WriteNote";
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private String bitmapDataByString;

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public Paint getmPaint() {
        return mPaint;
    }

    public WriteNote(Context context, String bitmapDataByString) {
        super(context);


        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        this.bitmapDataByString = bitmapDataByString;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i(TAG, "onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
        if (bitmapDataByString == null) {
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        } else {
            Bitmap bitmap = stringToBitmap(bitmapDataByString);
            mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        }
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw");
        canvas.drawBitmap(mBitmap, 0, 0, null);
        canvas.drawPath(mPath, mPaint); //현재 그리고 있는 내용
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent");
        // 이벤트가 발생하는 그 시점의 (x,y) 좌표
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.reset();
                mPath.moveTo(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE: // 누르고 움직였을 때
                mPath.lineTo(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP: // 누른 것을 떼면
                mPath.lineTo(x, y);
                mCanvas.drawPath(mPath, mPaint);
                mPath.reset();
                break;
        }
        this.invalidate();
        return true;
    }
}
