package application;

import java.util.Scanner;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar.Schema4Annotations;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Main extends Application {// all the usual javafx stuffs
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("gridTest.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	Label Ynumber;
	@FXML
	Label Xnumber;
	public static int X;
	public static int Y;

	public static String backround;
	public static String Primary;
	
	
	public static int prefX;
	public static int prefY;

	@FXML
	GridPane DrawGrid;

	public static int c = 1;

	public void addRow() {
		// DrawGrid.add(null, 0, r);
		RowConstraints Rcon = new RowConstraints();
		Rcon.setPrefHeight(prefX);// pref height of grid, to be determined how
									// calc
		DrawGrid.getRowConstraints().add(Rcon);

	}

	public void addCol() {
		// DrawGrid.add(null, c, 0);

		ColumnConstraints Ccon = new ColumnConstraints();
		Ccon.setPrefWidth(prefY);
		DrawGrid.getColumnConstraints().add(Ccon);

	}

	public void calcPrefSize() {

		prefX = (int) Math.ceil(((double) 600) / ((double) X));
		prefY = (int) Math.ceil(((double) 600) / ((double) Y));

		// makes biggest possible square boxes as possible that can fit within
		// bounds

		// so each square area is prefY * prefX, well rectangle area in some
		// cases

		// it will try to only use squares so if the dimensions are like 10 wide
		// by 37 tall, it will be a
		// tower of squares instead of being rectangel boxes
	}

	public void initPopulate() {// to be called to populate the grid with the
								// initial values for the cells

		DrawGrid.gridLinesVisibleProperty().set(true);// so you can see the grid
		//^^ looks weird with backround
		
		DrawGrid.setMaxSize(600, 600);

		for (int i = 1; i != Y; i++) {
			addRow();

		}

		for (int i = 1; i != X; i++) {
			addCol();

		}

	}

	public void getConf(ActionEvent e) {
		Scanner scanner = new Scanner(getClass().getResourceAsStream("conf.txt"));// opens
																					// config
																					// file
		for (int i = 1; i < 5; i++) {// read first 4 lines of file
			if (i == 1) {
				Y = scanner.nextInt() + 1;
				Ynumber.setText("Height: " + (Y - 1));
			} else if (i == 2) {
				X = scanner.nextInt() + 1;
				Xnumber.setText("Width: " + (X - 1));
			} else	if (i == 3) {
				backround = scanner.next();
				//System.out.println(backround);
			} else if (i == 4) {
				Primary = scanner.next();
				//System.out.println(Primary);
			} else {
				System.out.println("bad config error");
			}
			
		}
		
		System.out.println("Config loaded");
		scanner.close();

		// bounds are min 10x10 and max 100x100
		// is 0 indexed so it changes to 1 index check
		if (X < 11) {
			X = 11;
		}
		if (Y < 11) {
			Y = 11;
		}
		if (X > 101) {
			X = 101;
		}
		if (Y > 101) {
			Y = 101;
		}

		
		//TODO make this a main init class with 
		
		//getConfig(); 
		
		calcPrefSize();//finds best size fro grid

		initPopulate();//makes the grid

		fill();//fill adn sets colors

		// System.out.println("gridx "+DrawGrid.getHeight()+ "
		// gridy"+DrawGrid.get);

	}

	public void fill() {// can be used to set backrough color

		int size;
		if (prefX > prefY) {
			size = prefY;
		} else if (prefX < prefY) {
			size = prefX;
		} else {
			size = prefX;
		} // finds the smallest preferd size which ends up being the side length
			// of one of the squares on the grid
			// and makes a rectange that size and puts it on the grid

		for (int l = 1; l < Y; l++) {
			for (int p = 1; p < X; p++) {
				Rectangle o = new Rectangle();

				o.setHeight(size);
				o.setWidth(size);

				// o.setVisible(false);
				//o.setFill(Color.GAINSBORO);
				
				o.setFill(Color.web(backround));

				DrawGrid.add(o, p, l);
			}
		}

	}

	public void testButton(ActionEvent e) {

		popNode(c, c);
		if(c>1){
			unPopNode(c-1, c-1);
		}
		c++;

	}
	
	public boolean checkOutOfBounds(int x, int y){//checks if you are trying to edit a cell that isnt on the grid
		if(x>X-1){
			System.out.println("If you are seeing this, blame Andrew");
			return true;
		}
		if(y>Y-1){
			System.out.println("If you are seeing this, blame Andrew");
			return true;
		}
		return false;
	}
	
	public void popNode(int x, int y){

		if(checkOutOfBounds(x, y)){
			return;
		}
		
		int size;
		if (prefX > prefY) {
			size = prefY;
		} else if (prefX < prefY) {
			size = prefX;
		} else {
			size = prefX;
		} // finds the smallest preferd size which ends up being the side length
			// of one of the squares on the grid
			// and makes a rectange that size and puts it on the grid
		Rectangle r = new Rectangle();

		r.setHeight(size);
		r.setWidth(size);

		r.setFill(Color.web(Primary));
		
		DrawGrid.add(r, x, y);
	}
	
	public void unPopNode(int x, int y){

		if(checkOutOfBounds(x, y)){
			return;
		}
		
		int size;
		if (prefX > prefY) {
			size = prefY;
		} else if (prefX < prefY) {
			size = prefX;
		} else {
			size = prefX;
		} // finds the smallest preferd size which ends up being the side length
			// of one of the squares on the grid
			// and makes a rectange that size and puts it on the grid
		Rectangle r = new Rectangle();

		r.setHeight(size);
		r.setWidth(size);

		r.setFill(Color.web(backround));
		
		DrawGrid.add(r, x, y);
	}
	
	

	public static void main(String[] args) {
		launch(args);
	}
}
