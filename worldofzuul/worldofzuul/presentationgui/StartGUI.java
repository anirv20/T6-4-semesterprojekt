package worldofzuul.presentationgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import worldofzuul.domain.*;

public class StartGUI extends Application {
    private static City city = new City();

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Presentation.FXML"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static City getCity() {
        return city;
    }

    public static void roomChecker(Circle player){
        if ((player.getCenterX() > -66.6) && (player.getCenterX() < 66.6)) {
            if ((player.getCenterY() >= 0) && (player.getCenterY() < 133.3)) {
                city.setCurrentRoom(city.getCityHall());
            } else if ((player.getCenterY() > 266.6) && (player.getCenterY() < 400)) {
                city.setCurrentRoom(city.getWindFarm());
            } else {
                city.setCurrentRoom(city.getOutside());
                city.updateOutsideInfo();
            }
        } else if ((player.getCenterX() < -66.6) && (player.getCenterX() > -200)) {
            if ((player.getCenterY() > 133.3) && (player.getCenterY() < 266.6)) {
                city.setCurrentRoom(city.getNuclearReactor());
            } else {
                city.setCurrentRoom(city.getOutside());
                city.updateOutsideInfo();
            }
        } else if ((player.getCenterX() > 66.6) && (player.getCenterX() < 200)) {
            if ((player.getCenterY() > 133.3) && (player.getCenterY() < 266.6)) {
                city.setCurrentRoom(city.getCoalPowerPlant());
            } else {
                city.setCurrentRoom(city.getOutside());
                city.updateOutsideInfo();
            }
        } else {
            city.setCurrentRoom(city.getOutside());
            city.updateOutsideInfo();
        }
    }
}
