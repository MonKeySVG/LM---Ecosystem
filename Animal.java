import java.util.Random;

public abstract class Animal {
    protected int x;
    protected int y;
    protected boolean male;
    protected boolean alive;
    protected int age;
    protected int lastEaten;

    protected int lifespan;

    private Random random = new Random();

    public Animal(int x, int y) {
        this.x = x;
        this.y = y;

        this.alive = true;

        this.age = 0;

        this.lastEaten = 0;



        // Donne un sexe au hasard à la création d'un animal
        this.male = random.nextInt(2) == 0;
    }


    public void age() {
        this.age++;
        if (age >= lifespan) {
            this.alive = false;
        }
    }


    public void move() {
        // Logique de déplacement du mouton
        int direction = random.nextInt(4); // 0: haut, 1: bas, 2: gauche, 3: droite
        switch (direction) {
            case 0: if (y > 0) y--; break;
            case 1: if (y < Ecosystem.HEIGHT - 1) y++; break;
            case 2: if (x > 0) x--; break;
            case 3: if (x < Ecosystem.WIDTH - 1) x++; break;
        }

    }

    public boolean hasAdjacentSheep(Animal[][] ecosystem) {
        // Vérifiez si un mouton est présent sur une case adjacente
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < Ecosystem.WIDTH && j >= 0 && j < Ecosystem.HEIGHT && !(i == x && j == y)) {
                    // Vérifiez si la case adjacente contient un mouton
                    if (ecosystem[i][j] instanceof Sheep) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean hasAdjacentWolf(Animal[][] ecosystem) {
        // Vérifiez si un loup est présent sur une case adjacente
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i >= 0 && i < Ecosystem.WIDTH && j >= 0 && j < Ecosystem.HEIGHT && !(i == x && j == y)) {
                    // Vérifiez si la case adjacente contient un mouton
                    if (ecosystem[i][j] instanceof Wolf) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Getters pour x et y
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}