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
        Parent root = FXMLLoader.load(getClass().getResource("Presentation.fxml"));// filen

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.requestFocus();
        // et vindue i starten
        Window.popUp("Welcome!", "Welcome to World of Energy! You are the mayor of this lovely city and you have to make sure you produce enough" +
                "electricity for your citizens! Due to global warming, we have to try other power plants than the coal power plant we currently have." +
                "If we get enough money, we can invest in wind farms and nuclear reactors! You make money each turn. 100,000 coins from taxes and " +
                "some money from selling energy. Press the \"?\"-icon next to balance to get more information. The energy demand will increase each turn" +
                "so make sure you are producing plenty of energy. If you make it to 30 turns you win! If you need help, try pressing the \"?\"-icons. Have fun!");
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static City getCity() {
        return city;
    }

    public static void roomChecker(Circle player){
        //som hele tiden holder spilleren opdateret på hvor de og dermed kuglen befinder sig på mappet.
        // Metoden anvender x og y koordinaterne, som repræsenterer mappet’s bredde og længde, for at finde
        // kuglens lokation.
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
