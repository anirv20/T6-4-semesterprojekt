package worldofzuul;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Game
{
    private Parser parser;
    private Room currentRoom;
    private Economy economy;
    private List<PowerPlant> powerPlants = new ArrayList<>();
    private Room cityHall, outside, nuclearReactor, coalPowerPlant, windFarm;

    public Game() 
    {
        createRooms();
        parser = new Parser();
        economy = new Economy(1000000);
    }


    private void createRooms()
    {
        outside = new Room("outside the main entrance of the City Hall");
        cityHall = new Room("in the City Hall");
        nuclearReactor = new Room("at the nuclear power plant");
        coalPowerPlant = new Room("at the coal power plant");
        windFarm = new Room("at the wind turbines");
        
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
        printWelcome();

                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    private boolean processCommand(Command command) 
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
        return wantToQuit;
    }

    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
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
        }
    }

    private void buyPowerPlant() {
        if (currentRoom.equals(windFarm)) {
            if (economy.getBalance() >= WindFarm.getPrice()) {
                powerPlants.add(new WindFarm());
                economy.removeMoney(WindFarm.getPrice());
            } else {
                System.out.println("Not enough money! A wind farm costs " + WindFarm.getPrice());
            }
        } else if (currentRoom.equals(nuclearReactor)) {
            if (economy.getBalance() >= NuclearReactor.getPrice()) {
                powerPlants.add(new NuclearReactor());
                economy.removeMoney(NuclearReactor.getPrice());
            } else {
                System.out.println("Not enough money! A nuclear reactor costs " + NuclearReactor.getPrice());
            }
        } else if (currentRoom.equals(coalPowerPlant)) {
            if (economy.getBalance() >= CoalPowerPlant.getPrice()) {
                powerPlants.add(new CoalPowerPlant());
                economy.removeMoney(CoalPowerPlant.getPrice());
            } else {
                System.out.println("Not enough money! A coal power plant costs " + CoalPowerPlant.getPrice());
            }
        } else {
            System.out.println("You are not in the right location to buy a power plant");
        }
    }
    public void show(Command command) {
        if(!command.hasSecondWord()) {
            System.out.println("Show what?");
            return;
        }
        if (command.getSecondWord().equals("plants")) {
            System.out.println(powerPlants.toString());
        } else if (command.getSecondWord().equals("balance")) {
            System.out.println(economy.getBalance());
        } else {
            System.out.println("I don't understand");
        }
    }

    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;
        }
    }
}
