package worldofzuul.presentationgui;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import worldofzuul.domain.*;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Button btnBuy;
    @FXML private Button btnSell;
    @FXML private Button btnUpgrade;
    @FXML private Button btnNext;
    @FXML private Label lblCurrentLocation;
    @FXML private Label lblDemand;
    @FXML private Label lblTotalPollution;
    @FXML private Label lblTurnPollution;
    @FXML private Label lblTurnEnergy;
    @FXML private Label lblBalance;
    @FXML private Circle player;
    @FXML private TextArea txtInfo;
    @FXML private ListView<PowerPlant> listViewPowerPlants = new ListView<>();
    @FXML private ProgressIndicator pIndTurns;
    @FXML private ProgressBar pBarEnergy;
    @FXML private ProgressBar pBarPollution;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listViewPowerPlants.setItems(StartGUI.getCity().getPowerPlants());
        lblCurrentLocation.setText(StartGUI.getCity().getCurrentRoom().toString());
        txtInfo.setText(StartGUI.getCity().getCurrentRoom().getInfo());
        updateStats();
    }

    @FXML
    public void movePlayer(KeyEvent event) {
        switch (event.getCode()) {
            case S:
                if (player.getCenterY() < 390) {
                    player.setCenterY(player.getCenterY() + 5);
                }
                break;
            case W:
                if (player.getCenterY() > 0) {
                    player.setCenterY(player.getCenterY() - 5);
                }
                break;
            case A:
                if (player.getCenterX() > -195)
                player.setCenterX(player.getCenterX() - 5);
                break;
            case D:
                if (player.getCenterX() < 195)
                player.setCenterX(player.getCenterX() + 5);
                break;
        }
        StartGUI.roomChecker(player);
        lblCurrentLocation.setText(StartGUI.getCity().getCurrentRoom().toString());
        txtInfo.setText(StartGUI.getCity().getCurrentRoom().getInfo());
        listViewPowerPlants.setItems(StartGUI.getCity().getCurrentPowerPlants());
    }

    public void updateStats() {
        lblDemand.setText(StartGUI.getCity().getEnergy().getDemand() + " MW");
        lblTotalPollution.setText(StartGUI.getCity().getPollution().getTotalPollution() + " kgCO2e");
        lblTurnPollution.setText(StartGUI.getCity().getPollution().getTurnPollution() + " kgCO2e/turn");
        lblBalance.setText(numFormat(StartGUI.getCity().getEconomy().getBalance()) +" coins");
        lblTurnEnergy.setText(StartGUI.getCity().getEnergy().getTotalProduction() +" MW/turn");
    }
    public void buyPowerPlant() {
        StartGUI.getCity().buyPowerPlant();
        updateStats();
    }
    public void sellPowerPlant() {
        StartGUI.getCity().sellPowerPlant();
        updateStats();
    }
    public void upgradePowerPlant() {
        StartGUI.getCity().upgradePowerPlant();
        updateStats();
    }
    public String numFormat(long num){
        return String.format(Locale.US,"%,d", num);
    }
    public String numFormat(double num){
        return String.format(Locale.US,"%,d", num);
    }
}
