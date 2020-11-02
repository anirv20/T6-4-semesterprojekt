package worldofzuul;

public class NuclearReactor extends PowerPlant {

    public NuclearReactor() {
        super(100000, 10, 50, "This is a nuclear reactor...");
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
