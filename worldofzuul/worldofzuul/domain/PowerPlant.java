package worldofzuul.domain;

public abstract class PowerPlant {
    private double pollution, energyProduction;
    private long value;

    private long upgradePrice;
    private int level = 1;
    public static final int MAXLEVEL = 3;
    public static double bonus = 1;
    private String description;

    public PowerPlant(double pollution, double energyProduction, long value) {
        this.pollution = pollution;
        this.energyProduction = energyProduction * bonus;
        this.value = (long)(value*0.5);
        this.upgradePrice = (long)(this.value*0.5);
    }

    public static double getBonus() {
        return bonus;
    }

    public static void setBonus(double bonus) {
        PowerPlant.bonus = bonus;
    }

    public long getUpgradePrice() {
        return upgradePrice;
    }

    public void setUpgradePrice(long upgradePrice) {
        this.upgradePrice = upgradePrice;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
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

    public abstract boolean upgrade(); // har ikke body abstrakt

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PowerPlant";
    }
}



