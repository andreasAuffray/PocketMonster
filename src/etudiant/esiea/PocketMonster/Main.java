package etudiant.esiea.PocketMonster;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Monster> allMonsters = loadMonsters("data/monsters.txt");
        List<Attack> allAttacks = loadAttacks("data/attacks.txt");
        List<Item> allItems = loadItems("data/items.txt");

        // Créer les joueurs
        Player player1 = new Player("Player 1");
        Player player2 = new Player("Player 2");

        // Choisir les monstres pour chaque joueur
        chooseMonsters(player1, allMonsters, allAttacks);
        chooseMonsters(player2, allMonsters, allAttacks);

        // Ajouter des objets aux joueurs
        addItem(player1, allItems);
        addItem(player2, allItems);

        // Commencer le combat
        Battle battle = new Battle(player1, player2);
        battle.start();
    }

    public static List<Monster> loadMonsters(String filename) throws IOException {
        List<Monster> monsters = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("Monster")) {
                String name = readLineValue(reader);
                String type = readLineValue(reader);
                int hp = parseIntSafe(readLineValue(reader));
                int attack = parseIntSafe(readLineValue(reader));
                int defense = parseIntSafe(readLineValue(reader));
                int speed = parseIntSafe(readLineValue(reader));
                Monster monster = new Monster(name, type, hp, attack, defense, speed);
                monsters.add(monster);
                while (!(line = reader.readLine()).equals("EndMonster")) {
                    if (line.startsWith("Paralysis")) {
                        monster.paralysisChance = Double.parseDouble(line.split(" ")[1]);
                    } else if (line.startsWith("Flood")) {
                        monster.floodChance = Double.parseDouble(line.split(" ")[1]);
                    } else if (line.startsWith("Fall")) {
                        monster.fallChance = Double.parseDouble(line.split(" ")[1]);
                    }
                }
            }
        }
        reader.close();
        return monsters;
    }

    public static List<Attack> loadAttacks(String filename) throws IOException {
        List<Attack> attacks = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("Attack")) {
                String name = readLineValue(reader);
                String type = readLineValue(reader);
                int power = parseIntSafe(readLineValue(reader));
                int nbUse = parseIntSafe(readLineValue(reader));
                double failProbability = parseDoubleSafe(readLineValue(reader));
                Attack attack = new Attack(name, type, power, nbUse, failProbability);
                attacks.add(attack);
            }
        }
        reader.close();
        return attacks;
    }

    public static List<Item> loadItems(String filename) throws IOException {
        List<Item> items = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("Item")) {
                String name = readLineValue(reader);
                String effectType = readLineValue(reader);
                int effectAmount = parseIntSafe(readLineValue(reader));
                Item item = new Item(name, effectType, effectAmount);
                items.add(item);
            }
        }
        reader.close();
        return items;
    }

    private static String readLineValue(BufferedReader reader) throws IOException {
        String[] parts = reader.readLine().split(" ");
        return parts.length > 1 ? parts[1] : "";
    }

    private static int parseIntSafe(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static double parseDoubleSafe(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static void chooseMonsters(Player player, List<Monster> allMonsters, List<Attack> allAttacks) {
        Scanner scanner = new Scanner(System.in);
        BattleUI ui=new BattleUI();
        String temp;
        System.out.println("\n" + player.name + ", choose your monsters :\n");

        for (int i = 0; i < 3; i++) {
            System.out.println("Available monsters :\n");
            for (int j = 0; j < allMonsters.size(); j++) {
                System.out.println((j + 1) + ". " + allMonsters.get(j).name + " (Type: " + allMonsters.get(j).type + ")");
            }

            int monsterIndex = -1;
            while (monsterIndex < 0 || monsterIndex >= allMonsters.size()) {
                System.out.print("\n" + player.name + ", choose your monster number (1-" + allMonsters.size() + "): ");
                while(!scanner.hasNextInt()){
                    temp=scanner.nextLine();
                    ui.charRead();
                }
                monsterIndex = scanner.nextInt() - 1;
            }

            Monster chosenMonster = allMonsters.get(monsterIndex);
            allMonsters.remove(monsterIndex); // enlever le monstre choisi de la liste

            System.out.println("\nAvailable attacks for " + chosenMonster.name + " :\n");
            List<Attack> validAttacks = new ArrayList<>();
            for (Attack attack : allAttacks) {
                if (attack.type.equals("Normal") || attack.type.equals(chosenMonster.type)) {
                    validAttacks.add(attack);
                }
            }
            for (int j = 0; j < validAttacks.size(); j++) {
                System.out.println((j + 1) + ". " + validAttacks.get(j).name + " (Type: " + validAttacks.get(j).type + ")");
            }

            List<Integer> chosenAttacks = new ArrayList<>();
            for (int k = 0; k < 4; k++) {
                int attackIndex = -1;
                while (attackIndex < 0 || attackIndex >= validAttacks.size() || chosenAttacks.contains(attackIndex)) {
                    System.out.print("\n" + player.name + ", choose an attack " + (k + 1) + " for " + chosenMonster.name + " (1-" + validAttacks.size() + "): ");
                    while(!scanner.hasNextInt()){
                        temp=scanner.nextLine();
                        ui.charRead();
                    }
                    attackIndex = scanner.nextInt() - 1;
                }
                chosenAttacks.add(attackIndex);
                chosenMonster.addAttack(validAttacks.get(attackIndex));
            }
            player.addMonster(chosenMonster);
            System.out.println();
        }
    }

    public static void addItem(Player player, List<Item> allItems) {
        Scanner scanner = new Scanner(System.in);
        BattleUI ui=new BattleUI();
        System.out.println("\n" + player.name + ", choose 5 items :\n");
        String temp;
        for (int i = 0; i < 5; i++) {
            System.out.println("Available items :\n");
            for (int j = 0; j < allItems.size(); j++) {
                System.out.println((j + 1) + ". " + allItems.get(j).name + " (" + allItems.get(j).effectType + " de " + allItems.get(j).effectAmount + ")");
            }

            int itemIndex = -1;
            while (itemIndex < 0 || itemIndex >= allItems.size()) {
                System.out.print("\n" + player.name + ", choose your item number (1-" + allItems.size() + "): ");
                while(!scanner.hasNextInt()){
                    temp=scanner.nextLine();
                    ui.charRead();
                }
                itemIndex = scanner.nextInt() - 1;
            }

            Item chosenItem = allItems.get(itemIndex);
            player.addItem(chosenItem);
            System.out.println();
            
        }
    }
}
