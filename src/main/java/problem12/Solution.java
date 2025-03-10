package problem12;

class Solution {
    public static int[][] generateMatrix(int n) {
        int[][] matrix = new int[n][n];
        int num = 1;
        int startRow = 0, endRow = n - 1;
        int startCol = 0, endCol = n - 1;

        while (startRow <= endRow && startCol <= endCol) {
            // Fill top row from left to right
            for (int i = startCol; i <= endCol; i++) {
                matrix[startRow][i] = num++;
            }
            startRow++;

            // Fill right column from top to bottom
            for (int i = startRow; i <= endRow; i++) {
                matrix[i][endCol] = num++;
            }
            endCol--;

            // Fill bottom row from right to left if there are rows left
            if (startRow <= endRow) {
                for (int i = endCol; i >= startCol; i--) {
                    matrix[endRow][i] = num++;
                }
                endRow--;
            }

            // Fill left column from bottom to top if there are columns left
            if (startCol <= endCol) {
                for (int i = endRow; i >= startRow; i--) {
                    matrix[i][startCol] = num++;
                }
                startCol++;
            }
        }

        return matrix;
    }

    public static void main(String[] args) {
        int[][] arr = generateMatrix(5);
        for(int i = 0;i < arr.length;i++){
            for(int j  = 0 ; j < arr[0].length;j++){
                System.out.print(arr[i][j] + "     ");
            }
            System.out.println();
        }
    }
}