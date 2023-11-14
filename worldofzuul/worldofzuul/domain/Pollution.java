package worldofzuul.domain;

public class Pollution {
    public final double LIMIT = 5000; // max pollution
    private double totalPollution; // total
    private double turnPollution;// pollution per turn
    
    public double getLimit() {
        return LIMIT;
    }

    public double getTotalPollution() {
        return totalPollution;
    }

    public double getPollutionPercent() { // pullotion i procent
        return totalPollution/LIMIT*100;
    }

    public void setTotalPollution(double totalPollution) {
        this.totalPollution = totalPollution;
    }

    public double getTurnPollution() {
        return turnPollution;
    }

    public void setTurnPollution(double turnPollution) {
        this.turnPollution = turnPollution;
    }

}
