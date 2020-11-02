package worldofzuul;

public class CoalPowerPlant extends PowerPlant {

    public CoalPowerPlant() {
        super(100000, 10, 50, "This is a coal power plant...");
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
