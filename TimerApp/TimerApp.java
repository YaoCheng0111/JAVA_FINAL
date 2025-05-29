package myPackage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class TimerApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        TabPane tabPane = new TabPane();

        Tab alarmTab = new Tab("Alarm");
        alarmTab.setContent(new AlarmPane());
        alarmTab.setClosable(false);

        Tab timerTab = new Tab("Timer");
        timerTab.setContent(new TimerPane());
        timerTab.setClosable(false);

        tabPane.getTabs().addAll(alarmTab, timerTab);

        Scene scene = new Scene(tabPane, 560, 350);
        scene.getStylesheets().add(getClass().getResource("css/TimerStyle.css").toExternalForm()); // 加入CSS
        primaryStage.setScene(scene);
        primaryStage.setTitle("Alarm & Timer App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
