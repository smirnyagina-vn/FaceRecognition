package com.example.facerecognition;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.common.util.ArrayUtils;

import java.util.ArrayList;
import java.util.stream.Stream;

public class UserMetadata implements Parcelable {

    public PointF smilingProbability;
    public PointF eyeProbability;
    //public PointF rot;
    public PointF leftEar;
    public PointF leftCheek;
    public PointF lefEye;
    public PointF mouthLeft;
    public PointF rightEar;
    public PointF rightEye;
    public PointF rightCheek;
    public PointF mouthRight;
    public PointF mouthBottom;
    public PointF noseBase;


    public UserMetadata(
            PointF smilingProbability,
            PointF eyeProbability,
             PointF rot,
             PointF leftEar,
             PointF leftCheek,
             PointF lefEye,
             PointF mouthLeft,
             PointF rightEar,
             PointF rightEye,
             PointF rightCheek,
             PointF mouthRight,
             PointF mouthBottom,
             PointF noseBase
    )
    {
        this.smilingProbability = smilingProbability;
        this.eyeProbability = eyeProbability;
        //this.rot = rot;
        this.leftEar = leftEar;
        this.leftCheek = leftCheek;
        this.lefEye = lefEye;
        this.mouthLeft = mouthLeft;
        this.rightEar = rightEar;
        this.rightEye = rightEye;
        this.rightCheek = rightCheek;
        this.mouthRight = mouthRight;
        this.mouthBottom = mouthBottom;
        this.noseBase = noseBase;
    }


    public UserMetadata()
    {
        this.smilingProbability = new PointF();
        this.eyeProbability = new PointF();
        //this.rot = new PointF();
        this.leftEar = new PointF();
        this.leftCheek = new PointF();
        this.lefEye = new PointF();
        this.mouthLeft = new PointF();
        this.rightEar = new PointF();
        this.rightEye = new PointF();
        this.rightCheek = new PointF();
        this.mouthRight = new PointF();
        this.mouthBottom = new PointF();
        this.noseBase = new PointF();
    }

    protected UserMetadata(Parcel in) {
        smilingProbability = in.readParcelable(PointF.class.getClassLoader());
        eyeProbability = in.readParcelable(PointF.class.getClassLoader());
        //rot = in.readParcelable(PointF.class.getClassLoader());
        leftEar = in.readParcelable(PointF.class.getClassLoader());
        leftCheek = in.readParcelable(PointF.class.getClassLoader());
        lefEye = in.readParcelable(PointF.class.getClassLoader());
        mouthLeft = in.readParcelable(PointF.class.getClassLoader());
        rightEar = in.readParcelable(PointF.class.getClassLoader());
        rightEye = in.readParcelable(PointF.class.getClassLoader());
        rightCheek = in.readParcelable(PointF.class.getClassLoader());
        mouthRight = in.readParcelable(PointF.class.getClassLoader());
        mouthBottom = in.readParcelable(PointF.class.getClassLoader());
        noseBase = in.readParcelable(PointF.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(smilingProbability, flags);
        dest.writeParcelable(eyeProbability, flags);
        //dest.writeParcelable(rot, flags);
        dest.writeParcelable(leftEar, flags);
        dest.writeParcelable(leftCheek, flags);
        dest.writeParcelable(lefEye, flags);
        dest.writeParcelable(mouthLeft, flags);
        dest.writeParcelable(rightEar, flags);
        dest.writeParcelable(rightEye, flags);
        dest.writeParcelable(rightCheek, flags);
        dest.writeParcelable(mouthRight, flags);
        dest.writeParcelable(mouthBottom, flags);
        dest.writeParcelable(noseBase, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserMetadata> CREATOR = new Creator<UserMetadata>() {
        @Override
        public UserMetadata createFromParcel(Parcel in) {
            return new UserMetadata(in);
        }

        @Override
        public UserMetadata[] newArray(int size) {
            return new UserMetadata[size];
        }
    };

    public ArrayList<PointF> getAllFaceContours()
    {
        ArrayList<PointF> result = new ArrayList<PointF>();

        result.add(smilingProbability);
        result.add(eyeProbability);
        //result.add(rot);
        result.add(leftEar);
        result.add(leftCheek);
        result.add(lefEye);
        result.add(mouthLeft);
        result.add(rightEar);
        result.add(rightEye);
        result.add(rightCheek);
        result.add(mouthRight);
        result.add(mouthBottom);
        result.add(noseBase);

        return result;
    }

}
