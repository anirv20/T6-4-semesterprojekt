package worldofzuul;

public class WindTurbine extends PowerPlant {

    public WindTurbine() {
        super(100000, 10, 50, "This is a wind turbine...");
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
