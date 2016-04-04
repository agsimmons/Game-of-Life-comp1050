package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Main extends Application { // All the usual JavaFX stuffs
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

	@FXML
	Label Ynumber;
	@FXML
	Label Xnumber;
	public static int X;
	public static int Y;
	
	public static int dimX;
	public static int dimY;
	
	public static int colorX;
	public static int colorY;

	//TODO find bug that makes back and prime switch and x and y switch

	public static boolean isLoop = false;

	public static String backround;
	public static String Primary;

	public static int prefX;
	public static int prefY;

	public static Grid baseGrid;

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
	Button widthButton;

	@FXML
	Slider heightSlider;

	@FXML
	Slider widthSlider;

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
	Button saveButton;

	@FXML
	Button loadButton;
	
	@FXML
	CheckBox rule1;
	
	@FXML
	CheckBox rule2;
	
	@FXML
	CheckBox rule3;
	
	@FXML
	CheckBox rule4;
	

	public static int debugCounter = 0; // Debugging counter and first checking var

	public void initColors() { // Gets the colors from the config and sets the labels
		backColor.setValue(Color.web(backround));
		primaryColor.setValue(Color.web(Primary));
	}

	public void initHeight() {
		heightText.setText(Integer.toString(dimY - 1));
		heightSlider.setValue(dimY-1);
	}

	public void initWidth() {
		widthText.setText(Integer.toString(dimX - 1));
		widthSlider.setValue(dimX-1);
	}

	public void initLabels() {
		initColors();
		initHeight();
		initWidth();
	}
	
	public void FillExtras() {
		for (int i = 0; i < X; i++) {
			baseGrid.changeCellState(i, 0);
		}

		for (int i = 0; i < Y; i++) {
			baseGrid.changeCellState(0, i);
		}

		baseGrid.changeCellState(0, 0);
	}
	
	static getTiming timing = new getTiming() {//abstract class
	};
	
	static int interval = timing.defaultTiming();//timer interval in ms

    public Timeline timeline;
    public Label timerLabel = new Label();
    public DoubleProperty timeSeconds = new SimpleDoubleProperty();
    public Duration time = Duration.ZERO, splitTime = Duration.ZERO;

    
    

	public void startTimer(){
        timeline = new Timeline(
            new KeyFrame(Duration.millis(interval),
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    Duration duration = ((KeyFrame)t.getSource()).getTime();
                    time = time.add(duration);
                    timeSeconds.set(time.toSeconds());
                    baseGrid.simulateCycle();
            		reColor();
            		System.out.println("----------");
            		//baseGrid.drawStateCompatability();

            		// at the end it must re-update all of the nodes to tell them to be
            		// Clickable because some of them might have changed
            		makeClickable();
            		clearExtras();
                }
            })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    
    
    public void stopClearTimer(){
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
    
	
	
	public void clearExtras() {
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

	public void save(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Create saved state");
		//File stateFile = fileChooser.showOpenDialog(null);
		
		//TODO: Save state of grid variable into file.
	}

	public void load(ActionEvent e) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open saved state");
		File stateFile = fileChooser.showOpenDialog(null);
		
		int saveWidth;
		int saveHeight;
		Scanner saveScanner = null;
		
		try {
			System.out.println("Ding");
			saveScanner = new Scanner(stateFile);
		} catch (FileNotFoundException e1) {
			System.out.println("ERROR: File Not Found!");
			e1.printStackTrace();
		}
		
		//TODO: Load contents of file into grid variable, then redraw(?)
		try {
			System.out.println("Dong");

			saveWidth = saveScanner.nextInt();
			saveHeight = saveScanner.nextInt();

			boolean[][] tempGridState = new boolean[saveWidth][saveHeight];

			while(saveScanner.hasNextInt()) {
				tempGridState[saveScanner.nextInt()][saveScanner.nextInt()] = true;
			}

			baseGrid.setGridState(tempGridState);

			//TODO: Draw new grid to gui
			baseGrid.drawState();
			//it doesnt import right to the basegrid or something

		} catch(Exception e2) {
			System.out.println("ERROR: Invalid File!");
			e2.printStackTrace();
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

		//System.out.println("back: "+backround);
		//System.out.println("prime: "+Primary);
		// Needs to write the colors to config
		//initColors(); // Sets the color labels correctly

		reColor();
		//fill(); // Fill colors

		makeClickable(); // Makes all the cells clickable
		// This must be refreshed after any cell is modified
		try {
			writeConf();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/*
	 * TODO make the sliders live update the labels
	 * 
	 * //refresh on slider release public void refreshHeight(ActionEvent e){
	 * //heightText.setText(Double.toString(heightSlider.getValue()));
	 * System.out.println(heightSlider.getValue()); }
	 * 
	 * public void refreshWidth(ActionEvent e){
	 * widthText.setText(Double.toString(widthSlider.getValue()));
	 * 
	 * }
	 */
	// Apply and remake grid on button click

	public void writeConf() throws IOException {
		System.out.println("Config Saved");

		PrintWriter pw = new PrintWriter(new FileWriter("config.txt"));

		pw.write(Integer.toString((dimX - 1))+"\n");
		pw.write(Integer.toString((dimY - 1))+"\n");
		pw.write(backround+"\n");
		pw.write(Primary+"\n");

		pw.close();
	}
		
	
	public void applyDimensions(){

		if(Integer.parseInt(heightText.getText())!=(dimY-1)) {
			dimY=(Integer.parseInt(heightText.getText())+1);
			
		} else if((int) (heightSlider.getValue())!=(dimY-1)) {
			dimY=((int) (heightSlider.getValue())+1);
		}
		
		if(Integer.parseInt(widthText.getText())!=(dimX-1)) {
			dimX=(Integer.parseInt(widthText.getText())+1);
		} else if((int) (widthSlider.getValue())!=(dimX-1)) {
			dimX=((int) (widthSlider.getValue())+1);
		}

		initLabels();
		resetGrid();

	}
	
	
	//TODO for the height and width, need to recalc pref higthts etc before remakeing grid
	public void applyHeight(ActionEvent e) {
		applyDimensions();
	}

	public void applyWidth(ActionEvent e) {

		applyDimensions();
	}

	public void resetGrid() {
		try {
			writeConf();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//clearGrid();
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

	public void makeClickable() {

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
					
					reColor();
					reColor();
					//baseGrid.drawStateCompatability();

				}
			});
			// Once this part is done, make it so dragging also works
			// just copy the code and make it set the drag event
		}
	}

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

	public void initAll() throws FileNotFoundException {
		
		

		getConf(); // Reads the config file

		calcPrefSize(); // Finds best size from grid

		initPopulate(); // Makes the grid
		

		fill(); // Fill and sets colors

		makeClickable(); // Makes all the cells clickable
		// This must be refreshed after any cell is modified

		baseGrid = new Grid(X, Y);


		colorX=X;
		colorY=Y;
		dimX=X;
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
				if (Y > 51) {
					Y = 51;
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
		/*
		for (final Node target : nodes) {// for all nodes in the list nodes
			count++;
		}
		*/

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
		

		//if((backround==backColor.getValue().toString())&(Primary==primaryColor.getValue().toString())){//if no change
		//	return;
		//}
		
		

		// Goes through all grid nodes and re sets the color to new values
		for (int l = 2; l != (colorY+1); l++) {
			for (int p = 2; p != (colorX+1); p++) {
				
				//System.out.println(baseGrid.getCellState(p-1, l-1));
				if (baseGrid.getCellState(p-1, l-1)) { // Use the getpop thing

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
				} else {
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

					//System.out.println(p+"   "+l);
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
	}

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
		//System.out.println("pop:isnow"+baseGrid.getCellState(x-1, y-1));

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
		//System.out.println("unpop:isnow"+baseGrid.getCellState(x-1, y-1));

	}

	public static void main(String[] args) {
		launch(args);
	}
}
