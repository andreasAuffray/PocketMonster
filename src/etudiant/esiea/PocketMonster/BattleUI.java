package etudiant.esiea.PocketMonster;

public class BattleUI {
    
    public void startBattleMessage(Player player1, Player player2) {
        System.out.println("\n=================================");
        System.out.println("      THE BATTLE BEGINS !");
        System.out.println("=================================");
        System.out.println(player1.name + " vs " + player2.name + "!\n");
    }

    public void playerQuitMessage(Player player) {
        System.out.println("\n=================================");
        System.out.println(player.name + " leave the fight !");
        System.out.println("=================================\n");
    }

    public void switchMonsterMessage(Player player, Monster newMonster) {
        System.out.println("\n=================================");
        System.out.printf("%s change to %s!%n", player.name, newMonster.name);
        System.out.println("=================================\n");
    }

    public void monsterKOMsg(Monster monster) {
        System.out.println("\n=================================");
        System.out.println(monster.name + " is down !");
        System.out.println("=================================\n");
    }

    public void remainingHPMsg(Monster monster) {
        System.out.printf("\nHP de %s: %.1f%n", monster.name, (double)monster.hp);
    }

    public void displayActionChoices(Player player, Monster monster) {
        System.out.println("\n=================================");
        System.out.println(player.name + ", choose an action for " + monster.name + ":");
        System.out.println("1. Attack");
        System.out.println("2. Item");
        System.out.println("3. Change monster");
        System.out.println("4. Leave");
        System.out.println("5. Back");
        System.out.println("=================================");
    }

    public void displayAttackChoices(Player player, Monster monster) {
        System.out.println("\n=================================");
        System.out.println(player.name + ", choose an attack for " + monster.name + ":");
        for (int i = 0; i < monster.attacks.size(); i++) {
            System.out.println((i + 1) + ". " + monster.attacks.get(i).name);
        }
        System.out.println("5. Back");
        System.out.println("=================================");
    }

    public void displayItemChoices(Player player) {
        System.out.println("\n=================================");
        System.out.println(player.name + ", choose an item:");
        for (int i = 0; i < player.items.size(); i++) {
            System.out.println((i + 1) + ". " + player.items.get(i).name);
        }
        System.out.println("5. Back");
        System.out.println("=================================");
    }

    public void displaySwitchMonsterChoices(Player player) {
        System.out.println("\n=================================");
        System.out.println(player.name + ", choose an new monster:");
        for (int i = 0; i < player.monsters.size(); i++) {
            if (player.monsters.get(i).hp > 0) {
                System.out.println((i + 1) + ". " + player.monsters.get(i).name);
            }
        }
        System.out.println("=================================");
    }

    public void displayWinner(Player winner) {
        System.out.println("\n=================================");
        System.out.println(winner.name + " win the fight !");
        System.out.println("=================================\n");
    }

    public void invalidChoiceMessage() {
        System.out.println("\nInvalide choice. Please try again.");
    }

    public void statusEffectMessage(String effect, Monster monster) {
        System.out.println(monster.name + " " + effect + " !");
    }
    public void charRead(){
        System.out.println("Don't use char but number !");

    }
}
