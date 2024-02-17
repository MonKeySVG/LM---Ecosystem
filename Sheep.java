import java.util.Random;

public class Sheep extends Animal {

    private Random random = new Random(); // Déclaration de la variable random
    public Sheep(int x, int y) {

        super(x, y);
        this.lifespan = 50;

        this.maxFastingTime = 5;
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

        this.lastEaten = 0;
    }

    public Wolf findNearestWolf(Animal[][] ecosystem) {
        Wolf nearestWolf = null;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < Ecosystem.WIDTH; i++) {
            for (int j = 0; j < Ecosystem.HEIGHT; j++) {
                if (ecosystem[i][j] instanceof Wolf) {
                    double distance = Math.sqrt(Math.pow(this.x - i, 2) + Math.pow(this.y - j, 2));
                    if (distance < minDistance) {
                        minDistance = distance;
                        nearestWolf = (Wolf) ecosystem[i][j];
                    }
                }
            }
        }

        return nearestWolf;
    }

    public void move(Animal[][] ecosystem) {
        Wolf nearestWolf = findNearestWolf(ecosystem);
        if (nearestWolf != null) {
            int deltaX = this.x - nearestWolf.getX();
            int deltaY = this.y - nearestWolf.getY();

            // Déplacer le mouton dans la direction opposée au loup
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                this.x += Integer.signum(deltaX);
            } else {
                this.y += Integer.signum(deltaY);
            }
        } else {
            super.move(); // Appel à la méthode de déplacement originale si aucun loup n'est trouvé
        }

        // Assurez-vous que le mouton reste dans les limites de l'écosystème
        this.x = Math.max(0, Math.min(this.x, Ecosystem.WIDTH - 1));
        this.y = Math.max(0, Math.min(this.y, Ecosystem.HEIGHT - 1));
    }
}
