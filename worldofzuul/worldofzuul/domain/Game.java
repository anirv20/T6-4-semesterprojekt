package worldofzuul.domain;

import worldofzuul.presentationcli.Parser;

import java.util.ArrayList;
import java.util.List;

public class Game
{
    private Parser parser;
    private Room currentRoom;
    private Economy economy;
    private Energy energy;
    private Pollution pollution;
    private List<PowerPlant> powerPlants = new ArrayList<>();
    private Room cityHall, outside, nuclearReactor, coalPowerPlant, windFarm;
    private int currentTurn = 1;
    private final int MAXTURN = 30;

    public Game() 
    {
        createRooms();
        powerPlants.add(new CoalPowerPlant());
        parser = new Parser();
        economy = new Economy(1000000);
        energy = new Energy();
        pollution = new Pollution();

        sumTotalProduction();
        sumTurnPollution();
        energy.checkDifference();
    }


    private void createRooms()
    {
        outside = new Room("outside the main entrance of the City Hall");
        cityHall = new Room("in the City Hall");
        nuclearReactor = new Room("at the nuclear power plant. A nuclear reactor costs 3100000 coins " +
                "and produces 1000 MW. The pollution is 12000 kgCO2e/turn");
        coalPowerPlant = new Room("at the coal power plant. A coal power plant costs 450000 coins " +
                "and produces 600 MW. The pollution is 492000 kgCO2e/turn");
        windFarm = new Room("at the wind farms. A wind farm consists of 100 wind turbines, costs 1240000 coins " +
                "and produces 400 MW. The pollution is 4400 kgCO2e/turn");
        
        cityHall.setExit("south", outside);

        outside.setExit("west", nuclearReactor);
        outside.setExit("east", coalPowerPlant);
        outside.setExit("south", windFarm);
        outside.setExit("north", cityHall);

        coalPowerPlant.setExit("west", outside);

        nuclearReactor.setExit("east", outside);

        windFarm.setExit("north", outside);

        currentRoom = cityHall;
    }

    public int buyPowerPlant() {
        //Checks which room you are in, and buys the corresponding power plant if you have enough money
        if (currentRoom.equals(windFarm)) {
            if (economy.getBalance() >= WindFarm.getPrice()) {
                PowerPlant powerPlant = new WindFarm();
                powerPlants.add(powerPlant);
                economy.removeMoney(WindFarm.getPrice());
                return 0;
            } else {
                return 1;
            }
        } else if (currentRoom.equals(nuclearReactor)) {
            if (economy.getBalance() >= NuclearReactor.getPrice()) {
                PowerPlant powerPlant = new NuclearReactor();
                powerPlants.add(powerPlant);
                economy.removeMoney(NuclearReactor.getPrice());
                return 2;
            } else {
                return 3;
            }
        } else if (currentRoom.equals(coalPowerPlant)) {
            if (economy.getBalance() >= CoalPowerPlant.getPrice()) {
                PowerPlant powerPlant = new CoalPowerPlant();
                powerPlants.add(powerPlant);
                economy.removeMoney(CoalPowerPlant.getPrice());
                return 4;
            } else {
                return 5;
            }
        } else {
            return 6;
        }
    }

    public boolean sellPowerPlant(int sellIndex) {
        //Makes an arraylist of sellable power plants in the room and lets you choose which power plant to sell.
        List<PowerPlant> sellList = getCurrentPowerPlants();
        try {
            powerPlants.remove(sellList.get(sellIndex-1));
            economy.addMoney(sellList.get(sellIndex-1).getValue());
            return true;
        } catch(IndexOutOfBoundsException e) {
            return false;
        }
    }

