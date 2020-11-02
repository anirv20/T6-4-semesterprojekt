package worldofzuul;

public class WindFarm extends PowerPlant {
    private static long price = 124000;

    public WindFarm() {
        super(10, 50, "This is a wind turbine farm consisting of 10 wind turbines.");
    }
    public static long getPrice() {
        return price;
    }

    @Override
    public void upgrade() {
        if (getLevel() < PowerPlant.MAXLEVEL) {
            setLevel(getLevel() + 1);
            setPollution(getPollution()-2);
            setEnergyProduction(getEnergyProduction()+10);
        }
    }
}
