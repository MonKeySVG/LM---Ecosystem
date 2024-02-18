// Pour compiler :
// javac --module-path /Users/reda/IntelliJProjects/JavaFX/javafx-sdk-21.0.1/lib --add-modules javafx.controls,javafx.fxml,javafx.media Animal.java Sheep.java Wolf.java Grass.java Ecosystem.java Main.java
// java --module-path /Users/reda/IntelliJProjects/JavaFX/javafx-sdk-21.0.1/lib --add-modules javafx.controls,javafx.fxml,javafx.media Main

// Remplacer le chemin vers le dossier lib de la librairie javafx par pour qu'il corresponde à l'emplacement de votre dossier lib sur votre machine



import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;

import static javafx.geometry.Pos.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;


public class Main extends Application {

    // Colors
    Color darkColor = Color.rgb(15, 30, 37);
    Color grassColor = Color.rgb(200, 230, 144);

    Color maleSheepColor = Color.rgb(0, 204, 191);

    Color femaleSheepColor = Color.rgb(114, 242, 235);

    Color maleWolfColor = Color.rgb(161, 60, 58);
    Color femaleWolfColor = Color.rgb(255, 95, 93);





    private Scene menu, simulation, stats;
    public Ecosystem ecosystem;
    private Canvas canvas;
    private long lastUpdateTime;
    private Label wolvesLabel;
    private Label sheepsLabel;

    private Label numTurnLabel;

    private int simulationSpeed = 2;

    private boolean isPaused = false;

    private boolean onStatsScene = false;

    private MediaPlayer mediaPlayer;

    private Button menuButton = new Button("Menu");

    private Button startButton = new Button("Start");
    private Button restartButton = new Button("Restart");




    XYChart.Series<Number, Number> wolfSeries = new XYChart.Series<>();
    XYChart.Series<Number, Number> sheepSeries = new XYChart.Series<>();

