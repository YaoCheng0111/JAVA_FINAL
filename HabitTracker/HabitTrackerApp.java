package myPackage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HabitTrackerApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        HabitManager habitManager = new HabitManager();
        HabitTrackerPane mainPane = new HabitTrackerPane(habitManager);
        Scene scene = new Scene(mainPane, 700, 400);
        scene.getStylesheets().add(getClass().getResource("HabitTrackerstyle.css").toExternalForm()); //加入CSS
        primaryStage.setTitle("Habit Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
