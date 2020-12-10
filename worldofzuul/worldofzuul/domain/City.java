package worldofzuul.domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class City
{
    private Room currentRoom;
    private Economy economy;
    private Energy energy;
    private Pollution pollution;
    private ObservableList<PowerPlant> powerPlants = FXCollections.observableArrayList();
    private Room cityHall, outside, nuclearReactor, coalPowerPlant, windFarm;
    private int currentTurn = 1;
    private final int MAXTURN = 30;
    private boolean includeWind = true;

    public City()
    {
        powerPlants.add(new CoalPowerPlant());
        economy = new Economy(1000000);
        energy = new Energy();
        pollution = new Pollution();
        createRooms();

        sumTurnPollution();
        sumTotalProduction();
        energy.checkDifference();
    }


    private void createRooms()
    {
        outside = new Room("outside the main entrance of the City Hall", "Outside");
        cityHall = new Room("in the City Hall", "City Hall");
        nuclearReactor = new Room("at the nuclear power plant. A nuclear reactor costs 3,100,000 coins " +
                "and produces 1000 MW. The pollution is 12 kgCO2e/turn", "Nuclear Reactor");
        coalPowerPlant = new Room("at the coal power plant. A coal power plant costs 450,000 coins " +
                "and produces 600 MW. The pollution is 492 kgCO2e/turn", "Coal Power Plant");
        windFarm = new Room("at the wind farms. A wind farm consists of 100 wind turbines, costs 1,240,000 coins " +
                "and produces 400 MW. The pollution is 4 kgCO2e/turn", "Wind Farm");

        cityHall.setExit("outside", outside);
        cityHall.setInfo("Buy power plants to produce enough energy for the city. " +
                "You earn money buy producing more power than your city needs." +
                "You also make 100,000 coins after each turn in taxes." +
                "Watch out for pollution. It is necessary to invest in green energy.");

        outside.setExit("nuclear", nuclearReactor);
        outside.setExit("coal", coalPowerPlant);
        outside.setExit("wind", windFarm);
        outside.setExit("cityhall", cityHall);
        updateOutsideInfo();

        coalPowerPlant.setExit("outside", outside);
        coalPowerPlant.setInfo("This is a coal power plant. A coal power plant costs " + CoalPowerPlant.getPrice() + " coins " +
                "and produces 600 MW. The pollution is 492 kgCO2e/turn \n" +
                "Coal power plants are huge structures usually build in industrial districts. " +
                "They use coal to produce heat, the heat is used to boil water and the steam from " +
                "the water is used to drive generators. The environmental impact from burning coal " +
                "is seen in the form of acid rain, smog and of course the devastating amounts of CO2 " +
                "emissions. They are however consistent in their production of electricity and generate " +
                "lots of it very cheaply");

        nuclearReactor.setExit("outside", outside);
        nuclearReactor.setInfo("This is a nuclear reactor. A nuclear reactor costs " + NuclearReactor.getPrice() + " coins " +
                "and produces 6,100 MW. The pollution is 12 kgCO2e/turn \n" +
                "Nuclear power plants split tiny atoms to produce heat. This creates some amount of radiation, " +
                "that for the most part can be contained within the reactor itself. The heat is used to boil water " +
                "and the steam from the water is used to drive generators. They are usually perceived as dangerous, " +
                "because of accidents like Chernobyl and Fukushima, but the reality is that modern reactors have not " +
                "killed anyone. Technically they are not a renewable source of energy, as they use uranium as fuel, " +
                "but it is estimated that we have a couple hundred years of supply.");

        windFarm.setExit("outside", outside);
        windFarm.setInfo("This is a wind farm consisting of 100 wind turbines. A wind farm costs " + WindFarm.getPrice() +
                " coins and produces 400 MW. The pollution is 4 kgCO2e/turn \n" +
                "Wind farms consist of 100 wind turbines placed on either land or in the ocean. " +
                "When the wind hits the giant blades, the wind turbine drives a generator that creates " +
                "electricity. On the ocean these wind turbines can be several hundred meters tall. Because " +
                "the wind is not consistent, the electricity output varies. Their environmental impact is minimal, " +
                "only emitting CO2 during production.");

        currentRoom = cityHall;
    }

    public int buyPowerPlant() {
        //Checks which room you are in, and buys the corresponding power plant if you have enough money
        if (currentRoom.equals(windFarm)) {
            if (economy.getBalance() >= WindFarm.getPrice()) {
                PowerPlant powerPlant = new WindFarm();
                powerPlants.add(powerPlant);
                economy.removeMoney(WindFarm.getPrice());
                sumTurnPollution();
                sumTotalProduction();
                return 0;
            } else {
                return 1;
            }
        } else if (currentRoom.equals(nuclearReactor)) {
            if (economy.getBalance() >= NuclearReactor.getPrice()) {
                PowerPlant powerPlant = new NuclearReactor();
                powerPlants.add(powerPlant);
                economy.removeMoney(NuclearReactor.getPrice());
                sumTurnPollution();
                sumTotalProduction();
                return 2;
            } else {
                return 3;
            }
        } else if (currentRoom.equals(coalPowerPlant)) {
            if (economy.getBalance() >= CoalPowerPlant.getPrice()) {
                PowerPlant powerPlant = new CoalPowerPlant();
                powerPlants.add(powerPlant);
                economy.removeMoney(CoalPowerPlant.getPrice());
                sumTurnPollution();
                sumTotalProduction();

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
        ObservableList<PowerPlant> sellList = getCurrentPowerPlants();
        try {
            powerPlants.remove(sellList.get(sellIndex));
            economy.addMoney(sellList.get(sellIndex).getValue());
            sumTurnPollution();
            sumTotalProduction();
            return true;
        } catch(IndexOutOfBoundsException e) {
            return false;
        }
    }

    public int upgradePowerPlant(int upgradeIndex) {
        //Makes an arraylist of upgradeable power plants in the room and lets you choose which power plant to upgrade.
        ObservableList<PowerPlant> upgradeList = getCurrentPowerPlants();
        PowerPlant p = upgradeList.get(upgradeIndex);
        try {
            if (economy.getBalance() >= p.getUpgradePrice()) {
                boolean success = p.upgrade();
                if (success) {
                    long price = p.getUpgradePrice();
                    economy.removeMoney(price);
                    sumTurnPollution();
                    sumTotalProduction();
                    p.setValue(p.getValue() + price);
                    p.setUpgradePrice((long)(p.getValue()*0.8));
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
            if (!(p instanceof WindFarm)){
                energy.setTotalProduction(energy.getTotalProduction() + p.getEnergyProduction());
            } else if (includeWind) {
                energy.setTotalProduction(energy.getTotalProduction() + p.getEnergyProduction());
            }
        }
    }

    public void sumTurnPollution() {
        // Calculates the total pollution per turn of all currently owned power plants.
        pollution.setTurnPollution(0);
        for (PowerPlant p : powerPlants) {
            pollution.setTurnPollution(pollution.getTurnPollution() + p.getPollution());
        }
    }

    public int updateEconomy() {
        energy.checkDifference();
        economy.addMoney(100000); //Adds money from taxes
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

            pollution.setTotalPollution(pollution.getTotalPollution() + pollution.getTurnPollution());

            energy.setDemand(energy.getDemand()*1.1);

            includeWind = true;
            sumTotalProduction();
            if (pollution.getTotalPollution() >= pollution.LIMIT) {
                 return 1;
                 //too much pollution
            }
            if (economy.getBalance() < 0) {
                return 2;
                //bankrupt
            }
        }
        else {
            return 3;
            //you won
        }
        return 0;
        //continue
    }

    public int eventManager() {
        if (currentTurn > 4) {
            int eventHappens = (int)(Math.random() * 10);
            if (eventHappens >= 7) {
                int whichEvent = (int)(Math.random()*10);

                if (whichEvent == 0){
                    boolean hasNuclear = false;
                    for (PowerPlant p : powerPlants) {
                        if (p instanceof NuclearReactor) {
                            hasNuclear = true;
                            break;
                        }
                    }
                    if (hasNuclear) {
                        economy.removeMoney(250000, true);
                        return 0;
                        // "There has been violent protests around the new nuclear reactor in town.
                        // The police had to get involved, and it cost you 250,000 coins."
                    } else {
                        economy.addMoney(500000);
                        return 1;
                        // "The International Atomic Energy Agency is pushing for more nuclear reactors.
                        // You have received 500,000 from the UN as an incentive."
                    }
                }
                else if (whichEvent == 1) {
                    boolean hasCoal = false;
                    for (PowerPlant p : powerPlants) {
                        if (p instanceof CoalPowerPlant) {
                            hasCoal = true;
                            break;
                        }
                    }
                    if (hasCoal) {
                        economy.removeMoney(600000, true);
                        return 10;

                        // "There has been a fire at a coal power plant.
                        // You had to put out the fire and rebuild the power plant. Total cost: 600,000 coins"
                    } else {
                        economy.addMoney(250000);
                        return 11;
                        // "The organization for advancement of technology, IEEE, has awarded your city a price of
                        // 250000 coins for not having any coal power plants in your city."
                    }
                } else if (whichEvent == 2) {
                    int windFarmsAmount = 0;
                    double windEnergy = 0;
                    for (PowerPlant p : powerPlants) {
                        if (p instanceof WindFarm) {
                            windFarmsAmount++;
                            windEnergy += p.getEnergyProduction();
                        }
                    }
                    if (windFarmsAmount != 0 && includeWind) {
                        energy.setTotalProduction(energy.getTotalProduction()-windEnergy);
                        includeWind = false;
                        return 20;
                        //"There is not enough wind today, for your wind turbines to produce any energy."
                    } else {
                        WindFarm.setPrice(WindFarm.getPrice()-250000);
                        createRooms();
                        //"There has been advancements in the wind technology. Wind farms are now permanently 250,000 coins cheaper."
                        return 21;
                    }
                } else if (whichEvent == 3) {
                    energy.setDemand(energy.getDemand()*1.1);
                    return 30;
                    //"Global warming has made the climate warmer. The citizens now use more electricity on air condition.
                    //The energy demand has increased by 10%"
                } else if (whichEvent == 4) {
                    PowerPlant.setBonus(PowerPlant.getBonus()+0.1);
                    for (PowerPlant p : powerPlants) {
                        p.setEnergyProduction(p.getEnergyProduction()*PowerPlant.getBonus());
                    }
                    sumTotalProduction();
                    //"Your city's electricity infrastructure has been upgraded.
                    //All power plants are now 10% more energy efficient."
                    return 40;
                } else {
                    return 100;
                }
            } else {
                return 100;
            }
        }
        return 100;
        //No event
    }

    public ObservableList<PowerPlant> getCurrentPowerPlants() {
        //creates a list of power plants the player can sell/upgrade at the current location
        ObservableList<PowerPlant> currentPowerPlants = FXCollections.observableArrayList();
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
        } else if (currentRoom.equals(cityHall)) {
            for (PowerPlant p : powerPlants) {
                currentPowerPlants.add(p);
            }
        }
        return currentPowerPlants;
    }

    public void updateOutsideInfo() {
        if (pollution.getTurnPollution() < 492) {
            outside.setInfo("Ah - fresh air!");
        } else if (pollution.getTurnPollution() < 492 * 2) {
            outside.setInfo("The air isn't as fresh as it used to be around here.");
        } else {
            outside.setInfo("I'm having trouble breathing!");
        }
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

    public ObservableList<PowerPlant> getPowerPlants() {
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
