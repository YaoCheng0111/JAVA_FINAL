package myPackage;

import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class TimerApp{
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        Tab alarmTab = new Tab("鬧鐘");
        alarmTab.setContent(new AlarmPane());
        alarmTab.setClosable(false);

        Tab timerTab = new Tab("計時器");
        timerTab.setContent(new TimerPane());
        timerTab.setClosable(false);

        tabPane.getTabs().addAll(alarmTab, timerTab);

        Scene scene = new Scene(tabPane, 560, 350);
        scene.getStylesheets().add(getClass().getResource("css/TimerStyle.css").toExternalForm()); // 加入CSS
        stage.setScene(scene);
        stage.setTitle("鬧鐘 & 計時器");
        stage.show();
    }
}
