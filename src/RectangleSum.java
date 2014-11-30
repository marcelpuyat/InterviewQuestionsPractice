import java.util.Random;


public class RectangleSum {
	private int[][] matrix;
	public int[][] squareSums;
	
	public static void main(String[] args) {
		Random random = new Random();
		int[][]matrix = new int[5][5];
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				matrix[i][j] = random.nextInt(10);
			}
		}
		
		RectangleSum rs = new RectangleSum(matrix);
		rs.printMatrix();
		int i1 = random.nextInt(3);
		int j1 = random.nextInt(3);
		int i2 = i1 + random.nextInt(3);
		int j2 = j1 + random.nextInt(3);
		System.out.println("Computing sum of rectangle from (" + i1 + "," + j1 + ") to (" + i2 + "," + j2 + "):");
		System.out.println(rs.getSum(i1, j1, i2, j2));
	}
	
	public RectangleSum(int[][] matrix) {
		this.matrix = matrix;
		squareSums = new int[matrix.length][matrix[0].length];
		preprocess();
	}
	
	public int getSum(int i1, int j1, int i2, int j2) {
		if (i1 == 0 && j1 == 0) return squareSums[i2][j2];
		if (i1 == 0) {
			return squareSums[i2][j2] - squareSums[i2][j1 - 1];
		}
		if (j1 == 0) {
			return squareSums[i2][j2] - squareSums[i1 - 1][j2];
		}
		return squareSums[i2][j2] - squareSums[i1 - 1][j2] - squareSums[i2][j1 - 1] + squareSums[i1 - 1][j1 - 1]; 
	}
	
	private void preprocess() {
		squareSums[0][0] = matrix[0][0];
		for (int i = 0; i < squareSums.length; i++) {
			for (int j = 0; j < squareSums[0].length; j++) {
				if (i == 0 && j == 0) continue;
				if (i == 0) {
					squareSums[0][j] = squareSums[0][j - 1] + matrix[0][j];
				}
				else if (j == 0) {
					squareSums[i][0] = squareSums[i - 1][0] + matrix[i][0];
				}
				else {
					squareSums[i][j] = squareSums[i - 1][j] + squareSums[i][j - 1] + matrix[i][j] - squareSums[i - 1][j - 1];
				}
			}
		}
	}
	
	public void printMatrix() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.print("\n");
		}
	}
}
