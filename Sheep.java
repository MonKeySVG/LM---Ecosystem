import java.util.Random;

public class Sheep extends Animal {

    private Random random = new Random(); // Déclaration de la variable random
    public Sheep(int x, int y) {
        super(x, y);
    }

    public Sheep reproduce() {
        int newX = x + random.nextInt(3) - 1; // Ajoute un décalage aléatoire entre -1 et 1 en x
        int newY = y + random.nextInt(3) - 1; // Ajoute un décalage aléatoire entre -1 et 1 en y

        // Assurez-vous que les nouvelles positions restent dans les limites de l'écosystème
        newX = Math.max(0, Math.min(Ecosystem.WIDTH - 1, newX));
        newY = Math.max(0, Math.min(Ecosystem.HEIGHT - 1, newY));

        // Créez un nouveau mouton à la position calculée
        Sheep newSheep = new Sheep(newX, newY);
        return newSheep;
    }

    public void eatGrass(Ecosystem ecosystem) {
        Grass grass = ecosystem.getGrassAt(x, y);
        if (grass != null && !grass.isEaten()) {
            grass.setEaten(true);

        }
    }
}
