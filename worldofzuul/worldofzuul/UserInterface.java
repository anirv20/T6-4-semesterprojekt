package worldofzuul;

public class UserInterface {
    public void printWelcome(Room currentRoom, Parser parser) {
        System.out.println();
        System.out.println("Buy power plants to produce energy.");
        System.out.println("Produce more energy than your city needs.");
        System.out.println("Avoid pollution.");
        System.out.println();;
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        System.out.println();
    }

    public void printStats(int currentTurn, int MAXTURN, Economy economy, Energy energy, Pollution pollution) {
        System.out.println("-CITY STATS-");
        System.out.println("Current turn: " + currentTurn + "/" + MAXTURN);
        System.out.println("Current balance: " + economy.getBalance() + " coins.");
        System.out.printf("Current energy production/demand: %.2f MW / %.2f MW %n", energy.getTotalProduction(), energy.getDemand());
        System.out.println("Current pollution/turn: " + pollution.getTurnPollution() + " kgCO2e/turn");
        System.out.printf("Total pollution: %.2f / %.2f kgCO2e (%.2f%%)%n", pollution.getTotalPollution(), pollution.getLimit(), pollution.getPollutionPercent());
    }

}
