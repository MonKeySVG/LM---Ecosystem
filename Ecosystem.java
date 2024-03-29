import java.util.ArrayList;
import java.util.Random;

public class Ecosystem {

    // Définition de la taille de l'écosystème (Le nombre de cases)
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;

    private Animal[][] universe;
    private final Random random = new Random();

    // Nombre initial de loups et de moutons
    private static int NUM_WOLVES = 300;
    private static int NUM_SHEEPS = 300;

    private final Grass[][] grass;

    // Variables pour afficher l'évolution du nombre de loups et de moutons
    private int numWolves;
    private int numSheeps;

    private int numTurn;

    private int numTotalWolves;

    private int numTotalSheeps;

    private int totalWolvesDead;
    private int totalSheepsDead;

    static boolean[][] fertilizer;

    public static boolean SmartMovementSheeps = false;
    public static boolean SmartMovementWolves = false;

    public Ecosystem(int width, int height) {

        // Nouvelle matrice contenant des éléments de type "Animal"
        universe = new Animal[width][height];
        fertilizer = new boolean[width][height];

        numWolves = 0;
        numSheeps = 0;

        numTurn = 0;

        totalSheepsDead = 0;

        totalWolvesDead = 0;

        // Placer les animaux à des emplacements aléatoires
        placeRandomAnimals(Wolf.class, NUM_WOLVES);
        placeRandomAnimals(Sheep.class, NUM_SHEEPS);

        grass = new Grass[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grass[i][j] = new Grass();
            }
        }
    }

    static void dropFertilizer(int x, int y) {
        fertilizer[x][y] = true;
    }

    // Méthode pour obtenir l'herbe à une position spécifique
    public Grass getGrassAt(int x, int y) {
        if (x >= 0 && x < grass.length && y >= 0 && y < grass[x].length) {
            return grass[x][y];
        }
        return null; // Retourne null si les coordonnées sont hors des limites
    }

    public Grass[][] getGrass() {
        return grass;
    }

    // Vérifie si une case est libre
    public boolean isCellEmpty(int x, int y) {
        return universe[x][y] == null;
    }

    // Vérifie si une case contient un mouton
    public boolean hasSheepAt(int x, int y) {
        // Vérifiez si un mouton est présent à la position spécifiée
        return universe[x][y] instanceof Sheep;
    }

    // Fonction pour placer les animaux a un emplacement aléatoire
    private void placeRandomAnimals(Class<? extends Animal> animalType, int numAnimals) {
        Random random = new Random();

        for (int i = 0; i < numAnimals; i++) {
            int x, y;
            do {
                x = random.nextInt(universe.length);
                y = random.nextInt(universe[0].length);
            } while (universe[x][y] != null);

            try {
                if (animalType == Wolf.class) {
                    universe[x][y] = new Wolf(x, y);
                    numWolves++;
                } else if (animalType == Sheep.class) {
                    universe[x][y] = new Sheep(x, y);
                    numSheeps++;
                }
            } catch (Exception e) {
                // Handle instantiation exception
                e.printStackTrace();
            }
        }
    }

    public void update() {
        // Créer un nouveau tableau pour la mise à jour
        Animal[][] newUniverse = new Animal[WIDTH][HEIGHT];

        // Tableau pour suivre les cellules verrouillées
        boolean[][] cellLock = new boolean[WIDTH][HEIGHT];

        for (int i = 0; i < universe.length; i++) {
            for (int j = 0; j < universe[i].length; j++) {

                // Vérifier si il y a des sels minéraux
                if (fertilizer[i][j]) {
                    fertilizer[i][j] = false;
                    grass[i][j].grow();
                }




                if (universe[i][j] instanceof Sheep sheep) {
                    sheep.age();
                    sheep.foodCheck();

                    if (sheep.alive) {
                        if (SmartMovementSheeps) {
                            sheep.move(universe);
                        } else {
                            sheep.move();
                        }


                        sheep.eatGrass(this);

                        int newX = sheep.getX();
                        int newY = sheep.getY();
                        // Assurer que la nouvelle position est dans les limites de l'écosystème
                        if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT && !cellLock[newX][newY]) {
                            if (isCellEmpty(newX, newY)) {
                                newUniverse[newX][newY] = sheep;
                                // cellLock[newX][newY] = true; // Verrouiller la cellule pour éviter les déplacements simultanés
                            } else {
                                newUniverse[i][j] = sheep; // Remettre le mouton à sa position actuelle
                            }
                        } else {
                            newUniverse[i][j] = sheep; // Remettre le mouton à sa position actuelle
                        }

                        if (sheep.hasMaleAdjacentSheep(universe) && !sheep.male) {
                            Sheep newSheep = sheep.reproduce();
                            // Vérifier si la cellule n'est pas déjà occupée
                            if (newSheep.getX() >= 0 && newSheep.getX() < WIDTH && newSheep.getY() >= 0 && newSheep.getY() < HEIGHT && !cellLock[newSheep.getX()][newSheep.getY()]) {
                                if (isCellEmpty(newSheep.getX(), newSheep.getY())) {
                                    newUniverse[newSheep.getX()][newSheep.getY()] = newSheep;
                                    numSheeps++;
                                }
                            }
                        }
                    } else {
                        sheep.die();
                        totalSheepsDead++;
                    }


                }

                if (universe[i][j] instanceof Wolf wolf) {
                    wolf.age();
                    wolf.foodCheck();

                    if (wolf.alive) {
                        ArrayList<Sheep> adjacentSheeps = wolf.getAdjacentSheepsList(universe);
                        if (!wolf.getAdjacentSheepsList(universe).isEmpty()) {
                            Sheep chosenSheep = adjacentSheeps.get(random.nextInt(adjacentSheeps.size()));
                            newUniverse[chosenSheep.x][chosenSheep.y] = wolf;
                            wolf.lastEaten = 0;
                            totalSheepsDead++;
                        } else {
                            if (SmartMovementWolves) {
                                wolf.move(universe);
                            } else {
                                wolf.move();
                            }

                            int newX = wolf.getX();
                            int newY = wolf.getY();
                            // Assurer que la nouvelle position est dans les limites de l'écosystème
                            if (newX >= 0 && newX < WIDTH && newY >= 0 && newY < HEIGHT && !cellLock[newX][newY]) {
                                if (isCellEmpty(newX, newY)) {
                                    newUniverse[newX][newY] = wolf;
                                    cellLock[newX][newY] = true; // Verrouiller la cellule pour éviter les déplacements simultanés
                                } else {
                                    newUniverse[i][j] = wolf; // Remettre le loup à sa position actuelle
                                }
                            } else {
                                newUniverse[i][j] = wolf; // Remettre le mouton à sa position actuelle
                            }


                        }

                        if (wolf.hasMaleAdjacentWolf(universe) && !wolf.male) {
                            Wolf newWolf = wolf.reproduce();
                            // Vérifier si la cellule n'est pas déjà occupée
                            if (newWolf.getX() >= 0 && newWolf.getX() < WIDTH && newWolf.getY() >= 0 && newWolf.getY() < HEIGHT && !cellLock[newWolf.getX()][newWolf.getY()]) {
                                if (isCellEmpty(newWolf.getX(), newWolf.getY())) {
                                    newUniverse[newWolf.getX()][newWolf.getY()] = newWolf;
                                    numWolves++;
                                }
                            }
                        }

                    } else {
                        wolf.die();
                        totalWolvesDead++;
                    }

                }
            }
        }

        universe = newUniverse;
        // Mettre à jour le nombre de loups et de moutons
        updateAnimalCounts();

        numTurn++;
    }



    private void updateAnimalCounts() {
        numWolves = 0;
        numSheeps = 0;

        for (Animal[] animals : universe) {
            for (Animal animal : animals) {
                if (animal instanceof Wolf) {
                    numWolves++;
                } else if (animal instanceof Sheep) {
                    numSheeps++;
                }
            }
        }
    }

    public Animal[][] getUniverse() {
        return universe;
    }

    public int getNumWolves() {
        return numWolves;
    }

    public int getNumSheeps() {
        return numSheeps;
    }

    public int getNumTurn() {
        return numTurn;
    }

    public static void setNumWolves(int numWolves) {
        NUM_WOLVES = numWolves;
    }

    public static void setNumSheeps(int numSheeps) {
        NUM_SHEEPS = numSheeps;
    }


    public int getTotalWolvesDead() { return totalWolvesDead; }
    public int getTotalSheepsDead() { return totalSheepsDead; }
}
