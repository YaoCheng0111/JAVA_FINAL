package myPackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.util.Duration;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class AlarmPane extends BorderPane {

    private final ObservableList<AlarmItem> alarms = FXCollections.observableArrayList();
    private final ListView<Object> listView = new ListView<>();
    private final AudioClip alarmSound = new AudioClip(getClass().getResource("/Resource/sound/timer_sound.wav").toString());

    public AlarmPane() {
        listView.setCellFactory(list -> new ListCell<>() {
            private final Label titleLabel = new Label();
            private final Label timeLabel = new Label();
            private final Button activeBtn = new Button("啟用/關閉");
            private final Button deleteBtn = new Button("刪除");
            private final HBox buttonsBox = new HBox(10, activeBtn, deleteBtn);
            private final VBox card = new VBox(5, titleLabel, timeLabel);
            private AlarmItem currentAlarm;
            private Timeline updateTimeline;
            private boolean showingDetails = false;

            //listview裡面的UI
            {
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                card.setPadding(new Insets(10));
                card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 6, 0, 0, 2);");
                titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
                timeLabel.setStyle("-fx-text-fill: gray;");
                buttonsBox.setVisible(false);
                buttonsBox.managedProperty().bind(buttonsBox.visibleProperty());
                card.getChildren().add(buttonsBox);

                loadAlarmsFromFile();
                showList();

                card.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                    showingDetails = !showingDetails;
                    buttonsBox.setVisible(showingDetails);
                    e.consume();
                });

                activeBtn.setOnAction(e -> {
                    if (currentAlarm != null) {
                        currentAlarm.setActive();
                        updateTimeLabel();
                        saveAlarmsToFile();
                    }
                });

                deleteBtn.getStyleClass().add("cancel-button");
                deleteBtn.setOnAction(e -> {
                    if (currentAlarm != null) {
                        alarms.remove(currentAlarm);
                        saveAlarmsToFile();
                        showingDetails = false;
                        showList();
                    }
                });
            }

            //更新alarmItem的title and time label
            private void updateTimeLabel() {
                if (currentAlarm != null) {
                    titleLabel.setText(String.format("%s (%s)",currentAlarm.getTitle(),currentAlarm.getIsActive()?"啟用":"關閉"));
                    timeLabel.setText(currentAlarm.getRemainingTime());
                }
            }


            //偵測是哪種object來顯示list
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                if (updateTimeline != null) {
                    updateTimeline.stop();
                    updateTimeline = null;
                }

                if (empty || item == null) {
                    currentAlarm = null;
                    setGraphic(null);
                } else if (item instanceof String str && str.equals("ADD_NEW")) {
                    Button addButton = new Button("+ 新增鬧鐘");
                    addButton.setOnAction(e -> showAddForm());
                    setGraphic(addButton);
                } else if (item instanceof AlarmItem alarm) {
                    currentAlarm = alarm;
                    titleLabel.setText(String.format("%s (%s)",currentAlarm.getTitle(),currentAlarm.getIsActive()?"啟用":"關閉"));
                    updateTimeLabel();

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

        //每秒鐘檢查是否要跳通知
        Timeline checkTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> checkAlarms()));
        checkTimeline.setCycleCount(Timeline.INDEFINITE);
        checkTimeline.play();
    }

    //把各個list物件裝進observablelist最後做輸出
    private void showList() {
        ObservableList<Object> displayItems = FXCollections.observableArrayList();
        displayItems.add("ADD_NEW");
        displayItems.addAll(alarms);
        listView.setItems(displayItems);
        setCenter(listView);
    }

    //檢查要不要響
    private void checkAlarms() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        for (AlarmItem alarm : alarms) {
            if (alarm.shouldTrigger(now)) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("鬧鐘通知");
                    alert.setHeaderText(null);
                    alert.setContentText(alarm.getTitle() + " 時間到！");
                    alert.show();
                    alarmSound.play();
                });
            }
        }
    }

    //新增頁面
    private void showAddForm() {
        //UI
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));

        //title
        Label titleLabel = new Label("新增鬧鐘");
        titleLabel.getStyleClass().add("title-label");

        TextField titleField = new TextField();
        titleField.setPromptText("鬧鐘主題 (預設：鬧鐘)");

        //alarmTime
        ComboBox<Integer> hourBox = new ComboBox<>();
        ComboBox<Integer> minBox = new ComboBox<>();
        ComboBox<Integer> secBox = new ComboBox<>();

        for (int i = 0; i < 60; i++) {
            if (i < 24) hourBox.getItems().add(i);
            minBox.getItems().add(i);
            secBox.getItems().add(i);
        }

        hourBox.setValue(0);
        minBox.setValue(0);
        secBox.setValue(0);

        HBox timeRow = new HBox(10,
                new Label("hr:"), hourBox,
                new Label("min:"), minBox,
                new Label("sec:"), secBox
        );

        // repeatDays 
        Label repeatLabel = new Label("重複星期：");
        String[] dayNames = {"日", "一", "二", "三", "四", "五", "六"};
        List<CheckBox> dayCheckboxes = new ArrayList<>();
        for (String name : dayNames) {
            dayCheckboxes.add(new CheckBox(name));
        }
        HBox repeatRow = new HBox(10);
        repeatRow.getChildren().addAll(repeatLabel);
        repeatRow.getChildren().addAll(dayCheckboxes);

        Button confirm = new Button("新增");
        Button cancel = new Button("取消");
        cancel.getStyleClass().add("cancel-button");

        confirm.setOnAction(e -> {
            int h = hourBox.getValue();
            int m = minBox.getValue();
            int s = secBox.getValue();

            // 轉換 checkbox 勾選狀態成 repeatDays (boolean list)
            List<Boolean> repeatDays = new ArrayList<>();
            int sevenFalse=0;
            for (CheckBox cb : dayCheckboxes) {
                if(!cb.isSelected())sevenFalse++;
                repeatDays.add(cb.isSelected());
            }
            if(sevenFalse==7){
                repeatDays.clear();
                for (int i = 0; i < 7; i++) {
                    repeatDays.add(true);
                }
            }

            String title = titleField.getText().isBlank() ? "鬧鐘" : titleField.getText();
            title += String.format(" (%02d:%02d:%02d)", h, m, s);

            alarms.add(new AlarmItem(title, h ,m ,s ,repeatDays,true));
            saveAlarmsToFile();
            showList();
        });

        cancel.setOnAction(e -> showList());

        HBox buttons = new HBox(10, confirm, cancel);
        form.getChildren().addAll(titleLabel, titleField, timeRow,repeatRow, buttons);

        setCenter(form);
    }

    //讀存檔邏輯
    private final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .setPrettyPrinting()
        .create();

    private final String FILE_PATH = "JsonData/alarms.json";

    private void loadAlarmsFromFile() {
        try {
            var path = Paths.get(FILE_PATH);
            if(!Files.exists(path)){
                Files.createDirectories(path.getParent());
                Files.writeString(path,"[]");
            }

            String json = Files.readString(path).trim();
            if(json.isEmpty()) json = "[]";

            Type listType = new TypeToken<List<AlarmItem>>() {}.getType();
            List<AlarmItem> loaded = gson.fromJson(json, listType);
            
            alarms.clear();
            if (loaded != null) alarms.setAll(loaded);
        } catch (IOException e) {
            System.out.println("無法載入鬧鐘檔案: " + e.getMessage());
        } catch (JsonSyntaxException e) {
        System.out.println("JSON 格式錯誤，可能檔案已損壞。");
        e.printStackTrace();
        }
    }

    private void saveAlarmsToFile() {
        try{
            String json = gson.toJson(alarms);
            Files.createDirectories( Paths.get(FILE_PATH).getParent());
            Files.writeString(Paths.get(FILE_PATH),json);
        } catch (IOException e) {
            System.out.println("無法儲存鬧鐘檔案: " + e.getMessage());
        }
    }

}
