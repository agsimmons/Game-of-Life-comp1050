package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
//		try {
//			BorderPane root = new BorderPane();
//			Scene scene = new Scene(root,400,400);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			primaryStage.setScene(scene);
//			primaryStage.show();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public static void main(String[] args) {
//		launch(args);
		
		Grid grid = new Grid(10, 5);
		grid.changeCellState(2, 1);
		grid.changeCellState(7, 1);
		grid.changeCellState(2, 3);
		grid.changeCellState(3, 4);
		grid.changeCellState(4, 4);
		grid.changeCellState(5, 4);
		grid.changeCellState(6, 4);
		grid.changeCellState(7, 3);
		grid.drawState();
	}
}
