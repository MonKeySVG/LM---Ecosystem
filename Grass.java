public class Grass {

    private boolean isEaten = false;

    public boolean isEaten() {
        return this.isEaten;
    }

    public void setEaten(boolean x) {
        this.isEaten = x;
    }

    public void grow() {
        if (this.isEaten()) {
            this.isEaten = false;
        }
    }
}
