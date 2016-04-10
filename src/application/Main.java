/*
*    Group Members:
*        Andrew Simmons
*        Nick Smith
*/

package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application { // All the usual JavaFX stuffs such as loading the fxml file
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("gui.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Game of Life");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//init ALL THE VARIABLES
	public static int X;
	public static int Y;
	public static int dimY;
	public static int colorX;
	public static int colorY;
	public static boolean isLoop = false;
	public static String backround;
	public static String Primary;
	public static int prefX;
	public static int prefY;
	public static Grid baseGrid;
	public static int debugCounter = 0;
	
	@FXML
	Label Ynumber;
	@FXML
	Label Xnumber;
	@FXML
	GridPane DrawGrid;
	@FXML
	CheckBox loop;
	@FXML
	Button startButton;
	@FXML
	Button nextButton;
	@FXML
	Button colorButton;
	@FXML
	Button heightButton;
	@FXML
	Slider heightSlider;
	@FXML
	ColorPicker backColor;
	@FXML
	ColorPicker primaryColor;
	@FXML
	TextField heightText;
	@FXML
	TextField widthText;
	@FXML
	TextField randomCount;
	@FXML
	Button addRandomsButton;
	@FXML
	CheckBox rule1;
	@FXML
	CheckBox rule2;
	@FXML
	CheckBox rule3;
	@FXML
	CheckBox rule4;

	
	public void initColors() { // Gets the colors from the config and sets the labels
		backColor.setValue(Color.web(backround));
		primaryColor.setValue(Color.web(Primary));
	}

	public void initHeight() {//init the height and width
		heightText.setText(Integer.toString(dimY - 1));
		heightSlider.setValue(dimY-1);
	}


	public void initLabels() {
		initColors();
		initHeight();
	}
	
	public void FillExtras() {//prevents the randoms button from bugging up everything
		for (int i = 0; i < X; i++) {
			baseGrid.changeCellState(i, 0);
		}

		for (int i = 0; i < Y; i++) {
			baseGrid.changeCellState(0, i);
		}

		baseGrid.changeCellState(0, 0);
	}
	
	
	static getTiming timing = new getTiming() {//abstract class that gets the default time of 1s
	};
	
	static int interval = timing.defaultTiming();//timer interval in ms

	//inits/declares all timing parts
    public Timeline timeline;
    public Label timerLabel = new Label();
    public DoubleProperty timeSeconds = new SimpleDoubleProperty();
    public Duration time = Duration.ZERO, splitTime = Duration.ZERO;

	public void startTimer(){//starts the timer/is the timer on state
        timeline = new Timeline(
            new KeyFrame(Duration.millis(interval),
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    Duration duration = ((KeyFrame)t.getSource()).getTime();
                    time = time.add(duration);
                    timeSeconds.set(time.toSeconds());
                    baseGrid.simulateCycle();//once a second simulate a cycle
            		reColor();//and recolor all the nodes and the usual functionality
            		makeClickable();
            		clearExtras();
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    
    public void stopClearTimer(){//stops the timer and sets the timeline to zero
        timeline.stop();
        time = Duration.ZERO;
        //reset timeline
        timeline = new Timeline(
                new KeyFrame(Duration.millis(interval),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        Duration duration = ((KeyFrame)t.getSource()).getTime();
                        time = time.add(duration);
                        timeSeconds.set(time.toSeconds());
                    }
                })
            );
        timeline.setCycleCount(Timeline.INDEFINITE);    
    }
    
	
	
	public void clearExtras() {//clears the invivible extras
		for (int i = 0; i < X; i++) {
			if(baseGrid.getCellState(i, 0)){
				baseGrid.changeCellState(i, 0);
			}
		}
		for (int i = 0; i < Y; i++) {
			if(baseGrid.getCellState(0, i)){
				
			
			baseGrid.changeCellState(0, i);
			}
		}
		if(baseGrid.getCellState(0, 0)){
		baseGrid.changeCellState(0, 0);
		}
	}

	public void setRules(ActionEvent e) {
		
		baseGrid.setRuleOneState(rule1.isSelected());
		baseGrid.setRuleTwoState(rule2.isSelected());
		baseGrid.setRuleThreeState(rule3.isSelected());
		baseGrid.setRuleFourState(rule4.isSelected());
		
		//sets the rules to the values of the checkbox's
		//defaults to all true
	}

	
	
	public void applyColor(ActionEvent e) {
		String baseBack = "#";
		String basePrimary = "#";
		// Reads the color settings and writes their settings to the color vars
		String back = backColor.getValue().toString();

		String primary = primaryColor.getValue().toString();

		back = back.substring(2, 8); // Trims the alpha values and the other parts
		primary = primary.substring(2, 8);
		backround = baseBack.concat(back); // And sets them to web hex color strings
		Primary = basePrimary.concat(primary);

		reColor();//re colors the nodes

		makeClickable(); // Makes all the cells clickable
		// This must be refreshed after any cell is modified
		try {
			writeConf();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void writeConf() throws IOException {//writes the current variables to the config
		System.out.println("Config Saved");

		PrintWriter pw = new PrintWriter(new FileWriter("config.txt"));

		pw.write(Integer.toString((dimY - 1))+"\n");
		pw.write(Integer.toString((dimY - 1))+"\n");
		pw.write(backround+"\n");
		pw.write(Primary+"\n");
		pw.close();
	}
		
	
	public void applyDimensions(){//sets the grid to the dimensions from the config file and update the height slider
		dimY = (Integer.parseInt(heightText.getText()) + 1);
		dimY = ((int) (heightSlider.getValue()) + 1);

		initLabels();
		resetGrid();

	}
	
	public void applyHeight(ActionEvent e) {
		applyDimensions();
	}

	public void applyWidth(ActionEvent e) {

		applyDimensions();
	}

	public void resetGrid() {//used to reset the grid, now it just saves the config
		try {
			writeConf();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void addRandoms(ActionEvent e) {// adds random amount
		
		try {
			int rand = Integer.parseInt(randomCount.getText());
			
			// If nothing is entered in the random amount text field, do nothing
			if(Integer.parseInt(randomCount.getText())>(X*Y)){
				System.out.println("Too big, add error here");
				return;
			}
			populateRandom(rand);
		} catch (Exception exception) {
			populateRandom(0);
		}
		FillExtras();//removes the extras so that they dont interfere w/ simulation
	}

	public boolean isFirst() {
		if (debugCounter == 0) {
			debugCounter++;
			return true;
			// nextButton.setText("Next");
			// startButton.setText("start loop");
		}
		return false;
	}

	public void populateRandom(int number) {//TODO fix this
		FillExtras();//fills nonexistant cells with nonexistant values
		//to keep them from being counted in random

		
		int availableNodes = 0;
		
		ArrayList<String> usedCoords = new ArrayList<>();
		
		
		for(int tx = 0;tx<(X-1);tx++){//finds how many avabaible nnotes there are
			for(int ty = 0;ty<(Y-1);ty++){
				if(baseGrid.getCellState(tx, ty)==false){//if cell is not populated add to avaiable spaces
					availableNodes++;
				}else{
					String currentCoord = (Integer.toString(tx)+"x"+Integer.toString(ty)+"y");
					usedCoords.add(currentCoord);
				}
			}
		}
		
		//System.out.println(usedCoords);
		if((number>((X-1)*(Y-1)))|(availableNodes==0)){
			//System.out.println("Too big or no avaiable nodes");
			return;
		}
		int added = 0;
		
		if(number==0){
			return;
		}
		
		do {
			//System.out.println("avaialale nodes: "+availableNodes);
			//System.out.println("todadd: "+number+"     added:"+added);
			
			int randX = (int) (Math.random() * ((X - 2) + 1) + 1);
			int randY = (int) (Math.random() * ((Y - 2) + 1) + 1);

			String currentCoord = (Integer.toString(randX)+"x"+Integer.toString(randY)+"y");
			//(usedCoords.contains(currentCoord)==false)
			
			if(usedCoords.contains(currentCoord)){
				//System.out.println(currentCoord+" is used");
			}else{
				populateNode(randX, randY); // Then populate random node
				availableNodes--;
				added++;
				usedCoords.add(currentCoord);
			}			
			if(availableNodes<=0){
				System.out.println("out of available nodes, or java had a memory error");
				return;
			}
			if(added>=number){
				System.out.println("done, or java had a memory error");
				return;
			}

			
			
		} while (true);
	}

	public void NextButtton(ActionEvent e) {
		if (isFirst()) {
			try {
				initAll();
				// Fixes the button labels and loop state
				startButton.setText("Start Loop");
				isLoop = false;
				loop.setSelected(false);
				nextButton.setText("Next");

				return;
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// needs to simulate a cycle

		baseGrid.simulateCycle();
		reColor();
		System.out.println("----------");
		//baseGrid.drawStateCompatability();

		// at the end it must re-update all of the nodes to tell them to be
		// Clickable because some of them might have changed
		makeClickable();
		clearExtras();

	}

	public void StartButton(ActionEvent e) {
		if (isFirst()) {
			try {
				initAll();
				// Fixes the button labels and loop state
				startButton.setText("Start Loop");
				isLoop = false;
				loop.setSelected(false);
				nextButton.setText("Next");

			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		// Button loop labels
		if (isLoop) {
			startButton.setText("Start Loop");
			isLoop = false;
			loop.setSelected(false);
			stopClearTimer();
		} else {
			startButton.setText("Stop Loop");
			isLoop = true;
			loop.setSelected(true);
			startTimer();
		}

		makeClickable();
	}

	public void makeClickable() {//iterates through all the nodes and makes them clickable

		// Note to future self, getchildren returns an Observablelist, just so u
		// know and don't have to trial and error alot again
		ObservableList<Node> nodes = DrawGrid.getChildren(); // Adds all of the Drawgrids children to a list

		for (final Node target : nodes) { // For all nodes in the list nodes
			target.setOnMouseClicked(new EventHandler<MouseEvent>()
			// This is what happens when the mouse is clicked on the node
			{

				@Override
				public void handle(MouseEvent m) {

					// System.out.println(target.getProperties());
					// returns the x and y of the node when clicked

					// Reads the nodes properties, looks for the property of row or column , makes it a string then an int
					int ClickedColumn = Integer.parseInt(target.getProperties().get("gridpane-column").toString());
					System.out.println("Column: " + ClickedColumn);

					int ClickedRow = Integer.parseInt(target.getProperties().get("gridpane-row").toString());
					System.out.println("Row: " + ClickedRow);

					
					if (baseGrid.getCellState(ClickedRow-1, ClickedColumn-1)== false) {// if cell doesnt return true/populated
								// populate or unpopulate the node respcitvly
						populateNode(ClickedColumn, ClickedRow);
						

					} else {
						unpopulateNode(ClickedColumn, ClickedRow);

					}
					
					reColor();//inverts the colors because reasons
					reColor();//fixes the colors for the same reasons

				}
			});
		}
	}

	//add row and col add rows and columns during the init time
	public void addRow() {
		RowConstraints Rcon = new RowConstraints();
		Rcon.setPrefHeight(prefX);
		DrawGrid.getRowConstraints().add(Rcon);
	}

	public void addCol() {
		ColumnConstraints Ccon = new ColumnConstraints();
		Ccon.setPrefWidth(prefY);
		DrawGrid.getColumnConstraints().add(Ccon);
	}

	//calculates the prefered size of the nodes/rectangles
	public void calcPrefSize() {

		prefX = (int) Math.ceil(((double) 600) / ((double) X));
		prefY = (int) Math.ceil(((double) 600) / ((double) Y));

		// Makes biggest possible square boxes as possible that can fit within bounds

		// so each square area is prefY * prefX, well rectangle area in some cases

		// it will try to only use squares so if the dimensions are like 10 wide
		// by 37 tall, it will be a
		// tower of squares instead of being rectangles boxes
	}

	public void initPopulate() { // To be called to populate the grid with the initial values for the cells

		DrawGrid.gridLinesVisibleProperty().set(true);// so you can see the grid border
		DrawGrid.setMaxSize(600, 600);// wont let the grid be bigger than the gridpane

		for (int i = 1; i != Y; i++) {
			addRow();
		}

		for (int i = 1; i != X; i++) {
			addCol();
			
		}
	}

	public void initAll() throws FileNotFoundException {// the main init fucntion that runs all the init functions
		
		getConf(); // Reads the config file

		calcPrefSize(); // Finds best size from grid

		initPopulate(); // Makes the grid
		
		fill(); // Fill and sets colors

		makeClickable(); // Makes all the cells clickable
		// This must be refreshed after any cell is modified

		baseGrid = new Grid(X, Y);


		colorX=X;//these vars keep the grid from going nuts when you change its size
		colorY=Y;// they are only used to write the config later
		dimY=Y;
		
		initLabels(); // Refreshes all the options labels with config file values
	}

	public void getConf() throws FileNotFoundException {
		File configFile = new File("config.txt");

		Scanner scanner = new Scanner(configFile); // Opens config file
		for (int i = 1; i < 5; i++) { // Read first 4 lines of file
			if (i == 1) {
				Y = scanner.nextInt() + 1;
				// Bounds are min 10x10 and max 100x100
				// is 0 indexed so it changes to 1 index check
				if (Y < 11) {
					Y = 11;
				}
				if (Y > 101) {
					Y = 101;
				}
				Ynumber.setText("Height: " + (Y - 1));
			} else if (i == 2) {
				X = scanner.nextInt() + 1;
				// Bounds are min 10x10 and max 100x100
				// is 0 indexed so it changes to 1 index check
				if (X < 11) {
					X = 11;
				}
				if (X > 51) {
					X = 51;
				}
				Xnumber.setText("Width: " + (X - 1));
			} else if (i == 3) {
				backround = scanner.next();
				// System.out.println(background);
			} else if (i == 4) {
				Primary = scanner.next();
				// System.out.println(Primary);
			} else {
				System.out.println("bad config error");
			}

		}

		Y=X;// yeah, we made the grid a square only grid after the UI was to complex
		//It used to be able to be and dimensions such as 56x73 but it was to confusing to change 
		//the values. accoridng to test humans
		
		System.out.println("Config loaded");
		scanner.close();
	}

	public void fill() { // Can be used to set background color

		int size;
		if (prefX > prefY) {
			size = prefY;
		} else if (prefX < prefY) {
			size = prefX;
		} else {
			size = prefX;
		} // finds the smallest preferred size which ends up being the side length
			// of one of the squares on the grid
			// and makes a rectangle that size and puts it on the grid

		for (int l = 1; l < Y; l++) {
			for (int p = 1; p < X; p++) {

				Rectangle o = new Rectangle();

				o.setHeight(size);
				o.setWidth(size);

				// o.setVisible(false);
				// o.setFill(Color.GAINSBORO);

				o.setFill(Color.web(backround));

				DrawGrid.add(o, p, l);
			}
		}
	}

	public void clearGrid() {

		ObservableList<Node> nodes = DrawGrid.getChildren();// adds all of the
		// Drawgrids children to a list

		int count = nodes.size();

		System.out.println("cells#" + (X - 1) * (Y - 1));
		System.out.println(DrawGrid.getChildren());
		System.out.println("nodes# " + count);

		DrawGrid.setPrefSize(1, 1);
		DrawGrid.setMaxSize(600, 600);
	}

	public void reColor() { // Can be used to set background color, will be
							// triggered by the apply button from the color
							// changers

		// Also make it write the current color values to the config file so
		// they are persistent
		// Goes through all grid nodes and re sets the color to new values
		for (int l = 2; l != (colorY+1); l++) {
			for (int p = 2; p != (colorX+1); p++) {
				
				if (baseGrid.getCellState(p-1, l-1)) { // if the cell state is populated then set primary color

					if (checkOutOfBounds(p-1, l-1)) {
						return;
					}

					int size;
					if (prefX > prefY) {
						size = prefY;
					} else if (prefX < prefY) {
						size = prefX;
					} else {
						size = prefX;
					} // Finds the smallest preferred size which ends up being the side length
						// of one of the squares on the grid
						// and makes a rectangle that size and puts it on the grid
					Rectangle r = new Rectangle();

					r.setHeight(size);
					r.setWidth(size);

					r.setFill(Color.web(Primary));

					//System.out.println(p+"   "+l);
					DrawGrid.add(r, p-1, l-1);
				} else {//if the cell is unpopulated it is now backround
					if (checkOutOfBounds(p-1, l-1)) {
						return;
					}

					int size;
					if (prefX > prefY) {
						size = prefY;
					} else if (prefX < prefY) {
						size = prefX;
					} else {
						size = prefX;
					} // Finds the smallest preferred size which ends up being the side length
						// of one of the squares on the grid
						// and makes a rectangle that size and puts it on the grid
					Rectangle r = new Rectangle();

					r.setHeight(size);
					r.setWidth(size);

					r.setFill(Color.web(backround));

					DrawGrid.add(r, p-1, l-1);					
				}
				
			}
				
		}
		
		makeClickable();
	}

	public boolean checkOutOfBounds(int x, int y) {// checks if you are trying to edit a cell that isnt on the grid
		if (x > X - 1) {
			System.out.println("out of bounds error, but don't worry, I caught it for you");
			return true;
		}
		if (y > Y - 1) {
			System.out.println("out of bounds error, but don't worry, I caught it for you");
			return true;
		}
		return false;
	}//if the universe like people (spefically us who wrote this) then you will never see this method

	public void populateNode(int x, int y) {

		if (checkOutOfBounds(x, y)) {
			return;
		}

		int size;
		if (prefX > prefY) {
			size = prefY;
		} else if (prefX < prefY) {
			size = prefX;
		} else {
			size = prefX;
		} // Finds the smallest preferred size which ends up being the side length
			// of one of the squares on the grid
			// and makes a rectangle that size and puts it on the grid
		Rectangle r = new Rectangle();

		r.setHeight(size);
		r.setWidth(size);

		r.setFill(Color.web(Primary));

		DrawGrid.add(r, x, y);

		baseGrid.changeCellState(x, y);
		makeClickable();

	}

	public void unpopulateNode(int x, int y) {

		if (checkOutOfBounds(x, y)) {
			return;
		}

		int size;
		if (prefX > prefY) {
			size = prefY;
		} else if (prefX < prefY) {
			size = prefX;
		} else {
			size = prefX;
		} // Finds the smallest preferred size which ends up being the side length
			// of one of the squares on the grid
			// and makes a rectangle that size and puts it on the grid
		Rectangle r = new Rectangle();

		r.setHeight(size);
		r.setWidth(size);

		r.setFill(Color.web(backround));

		DrawGrid.add(r, x, y);

		baseGrid.changeCellState(x, y);
		makeClickable();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
