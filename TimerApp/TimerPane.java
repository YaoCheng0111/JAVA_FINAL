package myPackage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.time.LocalDateTime;

public class TimerPane extends BorderPane {

    private final ObservableList<TimerItem> timers = FXCollections.observableArrayList();
    private final ListView<Object> listView = new ListView<>();

    public TimerPane() {
        listView.setCellFactory(list -> new ListCell<>() {
            private final Label label = new Label();
            private TimerItem currentTimer;
            private Timeline updateTimeline;

            {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            }

            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                if (updateTimeline != null) {
                    updateTimeline.stop();
                    updateTimeline = null;
                }

                if (empty || item == null) {
                    currentTimer = null;
                    setGraphic(null);
                } else if (item instanceof String str && str.equals("ADD_NEW")) {
                    Button addButton = new Button("+ 新增計時器");
                    addButton.setOnAction(e -> showAddForm());
                    setGraphic(addButton);
                } else if (item instanceof TimerItem timer) {
                    currentTimer = timer;
                    label.setText(timer.title + " - " + timer.getRemainingTime());
                    setGraphic(label);

                    updateTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                        if (currentTimer != null) {
                            label.setText(currentTimer.title + " - " + currentTimer.getRemainingTime());
                        }
                    }));
                    updateTimeline.setCycleCount(Timeline.INDEFINITE);
                    updateTimeline.play();
                }
            }
        });

        showList();
    }

    private void showList() {
        ObservableList<Object> displayItems = FXCollections.observableArrayList();
        displayItems.add("ADD_NEW");
        displayItems.addAll(timers);
        listView.setItems(displayItems);
        setCenter(listView);
    }

    private void showAddForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));

        Label title = new Label("新增計時器");
        title.getStyleClass().add("title-label");

        TextField titleField = new TextField();
        titleField.setPromptText("計時器主題（預設：計時器）");

        ComboBox<Integer> hourBox = new ComboBox<>();
        ComboBox<Integer> minBox = new ComboBox<>();
        ComboBox<Integer> secBox = new ComboBox<>();

        for (int i = 0; i < 60; i++) {
            if (i < 24)
                hourBox.getItems().add(i);
            minBox.getItems().add(i);
            secBox.getItems().add(i);
        }

        hourBox.setValue(0);
        minBox.setValue(0);
        secBox.setValue(0);

        HBox timeRow = new HBox(10,
                new Label("hr:"), hourBox,
                new Label("min:"), minBox,
                new Label("sec:"), secBox);

        Button confirm = new Button("新增");
        Button cancel = new Button("取消");
        cancel.getStyleClass().add("cancel-button");

        confirm.setOnAction(e -> {
            int h = hourBox.getValue();
            int m = minBox.getValue();
            int s = secBox.getValue();
            LocalDateTime end = LocalDateTime.now().plusHours(h).plusMinutes(m).plusSeconds(s);
            String titleStr = titleField.getText().isBlank() ? "計時器" : titleField.getText();

            timers.add(new TimerItem(titleStr, end));
            showList();
        });

        cancel.setOnAction(e -> showList());

        HBox buttons = new HBox(10, confirm, cancel);
        form.getChildren().addAll(title, titleField, timeRow, buttons);

        setCenter(form);
    }
}
