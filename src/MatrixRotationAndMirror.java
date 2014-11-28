
/**
 * Rotates and mirrors matrix, both done in place taking O(n^2) time.
 * @author marcelp
 *
 */
public class MatrixRotationAndMirror {
	private int[][] matrix;
	public enum Rotation {CLOCKWISE, COUNTERCLOCKWISE};
	
	/**
	 * Rotate matrix in place, layer by layer. O(n^2) time and O(1) space for local variables.
	 * @param rotation
	 */
	public void rotateClockwise(Rotation rotation) {
		int layers = matrix.length / 2 + 1;
		
		for (int currLayer = 0; currLayer < layers; currLayer++) {
			int endOfLayer = matrix.length - currLayer - 1;
			
			for (int pointInLayer = currLayer; pointInLayer < endOfLayer; pointInLayer++) {
				int topEdgeCell = matrix[currLayer][pointInLayer];
				int rightEdgeCell = matrix[pointInLayer][endOfLayer - currLayer];
				int bottomEdgeCell = matrix[endOfLayer - currLayer][endOfLayer - pointInLayer];
				int leftEdgeCell = matrix[endOfLayer - pointInLayer][currLayer];
				
				matrix[currLayer][pointInLayer] = rotation == Rotation.CLOCKWISE ? leftEdgeCell : 
																				   rightEdgeCell;
				matrix[pointInLayer][endOfLayer] = rotation == Rotation.CLOCKWISE ? topEdgeCell : 
																					bottomEdgeCell;
				matrix[endOfLayer][matrix.length - pointInLayer - 1] = rotation == Rotation.CLOCKWISE ? rightEdgeCell : 
																										leftEdgeCell;
				matrix[matrix.length - pointInLayer - 1][currLayer] = rotation == Rotation.CLOCKWISE ? bottomEdgeCell : 
																									   topEdgeCell;
			}
		}
	}
	
	/**
	 * Mirrors matrix in place across x-axis. O(n^2) time and O(1) space used for 3 variables
	 */
	public void mirrorMatrix() {
		int numColumnsToMirror = matrix.length / 2;
		for (int row = 0; row < matrix.length; row++) {
			for (int col = 0; col < numColumnsToMirror; col++) {
				// XOR used for swapping so no space used
				matrix[row][col] = matrix[row][col] ^ matrix[row][matrix.length - col - 1];
				matrix[row][matrix.length - col - 1] = matrix[row][col] ^ matrix[row][matrix.length - col - 1];
				matrix[row][col] = matrix[row][col] ^ matrix[row][matrix.length - col - 1];
			}
		}
	}
	
	public int[][] getMatrix() {
		return this.matrix;
	}
	
	public void printMatrix() {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.print("\n");
		}
		System.out.print("\n");
	}
	
	public MatrixRotationAndMirror(int[][] matrix) {
		assert(matrix != null && matrix.length > 0 && matrix.length == matrix[0].length);
		this.matrix = matrix;
	}
	
	/**
	 * Example
	 * @param args
	 */
	public static void main(String[] args) { 
		MatrixRotationAndMirror matrix = new MatrixRotationAndMirror(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
		matrix.printMatrix();
		matrix.mirrorMatrix();
		matrix.printMatrix();
	}
}
