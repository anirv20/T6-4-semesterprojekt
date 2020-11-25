package worldofzuul.domain;

public class Energy {
    private double demand;
    private double totalProduction;
    private double difference;

    public Energy() {
        this.demand = 600;
    }

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

    public void checkDifference() {
        this.difference = getTotalProduction() - getDemand();
    }
}
