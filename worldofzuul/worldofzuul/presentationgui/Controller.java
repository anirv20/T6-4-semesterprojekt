package worldofzuul.presentationgui;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import worldofzuul.domain.*;

import java.net.URL;
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
        lblBalance.setText(StartGUI.getCity().getEconomy().getBalance() +" coins");
        lblTurnEnergy.setText(StartGUI.getCity().getEnergy().getTotalProduction() +" MW/turn");
        listViewPowerPlants.setItems(StartGUI.getCity().getCurrentPowerPlants());
    }

    public void buyPowerPlant() {
        if (StartGUI.getCity().getCurrentRoom().equals(StartGUI.getCity().getCityHall()) || StartGUI.getCity().getCurrentRoom().equals(StartGUI.getCity().getOutside())) {
            Window.popUp("buy","You are not in the right location to buy a power plant.");
        } else {
            int result = StartGUI.getCity().buyPowerPlant();
            if (result == 0) {
                Window.popUp("buy","You successfully bought a wind farm for "+ WindFarm.getPrice() +" coins.");
            } else if (result == 1){
                Window.popUp("buy","Not enough money. A wind farm costs " + WindFarm.getPrice() + " coins.");
            } else if (result == 2) {
                Window.popUp("buy","You successfully bought a nuclear reactor for "+ NuclearReactor.getPrice()+" coins.");
            } else if (result == 3) {
                Window.popUp("buy","Not enough money. A nuclear reactor costs " + NuclearReactor.getPrice() + " coins.");
            } else if (result == 4) {
                Window.popUp("buy","You successfully bought a coal power plant for " + CoalPowerPlant.getPrice() + " coins");
            } else if (result == 5) {
                Window.popUp("buy","Not enough money. A coal power plant costs " + CoalPowerPlant.getPrice() + " coins.");
            } else {
                Window.popUp("buy","I don't know what you did to get here.");
            }
        }
        updateStats();
    }
    public void sellPowerPlant() {
        StartGUI.getCity().sellPowerPlant(listViewPowerPlants.getSelectionModel().getSelectedIndex()+1);
        updateStats();
    }
    public void upgradePowerPlant() {
        int result = StartGUI.getCity().upgradePowerPlant(listViewPowerPlants.getSelectionModel().getSelectedIndex());
        if (result == 0) {
            Window.popUp("Upgrade","Upgraded 1 power plant.");
        } else if (result == 1) {
            Window.popUp("Upgrade","Can't upgrade. The power plant is at max level.");
        } else if (result == 2) {
            Window.popUp("Upgrade", "You don't have enough money. Upgrading this power plant costs " + StartGUI.getCity().getCurrentPowerPlants().get(listViewPowerPlants.getSelectionModel().getSelectedIndex()).getUpgradePrice() + " coins");
        }
        updateStats();
    }


    public void nextTurn() {
        StartGUI.getCity().nextTurn();
        updateStats();
    }
}
