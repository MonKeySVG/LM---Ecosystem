# Simulation d'écosystème

## Guide d'utilisation

### Installation

Avant de commencer, assurez-vous d'avoir installé les éléments suivants :
- IDE Java
- JDK Java
- JavaFX

### Prérequis

1. Ouvrez le projet dans votre IDE.
2. Accédez à la console à la racine du projet et exécutez la commande suivante pour compiler :

`javac --module-path Chemin vers le dossier « lib » du dossier JavaFX --add-modules javafx.controls,javafx.fxml,javafx.media Animal.java Sheep.java Wolf.java Grass.java Ecosystem.java Main.java`

Cela ajoutera les modules nécessaires au bon fonctionnement de l'application.

### Lancement de l'application

Pour lancer l'application, utilisez la commande suivante :

`java --module-path Chemin vers le dossier « lib » du dossier JavaFX --add-modules javafx.controls,javafx.fxml,javafx.media Main`


### Utilisation

À l'ouverture, l'application se présente comme suit :

- Sur la partie gauche de la fenêtre, observez l'écosystème évoluer au fil du temps.
- En haut à droite, consultez les informations en temps réel sur l'écosystème.
- Au milieu, accédez à la légende pour comprendre les événements dans l'écosystème.
- Utilisez le menu déroulant pour ajuster la fréquence d'actualisation de l'écosystème.
- Utilisez le bouton "Start" pour démarrer la simulation.
- Cliquez sur la partie gauche de la fenêtre pour mettre en pause la simulation.

Les statistiques de la simulation sont affichées une fois celle-ci terminée. Vous pouvez ensuite choisir de relancer la simulation avec les mêmes paramètres ou de revenir au menu pour en tester d'autres.

### Axes d'évolution de l'application

1. Ajouter des points d'eau, des rochers, des arbres.
2. Intégrer un système de météo.
3. Permettre à l'utilisateur de placer les animaux, les rochers et l'eau sur la carte.
4. Ajouter un cycle jour/nuit.
