package myPackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;


public class TimerPane extends BorderPane {

    private final ObservableList<TimerItem> timers = FXCollections.observableArrayList();
    private final ListView<Object> listView = new ListView<>();

    public TimerPane() {

        loadTimers();

        listView.setCellFactory(list -> new ListCell<>() {
            private final Label titleLabel = new Label();
            private final Label timeLabel = new Label();
            private final Button restartBtn = new Button("重新開始");
            private final Button deleteBtn = new Button("刪除");
            private final HBox buttonsBox = new HBox(10, restartBtn, deleteBtn);
            private final VBox card = new VBox(5, titleLabel, timeLabel);
            private TimerItem currentTimer;
            private Timeline updateTimeline;
            private boolean showingDetails = false;

            {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                card.setPadding(new Insets(10));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 2);");
                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                timeLabel.setStyle("-fx-text-fill: gray;");
                buttonsBox.setVisible(false);
                buttonsBox.managedProperty().bind(buttonsBox.visibleProperty());
                card.getChildren().add(buttonsBox);

                // 點擊切換顯示/隱藏按鈕區域
                card.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                    showingDetails = !showingDetails;
                    buttonsBox.setVisible(showingDetails);
                    // 防止點擊後同時觸發 ListView 選取
                    e.consume();
                });

                restartBtn.setOnAction(e -> {
                    if (currentTimer != null) {
                        currentTimer.restart();
                        updateTimeLabel();
                    }
                });

                deleteBtn.getStyleClass().add("cancel-button");
                deleteBtn.setOnAction(e -> {
                    if (currentTimer != null) {
                        timers.remove(currentTimer);
                        saveTimers();
                        showingDetails = false;
                        showList();
                    }
                });
            }

            private void updateTimeLabel() {
                if (currentTimer != null) {
                    timeLabel.setText(currentTimer.getRemainingTime());
                }
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
                    titleLabel.setText(timer.title);
                    updateTimeLabel();

                    // 按項目顯示按鈕區域預設關閉
                    showingDetails = false;
                    buttonsBox.setVisible(false);

                    updateTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTimeLabel()));
                    updateTimeline.setCycleCount(Timeline.INDEFINITE);
                    updateTimeline.play();

                    setGraphic(card);
                }
            }
        });

        showList();
    }

    //GSON
    private final String timerFile = "JsonData/timers.json";
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();

    public void saveTimers() {
        try {
            String json = gson.toJson(timers);
            Files.writeString(Paths.get(timerFile), json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTimers() {
        try {
            var path = Paths.get(timerFile);
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.writeString(path, "[]");  // 建立空的 JSON 陣列
            }
            String json = Files.readString(path);
            List<TimerItem> list = gson.fromJson(json, new TypeToken<List<TimerItem>>(){}.getType());
            timers.clear();
            if (list != null) {
                timers.addAll(list);
            }
            showList();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            long totalSecs = h * 3600 + m * 60 + s;

            if (totalSecs == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "倒數時間不可為零", ButtonType.OK);
                alert.show();
                return;
            }

            LocalDateTime end = LocalDateTime.now().plusSeconds(totalSecs);
            String titleStr = titleField.getText().isBlank() ? "計時器" : titleField.getText();
            titleStr += String.format(" (%02d:%02d:%02d)", h, m, s);

            timers.add(new TimerItem(titleStr, end, totalSecs));
            saveTimers();
            showList();
        });

        cancel.setOnAction(e -> showList());

        HBox buttons = new HBox(10, confirm, cancel);
        form.getChildren().addAll(title, titleField, timeRow, buttons);

        setCenter(form);
    }
}
