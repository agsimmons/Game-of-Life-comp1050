package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar.Schema4Annotations;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

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

	public static boolean isLoop = false;
	
	public static String backround;
	public static String Primary;
	
	
	public static int prefX;
	public static int prefY;

	@FXML
	GridPane DrawGrid;
	
	@FXML
	CheckBox loop;

	@FXML
	Button startButton;
	
	@FXML
	Button nextButton;
	
	
	public static int c = 0;
	
	public boolean isFirst(){
		if(c==0){
		c++;
			return true;
			//nextButton.setText("Next");
			//startButton.setText("start loop");
		}
		return false;
	}
	
	public void NextButtton(ActionEvent e){
		if(isFirst()){
			try {
				initAll();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		System.out.println("next  "+c);
		//fixes the button lables
		if(isLoop){
			startButton.setText("Stop Loop");
		}else{
			startButton.setText("Start Loop");
		}
		
		nextButton.setText("Next");
		
		
		testButton();
		makeClickable();
		
	}

	
	public void StartButton(ActionEvent e){
		if(isFirst()){
			try {
				initAll();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		System.out.println("start");
		
		//button loop lables
		if(isLoop){
			startButton.setText("Stop Loop");
		}else{
			startButton.setText("Start Loop");
		}
		
		makeClickable();
	}

	public void makeClickable(){
		Node toMod = null;//the javafx node to be modified
		
		
		//note to future self, getchildren returns an Observablelist, just so u know and dont have to trial and error alot again
		ObservableList<Node> nodes = DrawGrid.getChildren();//adds all of the Drawgrids children to a list 
		
		for(final Node target : nodes){//for all nodes in the list nodes
			target.setOnMouseClicked(new EventHandler<MouseEvent>()
		    {

		        @Override
		        public void handle(MouseEvent m)
		        {
		            //sets its color to the primary color
		            ((Shape) target).setFill(Color.web(Primary));
		            
		            //System.out.println(target.getProperties());
		            //returns the x and y of the node when clicked
		            
		            //reads the nodes properties, looks for the property of row or column , makes it a string then an int
		            int ClickedColumn = Integer.parseInt(target.getProperties().get("gridpane-column").toString());
		            System.out.println("Column: "+ClickedColumn);
		            
		            int ClickedRow = Integer.parseInt(target.getProperties().get("gridpane-row").toString());
		            System.out.println("Row: "+ClickedRow);
		            
		            

		        }
		    });
		}
		
		
		
	}
	
	
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
	
	
	public void initAll() throws FileNotFoundException{

		getConf();//reads the config file
		
		calcPrefSize();//finds best size fro grid

		initPopulate();//makes the grid

		fill();//fill adn sets colors
		
		makeClickable();//makes all the cells clickable
		//this must be refeshed after any cell is modified
		
	}
	
	public void getConf() throws FileNotFoundException {
		File configFile = new File("config.txt");

		
		Scanner scanner = new Scanner(configFile);// opens
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

	public void testButton() {

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
