package application;

public class Grid implements GridInterface {

	private int gridWidth;
	private int gridHeight;
	private boolean[][] gridState;
	
	private boolean ruleOneEnabled;
	private boolean ruleTwoEnabled;
	private boolean ruleThreeEnabled;
	private boolean ruleFourEnabled;

	public Grid(int width, int height) {
		gridWidth = width;
		gridHeight = height;
		gridState = new boolean[width][height];
		
		ruleOneEnabled = ruleTwoEnabled = ruleThreeEnabled = ruleFourEnabled = true;
	}

	public void changeCellState(int x, int y) {
		if (gridState[x][y] == true) {
			gridState[x][y] = false;
		} else {
			gridState[x][y] = true;
		}
	}

	public void simulateCycle() {
		boolean[][] tempGridState = new boolean[gridWidth][gridHeight];
		
		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				if (gridState[x][y] == true) { // If cell is alive
					// Any live cell with fewer than two live neighbors dies
					if (ruleOneEnabled) {
						if (getNeighborCount(x, y) < 2) {
							tempGridState[x][y] = false;
						}
					}

					// Any live cell with two or three live neighbors lives on to the next generation.
					if (ruleTwoEnabled) {
						if (getNeighborCount(x, y) == 2 || getNeighborCount(x, y) == 3) {
							tempGridState[x][y] = true;
						}
					}

					// Any live cell with more than three live neighbors dies
					if (ruleThreeEnabled) {
						if (getNeighborCount(x, y) > 3) {
							tempGridState[x][y] = false;
						}
					}
				} else { // If cell is dead
					// Any dead cell with exactly three live neighbors becomes a live cell
					if (ruleFourEnabled) {
						if (getNeighborCount(x, y) == 3) {
							tempGridState[x][y] = true;
						}
					}
				}
			}
		}
		gridState = tempGridState;
	}

	public void drawState() {
		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				if (gridState[x][y] == true) {
					System.out.print("■");
				} else {
					System.out.print("□");
				}
			}
			System.out.println();
		}
	}

	private int getNeighborCount(int x, int y) {
		int count = 0;

		// Check Left
		if (x > 0) { if (gridState[x - 1][y] == true) {count++;} }
		// Check Top-Left
		if (x > 0 && y > 0) { if (gridState[x - 1][y - 1] == true) {count++;} }
		// Check Top
		if (y > 0) { if (gridState[x][y - 1] == true) {count++;} }
		// Check Top-Right
		if (x < gridWidth - 1 && y > 0) { if (gridState[x + 1][y - 1] == true) {count++;} }
		// Check Right
		if (x < gridWidth - 1) { if (gridState[x + 1][y] == true) {count++;} }
		// Check Bottom-Right
		if (x < gridWidth - 1 && y < gridHeight - 1) { if (gridState[x + 1][y + 1] == true) {count++;} }
		// Check Bottom
		if (y < gridHeight - 1) { if (gridState[x][y + 1] == true) {count++;} }
		// Check Bottom-Left
		if (x > 0 && y < gridHeight - 1) { if (gridState[x - 1][y + 1] == true) {count++;} }

		return count;
	}

	public boolean getCellState(int x, int y) {
		return gridState[x][y];
	}

	public void setRuleOneState(boolean newState) {
		ruleOneEnabled = newState;
	}

	public void setRuleTwoState(boolean newState) {
		ruleTwoEnabled = newState;
	}

	public void setRuleThreeState(boolean newState) {
		ruleThreeEnabled = newState;
	}

	public void setRuleFourState(boolean newState) {
		ruleFourEnabled = newState;
	}
}
