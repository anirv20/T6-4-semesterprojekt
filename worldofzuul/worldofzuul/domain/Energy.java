package worldofzuul.domain;

public class Energy {
    private double demand;  //minimum mængde af energi demand for at sælge energi for hver station
    private double totalProduction;// total energi
    private double difference;// forskellen mellem totalproduction og demand

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

    public void checkDifference() {  // // forskellen mellem totalproduction og demand
        this.difference = getTotalProduction() - getDemand();
    }
}
