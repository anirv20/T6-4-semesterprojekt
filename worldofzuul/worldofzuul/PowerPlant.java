package worldofzuul;

public abstract class PowerPlant {
    private double price, pollution, energyProduction;
    private int level = 1;
    public static final int MAXLEVEL = 3;
    private String description;

    public PowerPlant(double price, double pollution, double energyProduction, String description) {
        this.price = price;
        this.pollution = pollution;
        this.energyProduction = energyProduction;
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPollution() {
        return pollution;
    }

    public void setPollution(double pollution) {
        this.pollution = pollution;
    }

    public double getEnergyProduction() {
        return energyProduction;
    }

    public void setEnergyProduction(double energyProduction) {
        this.energyProduction = energyProduction;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public abstract void upgrade();

}
