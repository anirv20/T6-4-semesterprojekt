package worldofzuul;

public class Pollution {
    public static final double LIMIT = 500.0;
    private double totalPollution;
    private double turnPollution;
    
    
    public Pollution(double LIMIT, double totalPollution, double turnPollution) {
        this.Limit = LIMIT;
        this.totalPollution = totalPollution;
        this.turnpullotion = turnPollution;
    }
    
    public double getLimit() {
        return LIMIT;
    }

    public double getTotalPollution() {
        return totalPollution;
    }

    public void setTotalPollution(double totalPollution) {
        this.totalPollution = totalPollution;
    }

    public double getTurnPollution() {
        return turnPollution;
    }

}
