package com.dirk41.draganddraw;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lingchong on 15-9-25.
 */
public class Box implements Parcelable {
    private PointF mPointFOrigin;
    private PointF mPointFCurrent;

    public Box(PointF pointF) {
        mPointFOrigin = mPointFCurrent = pointF;
    }

    public PointF getPointFOrigin() {
        return mPointFOrigin;
    }

    public PointF getPointFCurrent() {
        return mPointFCurrent;
    }

    public void setPointFCurrent(PointF pointFCurrent) {
        mPointFCurrent = pointFCurrent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeParcelable(mPointFOrigin, flags);
        dest.writeParcelable(mPointFCurrent, flags);
    }

    protected Box(Parcel in) {
        mPointFOrigin = in.readParcelable(PointF.class.getClassLoader());
        mPointFCurrent = in.readParcelable(PointF.class.getClassLoader());
    }

    public static final Creator<Box> CREATOR = new Creator<Box>() {
        @Override
        public Box createFromParcel(Parcel in) {
            return new Box(in);
        }

        @Override
        public Box[] newArray(int size) {
            return new Box[size];
        }
    };
}
