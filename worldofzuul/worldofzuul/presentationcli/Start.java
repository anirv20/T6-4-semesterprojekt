package worldofzuul.presentationcli;

import worldofzuul.domain.*;

import java.util.List;

public class Start {
    public static void main(String[] args) {
        Game game = new Game();

        System.out.println("Buy power plants to produce energy.");
        System.out.println("Produce more energy than your city needs.");
        System.out.println("Avoid pollution.");
        System.out.println();
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println("Your command words are:");
        game.getParser().showCommands();
        System.out.println();
        System.out.println(game.getCurrentRoom().getLongDescription());
        System.out.println();

        printStats(game);

        int finished = 0;

        while (finished == 0) {
            game.sumTotalProduction();
            game.sumTurnPollution();
            game.getEnergy().checkDifference();

            Command command = game.getParser().getCommand();

            CommandWord commandWord = command.getCommandWord();

            if(commandWord == CommandWord.UNKNOWN) {
                System.out.println("I don't know what you mean...");
            }
            else if (commandWord == CommandWord.HELP) {
                printHelp(game);
            }
            else if (commandWord == CommandWord.GO) {
                goRoom(command, game);
            }
            else if (commandWord == CommandWord.QUIT) {
                finished = 4;
            }
            else if (commandWord == CommandWord.BUY) {
                if (game.getCurrentRoom().equals(game.getCityHall()) || game.getCurrentRoom().equals(game.getOutside())) {
                    System.out.println("You are not in the right location to buy a power plant.");
                } else {
                    int result = game.buyPowerPlant();
                    if (result == 0) {
                        System.out.println("You successfully bought a wind farm for "+ WindFarm.getPrice() +" coins.");
                    } else if (result == 1){
                        System.out.println("A wind farm costs " + WindFarm.getPrice() + " coins.");
                    } else if (result == 2) {
                        System.out.println("You successfully bought a nuclear reactor for "+ NuclearReactor.getPrice()+" coins.");
                    } else if (result == 3) {
                        System.out.println("A nuclear reactor costs " + NuclearReactor.getPrice() + " coins.");
                    } else if (result == 4) {
                        System.out.println("You successfully bought a coal power plant for " + CoalPowerPlant.getPrice() + " coins");
                    } else if (result == 5) {
                        System.out.println("A wind farm costs " + CoalPowerPlant.getPrice() + " coins.");
                    } else {
                        System.out.println("I don't know what you did to get here.");
                    }
                }

            }
            else if (commandWord == CommandWord.SHOW) {
                show(command, game);
            }
            else if (commandWord == CommandWord.NEXT) {
                int soldEnergy = game.sellEnergy();
                if (soldEnergy == 0) {
                    System.out.println("You were not producing enough power for the city and lost " + (long)game.getEnergy().getDifference()*1000 + " coins");
                } else if (soldEnergy == 1) {
                    System.out.println("You are selling power and earned " + (long)game.getEnergy().getDifference()*1000 + " coins");
                } else {
                    System.out.println("You produced just enough electricity.");
                }
                finished = game.nextTurn();
                System.out.println("You earned 100,000 coins from taxes.");
                printStats(game);
            }
            else if (commandWord == CommandWord.SELL) {
                List<PowerPlant> sellList = game.getCurrentPowerPlants();
                if (sellList.size() == 0) {
                    System.out.println("You don't have any PowerPlants to sell here");
                } else if (!command.hasSecondWord()) {
                    System.out.println("Choose an index from 1 to " + sellList.size() + " to sell");
                    System.out.println(sellList.toString());
                } else {
                    int sellIndex = Integer.parseInt(command.getSecondWord());
                    boolean success = game.sellPowerPlant(sellIndex);
                    if (success == true) {
                        System.out.println("Sold one power plant (+ " + sellList.get(sellIndex - 1).getValue() + " coins)");
                    } else {
                        System.out.println("Invalid number. Try again.");
                        System.out.println("Choose an index from 1 to " + sellList.size() + " to sell");
                    }
                }
            }
            else if (commandWord == CommandWord.UPGRADE) {
                List<PowerPlant> upgradeList = game.getCurrentPowerPlants();
                if (upgradeList.size() == 0) {
                    System.out.println("You don't have any PowerPlants to upgrade here");
                } else if (!command.hasSecondWord()) {
                    System.out.println("Choose an index from 1 to " + upgradeList.size() + " to upgrade");
                    System.out.println(upgradeList.toString());
                } else {
                    int upgradeIndex = Integer.parseInt(command.getSecondWord()) - 1;
                    int result = game.upgradePowerPlant(upgradeIndex);
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

    private static void goRoom(Command command, Game game)
    {
        if(!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        Room nextRoom = game.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            game.setCurrentRoom(nextRoom);
            System.out.println(game.getCurrentRoom().getLongDescription());
            if (game.getCurrentPowerPlants().size() > 0) {
                System.out.println(game.getCurrentPowerPlants().toString());
            }
        }
    }

    private static void printStats(Game game) {
        System.out.println("-CITY STATS-");
        System.out.println("Current turn: " + game.getCurrentTurn() + "/" + game.getMAXTURN());
        System.out.println("Current balance: " + game.getEconomy().getBalance() + " coins.");
        System.out.printf("Current energy production/demand: %.2f MW / %.2f MW %n", game.getEnergy().getTotalProduction(), game.getEnergy().getDemand());
        System.out.println("Current pollution/turn: " + game.getPollution().getTurnPollution() + " kgCO2e/turn");
        System.out.printf("Total pollution: %.2f / %.2f kgCO2e (%.2f%%)%n", game.getPollution().getTotalPollution(), game.getPollution().getLimit(), game.getPollution().getPollutionPercent());
    }

    private static void printHelp(Game game)
    {
        System.out.println("Your command words are:");
        game.getParser().showCommands();
        System.out.println();
        System.out.println(game.getCurrentRoom().getLongDescription());
        System.out.println();
    }

    public static void show(Command command, Game game) {
        if(!command.hasSecondWord()) {
            System.out.println("Show \"power-plants\" or \"balance\"?");
            return;
        }
        if (command.getSecondWord().equals("power-plants")) {
            System.out.println(game.getPowerPlants().toString());
        } else if (command.getSecondWord().equals("balance")) {
            System.out.println(game.getEconomy().getBalance());
        } else if (command.getSecondWord().equals("stats")) {
            printStats(game);
        } else {
            System.out.println("I don't understand");
        }
    }
}
