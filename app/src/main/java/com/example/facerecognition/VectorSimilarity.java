package com.example.facerecognition;


public class VectorSimilarity {


    //3 O(n)
    public static double Cosine(double[] v1, double[] v2) {
        double result = 0;

        result = InnerProduct(v1, v2) / (VectorSize(v1) * VectorSize(v2));

        return result;
    }

    //O(n)
    public static double VectorSize(double[] vector) {
        double vector_size = 0;

        for (int i = 0; i < vector.length; i++) {
            vector_size += Math.pow(vector[i], 2);
        }
        vector_size = Math.sqrt(vector_size);

        return vector_size;
    }

    //O(n)
    public static double InnerProduct(double[] v1, double[] v2) {
        double Inner = 0;
        for (int i = 0; i < v1.length; i++) {
            Inner += v1[i] * v2[i];
        }
        return Inner;
    }

    //O(n)
    public static double Euclidean(double[] v1, double[] v2) {
        double ED = 0;
        for (int i = 0; i < v1.length; i++) {
            double sec = v1[i] - v2[i];
            ED += Math.pow(sec, 2);
        }

        ED = Math.sqrt(ED);

        return ED;
    }

    //O(n)
    public static double Theta(double[] v1, double[] v2) {
        double V = Cosine(v1, v2);
        double theta = Math.acos(V) + Math.toRadians(10);

        return theta;
    }

    //3 O(n)
    public static double Triangle(double[] v1, double[] v2) {
        double theta = Theta(v1, v2);
        theta = Math.toRadians(theta);
        double TS = 0;
        TS = (VectorSize(v1) * VectorSize(v2) * Math.sin(theta)) / 2;


        return TS;

    }

    //2 O(n)
    public static double Magnitude_Difference(double[] v1, double[] v2) {
        double MD = 0;
        MD = Math.abs(VectorSize(v1) - VectorSize(v2));

        return MD;
    }

    //4 O(n)?
    public static double Sector(double[] v1, double[] v2) {
        double SS = 0;
        SS = Math.PI * (Math.pow((Euclidean(v1, v2) + Magnitude_Difference(v1, v2)), 2)) * (Theta(v1, v2) / 360);

        return SS;
    }

    //7 O(n)
    public static double TS_SS(double[] v1, double[] v2) {
        double TS_SS = 0;
        TS_SS = Triangle(v1, v2) * Sector(v1, v2);

        return TS_SS;
    }
}
