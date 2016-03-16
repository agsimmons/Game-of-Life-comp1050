package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Paths;
import java.util.Scanner;

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

	public static int c = 0;// debugging counter and first checking var

	public void initColors() {// gets the colors from the config and sets the
								// labels
		backColor.setValue(Color.web(backround));
		primaryColor.setValue(Color.web(Primary));
	}

	public void initHeight() {
		heightText.setText(Integer.toString(X - 1));
		heightSlider.setValue(X);
	}

	public void initWidth() {
		widthText.setText(Integer.toString(Y - 1));
		widthSlider.setValue(Y);
	}

	public void initLabels() {
		initColors();
		initHeight();
		initWidth();
	}

	public void applyColor(ActionEvent e) {
		String baseBack = "#";
		String basePrimary = "#";
		// reads the color settings and writes their settings to the color vars
		String back = backColor.getValue().toString();

		String primary = primaryColor.getValue().toString();

		back = back.substring(2, 8);// trims the alpha values and the other
									// parts
		primary = primary.substring(2, 8);
		backround = baseBack.concat(back);// and sets them to web hex color
											// strings
		Primary = basePrimary.concat(primary);

		// needs to write the colors to config

		fill();// fill colors

		initColors();// sets the color labels correctly

		makeClickable();// makes all the cells clickable
		// this must be refeshed after any cell is modified

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
	// apply and remake grid on button click
	public void applyHeight(ActionEvent e) {
		heightText.setText(Integer.toString((int) heightSlider.getValue()));
		X = (int) (heightSlider.getValue());
		widthText.setText(Integer.toString((int) widthSlider.getValue()));

		Y = (int) (widthSlider.getValue());

		resetGrid();
	}

	public void applyWidth(ActionEvent e) {
		widthText.setText(Integer.toString((int) widthSlider.getValue()));

		Y = (int) (widthSlider.getValue());

		heightText.setText(Integer.toString((int) heightSlider.getValue()));
		X = (int) (heightSlider.getValue());

		resetGrid();
	}

	public void resetGrid() {
		clearGrid();

	}

	public void addRandoms(ActionEvent e) {// adds random amount
		try {
			popRandom(Integer.parseInt(randomCount.getText()));// if nothing is
																// entered in
																// the ranom
																// amount text
																// field, do
																// nothing
		} catch (Exception exception) {
			popRandom(0);
		}

	}

	public boolean isFirst() {
		if (c == 0) {
			c++;
			return true;
			// nextButton.setText("Next");
			// startButton.setText("start loop");
		}
		return false;
	}

	/*
	 * uses ~350mb ram on launch, later goes down to ~300mb
	 * 
	 */

	public void popRandom(int number) {

		// does not take into account the amount of cells already populated,
		// makes it so if spammed grid not fill
		/*
		 * if(number>((X/10)*8)){//limits the amount of random number of cells
		 * populated number=((X/10)*8);//to 80% of the total amount of cells }
		 */
		int i = 0;
		do {// tries to populate random cells "number" times,
			// these can overlap so if its told to populate 12 cells then 8
			// could be populated with 4 overlap
			int popOrNor = (int) Math.round(Math.random());// 0 or 1 for yess
															// poplate or not
			// then random x and y coords that are possible
			int randX = (int) (Math.random() * ((X - 2) + 1) + 1);
			int randY = (int) (Math.random() * ((Y - 2) + 1) + 1);

			if (popOrNor == 1) {// if populate
				if (!true) {// if the cell is populated or not

					popNode(randX, randY);// then populate random node

					i++;
				}
			}
		} while (i < number);

	}

	public void NextButtton(ActionEvent e) {
		if (isFirst()) {
			try {
				initAll();
				// fixes the button lables and loop state
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

		// for now it just does test stuff

		popRandom(79);// popuate 79 random cells

		// at the end it must reupdate all of the nodes to tell them to be
		// clckable because some of them might have chnaged
		makeClickable();

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

		// button loop lables
		if (isLoop) {
			startButton.setText("Start Loop");
			isLoop = false;
			loop.setSelected(false);
		} else {
			startButton.setText("Stop Loop");
			isLoop = true;
			loop.setSelected(true);
		}

		makeClickable();
	}

	public void makeClickable() {

		// note to future self, getchildren returns an Observablelist, just so u
		// know and dont have to trial and error alot again
		ObservableList<Node> nodes = DrawGrid.getChildren();// adds all of the
															// Drawgrids
															// children to a
															// list

		for (final Node target : nodes) {// for all nodes in the list nodes
			target.setOnMouseClicked(new EventHandler<MouseEvent>()
			// this is what happens when the mouse is clicked on the node
			{

				@Override
				public void handle(MouseEvent m) {

					// System.out.println(target.getProperties());
					// returns the x and y of the node when clicked

					// reads the nodes properties, looks for the property of row
					// or column , makes it a string then an int
					int ClickedColumn = Integer.parseInt(target.getProperties().get("gridpane-column").toString());
					System.out.println("Column: " + ClickedColumn);

					int ClickedRow = Integer.parseInt(target.getProperties().get("gridpane-row").toString());
					System.out.println("Row: " + ClickedRow);

					String ClickedColor = target.getId();
					System.out.println(ClickedColor);

					if (true) {// if andrews method returrns true or false
								// populate or unpopulate the node respcitvly
						popNode(ClickedColumn, ClickedRow);

					} else {
						unPopNode(ClickedColumn, ClickedRow);

					}
				}
			});
			// once this part is done, make it so dragging also works
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
														// border

		DrawGrid.setMaxSize(600, 600);// wont let the grid be bigger than the
										// gridpane

		for (int i = 1; i != Y; i++) {
			addRow();

		}

		for (int i = 1; i != X; i++) {
			addCol();

		}

	}

	public void initAll() throws FileNotFoundException {

		getConf();// reads the config file

		calcPrefSize();// finds best size fro grid

		initPopulate();// makes the grid

		fill();// fill adn sets colors

		initLabels();// refreshes all the options labels with config file values

		makeClickable();// makes all the cells clickable
		// this must be refeshed after any cell is modified

	}

	public void getConf() throws FileNotFoundException {
		File configFile = new File("config.txt");

		Scanner scanner = new Scanner(configFile);// opens
													// config
													// file
		for (int i = 1; i < 5; i++) {// read first 4 lines of file
			if (i == 1) {
				Y = scanner.nextInt() + 1;
				// bounds are min 10x10 and max 100x100
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
				// bounds are min 10x10 and max 100x100
				// is 0 indexed so it changes to 1 index check
				if (X < 11) {
					X = 11;
				}
				if (X > 101) {
					X = 101;
				}
				Xnumber.setText("Width: " + (X - 1));
			} else if (i == 3) {
				backround = scanner.next();
				// System.out.println(backround);
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
				// o.setFill(Color.GAINSBORO);

				o.setFill(Color.web(backround));

				DrawGrid.add(o, p, l);
			}
		}

	}

	public void clearGrid() {

		ObservableList<Node> nodes = DrawGrid.getChildren();// adds all of the
		// Drawgrids
		// children to a
		// list

		int count = 0;
		for (final Node target : nodes) {// for all nodes in the list nodes
			count++;

		}

		System.out.println("cells#" + (X - 1) * (Y - 1));
		System.out.println(DrawGrid.getChildren());
		System.out.println("nodes# " + count);

		DrawGrid.setPrefSize(0, 0);

	}

	public void reColor() {// can be used to set backrough color, will be
							// triggered by the apply button from the colro
							// changers

		// also make it write the current color values to the config file so
		// they are presistant

		int size;
		if (prefX > prefY) {
			size = prefY;
		} else if (prefX < prefY) {
			size = prefX;
		} else {
			size = prefX;
		} // get the correct x and y

		// goes through all grid nodes and re sets the color to new values
		for (int l = 1; l < Y; l++) {
			for (int p = 1; p < X; p++) {

				Rectangle o = new Rectangle();

				if (true) {// use the getpop thing
					o.setFill(Color.web(Primary));

				} else {
					o.setFill(Color.web(backround));
				}

				DrawGrid.add(o, p, l);
			}
		}

	}

	public boolean checkOutOfBounds(int x, int y) {// checks if you are trying
													// to edit a cell that isnt
													// on the grid
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

	public void popNode(int x, int y) {

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
		} // finds the smallest preferd size which ends up being the side length
			// of one of the squares on the grid
			// and makes a rectange that size and puts it on the grid
		Rectangle r = new Rectangle();

		r.setHeight(size);
		r.setWidth(size);

		r.setFill(Color.web(Primary));

		DrawGrid.add(r, x, y);

		// then tell andrews code that a change has occured
	}

	public void unPopNode(int x, int y) {

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
		} // finds the smallest preferd size which ends up being the side length
			// of one of the squares on the grid
			// and makes a rectange that size and puts it on the grid
		Rectangle r = new Rectangle();

		r.setHeight(size);
		r.setWidth(size);

		r.setFill(Color.web(backround));

		DrawGrid.add(r, x, y);

		// then tell andrews code the chnage has happened
	}

	public static void main(String[] args) {
		launch(args);

	}
}
