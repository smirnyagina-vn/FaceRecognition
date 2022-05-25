package com.example.facerecognition;

import java.util.ArrayList;
import java.util.stream.Stream;

public class UserMetadataExtension {

    public static double[] getAllFaceContoursXCoordinates(UserMetadata userMetadata)
    {
        ArrayList<Double> result = new ArrayList<Double>();

        result.add((double) userMetadata.smilingProbability.x);
        result.add((double) userMetadata.eyeProbability.x);
        //result.add((double)userMetadata.rot.x);
        result.add((double)userMetadata.leftEar.x);
        result.add((double)userMetadata.leftCheek.x);
        result.add((double)userMetadata.lefEye.x);
        result.add((double)userMetadata.mouthLeft.x);
        result.add((double)userMetadata.rightEar.x);
        result.add((double)userMetadata.rightEye.x);
        result.add((double)userMetadata.rightCheek.x);
        result.add((double)userMetadata.mouthRight.x);
        result.add((double)userMetadata.mouthBottom.x);
        result.add((double)userMetadata.noseBase.x);


        Double[] array = result.toArray(new Double[0]);

        double[] doubleArray = Stream.of(array).mapToDouble(Double::doubleValue).toArray();

        return doubleArray;
    }

    public static double[] getAllFaceContoursYCoordinates(UserMetadata userMetadata)
    {
        ArrayList<Double> result = new ArrayList<Double>();

        result.add((double) userMetadata.smilingProbability.y);
        result.add((double) userMetadata.eyeProbability.y);
        //result.add((double)userMetadata.rot.y);
        result.add((double)userMetadata.leftEar.y);
        result.add((double)userMetadata.leftCheek.y);
        result.add((double)userMetadata.lefEye.y);
        result.add((double)userMetadata.mouthLeft.y);
        result.add((double)userMetadata.rightEar.y);
        result.add((double)userMetadata.rightEye.y);
        result.add((double)userMetadata.rightCheek.y);
        result.add((double)userMetadata.mouthRight.y);
        result.add((double)userMetadata.mouthBottom.y);
        result.add((double)userMetadata.noseBase.y);

        Double[] array = result.toArray(new Double[0]);

        double[] doubleArray = Stream.of(array).mapToDouble(Double::doubleValue).toArray();

        return doubleArray;
    }


}
