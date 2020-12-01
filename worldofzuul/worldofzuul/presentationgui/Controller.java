package worldofzuul.presentationgui;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import worldofzuul.domain.*;

import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;
import java.util.Locale;


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
    private Window window = new Window();
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
        listViewPowerPlants.setItems(StartGUI.getCity().getCurrentPowerPlants());
        pBarEnergy.setProgress(StartGUI.getCity().getEnergy().getTotalProduction()/StartGUI.getCity().getEnergy().getDemand());
        pBarPollution.setProgress(StartGUI.getCity().getPollution().getTotalPollution()/StartGUI.getCity().getPollution().getLimit());
        pIndTurns.setProgress(StartGUI.getCity().getCurrentTurn()/StartGUI.getCity().getMAXTURN());
    }

    public void buyPowerPlant() throws Exception {
        if (StartGUI.getCity().getCurrentRoom().equals(StartGUI.getCity().getCityHall()) || StartGUI.getCity().getCurrentRoom().equals(StartGUI.getCity().getOutside())) {
            window.popUp("buy","You are not in the right location to buy a power plant.");
        } else {
            int result = StartGUI.getCity().buyPowerPlant();
            if (result == 0) {
                window.popUp("buy","You successfully bought a wind farm for "+ numFormat(WindFarm.getPrice()) +" coins.");
            } else if (result == 1) {
                window.popUp("buy","Not enough money. A wind farm costs " + numFormat(WindFarm.getPrice()) + " coins.");
            } else if (result == 2) {
                window.popUp("buy","You successfully bought a nuclear reactor for "+ numFormat(NuclearReactor.getPrice())+" coins.");
            } else if (result == 3) {
                window.popUp("buy","Not enough money. A nuclear reactor costs " + numFormat(NuclearReactor.getPrice()) + " coins.");
            } else if (result == 4) {
                window.popUp("buy","You successfully bought a coal power plant for " + numFormat(CoalPowerPlant.getPrice()) + " coins");
            } else if (result == 5) {
                window.popUp("buy","Not enough money. A coal power plant costs " + numFormat(CoalPowerPlant.getPrice()) + " coins.");
            } else {
                window.popUp("buy","I don't know what you did to get here.");
            }
        }
        updateStats();
    }
    public void sellPowerPlant() {
        StartGUI.getCity().sellPowerPlant(listViewPowerPlants.getSelectionModel().getSelectedIndex());
        updateStats();
    }
    public void upgradePowerPlant() throws Exception{
        int result = StartGUI.getCity().upgradePowerPlant(listViewPowerPlants.getSelectionModel().getSelectedIndex());
        updateStats();
        if (result == 0) {
            window.popUp("Upgrade","Upgraded 1 power plant.");
        } else if (result == 1) {
            window.popUp("Upgrade","Can't upgrade. The power plant is at max level.");
        } else if (result == 2) {
            window.popUp("Upgrade", "You don't have enough money. Upgrading this power plant costs " + numFormat(StartGUI.getCity().getCurrentPowerPlants().get(listViewPowerPlants.getSelectionModel().getSelectedIndex()).getUpgradePrice()) + " coins");
        }
    }

    public void nextTurn() throws Exception{
        int outcome = StartGUI.getCity().nextTurn();

        if (outcome == 1) {
            window.popUp("Game over", "You polluted too much.");
        } else if (outcome == 2) {
            window.popUp("Game over","You are bankrupt.");
        } else if (outcome == 3) {
            window.popUp("Game over","You reached the end. Congratulations!");
        }
        updateStats();
    }
    public String numFormat(long num) {
        return String.format(Locale.US, "%,d", num);
    }
}

