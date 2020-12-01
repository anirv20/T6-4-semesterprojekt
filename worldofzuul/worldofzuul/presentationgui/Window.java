package worldofzuul.presentationgui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Window {

    @FXML private Button btnOK;
    @FXML private TextArea txtArea;
    private Stage window;

    public void popUp(String title, String message) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Window.FXML"));
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); //Blocks other windows until you close this one
        window.setTitle(title);
        txtArea.setText(message);
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();

    }
    @FXML
    public void close(){
        window.hide();
    }
}
