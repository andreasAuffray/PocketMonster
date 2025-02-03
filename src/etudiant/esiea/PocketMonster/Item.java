package etudiant.esiea.PocketMonster;

public class Item {
    String name;
    String effectType;
    int effectAmount;

    public Item(String name, String effectType, int effectAmount) {
        this.name = name;
        this.effectType = effectType;
        this.effectAmount = effectAmount;
    }

    public void applyEffect(Monster monster) {
        switch (effectType) {
            case "heal":
                monster.heal(effectAmount);
                break;
            case "buff":
                monster.attack += effectAmount;
                System.out.println(monster.name + " increased its attack by " + effectAmount);
                break;
            case "status":
                monster.isParalyzed = false;
                monster.isBurned = false;
                monster.isFlooded = false;
                monster.isUnderground = false;
                System.out.println(monster.name + " is healed !");
                break;
        }
    }
}
