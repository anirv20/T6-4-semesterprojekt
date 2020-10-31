package worldofzuul;

public class Energy {
    private static double demand;
    private static double totalProduction;
    private static double difference;

    public double getDemand() {
        return demand;
    }

    public void setDemand(double demand) {
        this.demand = demand;
    }

    public double getTotalProduction() {
        return totalProduction;
    }

    public void setTotalProduction(double totalProduction) {
        this.totalProduction = totalProduction;
    }

    public double getDifference() {
        return difference;
    }

    public void setDifference(double difference) {
        this.difference = difference;
    }
    public double checkDifference() {
        this.difference = getDemand() - getTotalProduction();
        return difference;
    }
}
