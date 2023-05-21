/**
  	* @author Deep Shah
  	* @author Aryaman Patel
 */



package songlib.view;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.io.*; 

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import songlib.app.Song; 

public class SonglibController {
	
	@FXML ListView<Song> songListView;
	@FXML Button addSongBtn;
	@FXML Button editSongBtn;
	@FXML Button deleteSongBtn;
	@FXML Button saveBtn; 
	@FXML Button cancelBtn;
	@FXML TextArea songDetailView;
	@FXML TextField songName;
	@FXML TextField artistName;
	@FXML TextField albumName;
	@FXML TextField songYear;
	
	private ObservableList<Song> songObjList; 
	private Song currentSong;
	private int currentIndex;
	private boolean addClicked = false; 
	
	/**
	 	* Function called once the app is launched
	 	* @param mainStage
	 */
	
	public void init(Stage mainStage) {
		
		// Creating a song list view
		
		songObjList = FXCollections.observableArrayList();
		
		//creating a new file and checking a existing file
		File myPlaylist = new File("./src/songlib/files/playlist.txt");
	      try {
			if (myPlaylist.createNewFile()) {
			    System.out.println("File created: " + myPlaylist.getName());
			  } else {
			    System.out.println("Playlist file already exists.");
			  }
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	      
	    //reads the playlist file
	      
	      try {
	          Scanner myReader = new Scanner(myPlaylist);
	          while (myReader.hasNextLine()) {
	            String data = myReader.nextLine();
	            String[] details = data.split(",",-1); 
	            Song newSong = new Song(details[0], details[1]); 
	            if(details[2] != null) {
	            	newSong.albumName = details[2]; 
	            }
	            if(details[3] != null) {
	            	newSong.year = details[3]; 
	            }
	            songObjList.add(newSong); 
	          }
	          myReader.close();
	        } catch (FileNotFoundException e) {
	          System.out.println("An error occurred.");
	          e.printStackTrace();
	     }
				
		Collections.sort(songObjList);    
		
		songListView.setCellFactory(stringListView -> new CenteredListViewCell());
		
		songListView.setItems(songObjList);
		
		songListView.getSelectionModel().select(0);
		showItem(mainStage);
		
		//sets listener 
		songListView.getSelectionModel().selectedItemProperty().addListener(new
		ChangeListener<Song>() { public void changed(ObservableValue<? extends Song>
			observable, Song oldValue, Song newValue) { showItem(mainStage);
		 } });
		
		saveBtn.setStyle("-fx-font: 14 arial; -fx-base: #b6e7c9;");
		cancelBtn.setStyle("-fx-font: 14 arial; -fx-base: #b6e7c9;");
		addSongBtn.setStyle("-fx-font: 14 arial; -fx-base: #b6e7c9;");
		editSongBtn.setStyle("-fx-font: 14 arial; -fx-base: #b6e7c9;");
		deleteSongBtn.setStyle("-fx-font: 14 arial; -fx-base: #b6e7c9;");
		
		//enable or disable fields when song lib application is started
		songName.setDisable(true);
		albumName.setDisable(true);
		artistName.setDisable(true);
		songYear.setDisable(true); 	
		saveBtn.setDisable(true);
		cancelBtn.setDisable(true);
		
		 
	}
	
	/**
	 * Function called upon selecting a song
	 * Populates song details view
	 * @param mainStage
	 */
	
	private void showItem(Stage mainStage) {
		int index = songListView.getSelectionModel().getSelectedIndex();
		if(index < 0 || index > songObjList.size()-1) {
			return;
		}
		
		//Adds song to the list view
		
		String content = "Song Name: \t\t\t" + songObjList.get(index).songName + "\n" + "Artist: \t\t\t\t" + songObjList.get(index).artistName;
		songDetailView.setText(content);
		String cont = ""; 
		if(songObjList.get(index).albumName != null) {
			cont = cont + "\n" + "Album Name: \t\t\t" + songObjList.get(index).albumName; 
		}
		if(songObjList.get(index).year != null) {
			cont = cont + "\n" + "Year: \t\t\t\t" + songObjList.get(index).year; 
		}
		songDetailView.appendText(cont); 
		
		//Adds current song
		
		currentSong = songObjList.get(index);
		currentIndex = index;
	}
	
	/**
	 * Adds a song to songObjList
	 * @param e
	 */
	
	public void addSong(ActionEvent e) {
		
		// confirmation to proceed
		Alert new_alert = new Alert(AlertType.CONFIRMATION);
		new_alert.setTitle("Add Confirmation");
		new_alert.setHeaderText("ARE YOU SURE?");
		String new_content = "You will create a new song."; 
		new_alert.setContentText(new_content);
		new_alert.showAndWait();
		
		if(new_alert.getResult().getText().contentEquals("Cancel")) {
			return;
		}
		
		//add button is clicked
		addClicked = true; 
		
		//list view is disabled
		songListView.setDisable(true);
		
		//opens the text field so user can edit
		
		songName.setDisable(false);
		songName.setPromptText("Song Name...");
		albumName.setDisable(false);
		albumName.setPromptText("Album Name...");
		artistName.setDisable(false);
		artistName.setPromptText("Artist Name...");
		songYear.setDisable(false); 
		songYear.setPromptText("YYYY");
		
		//makes button clickable 
		saveBtn.setDisable(false);
		cancelBtn.setDisable(false);
		deleteSongBtn.setDisable(true); 
		editSongBtn.setDisable(true); 
		
		//clears all the text feild
		songName.clear();
		albumName.clear();
		songYear.clear();
		artistName.clear(); 
		
	}
	
	/**
	 * Delete selected song form list
	 * @param e
	 */
	
	public void deleteSong(ActionEvent e) {
		if(songObjList.isEmpty()) {
			
			//error checks
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("No more songs left!");
			alert.setHeaderText("Error!");
			String content = "There are no songs to delete."; 
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		// confirmation to proceed
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Delete Confirmation");
		alert.setHeaderText("ARE YOU SURE?");
		String content = "This song will be deleted permanently."; 
		alert.setContentText(content);
		alert.showAndWait();
		
		if(alert.getResult().getText().contentEquals("Cancel")) {
			return;
		}
		songObjList.remove(currentSong);
		
		// selection check
		if(songObjList.isEmpty()) {
			songDetailView.clear();
			return;
		}
		
		if(currentIndex == songObjList.size()-1) {
			songListView.getSelectionModel().select(currentIndex);
		}else {
			songListView.getSelectionModel().select(currentIndex+1);
		}	
	}
	
	/**
	 * Save form details into song form list
	 * @param e
	 */
	
	public void saveSong(ActionEvent e) {
		
		// confirmation to proceed
		Alert new_alert = new Alert(AlertType.CONFIRMATION);
		new_alert.setTitle("Save Confirmation");
		new_alert.setHeaderText("ARE YOU SURE?");
		String new_content = "This song will be saved."; 
		new_alert.setContentText(new_content);
		new_alert.showAndWait();
		
		
		if(songName.getText().isEmpty() || artistName.getText().isEmpty()) {
			//error message
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Incomplete Fields");
			alert.setHeaderText("Error!");
			String content = "Please fill in the Song Name and Artist Name Fields."; 
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		if(!songYear.getText().isEmpty()) {
			if(!songYear.getText().matches("[0-9]+")) {
				//error message
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Wrong Input");
				alert.setHeaderText("Error!");
				String content = "The Year Text Field should only contain numbers."; 
				alert.setContentText(content);
				alert.showAndWait();
				return;
			}
		}
		Song newSong = new Song(songName.getText(), artistName.getText()); 
		newSong.albumName = albumName.getText(); 
		newSong.year = songYear.getText();
		
		//check for duplicates
		if(addClicked) {			
			
			if(containsSong(songObjList,newSong)) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Song Already Exists!");
				alert.setHeaderText("Error!");
				String content = "This song already exists in the song list."; 
				alert.setContentText(content);
				alert.showAndWait();
				return;
			}
			songObjList.add(newSong); 
			Collections.sort(songObjList);
			

			addClicked = false; 
		}
		else {			
			
			
			//Check for duplicates
			if(containsSong(songObjList,newSong) && currentSong.compareTo(newSong) != 0) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Song Already Exists!");
				alert.setHeaderText("Error!");
				String content = "This song already exists in the song list."; 
				alert.setContentText(content);
				alert.showAndWait();
				return;
			}
			
			songObjList.remove(currentSong);
			songObjList.add(newSong); 
			Collections.sort(songObjList);
		}
		
		// selects last added songs
		songListView.getSelectionModel().select(songObjList.indexOf(newSong));
		
		//Clears text fields 
		songName.clear();
		artistName.clear();
		albumName.clear();
		songYear.clear();
		
		songName.setDisable(true);
		albumName.setDisable(true);
		artistName.setDisable(true);
		songYear.setDisable(true); 
		
		//list view
		songListView.setDisable(false);
		
		//enable or disable buttons
		saveBtn.setDisable(true);
		cancelBtn.setDisable(true); 
		deleteSongBtn.setDisable(false);
		addSongBtn.setDisable(false);
		editSongBtn.setDisable(false);
	}
	
	/**
	 * Edit selected song form list
	 * @param e
	 */
	
	public void editSong(ActionEvent e) {
		
		// confirmation to proceed
		Alert new_alert = new Alert(AlertType.CONFIRMATION);
		new_alert.setTitle("Add Confirmation");
		new_alert.setHeaderText("ARE YOU SURE?");
		String new_content = "You will edit a saved song."; 
		new_alert.setContentText(new_content);
		new_alert.showAndWait();
		
		if(new_alert.getResult().getText().contentEquals("Cancel")) {
			return;
		}
		
		
		addClicked = false; 
		
		if(songObjList.isEmpty()) {
			//error check
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Playlist Empty!");
			alert.setHeaderText("Error!");
			String content = "There are no songs to edit."; 
			alert.setContentText(content);
			alert.showAndWait();
			return;
		}
		
		//list view
		songListView.setDisable(true);
		
		//makes the text fields editable
		songName.setDisable(false);
		albumName.setDisable(false);
		artistName.setDisable(false);
		songYear.setDisable(false);
		
		//makes the buttons editable
		saveBtn.setDisable(false);
		cancelBtn.setDisable(false);
		deleteSongBtn.setDisable(true);
		addSongBtn.setDisable(true);
		
		//fills the text fields with existing song details
		songName.setText(currentSong.songName);
		albumName.setText(currentSong.albumName);
		artistName.setText(currentSong.artistName);
		songYear.setText(currentSong.year);		
	}
	
	/**
	 * Cancel ongoing action
	 * @param e
	 */
	
	public void cancelSong(ActionEvent e) {
		
		//Clears text fields
		songName.clear();
		artistName.clear();
		albumName.clear();
		songYear.clear();
		
		//Disables text fields
		songName.setDisable(true);
		albumName.setDisable(true);
		artistName.setDisable(true);
		songYear.setDisable(true);
		
		//lists view
		songListView.setDisable(false);
		
		//disable or enable buttons
		saveBtn.setDisable(true);
		cancelBtn.setDisable(true);
		deleteSongBtn.setDisable(false);
		addSongBtn.setDisable(false);
		editSongBtn.setDisable(false);
		
	}
	
	/**
	 * Helper function to check duplicate songs
	 * @param list
	 * @param song
	 * @return
	 */
	
	public boolean containsSong(final List<Song> list, final Song song){
	   for(int i = 0; i < list.size(); i++) {
		   if(list.get(i).songName.toLowerCase().equals(song.songName.toLowerCase()) && list.get(i).artistName.toLowerCase().equals(song.artistName.toLowerCase())) {
			   return true; 
		   }
	   }
	   return false;
	}
	
	/**
	 * closes this session and saves updated song list into playlist file
	 */
	
	public void close() {
		//write the list into playlist
		try {
		      FileWriter myWriter = new FileWriter("./src/songlib/files/playlist.txt");
		      for(int i = 0; i < songObjList.size(); i++) {
		    	  Song thisSong = songObjList.get(i); 
		    	  myWriter.write("" + thisSong.songName + "," + thisSong.artistName + "," + thisSong.albumName + "," + thisSong.year + "\n"); 
		      }
		      myWriter.close();
		      System.out.println("Playlist successfully written.");
		   } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		   }
	}
}


// new class
final class CenteredListViewCell extends ListCell<Song> {
	HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Label artistLabel = new Label("(empty)");
    Pane pane = new Pane();
    Song lastItem;
    
    public CenteredListViewCell() {
        super();
        hbox.getChildren().addAll(label, pane, artistLabel);
        HBox.setHgrow(pane, Priority.ALWAYS);
    }
	
    @Override
    protected void updateItem(Song item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
        	lastItem = null;
            setGraphic(null);
        } else {
        	lastItem = item;
            label.setText(item!=null ? item.songName : "<null>");
            artistLabel.setText(item!=null ? item.artistName : "<null>");
            setGraphic(hbox);
        }
    }
}