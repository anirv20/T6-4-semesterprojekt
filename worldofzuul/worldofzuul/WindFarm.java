package worldofzuul;

public class WindFarm extends PowerPlant {
    private static long price = 1240000;

    public WindFarm() {
        super(4400, 400);
        setDescription("This is a wind farm consisting of 100 wind turbines. A wind farm costs " + getPrice() +
                " coins and produces " + getEnergyProduction() + " MW. The pollution is " + getPollution() + " kgCO2e/turn");
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

    @Override
    public String toString() {
        return "WindFarm (lvl: " + getLevel() + ")";
    }
}
