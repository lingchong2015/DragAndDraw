package com.dirk41.draganddraw;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by lingchong on 15-9-25.
 */
public class BoxDrawingView extends View {
    private Box mBox;
    private ArrayList<Box> mBoxArrayList = new ArrayList<>();
    private Paint mPaintBox;
    private Paint mPaintBackground;
//    private boolean mIsSingleTouch = true;
//    private float mFloatOldDistance;
//    private float mFloatStartAngle;
//    private Matrix mCurrentMatrix;
//    private PointF mPointFMiddle = new PointF(0 , 0);

    private static final String TAG = "BoxDrawingView";

    public BoxDrawingView(Context context) {
        super(context);
        initPaintTools();
        setBoxColor(Color.parseColor("#22ff0000"));
        setBackgroundColor(Color.parseColor("#fff8efe0"));
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaintTools();
        setBoxColor(Color.parseColor("#22ff0000"));
        setBackgroundColor(Color.parseColor("#fff8efe0"));
    }

    private void initPaintTools() {
        mPaintBox = new Paint();
        mPaintBackground = new Paint();
    }

    public void setBoxColor(int color) {
        mPaintBox.setColor(color);
    }

    public void setBackgroundColor(int color) {
        mPaintBackground.setColor(color);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF pointFCurrent = new PointF(event.getX(), event.getY());
        Log.i(TAG, "Received event at x = " + pointFCurrent.x + ", y = " + pointFCurrent.y + " : ");

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "ACTION_MOVE.");
                if (mBox != null) {
                    mBox.setPointFCurrent(pointFCurrent);
                    invalidate();
                }
//                if (mIsSingleTouch) {
//                    Log.i(TAG, "ACTION_MOVE.");
//                    if (mBox != null) {
//                        mBox.setPointFCurrent(pointFCurrent);
//                        invalidate();
//                    }
//                }
//                else {
//                    float floatEndAngle = getAngle(event);
//                    mCurrentMatrix.postRotate(-(floatEndAngle - mFloatStartAngle) * 180, mPointFMiddle.x, mPointFMiddle.y);
//                }
                break;
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "ACTION_DOWN.");
                mBox = new Box(pointFCurrent);
                mBoxArrayList.add(mBox);
//                mIsSingleTouch = true;
                break;
            case MotionEvent.ACTION_UP:
                Log.i(TAG, "ACTION_UP.");
                mBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "ACTION_CANCEL.");
                mBox = null;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.i(TAG, "ACTION_POINTER_DOWN.");
//                mIsSingleTouch = false;
//                mFloatOldDistance = spacing(event);
//                mFloatStartAngle = getAngle(event);
//                middlePoint(mPointFMiddle, event);
//                mCurrentMatrix = new Matrix();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                Log.i(TAG, "ACTION_POINTER_UP");
//                mIsSingleTouch = false;
                break;
        }

        return true;
    }

//    private float getAngle(MotionEvent motionEvent) {
//        double deltaX = motionEvent.getX(0) - motionEvent.getX(1);
//        double deltaY = motionEvent.getY(0) - motionEvent.getY(1);
//        return (float) Math.atan2(deltaX, deltaY);
//    }
//
//    private float spacing(MotionEvent motionEvent) {
//        float x = motionEvent.getX(0) - motionEvent.getX(1);
//        float y = motionEvent.getY(0) - motionEvent.getY(1);
//        return (float) Math.sqrt(x * x + y * y);
//    }
//
//    private void middlePoint(PointF pointF, MotionEvent motionEvent) {
//        float x = motionEvent.getX(0) + motionEvent.getX(1);
//        float y = motionEvent.getY(0) + motionEvent.getY(1);
//        pointF.set(x / 2, y / 2);
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPaint(mPaintBackground);

//        canvas.save();
//        rotateCanvas(canvas);
        for (Box box : mBoxArrayList) {
            float left = Math.min(box.getPointFOrigin().x, box.getPointFCurrent().x);
            float top = Math.min(box.getPointFOrigin().y, box.getPointFCurrent().y);
            float right = Math.max(box.getPointFOrigin().x, box.getPointFCurrent().x);
            float bottom = Math.max(box.getPointFOrigin().y, box.getPointFCurrent().y);

            canvas.drawRect(left, top, right, bottom, mPaintBox);
        }
//        canvas.restore();
    }

//    private void rotateCanvas(Canvas canvas) {
//
//    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable parcelable = super.onSaveInstanceState();

        return new MySaveState(parcelable, mBoxArrayList);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(((MySaveState) state).getSuperState());

        MySaveState mySaveState = (MySaveState) state;
        mBoxArrayList = mySaveState.getBoxArrayList();
        invalidate();
    }

    public static class MySaveState extends BaseSavedState {
        private ArrayList<Box> mBoxArrayList;

        public ArrayList<Box> getBoxArrayList() {
            return mBoxArrayList;
        }

        public void setBoxArrayList(ArrayList<Box> boxArrayList) {
            mBoxArrayList = boxArrayList;
        }

        public MySaveState(Parcelable parcelable, ArrayList<Box> boxArrayList) {
            super(parcelable);

            mBoxArrayList = boxArrayList;
        }

        public MySaveState(Parcel source) {
            super(source);

            mBoxArrayList = (ArrayList<Box>) source.readArrayList(Box.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);

            out.writeList(mBoxArrayList);
        }

        @Override
        public int describeContents() {
            return super.describeContents();
        }

        public static final Parcelable.Creator<MySaveState> CREATOR = new Creator<MySaveState>() {
            @Override
            public MySaveState createFromParcel(Parcel source) {
                return new MySaveState(source);
            }

            @Override
            public MySaveState[] newArray(int size) {
                return new MySaveState[0];
            }
        };
    }
}
