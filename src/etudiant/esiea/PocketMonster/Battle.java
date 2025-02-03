package etudiant.esiea.PocketMonster;

import java.util.Scanner;

public class Battle {
    Player player1;
    Player player2;
    Scanner scanner = new Scanner(System.in);
    BattleUI ui = new BattleUI();

    public Battle(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void start() {
        ui.startBattleMessage(player1, player2);

        int p1CurrentMonster = 0;
        int p2CurrentMonster = 0;

        while (player1.hasMonstersAlive() && player2.hasMonstersAlive()) {
            Monster monster1 = player1.monsters.get(p1CurrentMonster);
            Monster monster2 = player2.monsters.get(p2CurrentMonster);

            // Demander les actions à J1
            int p1Action;
            do {
                p1Action = chooseAction(player1, monster1);
            } while (p1Action == 5);
            if (p1Action == 4) {
                ui.playerQuitMessage(player1);
                return;
            }
            // Priorité au changement de monstre
            if (p1Action == 3) {
                p1CurrentMonster = switchMonster(player1);
                ui.switchMonsterMessage(player1, player1.monsters.get(p1CurrentMonster));
                monster1 = player1.monsters.get(p1CurrentMonster);
                
                
            }

            // Demander les actions à J2
            int p2Action;
            do {
                p2Action = chooseAction(player2, monster2);
            } while (p2Action == 5);
            if (p2Action == 4) {
                ui.playerQuitMessage(player2);
                return;
            }

            

            if (p2Action == 3) {
                p2CurrentMonster = switchMonster(player2);
                ui.switchMonsterMessage(player2, player2.monsters.get(p2CurrentMonster));
                monster2 = player2.monsters.get(p2CurrentMonster);
                
                
            }

            // Exécuter les actions, sauf si un joueur a changé de monstre
            if (monster1.speed >= monster2.speed) {
                executeAction(player1, p1Action, monster1, monster2);
                if (monster2.hp > 0) {
                    executeAction(player2, p2Action, monster2, monster1);
                }
            } else {
                executeAction(player2, p2Action, monster2, monster1);
                if (monster1.hp > 0) {
                    executeAction(player1, p1Action, monster1, monster2);
                }
            }

            // Vérifier si un monstre est K.O.
            if (monster1.hp <= 0) {
                ui.monsterKOMsg(monster1);
                if (!player1.hasMonstersAlive()) {
                    ui.displayWinner(player2);
                    return;
                }
                p1CurrentMonster = chooseNewMonster(player1);
            }

            if (monster2.hp <= 0) {
                ui.monsterKOMsg(monster2);
                if (!player2.hasMonstersAlive()) {
                    ui.displayWinner(player1);
                    return;
                }
                p2CurrentMonster = chooseNewMonster(player2);
            }

            // Afficher les PV restants après chaque tour
            ui.remainingHPMsg(player2.monsters.get(p2CurrentMonster));
            ui.remainingHPMsg(player1.monsters.get(p1CurrentMonster));
            monster1.applyStatusEffect();
            monster2.applyStatusEffect();

        }
    }

    private int chooseAction(Player player, Monster monster) {
        String temp;
        while (true) {
            ui.displayActionChoices(player, monster);
            while(!scanner.hasNextInt()){
                temp=scanner.nextLine();
                ui.charRead();

            }
            int action = scanner.nextInt();
            if (action == 5) {
                continue;
            } else if (action >= 1 && action <= 4) {
                if (action == 1) {
                    chooseAttack(player, monster);
                } else if (action == 2) {
                    chooseItem(player, monster);
                }
                return action;
            } else {
                ui.invalidChoiceMessage();
            }
        }
    }

    private void chooseAttack(Player player, Monster monster) {
        boolean validChoice = false;
        String temp;
        while (!validChoice) {
            ui.displayAttackChoices(player, monster);
            while(!scanner.hasNextInt()){
                temp=scanner.nextLine();
                ui.charRead();
            }
            int attackIndex = scanner.nextInt();
            if (attackIndex == 5) {
                return; // Retourner au menu précédent
            } else if (attackIndex > 0 && attackIndex <= monster.attacks.size()) {
                monster.setNextAttack(monster.attacks.get(attackIndex - 1));
                validChoice = true;
            } else {
                ui.invalidChoiceMessage();
            }
        }
    }

    private void chooseItem(Player player, Monster monster) {
        String temp;
        boolean validChoice = false;
        while (!validChoice) {
            ui.displayItemChoices(player);
            while(!scanner.hasNextInt()){
                temp=scanner.nextLine();
                ui.charRead();
            }
            int itemIndex = scanner.nextInt();
            if (itemIndex == 5) {
                return; // Retourner au menu précédent
            } else if (itemIndex > 0 && itemIndex <= player.items.size()) {
                monster.setNextItem(player.items.get(itemIndex - 1));
                validChoice = true;
            } else {
                ui.invalidChoiceMessage();
            }
        }
    }

    private int switchMonster(Player player) {
        String temp;
        while (true) {
            ui.displaySwitchMonsterChoices(player);
            while(!scanner.hasNextInt()){
                temp=scanner.nextLine();
                ui.charRead();
            }
            int monsterIndex = scanner.nextInt() - 1;
            if (monsterIndex == 5) {
                return -1; // Indicateur de retour
            } else if (monsterIndex >= 0 && monsterIndex < player.monsters.size() && player.monsters.get(monsterIndex).hp > 0) {
                return monsterIndex;
            } else {
                ui.invalidChoiceMessage();
            }
        }
    }

    private void executeAction(Player player, int action, Monster attacker, Monster opponent) {
        if (opponent == null) {
            return;
        }
        switch (action) {
            case 1:
                if (attacker.getNextAttack() != null) {
                    attacker.attack(opponent, attacker.getNextAttack());
                }
                break;
            case 2:
                if (attacker.getNextItem() != null) {
                    player.useItem(attacker.getNextItem(), attacker);
                }
                break;
        }
    }

    private int chooseNewMonster(Player player) {
        String temp;
        while (true) {
            ui.displaySwitchMonsterChoices(player);
            while(!scanner.hasNextInt()){
                temp=scanner.nextLine();
                ui.charRead();
            }
            int monsterIndex = scanner.nextInt() - 1;
            if (monsterIndex == 5) {
                return -1; // Indicateur de retour
            } else if (monsterIndex >= 0 && monsterIndex < player.monsters.size() && player.monsters.get(monsterIndex).hp > 0) {
                return monsterIndex;
            } else {
                ui.invalidChoiceMessage();
            }
        }
    }
}
