package worldofzuul.domain;

public class Pollution {
    public final double LIMIT = 5000000;
    private double totalPollution;
    private double turnPollution;
    
    public double getLimit() {
        return LIMIT;
    }

    public double getTotalPollution() {
        return totalPollution;
    }

    public double getPollutionPercent() {
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
