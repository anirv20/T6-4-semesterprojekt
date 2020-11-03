package worldofzuul;

public abstract class PowerPlant {
    private double pollution, energyProduction;
    private int level = 1;
    public static final int MAXLEVEL = 3;
    private String description;

    public PowerPlant(double pollution, double energyProduction) {
        this.pollution = pollution;
        this.energyProduction = energyProduction;
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

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract void upgrade();

    @Override
    public String toString() {
        return "PowerPlant";
    }
}
