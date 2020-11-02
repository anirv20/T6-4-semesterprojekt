package worldofzuul;

public class NuclearReactor extends PowerPlant {
    private static long price = 3100000;

    public NuclearReactor() {
        super(10, 50, "This is a nuclear reactor...");
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
