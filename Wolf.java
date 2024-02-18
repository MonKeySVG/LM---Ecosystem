import java.util.Random;

public class Wolf extends Animal {
    private final Random random = new Random(); // Déclaration de la variable random

    public Wolf(int x, int y) {
        super(x, y);

        this.lifespan = 60;

        this.maxFastingTime = 10;
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

    public Sheep findNearestSheep(Animal[][] ecosystem) {
        Sheep nearestSheep = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < Ecosystem.WIDTH; i++) {
            for (int j = 0; j < Ecosystem.HEIGHT; j++) {
                if (ecosystem[i][j] instanceof Sheep) {
                    double distance = Math.sqrt(Math.pow(this.x - i, 2) + Math.pow(this.y - j, 2));
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestSheep = (Sheep) ecosystem[i][j];
                    }
                }
            }
        }

        return nearestSheep;
    }

    public void move(Animal[][] ecosystem) {
        Sheep nearestSheep = findNearestSheep(ecosystem);
        if (nearestSheep != null) {
            int deltaX = nearestSheep.getX() - this.x;
            int deltaY = nearestSheep.getY() - this.y;

            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                this.x += Integer.signum(deltaX);
            } else {
                this.y += Integer.signum(deltaY);
            }
        } else {
            super.move(); // Appel à la méthode de déplacement originale si aucun mouton n'est trouvé
        }

        // Assurez-vous que le loup reste dans les limites de l'écosystème
        this.x = Math.max(0, Math.min(this.x, Ecosystem.WIDTH - 1));
        this.y = Math.max(0, Math.min(this.y, Ecosystem.HEIGHT - 1));
    }
}
