package com.example.facerecognition;

import android.graphics.PointF;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class UserMetadataOld implements Parcelable {

    public ArrayList<PointF> faceContour;
    public ArrayList<PointF> leftEyeBrowTop;
    public ArrayList<PointF> leftEyeBrowBottom;
    public ArrayList<PointF> rightEyeBrowTop;
    public ArrayList<PointF> rightEyeBrowBottom;
    public ArrayList<PointF> leftEyeContour;
    public ArrayList<PointF> rightEyeContour;
    public ArrayList<PointF> leftCheekCenter;
    public ArrayList<PointF> rightCheekCenter;
    public ArrayList<PointF> upperLipBottomContour;
    public ArrayList<PointF> upperLipTopContour;
    public ArrayList<PointF> lowerLipBottomContour;
    public ArrayList<PointF> lowerLipTopContour;
    public ArrayList<PointF> noseBridge;
    public ArrayList<PointF> noseBottom;

    public UserMetadataOld(
             ArrayList<PointF> faceContour,
             ArrayList<PointF> leftEyeBrowTop,
             ArrayList<PointF> leftEyeBrowBottom,
             ArrayList<PointF> rightEyeBrowTop,
             ArrayList<PointF> rightEyeBrowBottom,
             ArrayList<PointF> leftEyeContour,
             ArrayList<PointF> rightEyeContour,
             ArrayList<PointF> leftCheekCenter,
             ArrayList<PointF> rightCheekCenter,
             ArrayList<PointF> upperLipBottomContour,
             ArrayList<PointF> upperLipTopContour,
             ArrayList<PointF> lowerLipBottomContour,
             ArrayList<PointF> lowerLipTopContour,
             ArrayList<PointF> noseBridge,
             ArrayList<PointF> noseBottom
    )
    {
        this.faceContour = faceContour;
        this.leftEyeBrowTop = leftEyeBrowTop;
        this.leftEyeBrowBottom = leftEyeBrowBottom;
        this.rightEyeBrowTop = rightEyeBrowTop;
        this.rightEyeBrowBottom = rightEyeBrowBottom;
        this.leftEyeContour = leftEyeContour;
        this.rightEyeContour = rightEyeContour;
        this.leftCheekCenter = leftCheekCenter;
        this.rightCheekCenter = rightCheekCenter;
        this.upperLipBottomContour = upperLipBottomContour;
        this.upperLipTopContour = upperLipTopContour;
        this.lowerLipBottomContour = lowerLipBottomContour;
        this.lowerLipTopContour = lowerLipTopContour;
        this.noseBridge = noseBridge;
        this.noseBottom = noseBottom;
    }


    public UserMetadataOld()
    {
        this.faceContour = new ArrayList<PointF>();
        this.leftEyeBrowTop = new ArrayList<PointF>();
        this.leftEyeBrowBottom = new ArrayList<PointF>();
        this.rightEyeBrowTop = new ArrayList<PointF>();
        this.rightEyeBrowBottom = new ArrayList<PointF>();
        this.leftEyeContour = new ArrayList<PointF>();
        this.rightEyeContour = new ArrayList<PointF>();
        this.leftCheekCenter = new ArrayList<PointF>();
        this.rightCheekCenter = new ArrayList<PointF>();
        this.upperLipBottomContour = new ArrayList<PointF>();
        this.upperLipTopContour = new ArrayList<PointF>();
        this.lowerLipBottomContour = new ArrayList<PointF>();
        this.lowerLipTopContour = new ArrayList<PointF>();
        this.noseBridge = new ArrayList<PointF>();
        this.noseBottom = new ArrayList<PointF>();
    }


    public ArrayList<PointF> getAllFaceContours()
    {
        ArrayList<PointF> result = new ArrayList<PointF>();

        result.addAll(faceContour);
        result.addAll(leftEyeBrowTop);
        result.addAll(leftEyeBrowBottom);
        result.addAll(rightEyeBrowTop);
        result.addAll(rightEyeBrowBottom);
        result.addAll(leftEyeContour);
        result.addAll(rightEyeContour);
        result.addAll(leftCheekCenter);
        result.addAll(rightCheekCenter);
        result.addAll(upperLipBottomContour);
        result.addAll(upperLipTopContour);
        result.addAll(lowerLipBottomContour);
        result.addAll(lowerLipTopContour);
        result.addAll(noseBridge);
        result.addAll(noseBottom);


        return result;
    }

    protected UserMetadataOld(Parcel in) {
        faceContour = in.createTypedArrayList(PointF.CREATOR);
        leftEyeBrowTop = in.createTypedArrayList(PointF.CREATOR);
        leftEyeBrowBottom = in.createTypedArrayList(PointF.CREATOR);
        rightEyeBrowTop = in.createTypedArrayList(PointF.CREATOR);
        rightEyeBrowBottom = in.createTypedArrayList(PointF.CREATOR);
        leftEyeContour = in.createTypedArrayList(PointF.CREATOR);
        rightEyeContour = in.createTypedArrayList(PointF.CREATOR);
        leftCheekCenter = in.createTypedArrayList(PointF.CREATOR);
        rightCheekCenter = in.createTypedArrayList(PointF.CREATOR);
        upperLipBottomContour = in.createTypedArrayList(PointF.CREATOR);
        upperLipTopContour = in.createTypedArrayList(PointF.CREATOR);
        lowerLipBottomContour = in.createTypedArrayList(PointF.CREATOR);
        lowerLipTopContour = in.createTypedArrayList(PointF.CREATOR);
        noseBridge = in.createTypedArrayList(PointF.CREATOR);
        noseBottom = in.createTypedArrayList(PointF.CREATOR);
    }

    public static final Creator<UserMetadataOld> CREATOR = new Creator<UserMetadataOld>() {
        @Override
        public UserMetadataOld createFromParcel(Parcel in) {
            return new UserMetadataOld(in);
        }

        @Override
        public UserMetadataOld[] newArray(int size) {
            return new UserMetadataOld[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(faceContour);
        dest.writeTypedList(leftEyeBrowTop);
        dest.writeTypedList(leftEyeBrowBottom);
        dest.writeTypedList(rightEyeBrowTop);
        dest.writeTypedList(rightEyeBrowBottom);
        dest.writeTypedList(leftEyeContour);
        dest.writeTypedList(rightEyeContour);
        dest.writeTypedList(leftCheekCenter);
        dest.writeTypedList(rightCheekCenter);
        dest.writeTypedList(upperLipBottomContour);
        dest.writeTypedList(upperLipTopContour);
        dest.writeTypedList(lowerLipBottomContour);
        dest.writeTypedList(lowerLipTopContour);
        dest.writeTypedList(noseBridge);
        dest.writeTypedList(noseBottom);
    }
}
