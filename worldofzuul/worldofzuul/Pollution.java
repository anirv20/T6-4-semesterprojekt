package worldofzuul;

public class Pollution {
    private double limit;
    private double totalPollution;
    private double turnpullotion;

    public Pollution(double LIMIT, double totalpollution, double turnpullotion) {
        this.limit = LIMIT;
        this.totalPollution = totalpollution;
        this.turnpullotion = turnpullotion;
    }

    public double getLimit() {
        return limit;
    }

    public double getTotalPollution() {
        return totalPollution;
    }

    public double getTurnpullotion() {
        return turnpullotion;
    }

    public void setTotalPollution(double totalPollution) {
        this.totalPollution = totalPollution;
    }
}
