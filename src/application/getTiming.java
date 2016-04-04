package application;

import javax.print.DocFlavor.STRING;

public abstract class getTiming{
	private String timeStr;
	private int timeInt;
	
	public int defaultTiming(){
		return 1000;
	}
	
	public int toTime(String s){
		timeInt=Integer.parseInt(s);
		return timeInt;
	}
}