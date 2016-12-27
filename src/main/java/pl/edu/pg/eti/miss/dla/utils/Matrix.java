package pl.edu.pg.eti.miss.dla.utils;

public class Matrix {

    public static int[][] deepCopy(int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            newMatrix[i] = matrix[i].clone();
        }
        return newMatrix;
    }
}
