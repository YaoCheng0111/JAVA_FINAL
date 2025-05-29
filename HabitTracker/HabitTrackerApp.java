package myPackage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HabitTrackerApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        WeeklyManager weeklyManager = new WeeklyManager();
        HabitManager habitManager = new HabitManager();
        if(weeklyManager.isNewWeek()){
            habitManager.resetAllStatus();
        }
        HabitTrackerPane mainPane = new HabitTrackerPane(habitManager,weeklyManager);
        Scene scene = new Scene(mainPane, 1000, 600);
        scene.getStylesheets().add(getClass().getResource("css/HabitTrackerstyle.css").toExternalForm()); // 加入CSS
        primaryStage.setTitle("Habit Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}