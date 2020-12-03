package worldofzuul.presentationgui;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import worldofzuul.domain.*;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lblCurrentLocation.setText(StartGUI.getCity().getCurrentRoom().toString());
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
        lblDemand.setText((long)StartGUI.getCity().getEnergy().getDemand() + " MW");
        lblTotalPollution.setText((long)StartGUI.getCity().getPollution().getTotalPollution() + " tonCO2e");
        lblTurnPollution.setText((long)StartGUI.getCity().getPollution().getTurnPollution() + " tonCO2e/turn");
        lblBalance.setText(numFormat(StartGUI.getCity().getEconomy().getBalance()) +" coins");
        lblTurnEnergy.setText((long)StartGUI.getCity().getEnergy().getTotalProduction() +" MW/turn");
        listViewPowerPlants.setItems(StartGUI.getCity().getCurrentPowerPlants());
        pBarEnergy.setProgress(StartGUI.getCity().getEnergy().getTotalProduction()/StartGUI.getCity().getEnergy().getDemand());
        pBarPollution.setProgress(StartGUI.getCity().getPollution().getTotalPollution()/StartGUI.getCity().getPollution().getLimit());
        pIndTurns.setProgress((double)(StartGUI.getCity().getCurrentTurn()-1)/(double)StartGUI.getCity().getMAXTURN());
        txtInfo.setText(StartGUI.getCity().getCurrentRoom().getInfo());
    }

    public void buyPowerPlant() {
        if (StartGUI.getCity().getCurrentRoom().equals(StartGUI.getCity().getCityHall()) || StartGUI.getCity().getCurrentRoom().equals(StartGUI.getCity().getOutside())) {
            Window.popUp("buy","You are not in the right location to buy a power plant.");
        } else {
            int result = StartGUI.getCity().buyPowerPlant();
            updateStats();
            if (result == 0) {
                Window.popUp("buy","You successfully bought a wind farm for "+ numFormat(WindFarm.getPrice()) +" coins.");
            } else if (result == 1){
                Window.popUp("buy","Not enough money. A wind farm costs " + numFormat(WindFarm.getPrice()) + " coins.");
            } else if (result == 2) {
                Window.popUp("buy","You successfully bought a nuclear reactor for "+ numFormat(NuclearReactor.getPrice())+" coins.");
            } else if (result == 3) {
                Window.popUp("buy","Not enough money. A nuclear reactor costs " + numFormat(NuclearReactor.getPrice()) + " coins.");
            } else if (result == 4) {
                Window.popUp("buy","You successfully bought a coal power plant for " + numFormat(CoalPowerPlant.getPrice()) + " coins");
            } else if (result == 5) {
                Window.popUp("buy","Not enough money. A coal power plant costs " + numFormat(CoalPowerPlant.getPrice()) + " coins.");
            }
        }
    }
    public void sellPowerPlant() {
        try {
            long price = StartGUI.getCity().getCurrentPowerPlants().get(listViewPowerPlants.getSelectionModel().getSelectedIndex()).getValue();
            StartGUI.getCity().sellPowerPlant(listViewPowerPlants.getSelectionModel().getSelectedIndex());
            lblBalance.setText(numFormat(StartGUI.getCity().getEconomy().getBalance()) + " coins");
            updateStats();
            Window.popUp("Sell", "You sold 1 power plant and earned " + numFormat(price) + " coins");
        } catch (IndexOutOfBoundsException e) {
            Window.popUp("Sell", "Choose a power plant to sell");
        }
    }

    public void upgradePowerPlant() {
        try {
            long price = StartGUI.getCity().getCurrentPowerPlants().get(listViewPowerPlants.getSelectionModel().getSelectedIndex()).getUpgradePrice();
            int result = StartGUI.getCity().upgradePowerPlant(listViewPowerPlants.getSelectionModel().getSelectedIndex());
            listViewPowerPlants.setItems(null);     //bug fix
            updateStats();
            if (result == 0) {
                Window.popUp("Upgrade","Upgraded 1 power plant. Cost: " + numFormat(price) + " coins");
            } else if (result == 1) {
                Window.popUp("Upgrade","Can't upgrade. The power plant is at max level.");
            } else if (result == 2) {
                Window.popUp("Upgrade", "You don't have enough money. Upgrading this power plant costs " + numFormat(price) + " coins");
            }
        } catch (IndexOutOfBoundsException e) {
            Window.popUp("Upgrade", "Can't upgrade. Select a power plant to upgrade.");
        }

    }

    public void nextTurn() {
        int outcome = StartGUI.getCity().nextTurn();
        updateStats();

        if (outcome != 0) {
            if (outcome == 1) {
                Window.popUp("Game over", "You polluted too much.");
            } else if (outcome == 2) {
                Window.popUp("Game over", "You are bankrupt.");
            } else if (outcome == 3) {
                Window.popUp("Game over", "You reached the end. Congratulations!");
            }
            Stage stage = (Stage) btnNext.getScene().getWindow();
            stage.hide();
        } else {
            int event = StartGUI.getCity().eventManager();
            if (event == 0) {
                Window.popUp("Event", "There has been violent protests around the new nuclear reactor in town. The police had to get involved, and it cost you 250,000 coins.");
            } else if (event == 1) {
                Window.popUp("Event", "The International Atomic Energy Agency is pushing for more nuclear reactors. You have received 500,000 from the UN as an incentive.");
            } else if (event == 10) {
                Window.popUp("Event", "There has been a fire at a coal power plant. You had to put out the fire and rebuild the power plant. Total cost: 600,000 coins");
            } else if (event == 11) {
                Window.popUp("Event", "The organization for advancement of technology, IEEE, has awarded your city a price of 250,000 coins for not having any coal power plants in your city.");
            } else if (event == 20) {
                Window.popUp("Event", "There is not enough wind today, for your wind turbines to produce any energy.");
            } else if (event == 21) {
                Window.popUp("Event", "There has been advancements in the wind technology. Wind farms are now permanently 250,000 coins cheaper.");
            } else if (event == 30) {
                Window.popUp("Event","Global warming has made the climate warmer. The citizens now use more electricity on air condition. The energy demand has increased by 10%.");
            } else if (event == 40) {
                Window.popUp("Event", "Your city's electricity infrastructure has been upgraded. All power plants are now 10% more energy efficient.");
            }
            updateStats();
        }
    }

    public String numFormat(long num) {
        return String.format(Locale.US, "%,d", num);
    }
}

