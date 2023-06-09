/**
  * @author Deep Shah
  * @author Aryaman Patel
 */


package songlib.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import songlib.view.SonglibController;

public class SongLib extends Application{
	/**
	 	* Starting the Application
	 	* @param primaryStage
	 */
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/songlib/view/songlib.fxml"));
		
		GridPane root = (GridPane)loader.load();
		SonglibController listController = loader.getController();
		root.setStyle("-fx-border-color: #b6e7c9; -fx-background-color: black;");
		listController.init(primaryStage);
		
		Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Song Library");
        primaryStage.setResizable(false);
        
        //when the stage is closed
		primaryStage.setOnCloseRequest(event -> { 
		listController.close(); });
		 
        primaryStage.show();
		
	}
	/**
	 	* Launching the Application
	 	* @param args
	 */
	
	public static void main(String[] args) {
		launch(args);

	}

}