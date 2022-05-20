package com.example.facerecognition;


import android.graphics.PointF;

import java.util.ArrayList;

public class UserMetadata {

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

    public UserMetadata(
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

}
