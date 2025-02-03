package etudiant.esiea.PocketMonster;

public class Attack {
    String name;
    String type;
    int power;
    int nbUse;
    double failProbability;

    public Attack(String name, String type, int power, int nbUse, double failProbability) {
        this.name = name;
        this.type = type;
        this.power = power;
        this.nbUse = nbUse;
        this.failProbability = failProbability;
    }
}
