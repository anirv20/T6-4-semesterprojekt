package worldofzuul;

public class Pollution {
    private double LIMIT;
    private double totalpollution;
    private double turnpullotion;

    public Pollution(double LIMIT, double totalpollution, double turnpullotion) {
        this.LIMIT = LIMIT;
        this.totalpollution = totalpollution;
        this.turnpullotion = turnpullotion;
    }

    public double getLIMIT() {
        return LIMIT;
    }

    public double getTotalpollution() {
        return totalpollution;
    }

    public double getTurnpullotion() {
        return turnpullotion;
    }

    public void setTotalpollution(double totalpollution) {
        this.totalpollution = totalpollution;
    }
}
