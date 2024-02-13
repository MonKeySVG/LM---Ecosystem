// Pour compiler :
// javac --module-path /Users/reda/IntelliJProjects/JavaFX/javafx-sdk-21.0.1/lib --add-modules javafx.controls,javafx.fxml Animal.java Sheep.java Wolf.java Grass.java Ecosystem.java Main.java
// java --module-path /Users/reda/IntelliJProjects/JavaFX/javafx-sdk-21.0.1/lib --add-modules javafx.controls,javafx.fxml Main

// Remplacer le chemin vers le dossier lib de la librairie javafx par pour qu'il corresponde à l'emplacement de votre dossier lib sur votre machine



import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;

public class Main extends Application {

    private Ecosystem ecosystem;
    private Canvas canvas;
    private long lastUpdateTime;
    private Label labelLoups;
    private Label labelMoutons;





    @Override
    public void start(Stage primaryStage) {
        ecosystem = new Ecosystem(100, 100);
        canvas = new Canvas(500, 500);

        BorderPane root = new BorderPane();
        root.setLeft(canvas);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);

        labelLoups = new Label("Nombre de loups: ");
        labelMoutons = new Label("Nombre de moutons: ");

        vbox.getChildren().addAll(labelLoups, labelMoutons);
        root.setRight(vbox);

        Scene scene = new Scene(root, 800, 500);
        scene.setFill(Color.LIGHTGRAY);
        primaryStage.setTitle("Simulation d'Écosystème");
        primaryStage.setScene(scene);
        primaryStage.show();

        lastUpdateTime = System.nanoTime();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now - lastUpdateTime >= 1_000_000_000 / 2) { // 2 updates per second
                    ecosystem.update();
                    draw();
                    updateValues(); // Mettre à jour les valeurs en temps réel
                    lastUpdateTime = now;
                }
            }
        }.start();
    }


    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Animal[][] universe = ecosystem.getUniverse();
        Grass[][] herbes = ecosystem.getGrass();
        for (int i = 0; i < universe.length; i++) {
            for (int j = 0; j < universe[i].length; j++) {

                // Dessiner l'état de l'herbe
                if (herbes[i][j].isEaten()) {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(i * 5, j * 5, 5, 5);
                } else {
                    gc.setFill(Color.GREEN);
                    gc.fillRect(i * 5, j * 5, 5, 5);
                }


                // Dessiner les animaux
                if (universe[i][j] instanceof Wolf) {
                    gc.setFill(Color.RED);
                    gc.fillRect(i * 5, j * 5, 5, 5); // Dessiner un loup
                } else if (universe[i][j] instanceof Sheep) {
                    gc.setFill(Color.BLUE);
                    gc.fillRect(i * 5, j * 5, 5, 5); // Dessiner un mouton
                }
            }
        }
    }

    private void updateValues() {
        // Mettre à jour les valeurs affichées en temps réel
        labelLoups.setText("Nombre de loups: " + ecosystem.getNumWolves());
        labelMoutons.setText("Nombre de moutons: " + ecosystem.getNumSheeps());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
