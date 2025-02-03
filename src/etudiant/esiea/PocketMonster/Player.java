package etudiant.esiea.PocketMonster;

import java.util.*;

public class Player {
    String name;
    List<Monster> monsters = new ArrayList<>();
    List<Item> items = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public void addMonster(Monster monster) {
        if (monsters.size() < 6) {
            monsters.add(monster);
        }
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public boolean hasMonstersAlive() {
        for (Monster monster : monsters) {
            if (monster.hp > 0) {
                return true;
            }
        }
        return false;
    }

    public void useItem(Item item, Monster monster) {
        if (items.contains(item)) {
            item.applyEffect(monster);
            items.remove(item);
            System.out.printf("%s use %s%n", name, item.name);
        }
    }

    public int getAliveMonstersCount() {
        int count = 0;
        for (Monster monster : monsters) {
            if (monster.hp > 0) {
                count++;
            }
        }
        return count;
    }
}
