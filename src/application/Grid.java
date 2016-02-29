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
	
	public void setTrue(int x, int y) {
		grid[x][y] = true;
	}
	
	public void update() {
		// TODO
	}
	
	public void draw() {
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
	
	
}