    // Création des axes X et Y
    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);


    @Override
    public void start(Stage primaryStage) {



        // Création du graphique de ligne

        lineChart.setTitle("Évolution du nombre d'animaux");
        lineChart.setCreateSymbols(false); // Désactive les symboles pour les points de données



        // Ajout des séries de données au graphique
        lineChart.getData().add(wolfSeries);
        lineChart.getData().add(sheepSeries);







        ToggleButton musicButton = new ToggleButton("Musique: On");
        musicButton.setSelected(true);

        String musicFile = "assets/music.mp3";

        Media sound = new Media(new File(musicFile).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        mediaPlayer.setVolume(1);

        mediaPlayer.play();

        musicButton.setOnAction(event -> {
            if (musicButton.isSelected()) {
                musicButton.setText("Musique: On");
                mediaPlayer.play();
            } else {
                musicButton.setText("Musique: Off");
                mediaPlayer.pause();
            }
        });






        StackPane menuContainer = new StackPane();

        VBox borderMenu = new VBox();

        ImageView backgroundImageView = new ImageView();

        // Charger l'image de fond depuis un fichier
        Image backgroundImage = new Image("assets/bg.jpg");

        backgroundImageView.setImage(backgroundImage);

        // Ajustez les propriétés de mise à l'échelle pour remplir tout l'arrière-plan du menu
        backgroundImageView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundImageView.fitHeightProperty().bind(primaryStage.heightProperty());




        ImageView pauseIcon = new ImageView(new Image("assets/pauseButton.png"));




        // Ajout des éléments du menu


        TextField wolvesInput = new TextField(); // Champ de texte pour le nombre de loups
        wolvesInput.setText("250"); // Définit la valeur par défaut à "50"
        TextField sheepsInput = new TextField(); // Champ de texte pour le nombre de moutons
        sheepsInput.setText("250"); // Définit la valeur par défaut à "50"
        Label wolvesInputLabel = new Label("Nombre de loups : "); // Libellé pour le champ de texte des loups
        Label sheepsInputLabel = new Label("Nombre de moutons : "); // Libellé pour le champ de texte des moutons

        wolvesInputLabel.getStyleClass().add("menuLabel");
        sheepsInputLabel.getStyleClass().add("menuLabel");


        CheckBox wolvesMovementCheckBox = new CheckBox("Déplacement intelligent");
        CheckBox sheepsMovementCheckBox = new CheckBox("Déplacement intelligent");


        HBox wolvesMovementBox = new HBox(wolvesMovementCheckBox);
        HBox sheepsMovementBox = new HBox(sheepsMovementCheckBox);

        wolvesMovementBox.setSpacing(10);
        wolvesMovementBox.setAlignment(CENTER_LEFT);
        sheepsMovementBox.setSpacing(10);
        sheepsMovementBox.setAlignment(CENTER_LEFT);

        sheepsMovementCheckBox.setOnAction(e -> {
            Ecosystem.SmartMovementSheeps = sheepsMovementCheckBox.isSelected();
            System.out.println(Ecosystem.SmartMovementSheeps);
        });

        wolvesMovementCheckBox.setOnAction(e -> {
            Ecosystem.SmartMovementWolves = wolvesMovementCheckBox.isSelected();
            System.out.println(Ecosystem.SmartMovementWolves);
        });


        VBox menuLayout = new VBox(); // Utilisez un VBox pour organiser les éléments verticalement

        VBox wolvesContainer = new VBox();
        VBox sheepsContainer = new VBox();

        wolvesContainer.getChildren().addAll(wolvesInputLabel, wolvesInput, wolvesMovementBox);
        sheepsContainer.getChildren().addAll(sheepsInputLabel, sheepsInput, sheepsMovementBox);

        wolvesContainer.setSpacing(10); // Espacement entre le libellé et le champ pour les loups
        sheepsContainer.setSpacing(10); // Espacement entre le libellé et le champ pour les moutons

        menuLayout.getChildren().addAll(wolvesContainer, sheepsContainer, startButton);
        wolvesContainer.getStyleClass().add("menu");
        sheepsContainer.getStyleClass().add("menu");


        // Ajout de séparateurs et d'espacements
        menuLayout.setSpacing(20); // Espacement entre les éléments
        menuLayout.setPadding(new Insets(90, 200, 0, 200)); // Espacement autour des éléments

        menuLayout.setAlignment(CENTER); // Centrage des éléments dans le VBox

        menuContainer.getChildren().addAll(backgroundImageView, borderMenu);



        menu = new Scene(menuContainer, 800, 500);

        // Charge le fichier CSS
        menu.getStylesheets().add("style.css");




        // Ajout des éléments de la simulation

        canvas = new Canvas(500, 500);

        BorderPane root = new BorderPane();
        StackPane simulationPane = new StackPane();
        simulationPane.getChildren().add(canvas);
        root.setLeft(simulationPane);



        VBox bottomLeftBox = new VBox();
        bottomLeftBox.getChildren().add(musicButton);
        bottomLeftBox.setPadding(new Insets(20));
        borderMenu.getChildren().addAll(menuLayout, bottomLeftBox);






        numTurnLabel = new Label("Tour numéro : ");
        numTurnLabel.getStyleClass().add("simulationText");


        wolvesLabel = new Label("Nombre de loups : ");
        sheepsLabel = new Label("Nombre de moutons : ");

        wolvesLabel.getStyleClass().add("simulationText");
        sheepsLabel.getStyleClass().add("simulationText");


        Rectangle grassRectangle = new Rectangle(20, 20, grassColor);
        Circle maleWolf = new Circle(10, maleWolfColor);
        Circle femaleWolf = new Circle(10, femaleWolfColor);
        Circle maleSheep = new Circle(10, maleSheepColor);
        Circle femaleSheep = new Circle(10, femaleSheepColor);

        Label legendGrass = new Label("Herbe");
        Label legendMaleWolf = new Label("Loup Male");
        Label legendFemaleWolf = new Label("Loup Femelle");
        Label legendMaleSheep = new Label("Mouton Male");
        Label legendFemaleSheep = new Label("Mouton Femelle");

        legendGrass.getStyleClass().add("simulationText");
        legendMaleWolf.getStyleClass().add("simulationText");
        legendFemaleWolf.getStyleClass().add("simulationText");
        legendMaleSheep.getStyleClass().add("simulationText");
        legendFemaleSheep.getStyleClass().add("simulationText");

        VBox colorLegendBox = new VBox();

        HBox grassRectangleBox = new HBox(grassRectangle, legendGrass);
        HBox maleWolfBox = new HBox(maleWolf, legendMaleWolf);
        HBox femaleWolfBox = new HBox(femaleWolf, legendFemaleWolf);
        HBox maleSheepBox = new HBox(maleSheep, legendMaleSheep);
        HBox femaleSheepBox = new HBox(femaleSheep, legendFemaleSheep);

        grassRectangleBox.setAlignment(CENTER_LEFT);
        maleWolfBox.setAlignment(CENTER_LEFT);
        femaleWolfBox.setAlignment(CENTER_LEFT);
        maleSheepBox.setAlignment(CENTER_LEFT);
        femaleSheepBox.setAlignment(CENTER_LEFT);

        grassRectangleBox.setSpacing(10);
        maleWolfBox.setSpacing(10);
        femaleWolfBox.setSpacing(10);
        maleSheepBox.setSpacing(10);
        femaleSheepBox.setSpacing(10);


        colorLegendBox.getChildren().addAll(
                grassRectangleBox,
                maleWolfBox,
                femaleWolfBox,
                maleSheepBox,
                femaleSheepBox
        );

        colorLegendBox.setSpacing(10);
        colorLegendBox.setPadding(new Insets(10));







        ComboBox<Integer> speedSelector = new ComboBox<>();
        speedSelector.getItems().addAll(1, 2, 3, 4, 5, 6);
        speedSelector.setValue(1);
        Label speedLabel = new Label("Vitesse");
        speedLabel.getStyleClass().add("simulationText");

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

        VBox speedSelectorBox = new VBox();
        speedSelectorBox.getChildren().addAll(speedLabel, speedSelector);

        Button statsButton = new Button("Stats");



        // Box pour afficher le nombre d'animaux en temps réel
        VBox labelsVBox = new VBox();
        labelsVBox.setMinWidth(300);
        labelsVBox.setMaxWidth(300);

        labelsVBox.setSpacing(5);

        labelsVBox.getChildren().addAll(numTurnLabel, wolvesLabel, sheepsLabel);
        HBox bottomHBox = new HBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        bottomHBox.getChildren().addAll(speedSelectorBox, spacer, statsButton);
        bottomHBox.setAlignment(BOTTOM_LEFT);

        Region Vspacer1 = new Region();
        VBox.setVgrow(Vspacer1, Priority.ALWAYS);

        Region Vspacer2 = new Region();
        VBox.setVgrow(Vspacer2, Priority.ALWAYS);

        VBox mainVBox = new VBox();
        mainVBox.getChildren().addAll(labelsVBox, Vspacer1, colorLegendBox, Vspacer2, bottomHBox);
        mainVBox.setMinHeight(500);

        mainVBox.getStyleClass().add("simulationSidebar");

        labelsVBox.setPadding(new Insets(10));
        bottomHBox.setPadding(new Insets(10));
        root.setRight(mainVBox);

        simulation = new Scene(root, 800, 500);
        simulation.getStylesheets().add("style.css");

        primaryStage.setTitle("Simulation d'Écosystème");



        // Scène de départ
        primaryStage.setScene(menu);
        primaryStage.show();

        // Gestionnaire d'événements du bouton "Start"
        startButton.setOnAction(e -> {
            isPaused = false;
            canvas.setOpacity(1);
            simulationPane.getChildren().remove(pauseIcon);

            wolfSeries.getData().clear();
            sheepSeries.getData().clear();

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
                    if (!onStatsScene && !isPaused) {
                        if (now - lastUpdateTime >= 1_000_000_000 / simulationSpeed) { // 2 mises a jour par secondes
                            ecosystem.update();
                            draw();
                            updateValues(); // Mettre à jour les valeurs en temps réel

                            updateGraph(ecosystem.getNumTurn(), ecosystem.getNumWolves(), ecosystem.getNumSheeps());


                            lastUpdateTime = now;
                        }
                    }

                }
            }.start();
        });

        statsButton.setOnAction(e -> {
            onStatsScene = true;
            showStats();
            primaryStage.setScene(stats);
        });

        menuButton.setOnAction(e -> {
            onStatsScene = false;
            primaryStage.setScene(menu);
        });

        restartButton.setOnAction(e -> {
            onStatsScene = false;
            isPaused = false;
            canvas.setOpacity(1);
            simulationPane.getChildren().remove(pauseIcon);

            wolfSeries.getData().clear();
            sheepSeries.getData().clear();

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
                    if (!onStatsScene && !isPaused) {
                        if (now - lastUpdateTime >= 1_000_000_000 / simulationSpeed) { // 2 mises a jour par secondes
                            ecosystem.update();
                            draw();
                            updateValues(); // Mettre à jour les valeurs en temps réel

                            updateGraph(ecosystem.getNumTurn(), ecosystem.getNumWolves(), ecosystem.getNumSheeps());


                            lastUpdateTime = now;
                        }
                    }

                }
            }.start();
        });


        canvas.setOnMouseClicked(event -> {

            pauseIcon.setFitWidth(50);
            pauseIcon.setFitHeight(50);
            pauseIcon.setOpacity(0.8);
            if (isPaused) {
                isPaused = false;
                canvas.setOpacity(1);
                mediaPlayer.setVolume(1);
                simulationPane.getChildren().remove(pauseIcon);
            } else {
                isPaused = true;
                canvas.setOpacity(0.5);
                mediaPlayer.setVolume(0.5);
                simulationPane.getChildren().add(pauseIcon);
                pauseIcon.setMouseTransparent(true);
            }
        });






    }





    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Animal[][] universe = ecosystem.getUniverse();
        Grass[][] herbes = ecosystem.getGrass();

        double tileSize = (double) 500 / Ecosystem.HEIGHT;

        for (int i = 0; i < universe.length; i++) {
            for (int j = 0; j < universe[i].length; j++) {

                // Dessiner l'état de l'herbe
                if (herbes[i][j].isEaten()) {
                    gc.setFill(darkColor);
                    gc.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
                } else {
                    gc.setFill(grassColor);
                    gc.fillRect(i * tileSize, j * tileSize, tileSize, tileSize);
                }


                // Dessiner les animaux
                if (universe[i][j] instanceof Wolf wolf) {
                    if (wolf.male) {
                        gc.setFill(maleWolfColor);
                    } else {
                        gc.setFill(femaleWolfColor);
                    }

                    double centerX = i * tileSize + tileSize / 2; // Centre X du cercle
                    double centerY = j * tileSize + tileSize / 2; // Centre Y du cercle
                    double radius = tileSize / 2; // Rayon du cercle

                    gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2); // Dessiner un loup
                } else if (universe[i][j] instanceof Sheep sheep) {
                    if (sheep.male) {
                        gc.setFill(maleSheepColor);
                    } else {
                        gc.setFill(femaleSheepColor);
                    }

                    double centerX = i * tileSize + tileSize / 2; // Centre X du cercle
                    double centerY = j * tileSize + tileSize / 2; // Centre Y du cercle
                    double radius = tileSize / 2; // Rayon du cercle

                    gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2); // Dessiner un mouton
                }
            }
        }
    }

    private void updateValues() {
        // Mettre à jour les valeurs affichées en temps réel
        wolvesLabel.setText("Nombre de loups: " + ecosystem.getNumWolves());
        sheepsLabel.setText("Nombre de moutons: " + ecosystem.getNumSheeps());
        numTurnLabel.setText("Tour numéro : " + ecosystem.getNumTurn());
    }

    private void showStats() {



        // Nombre de tours
        Label totalTurnsLabel = new Label("Nombre total de tours : " + ecosystem.getNumTurn());
        totalTurnsLabel.getStyleClass().add("bigTitle");

        // Nombre d'animaux vivants
        Label aliveTitle = new Label("Nombre d'animaux vivants");
        aliveTitle.getStyleClass().add("Title");

        Label aliveWolves =  new Label("Nombre de loups vivants : " + ecosystem.getNumWolves());
        aliveWolves.getStyleClass().add("statsText");

        Label aliveSheeps =  new Label("Nombre de moutons vivants : " + ecosystem.getNumSheeps());
        aliveSheeps.getStyleClass().add("statsText");


        // Nombre d'animaux morts au cours de la simulation
        Label deadTitle = new Label("Nombre d'animaux morts");
        deadTitle.getStyleClass().add("Title");


        Label totalWolvesDeadLabel = new Label("Nombre total de loups morts : " + ecosystem.getTotalWolvesDead());
        totalWolvesDeadLabel.getStyleClass().add("statsText");

        Label totalSheepsDeadLabel = new Label("Nombre total de moutons morts : " + ecosystem.getTotalSheepsDead());
        totalSheepsDeadLabel.getStyleClass().add("statsText");




        VBox statsBox = new VBox();

        VBox turnBox = new VBox();
        turnBox.getChildren().add(totalTurnsLabel);

        VBox aliveBox = new VBox();
        aliveBox.getChildren().addAll(aliveTitle, aliveWolves, aliveSheeps);

        VBox deadBox = new VBox();
        deadBox.getChildren().addAll(deadTitle, totalWolvesDeadLabel, totalSheepsDeadLabel);

        HBox animalsBox = new HBox();
        animalsBox.getChildren().addAll(aliveBox, deadBox);

        HBox buttonsBox = new HBox();
        buttonsBox.getChildren().addAll(menuButton, restartButton);

        lineChart.setMaxWidth(600);
        lineChart.setPrefHeight(100);

        HBox colorLegend = new HBox();
        HBox wolfLegend = new HBox();
        HBox sheepLegend = new HBox();

        Rectangle sheepBoxLegend = new Rectangle(30, 10, maleSheepColor);
        Label sheepTextLegend = new Label("Moutons");
        sheepTextLegend.getStyleClass().add("statsText");

        sheepLegend.getChildren().addAll(sheepTextLegend, sheepBoxLegend);
        sheepLegend.setAlignment(CENTER);
        sheepLegend.setSpacing(10);


        Rectangle wolfBoxLegend = new Rectangle(30, 10, femaleWolfColor);
        Label wolfTextLegend = new Label("Loups");
        wolfTextLegend.getStyleClass().add("statsText");

        wolfLegend.getChildren().addAll(wolfTextLegend, wolfBoxLegend);
        wolfLegend.setAlignment(CENTER);
        wolfLegend.setSpacing(10);


        colorLegend.setAlignment(CENTER);
        colorLegend.setSpacing(30);


        colorLegend.getChildren().addAll(wolfLegend, sheepLegend);


        statsBox.getChildren().addAll(turnBox, animalsBox, lineChart, colorLegend, buttonsBox);
        turnBox.setAlignment(CENTER);
        aliveBox.setAlignment(CENTER);
        deadBox.setAlignment(CENTER);
        animalsBox.setAlignment(CENTER);
        buttonsBox.setAlignment(CENTER);
        statsBox.setAlignment(CENTER);

        statsBox.getStyleClass().add("statsBox");
        statsBox.setSpacing(20);

        turnBox.setSpacing(10);
        aliveBox.setSpacing(10);
        deadBox.setSpacing(10);
        animalsBox.setSpacing(50);
        buttonsBox.setSpacing(50);



        stats = new Scene(statsBox, 800, 500);

        stats.getStylesheets().add("style.css");
    }

    private void updateGraph(int numTurn, int numWolves, int numSheeps) {
        // Ajoute les données du tour actuel au graphique
        wolfSeries.getData().add(new XYChart.Data<>(numTurn, numWolves));
        sheepSeries.getData().add(new XYChart.Data<>(numTurn, numSheeps));
    }

    public static void main(String[] args) {
        launch(args);
    }

}
