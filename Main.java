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
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;

import static javafx.geometry.Pos.*;


public class Main extends Application {

    private Scene menu, simulation;
    public Ecosystem ecosystem;
    private Canvas canvas;
    private long lastUpdateTime;
    private Label wolvesLabel;
    private Label sheepsLabel;

    private int simulationSpeed = 2;







    @Override
    public void start(Stage primaryStage) {

        // Ajout des éléments du menu
        Button startButton = new Button("Start");

        TextField wolvesInput = new TextField(); // Champ de texte pour le nombre de loups
        TextField sheepsInput = new TextField(); // Champ de texte pour le nombre de moutons
        Label wolvesInputLabel = new Label("Nombre de loups : "); // Libellé pour le champ de texte des loups
        Label sheepsInputLabel = new Label("Nombre de moutons : "); // Libellé pour le champ de texte des moutons


        VBox menuLayout = new VBox(); // Utilisez un VBox pour organiser les éléments verticalement

        VBox wolvesContainer = new VBox();
        VBox sheepsContainer = new VBox();

        wolvesContainer.getChildren().addAll(wolvesInputLabel, wolvesInput);
        sheepsContainer.getChildren().addAll(sheepsInputLabel, sheepsInput);

        wolvesContainer.setSpacing(10); // Espacement entre le libellé et le champ pour les loups
        sheepsContainer.setSpacing(10); // Espacement entre le libellé et le champ pour les moutons

        menuLayout.getChildren().addAll(wolvesContainer, sheepsContainer, startButton);

        // Ajout de séparateurs et d'espacements
        menuLayout.setSpacing(20); // Espacement entre les éléments
        menuLayout.setPadding(new Insets(200)); // Espacement autour des éléments

        menuLayout.setAlignment(CENTER); // Centrage des éléments dans le VBox

        menu = new Scene(menuLayout, 800, 500);




        // Ajout des éléments de la simulation

        canvas = new Canvas(500, 500);

        BorderPane root = new BorderPane();
        root.setLeft(canvas);

        VBox vbox = new VBox();
        vbox.setMinWidth(300);
        vbox.setMaxWidth(300);
        vbox.setMinHeight(500);
        vbox.setMaxHeight(500);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);


        wolvesLabel = new Label("Nombre de loups: ");
        sheepsLabel = new Label("Nombre de moutons: ");

        wolvesLabel.setStyle("-fx-font-size: 20px;"); // Définir la taille de la police pour le label des loups
        sheepsLabel.setStyle("-fx-font-size: 20px;"); // Définir la taille de la police pour le label des moutons

        ComboBox<Integer> speedSelector = new ComboBox<>();
        speedSelector.getItems().addAll(1, 2, 3, 4, 5, 6);
        speedSelector.setValue(1);

        speedSelector.setOnAction(e -> {
            int selectedSpeed = speedSelector.getValue();
            switch (selectedSpeed) {
                case 1:
                    simulationSpeed = 2;
                    break;
                case 2:
                    simulationSpeed = 4;
                    break;
                case 3:
                    simulationSpeed = 8;
                    break;
                case 4:
                    simulationSpeed = 16;
                    break;
                case 5:
                    simulationSpeed = 32;
                    break;
                case 6:
                    simulationSpeed = 64;
                    break;
                default:
                    simulationSpeed = 1;
                    break;
            }
        });

        vbox.getChildren().addAll(wolvesLabel, sheepsLabel);
        vbox.getChildren().add(speedSelector);
        root.setRight(vbox);

        simulation = new Scene(root, 800, 500);
        simulation.setFill(Color.LIGHTGRAY);
        primaryStage.setTitle("Simulation d'Écosystème");


        // Scène de départ
        primaryStage.setScene(menu);
        primaryStage.show();

        // Gestionnaire d'événements du bouton "Start"
        startButton.setOnAction(e -> {
            int numWolves = Integer.parseInt(wolvesInput.getText());
            int numSheeps = Integer.parseInt(sheepsInput.getText());

            // Mettre à jour les nombres de loups et de moutons dans Ecosystem
            Ecosystem.setNumWolves(numWolves);
            Ecosystem.setNumSheeps(numSheeps);

            ecosystem = new Ecosystem(100, 100);


            primaryStage.setScene(simulation);
            // Démarrage de la boucle d'animation ici
            lastUpdateTime = System.nanoTime();
            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (now - lastUpdateTime >= 1_000_000_000 / simulationSpeed) { // 2 mises a jour par secondes
                        ecosystem.update();
                        draw();
                        updateValues(); // Mettre à jour les valeurs en temps réel
                        lastUpdateTime = now;
                    }
                }
            }.start();
        });
    }




    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Animal[][] universe = ecosystem.getUniverse();
        Grass[][] herbes = ecosystem.getGrass();

        double tileSize = 500 / Ecosystem.HEIGHT;

        for (int i = 0; i < universe.length; i++) {
            for (int j = 0; j < universe[i].length; j++) {

                // Dessiner l'état de l'herbe
                if (herbes[i][j].isEaten()) {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
                } else {
                    gc.setFill(Color.GREEN);
                    gc.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
                }


                // Dessiner les animaux
                if (universe[i][j] instanceof Wolf) {
                    Wolf wolf = (Wolf) universe[i][j];
                    if (wolf.male) {
                        gc.setFill(Color.PURPLE);
                    } else {
                        gc.setFill(Color.RED);
                    }

                    gc.fillRect(i * tileSize, j * tileSize, tileSize, tileSize); // Dessiner un loup
                } else if (universe[i][j] instanceof Sheep) {
                    Sheep sheep = (Sheep) universe[i][j];
                    if (sheep.male) {
                        gc.setFill(Color.BLUE);
                    } else {
                        gc.setFill(Color.LIGHTBLUE);
                    }

                    gc.fillRect(i * tileSize, j * tileSize, tileSize, tileSize); // Dessiner un mouton
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
