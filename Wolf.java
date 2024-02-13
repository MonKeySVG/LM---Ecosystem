import java.util.Random;

public class Wolf extends Animal {
    private Random random = new Random(); // Déclaration de la variable random

    public Wolf(int x, int y) {
        super(x, y);

        this.lifespan = 7;

        this.maxFastingTime = 7;
    }

    public Wolf reproduce() {
        int newX = x + random.nextInt(3) - 1; // Ajoute un décalage aléatoire entre -1 et 1 en x
        int newY = y + random.nextInt(3) - 1; // Ajoute un décalage aléatoire entre -1 et 1 en y

        // Assurez-vous que les nouvelles positions restent dans les limites de l'écosystème
        newX = Math.max(0, Math.min(Ecosystem.WIDTH - 1, newX));
        newY = Math.max(0, Math.min(Ecosystem.HEIGHT - 1, newY));

        // Créez un nouveau mouton à la position calculée
        Wolf newWolf = new Wolf(newX, newY);
        return newWolf;
    }
}
