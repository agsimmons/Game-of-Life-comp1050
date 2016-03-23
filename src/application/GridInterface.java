package application;

public interface GridInterface {
	
	public void changeCellState(int x, int y);
	public void simulateCycle();
	public void drawState();
	public boolean getCellState(int x, int y);
	public void setRuleOneState(boolean newState);
	public void setRuleTwoState(boolean newState);
	public void setRuleThreeState(boolean newState);
	public void setRuleFourState(boolean newState);
	
}
