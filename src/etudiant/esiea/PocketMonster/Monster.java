package etudiant.esiea.PocketMonster;

import java.util.*;

public class Monster {
    String name;
    String type;
    int hp;
    int maxHp;
    int attack;
    int defense;
    int speed;
    boolean Insect;
    double paralysisChance;
    int paralysisTurns;
    double floodChance;
    int floodTurns;
    double fallChance;
    double burnChance;
    boolean isParalyzed;
    boolean isBurned;
    boolean isFlooded;
    boolean isEmpoi;
    boolean isUnderground;
    List<Attack> attacks = new ArrayList<>();
    private Attack nextAttack;
    private Item nextItem;

    public Monster(String name, String type, int hp, int attack, int defense, int speed) {
        Random random = new Random();
        this.name = name;
        this.type = type;
        this.hp = hp;
        this.maxHp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.Insect=random.nextBoolean();

    }

    public void addAttack(Attack attack) {
        if (attacks.size() < 4) {
            attacks.add(attack);
        }
    }

    public void setNextAttack(Attack attack) {
        this.nextAttack = attack;
    }

    public Attack getNextAttack() {
        return this.nextAttack;
    }

    public void setNextItem(Item item) {
        this.nextItem = item;
    }

    public Item getNextItem() {
        return this.nextItem;
    }

    public void applyStatusEffect() {
        if (this.isBurned) {
            this.hp -= this.attack / 10;
            System.out.println(this.name + " takes burn damage !");
        }
        if (this.isParalyzed) {
            if (Math.random() < 0.25) {
                System.out.println(this.name + " is paralyzed in cannot attack !");
                return;
            }
        }
        if (this.isFlooded) {
            this.floodTurns -= 1;
            if (this.floodTurns == 0) {
                this.isFlooded = false;
                System.out.println("Flooding has subsided !");
            }
            if(this.Insect==false){
                this.isBurned=false;
                this.isParalyzed=false;
                this.isEmpoi=false;
            }
            
        }
        if (this.isUnderground) {
            this.floodTurns -= 1;
            if (this.floodTurns == 0) {
                this.isUnderground = false;
                this.defense /= 2;
                System.out.println(this.name + " comes out from the ground !");
            }
        }
        if (this.isEmpoi) {
            
            this.hp -= this.attack / 10;
            System.out.println(this.name + " takes poison damage !");
            return;
            
        }
        
    }

    public void attack(Monster opponent, Attack attack) {
        double advantage = getAdvantage(attack.type, opponent.type);
        double coef = 0.85 + (Math.random() * (1 - 0.85));
        double damage = ((11 * this.attack * attack.power / (25 * opponent.defense)) + 2) * advantage * coef;
        opponent.hp -= damage;
        if (opponent.hp < 0) {
            opponent.hp = 0;
        }
        System.out.printf("%s use %s! (Damage: %.1f)%n", this.name, attack.name, damage);
        System.out.printf("Remaining HP of %s: %.1f%n", opponent.name, (double)opponent.hp);

        applySpecialEffects(opponent, attack.type);
    }

    private void applySpecialEffects(Monster opponent, String attackType) {
        if (Math.random() < 0.20) { // 20% chance for the effect to take place
            switch (this.type) {
                case "Electric":
                    opponent.isParalyzed = true;
                    opponent.paralysisTurns = 0;
                    System.out.println(opponent.name + " is paralyzed !");
                    break;
                case "Water":
                    this.isFlooded = true;
                    opponent.isFlooded=true;
                    this.floodTurns = 3; // Inonder le terrain pendant 3 tours
                    opponent.floodTurns=3;
                    System.out.println("Field flooded !");
                    break;
                case "Fire":
                    opponent.isBurned = true;
                    System.out.println(opponent.name + " is burning !");
                    break;
                case "Earth":
                    
                    this.isUnderground = true;
                    this.defense *= 2;
                    this.floodTurns = 3; // Se cacher sous terre pendant 3 tours
                    System.out.println(this.name + " hiding underground !");
                    
                    break;
                case "Nature":
                    this.hp+=20;
                    if(Insect==true){opponent.isEmpoi=true;}
                    break;
                
            }
        }
    }

    private double getAdvantage(String attackType, String opponentType) {
        Map<String, List<String>> advantages = new HashMap<>();
        advantages.put("Electric", Arrays.asList("Water"));
        advantages.put("Water", Arrays.asList("Fire"));
        advantages.put("Fire", Arrays.asList("Nature"));
        advantages.put("Nature", Arrays.asList("Water"));
        advantages.put("Earth", Arrays.asList("Electric"));

        if (advantages.containsKey(attackType) && advantages.get(attackType).contains(opponentType)) {
            return 2.0;
        } else if (advantages.containsKey(opponentType) && advantages.get(opponentType).contains(attackType)) {
            return 0.5;
        } else {
            return 1.0;
        }
    }

    public void heal(int amount) {
        this.hp += amount;
        if (this.hp > this.maxHp) {
            this.hp = this.maxHp;
        }
        System.out.printf("%s earn %d HP !%n", this.name, amount);
    }
}
