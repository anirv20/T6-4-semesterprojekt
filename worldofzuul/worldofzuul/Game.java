package worldofzuul;

import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game
{
    private Parser parser;
    private Room currentRoom;
    private Economy economy;
    private Energy energy;
    private Pollution pollution;
    private UserInterface ui;
    private List<PowerPlant> powerPlants = new ArrayList<>();
    private Room cityHall, outside, nuclearReactor, coalPowerPlant, windFarm;
    private int currentTurn = 1;
    private final int MAXTURN = 30;
    private boolean finished = false;

    //Constructor of the Game object
    public Game() 
    {
        createRooms();
        powerPlants.add(new CoalPowerPlant());
        parser = new Parser();
        economy = new Economy(1000000);
        energy = new Energy();
        pollution = new Pollution();
        ui = new UserInterface();

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

    public void play() 
    {
        printWelcome(); //Beginning of the game

        //boolean finished = false;

        while (!this.finished) {
            sumTotalProduction();
            sumTurnPollution();
            energy.checkDifference();

            Command command = parser.getCommand();

            if (processCommand(command)) { //if (processCommand(command) == true || quit()== true)
                this.finished = true;
            }
        }
        //System.out.println("Thank you for playing.  Good bye.");
    }

    private void printWelcome()
    {
        ui.printWelcome(currentRoom, parser);
        ui.printStats(currentTurn, MAXTURN, economy, energy, pollution);
    }

    private boolean processCommand(Command command) //Interprets the commands from the user
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        if(commandWord == CommandWord.UNKNOWN) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        if (commandWord == CommandWord.HELP) {
            printHelp();
        }
        else if (commandWord == CommandWord.GO) {
            goRoom(command);
        }
        else if (commandWord == CommandWord.QUIT) {
            wantToQuit = quit(command);
        }
        else if (commandWord == CommandWord.BUY) {
            buyPowerPlant();
        }
        else if (commandWord == CommandWord.SHOW) {
            show(command);
        }
        else if (commandWord == CommandWord.NEXT) {
            nextTurn();
        }
        else if (commandWord == CommandWord.SELL) {
            sellPowerPlant(command);
        }
        else if (commandWord == CommandWord.UPGRADE) {
            upgradePowerPlant(command);
        }
        return wantToQuit;
    }

    private void printHelp() 
    {
        System.out.println("Your command words are:");
        parser.showCommands();
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        System.out.println();
    }

    private void goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
            if (getCurrentPowerPlants().size() > 0) {
                System.out.println(getCurrentPowerPlants().toString());
            }
        }
    }

    private void buyPowerPlant() {
        //Checks which room you are in, and buys the corresponding power plant if you have enough money
        if (currentRoom.equals(windFarm)) {
            if (economy.getBalance() >= WindFarm.getPrice()) {
                powerPlants.add(new WindFarm());
                economy.removeMoney(WindFarm.getPrice());
                System.out.println("You have bought a wind farm.");
            } else {
                System.out.println("Not enough money! A wind farm costs " + WindFarm.getPrice());
            }
        } else if (currentRoom.equals(nuclearReactor)) {
            if (economy.getBalance() >= NuclearReactor.getPrice()) {
                powerPlants.add(new NuclearReactor());
                economy.removeMoney(NuclearReactor.getPrice());
                System.out.println("You have bought a nuclear reactor.");
            } else {
                System.out.println("Not enough money! A nuclear reactor costs " + NuclearReactor.getPrice());
            }
        } else if (currentRoom.equals(coalPowerPlant)) {
            if (economy.getBalance() >= CoalPowerPlant.getPrice()) {
                powerPlants.add(new CoalPowerPlant());
                economy.removeMoney(CoalPowerPlant.getPrice());
                System.out.println("You have bought a coal power plant.");
            } else {
                System.out.println("Not enough money! A coal power plant costs " + CoalPowerPlant.getPrice());
            }
        } else {
            System.out.println("You are not in the right location to buy a power plant");
        }
    }

    private void sellPowerPlant(Command command) {
        //Makes an arraylist of sellable power plants in the room and lets you choose which power plant to sell.
        List<PowerPlant> sellList = getCurrentPowerPlants();

        if(sellList.size() == 0) {
            System.out.println("You don't have any PowerPlants to sell here");
        } else if (!command.hasSecondWord()) {
            System.out.println("Choose an index from 1 to " + sellList.size() + " to sell");
            System.out.println(sellList.toString());
        } else {
            int sellIndex = Integer.parseInt(command.getSecondWord());
            powerPlants.remove(sellList.get(sellIndex-1));
            economy.addMoney(sellList.get(sellIndex-1).getValue());
            System.out.println("Sold one power plant (+ " + sellList.get(sellIndex-1).getValue() + " coins)");
        }
    }

    private void upgradePowerPlant(Command command) {
        //Makes an arraylist of upgradeable power plants in the room and lets you choose which power plant to upgrade.
        List<PowerPlant> upgradeList = getCurrentPowerPlants();

        if(upgradeList.size() == 0) {
            System.out.println("You don't have any PowerPlants to upgrade here");
        } else if (!command.hasSecondWord()) {
            System.out.println("Choose an index from 1 to " + upgradeList.size() + " to upgrade");
            System.out.println(upgradeList.toString());
        } else {
            int upgradeIndex = Integer.parseInt(command.getSecondWord()) - 1;
            if (economy.getBalance() >= upgradeList.get(upgradeIndex).getUpgradePrice()) {
                boolean success = upgradeList.get(upgradeIndex).upgrade();
                if (success) {
                    economy.removeMoney(upgradeList.get(upgradeIndex).getUpgradePrice());
                    System.out.println("Upgraded 1 power plant");
                } else {
                    System.out.println("The power plant is at max level");
                }
            } else {
                System.out.println("You don't have enough money. Upgrading this power plant costs " + upgradeList.get(upgradeIndex).getUpgradePrice() + " coins");
            }
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

    public void nextTurn() { //Goes to next turn and updates stats. Checks for lose conditions.
        if (currentTurn < MAXTURN) {
            currentTurn++;
            sumTotalProduction();
            sumTurnPollution();
            energy.checkDifference();
            pollution.setTotalPollution(pollution.getTotalPollution() + pollution.getTurnPollution());

            economy.addMoney(100000); //Adds money from taxes

            energy.setDemand(energy.getDemand()*1.1);
            if (energy.getDifference() < 0) {
                if (!economy.removeMoney(Math.abs((long)energy.getDifference()*1000))){
                    lose(1);
                }
                System.out.println("You were not producing enough power for the city and lost " + (long)energy.getDifference()*1000 + " coins");
            }
            else if (energy.getDifference() > 0) {
                economy.addMoney(Math.abs((long)energy.getDifference()*1000));
                System.out.println("You are selling power and earned " + (long)energy.getDifference()*1000 + " coins");
            }

            if (pollution.getTotalPollution() >= pollution.LIMIT) {
                lose(0);
                //System.out.println("You polluted too much");
            }
            if (economy.getBalance() < 0) {
                /*finished = true;
                System.out.println("You are bankrupt.");
                */
                lose(1);
            }
            if (!finished) {
                System.out.println("You earned 100000 coins from taxes.");
                ui.printStats(currentTurn, MAXTURN, economy, energy, pollution);
            }
        } else {
            win();
        }
    }

    public void show(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Show \"power-plants\" or \"balance\"?");
            return;
        }
        if (command.getSecondWord().equals("power-plants")) {
            System.out.println(powerPlants.toString());
        } else if (command.getSecondWord().equals("balance")) {
            System.out.println(economy.getBalance());
        } else if (command.getSecondWord().equals("stats")) {
            ui.printStats(currentTurn, MAXTURN, economy, energy, pollution);
        } else {
            System.out.println("I don't understand");
        }
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
    private void lose(int reason) {
        System.out.println("You loose");
        String[] reasons = {"You polluted too much."
                , "You are bankrupt. Current balance :" + economy.getBalance() };
        System.out.println(reasons[reason]);
        //ui.printStats(currentTurn, MAXTURN, economy, energy, pollution);
        quit();
    }
    private void win() {
        System.out.println("You win.");
        //ui.printStats(currentTurn, MAXTURN, economy, energy, pollution);
        quit();
    }

    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return quit();
        }
    }
    private boolean quit() {
        System.out.println("Thank you for playing Good bye.");
        this.finished = true;
        return true;
        }
}
