package worldofzuul.presentationcli;

import worldofzuul.domain.*;

import java.util.List;

public class Start {
    public static void main(String[] args) {
        City city = new City();
        Parser parser = new Parser();

        System.out.println("Buy power plants to produce energy.");
        System.out.println("Produce more energy than your city needs.");
        System.out.println("Avoid pollution.");
        System.out.println();
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
        System.out.println();
        System.out.println(city.getCurrentRoom().getLongDescription());
        System.out.println();

        printStats(city);

        int finished = 0;

        while (finished == 0) {
            city.sumTotalProduction();
            city.sumTurnPollution();
            city.getEnergy().checkDifference();

            Command command = parser.getCommand();

            CommandWord commandWord = command.getCommandWord();

            if(commandWord == CommandWord.UNKNOWN) {
                System.out.println("I don't know what you mean...");
            }
            else if (commandWord == CommandWord.HELP) {
                printHelp(city, parser);
            }
            else if (commandWord == CommandWord.GO) {
                goRoom(command, city);
            }
            else if (commandWord == CommandWord.QUIT) {
                finished = 4;
            }
            else if (commandWord == CommandWord.BUY) {
                if (city.getCurrentRoom().equals(city.getCityHall()) || city.getCurrentRoom().equals(city.getOutside())) {
                    System.out.println("You are not in the right location to buy a power plant.");
                } else {
                    int result = city.buyPowerPlant();
                    if (result == 0) {
                        System.out.println("You successfully bought a wind farm for "+ WindFarm.getPrice() +" coins.");
                    } else if (result == 1){
                        System.out.println("Not enough money. A wind farm costs " + WindFarm.getPrice() + " coins.");
                    } else if (result == 2) {
                        System.out.println("You successfully bought a nuclear reactor for "+ NuclearReactor.getPrice()+" coins.");
                    } else if (result == 3) {
                        System.out.println("Not enough money. A nuclear reactor costs " + NuclearReactor.getPrice() + " coins.");
                    } else if (result == 4) {
                        System.out.println("You successfully bought a coal power plant for " + CoalPowerPlant.getPrice() + " coins");
                    } else if (result == 5) {
                        System.out.println("Not enough money. A coal power plant costs " + CoalPowerPlant.getPrice() + " coins.");
                    } else {
                        System.out.println("I don't know what you did to get here.");
                    }
                }

            }
            else if (commandWord == CommandWord.SHOW) {
                show(command, city);
            }
            else if (commandWord == CommandWord.NEXT) {
                int soldEnergy = city.sellEnergy();
                if (soldEnergy == 0) {
                    System.out.println("You were not producing enough power for the city and lost " + (long) city.getEnergy().getDifference()*1000 + " coins");
                } else if (soldEnergy == 1) {
                    System.out.println("You are selling power and earned " + (long) city.getEnergy().getDifference()*1000 + " coins");
                } else {
                    System.out.println("You produced just enough electricity.");
                }
                finished = city.nextTurn();
                System.out.println("You earned 100,000 coins from taxes.");
                printStats(city);
            }
            else if (commandWord == CommandWord.SELL) {
                List<PowerPlant> sellList = city.getCurrentPowerPlants();
                if (sellList.size() == 0) {
                    System.out.println("You don't have any PowerPlants to sell here");
                } else if (!command.hasSecondWord()) {
                    System.out.println("Choose an index from 1 to " + sellList.size() + " to sell");
                    System.out.println(sellList.toString());
                } else {
                    int sellIndex = Integer.parseInt(command.getSecondWord());
                    boolean success = city.sellPowerPlant(sellIndex-1);
                    if (success) {
                        System.out.println("Sold one power plant (+ " + sellList.get(sellIndex - 1).getValue() + " coins)");
                    } else {
                        System.out.println("Invalid number. Try again.");
                        System.out.println("Choose an index from 1 to " + sellList.size() + " to sell");
                    }
                }
            }
            else if (commandWord == CommandWord.UPGRADE) {
                List<PowerPlant> upgradeList = city.getCurrentPowerPlants();
                if (upgradeList.size() == 0) {
                    System.out.println("You don't have any PowerPlants to upgrade here");
                } else if (!command.hasSecondWord()) {
                    System.out.println("Choose an index from 1 to " + upgradeList.size() + " to upgrade");
                    System.out.println(upgradeList.toString());
                } else {
                    int upgradeIndex = Integer.parseInt(command.getSecondWord()) - 1;
                    int result = city.upgradePowerPlant(upgradeIndex);
                    if (result == 0) {
                       System.out.println("Upgraded 1 power plant");
                    } else if (result == 1) {
                        System.out.println("The power plant is at max level");
                    } else if (result == 2) {
                        System.out.println("You don't have enough money. Upgrading this power plant costs " + upgradeList.get(upgradeIndex).getUpgradePrice() + " coins");
                    } else {
                        System.out.println("Invalid number. Try again.");
                        System.out.println("Choose an index from 1 to " + upgradeList.size() + " to upgrade");
                    }
                }
            }
        }

        if (finished == 1) {
            System.out.println("You polluted too much.");
        } else if (finished == 2) {
            System.out.println("You are bankrupt.");
        } else if (finished == 3) {
            System.out.println("You reached the end. Congratulations!");
        } else if (finished == 4) {
            System.out.println("You quit.");
        }
    }

    private static void goRoom(Command command, City city)
    {
        if(!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        Room nextRoom = city.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            city.setCurrentRoom(nextRoom);
            System.out.println(city.getCurrentRoom().getLongDescription());
            if (city.getCurrentPowerPlants().size() > 0) {
                System.out.println(city.getCurrentPowerPlants().toString());
            }
        }
    }

    private static void printStats(City city) {
        System.out.println("-CITY STATS-");
        System.out.println("Current turn: " + city.getCurrentTurn() + "/" + city.getMAXTURN());
        System.out.println("Current balance: " + city.getEconomy().getBalance() + " coins.");
        System.out.printf("Current energy production/demand: %.2f MW / %.2f MW %n", city.getEnergy().getTotalProduction(), city.getEnergy().getDemand());
        System.out.println("Current pollution/turn: " + city.getPollution().getTurnPollution() + " kgCO2e/turn");
        System.out.printf("Total pollution: %.2f / %.2f kgCO2e (%.2f%%)%n", city.getPollution().getTotalPollution(), city.getPollution().getLimit(), city.getPollution().getPollutionPercent());
    }

    private static void printHelp(City city, Parser parser)
    {
        System.out.println("Your command words are:");
        parser.showCommands();
        System.out.println();
        System.out.println(city.getCurrentRoom().getLongDescription());
        System.out.println();
    }

    public static void show(Command command, City city) {
        if(!command.hasSecondWord()) {
            System.out.println("Show \"power-plants\", \"stats\" or \"balance\"?");
            return;
        }
        if (command.getSecondWord().equals("power-plants")) {
            System.out.println(city.getPowerPlants().toString());
        } else if (command.getSecondWord().equals("balance")) {
            System.out.println(city.getEconomy().getBalance());
        } else if (command.getSecondWord().equals("stats")) {
            printStats(city);
        } else {
            System.out.println("I don't understand");
        }
    }
}
