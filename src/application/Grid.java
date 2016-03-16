package application;

public class Grid {

	private int width;
	private int height;
	private boolean[][] grid;
	
	private boolean enableRuleOne;
	private boolean enableRuleTwo;
	private boolean enableRuleThree;
	private boolean enableRuleFour;

	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		
		enableRuleOne = enableRuleTwo = enableRuleThree = enableRuleFour = true;
	}

	public void changeCellState(int x, int y) {
		if (grid[x][y] == true) {
			grid[x][y] = false;
		} else {
			grid[x][y] = true;
		}
	}

	public void simulateCycle() {
		boolean[][] tempGrid = new boolean[width][height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (grid[x][y] == true) { // If cell is alive
					// Any live cell with fewer than two live neighbors dies
					if (enableRuleOne) {
						if (getNeighbors(x, y) < 2) {
							tempGrid[x][y] = false;
						}
					}

					// Any live cell with two or three live neighbors lives on to the next generation.
					if (enableRuleTwo) {
						if (getNeighbors(x, y) == 2 || getNeighbors(x, y) == 3) {
							tempGrid[x][y] = true;
						}
					}

					// Any live cell with more than three live neighbors dies
					if (enableRuleThree) {
						if (getNeighbors(x, y) > 3) {
							tempGrid[x][y] = false;
						}
					}
				} else { // If cell is dead
					// Any dead cell with exactly three live neighbors becomes a live cell
					if (enableRuleFour) {
						if (getNeighbors(x, y) == 3) {
							tempGrid[x][y] = true;
						}
					}
				}
			}
		}
		
		grid = tempGrid;
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
		System.out.println();
	}

	private int getNeighbors(int x, int y) {
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
