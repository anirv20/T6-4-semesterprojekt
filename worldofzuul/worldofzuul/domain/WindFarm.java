package worldofzuul.domain;

public class WindFarm extends PowerPlant {
    private static long price = 1240000; // mellem
    // override constractor fra super klasse
    public WindFarm() { //den producerer ikke så meget pollution, mindest energi
        super(4, 400, price); // override constractor fra super klasse
        setDescription("This is a wind farm consisting of 100 wind turbines. A wind farm costs " + getPrice() +
                " coins and produces " + getEnergyProduction() + " MW. The pollution is " + getPollution() + " tonCO2e/turn");
    }
    public static long getPrice() {
        return price;
    }

    public static void setPrice(long price) {
        WindFarm.price = price;
    }

    @Override // fra superklassse
    public boolean upgrade() { // Changes the power plant's values when you upgrade.
        if (getLevel() < PowerPlant.MAXLEVEL) {
            setLevel(getLevel() + 1); // stiger
            setPollution(getPollution()*0.75);// mindre
            setEnergyProduction(getEnergyProduction()*1.5); // stiger hver
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "WindFarm (lvl: " + getLevel() + ")";
    }
}
