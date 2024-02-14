// Pour compiler :
// javac --module-path /Users/reda/IntelliJProjects/JavaFX/javafx-sdk-21.0.1/lib --add-modules javafx.controls,javafx.fxml Animal.java Sheep.java Wolf.java Grass.java Ecosystem.java Main.java
// java --module-path /Users/reda/IntelliJProjects/JavaFX/javafx-sdk-21.0.1/lib --add-modules javafx.controls,javafx.fxml Main

// Remplacer le chemin vers le dossier lib de la librairie javafx par pour qu'il corresponde à l'emplacement de votre dossier lib sur votre machine



import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;

public class Main extends Application {

    private Scene menu, simulation;

    private Ecosystem ecosystem;
    private Canvas canvas;
    private long lastUpdateTime;
    private Label wolvesLabel;
    private Label sheepsLabel;







    @Override
    public void start(Stage primaryStage) {

        // Ajout des éléments du menu
        Button startButton = new Button("Start");
        startButton.setOnAction(e -> primaryStage.setScene(simulation));
        StackPane mainMenuLayout = new StackPane();
        mainMenuLayout.getChildren().add(startButton);
        menu = new Scene(mainMenuLayout, 800, 500);




        // Ajout des éléments de la simulation
        ecosystem = new Ecosystem(100, 100);
        canvas = new Canvas(500, 500);

        BorderPane root = new BorderPane();
        root.setLeft(canvas);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);

        wolvesLabel = new Label("Nombre de loups: ");
        sheepsLabel = new Label("Nombre de moutons: ");

        wolvesLabel.setStyle("-fx-font-size: 26px;"); // Définir la taille de la police pour le label des loups
        sheepsLabel.setStyle("-fx-font-size: 26px;"); // Définir la taille de la police pour le label des moutons

        vbox.getChildren().addAll(wolvesLabel, sheepsLabel);
        root.setRight(vbox);

        simulation = new Scene(root, 800, 500);
        simulation.setFill(Color.LIGHTGRAY);
        primaryStage.setTitle("Simulation d'Écosystème");


        // Scène de départ
        primaryStage.setScene(menu);
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
                    Wolf wolf = (Wolf) universe[i][j];
                    if (wolf.male) {
                        gc.setFill(Color.PURPLE);
                    } else {
                        gc.setFill(Color.RED);
                    }

                    gc.fillRect(i * 5, j * 5, 5, 5); // Dessiner un loup
                } else if (universe[i][j] instanceof Sheep) {
                    Sheep sheep = (Sheep) universe[i][j];
                    if (sheep.male) {
                        gc.setFill(Color.BLUE);
                    } else {
                        gc.setFill(Color.LIGHTBLUE);
                    }

                    gc.fillRect(i * 5, j * 5, 5, 5); // Dessiner un mouton
                }
            }
        }
    }

    private void updateValues() {
        // Mettre à jour les valeurs affichées en temps réel
        wolvesLabel.setText("Nombre de loups: " + ecosystem.getNumWolves());
        sheepsLabel.setText("Nombre de moutons: " + ecosystem.getNumSheeps());
    }

    public static void main(String[] args) {
        launch(args);
    }

}