    public int upgradePowerPlant(int upgradeIndex) {
        //Makes an arraylist of upgradeable power plants in the room and lets you choose which power plant to upgrade.
        List<PowerPlant> upgradeList = getCurrentPowerPlants();
        try {
            if (economy.getBalance() >= upgradeList.get(upgradeIndex).getUpgradePrice()) {
                boolean success = upgradeList.get(upgradeIndex).upgrade();
                if (success) {
                    economy.removeMoney(upgradeList.get(upgradeIndex).getUpgradePrice());
                    return 0;
                    //success
                } else {
                    return 1;
                    //failed to upgrade - max level
                }
            } else {
                return 2;
                //failed to upgrade - not enough money
            }
        } catch(IndexOutOfBoundsException e) {
            return 3;
            //failed to upgrade - invalid index
        }
    }

    public void sumTotalProduction() {
        // Calculates the total production of all currently owned power plants
        energy.setTotalProduction(0);
        for (PowerPlant p : powerPlants) {
            energy.setTotalProduction(energy.getTotalProduction() + p.getEnergyProduction());
        }
    }

    public void sumTurnPollution() {
        // Calculates the total pollution per turn of all currently owned power plants.
        pollution.setTurnPollution(0);
        for (PowerPlant p : powerPlants) {
            pollution.setTurnPollution(pollution.getTurnPollution() + p.getPollution());
        }
    }

    public int sellEnergy() {
        if (energy.getDifference() < 0) {
            economy.removeMoney(Math.abs((long)energy.getDifference()*1000), true);
            return 0;
        }
        else if (energy.getDifference() > 0) {
            economy.addMoney(Math.abs((long)energy.getDifference()*1000));
            return 1;
        } else {
            return 2;
        }
    }

    public int nextTurn() { //Goes to next turn and updates stats. Checks for lose conditions.
        if (currentTurn < MAXTURN) {
            currentTurn++;
            sumTotalProduction();
            sumTurnPollution();
            energy.checkDifference();
            pollution.setTotalPollution(pollution.getTotalPollution() + pollution.getTurnPollution());

            economy.addMoney(100000); //Adds money from taxes

            energy.setDemand(energy.getDemand()*1.1);

            if (pollution.getTotalPollution() >= pollution.LIMIT) {
                 return 1;
                //System.out.println("You polluted too much");
            }
            if (economy.getBalance() < 0) {
                /*finished = true;
                System.out.println("You are bankrupt.");
                */
                return 2;
            }
        }
        else {
            return 3;
        }
        return 0;
    }

    public List<PowerPlant> getCurrentPowerPlants() {
        //creates a list of power plants the player can sell/upgrade at the current location
        List<PowerPlant> currentPowerPlants = new ArrayList<>();
        if (currentRoom.equals(windFarm)) {
            //iterates through all the power plants and adds the power plants at the location to the list
            for (PowerPlant p : powerPlants) {
                if (p instanceof WindFarm) {
                    currentPowerPlants.add(p);
                }
            }
        } else if (currentRoom.equals(coalPowerPlant)) {
            for (PowerPlant p : powerPlants) {
                if (p instanceof CoalPowerPlant) {
                    currentPowerPlants.add(p);
                }
            }
        } else if (currentRoom.equals(nuclearReactor)) {
            for (PowerPlant p : powerPlants) {
                if (p instanceof NuclearReactor) {
                    currentPowerPlants.add(p);
                }
            }
        }
        return currentPowerPlants;
    }

    public Parser getParser() {
        return this.parser;
    }

    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public Energy getEnergy() {
        return this.energy;
    }

    public Pollution getPollution() {
        return this.pollution;
    }

    public List<PowerPlant> getPowerPlants() {
        return this.powerPlants;
    }

    public int getCurrentTurn() {
        return this.currentTurn;
    }
    public int getMAXTURN(){
        return this.MAXTURN;
    }
    public void setCurrentRoom(Room room){
        this.currentRoom = room;
    }

    public Room getCityHall() {
        return cityHall;
    }

    public Room getOutside() {
        return outside;
    }

    public Room getNuclearReactor() {
        return nuclearReactor;
    }

    public Room getCoalPowerPlant() {
        return coalPowerPlant;
    }

    public Room getWindFarm() {
        return windFarm;
    }
}
