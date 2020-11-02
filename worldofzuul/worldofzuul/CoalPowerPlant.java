package worldofzuul;

public class CoalPowerPlant extends PowerPlant {
    private static long price = 450000;

    public CoalPowerPlant() {
        super(10, 50, "This is a coal power plant...");
    }

    public static long getPrice() {
        return price;
    }

    @Override
    public void upgrade() {
        if (getLevel() < PowerPlant.MAXLEVEL) {
            setLevel(getLevel() + 1);
            setPollution(getPollution()+10);
            setEnergyProduction(getEnergyProduction()+10);
        }
    }
}
