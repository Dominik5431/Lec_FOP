package h1;

public class Util {
	public static int binomialCoefficientCalls = 0;
	
	/**
	 * 
	 * @param a Matrix Multiplikant
	 * @param b Matrix Multiplikator
	 * @return a * b
	 */
	public static int[][] matrixMultiplication(int[][] a, int[][] b) {
		int aRows = a.length;
		int aCols = a[0].length;
		int bRows = b.length;
		int bCols = b[0].length;
		if (aCols != bRows)
			return null; // Dimensions check
		int[][] result = new int[aRows][bCols];
		int sum;

		for (int i = 0; i < result.length; i++) { // rows
			for (int j = 0; j < result[0].length; j++) { // cols
				sum = 0;
				for (int k = 0; k < aCols; k++) { // result[i][j] berechnen
					sum += a[i][k] * b[k][j];
				}
				result[i][j] = sum;
			}
		}
		return result;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return ( x choose y)
	 */
	public static int binomialCoefficient(int x, int y) {
		binomialCoefficientCalls++;
        int result = 1;      
	    if (y > x - y) y = x - y; 
	    for (int i = 0; i < y; i++) { 
	        result *= (x - i); 
	        result /= (i + 1); 
	    } 
	    return result;
	}
}
