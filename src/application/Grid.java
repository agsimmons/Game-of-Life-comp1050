package application;

public class Grid {

	private int width;
	private int height;
	private boolean[][] grid;

	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
	}

	public void changeCellState(int x, int y) {
		if (grid[x][y] == true) {
			grid[x][y] = false;
		} else {
			grid[x][y] = true;
		}
	}

	public void simulateCycle() {
		// TODO
//		boolean tempGrid[][] = grid;
//		
//		for (int y = 0; y < height; y++) {
//			for (int x = 0; x < width; x++) {
//				
//				if (grid[x][y] == true) { // If cell is alive
//					// Any live cell with fewer than two live neighbours dies
//					
//					// Any live cell with two or three live neighbours lives on to the next generation.
//					
//					// Any live cell with more than three live neighbours dies
//				} else { // If cell is dead
//					// Any dead cell with exactly three live neighbours becomes a live cell
//					
//				}
//			}
//		}
	}

	public void drawState() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (grid[x][y] == true) {
					System.out.print("■");
				} else {
					System.out.print("□");
				}
			}
			System.out.println();
		}
	}

	public int getNeighbors(int x, int y) {
		int count = 0;

		// Check Left
		if (x > 0) { if (grid[x - 1][y] == true) {count++;} }
		// Check Top-Left
		if (x > 0 && y > 0) { if (grid[x - 1][y - 1] == true) {count++;} }
		// Check Top
		if (y > 0) { if (grid[x][y - 1] == true) {count++;} }
		// Check Top-Right
		if (x < width - 1 && y > 0) { if (grid[x + 1][y - 1] == true) {count++;} }
		// Check Right
		if (x < width - 1) { if (grid[x + 1][y] == true) {count++;} }
		// Check Bottom-Right
		if (x < width - 1 && y < height - 1) { if (grid[x + 1][y + 1] == true) {count++;} }
		// Check Bottom
		if (y < height - 1) { if (grid[x][y + 1] == true) {count++;} }
		// Check Bottom-Left
		if (x > 0 && y < height - 1) { if (grid[x - 1][y + 1] == true) {count++;} }

		return count;
	}

	public boolean getCellState(int x, int y) {
		return grid[x][y];
	}

}
