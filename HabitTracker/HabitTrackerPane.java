package myPackage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import myPackage.Habit;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class HabitTrackerPane extends BorderPane {
    private final HabitManager habitManager;
    private final GridPane tableGrid;
    private final GridPane perfectRowGrid = new GridPane();


    // 這三個 Label 改成類別成員
    private Label checkRateLabel;
    private Label perfectDaysLabel;
    private Label totalCountLabel;

    public HabitTrackerPane(HabitManager habitManager, WeeklyManager weeklyManager) {
        this.habitManager = habitManager;
        this.tableGrid = new GridPane();

        setPadding(new Insets(10));

        Label titleLabel = new Label("Habit Tracker");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setMaxWidth(Double.MAX_VALUE);
        titleLabel.setAlignment(Pos.CENTER);
        setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        tableGrid.setHgap(10);
        tableGrid.setVgap(10);
        tableGrid.setPadding(new Insets(10));

        ScrollPane scrollPane = new ScrollPane(tableGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setMaxHeight(400);
        scrollPane.setStyle("-fx-background-color: transparent;");

        HBox centerBox = new HBox(scrollPane);
        centerBox.setAlignment(Pos.CENTER);
        setCenter(centerBox);


        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER);
        Button addButton = new Button("+ Add Habit");
        Button deleteButton = new Button("- Delete Habit");
        buttonBox.getChildren().addAll(addButton, deleteButton);

        HBox infoBox = new HBox(40);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setPadding(new Insets(5, 0, 5, 0));

        // 初始化類別成員 Label
        checkRateLabel = new Label("打卡率(%)");
        perfectDaysLabel = new Label("完美打卡天數(天)");
        totalCountLabel = new Label("總完成數量");

        checkRateLabel.getStyleClass().add("info-card");
        perfectDaysLabel.getStyleClass().add("info-card");
        totalCountLabel.getStyleClass().add("info-card");

        infoBox.getChildren().addAll(checkRateLabel, perfectDaysLabel, totalCountLabel);

        VBox bottomBox = new VBox(5);
        bottomBox.getChildren().addAll(infoBox, buttonBox);
        bottomBox.setAlignment(Pos.CENTER);

        setBottom(bottomBox);

        refreshTable();

        addButton.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Habit");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter habit name:");
            dialog.showAndWait().ifPresent(name -> {
                if (!name.trim().isEmpty()) {
                    habitManager.addHabit(new Habit(name.trim()));
                    habitManager.saveHabits();
                    refreshTable();
                }
            });
        });

        deleteButton.setOnAction(e -> {
            List<String> habitNames = habitManager.getHabitNames();
            if (habitNames.isEmpty()) {
                showAlert("No habits to delete.");
                return;
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<>(habitNames.get(0), habitNames);
            dialog.setTitle("Delete Habit");
            dialog.setHeaderText(null);
            dialog.setContentText("Select habit to delete:");
            dialog.showAndWait().ifPresent(name -> {
                habitManager.removeHabit(name);
                habitManager.saveHabits();
                refreshTable();
            });
        });
    }

    private void refreshTable() {
        tableGrid.getChildren().clear();

        // 左上角空白格
        tableGrid.add(new Label(""), 0, 0);

        // 7天欄位標題（週一到週日）
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(DayOfWeek.of((i + 7 - 1) % 7 + 1).name().substring(0, 3));
            dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setPrefWidth(40);
            tableGrid.add(dayLabel, i + 1, 0);
        }

        // 新增右側「全勤」欄位
        Label attendanceTitle = new Label("全勤");
        attendanceTitle.getStyleClass().add("full-attendance");
        attendanceTitle.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        attendanceTitle.setAlignment(Pos.CENTER);
        attendanceTitle.setPrefWidth(80);
        tableGrid.add(attendanceTitle, 8, 0);  // 第9欄（index 8）

        List<Habit> habits = habitManager.getHabits();
        int today = LocalDate.now().getDayOfWeek().getValue() % 7;

        for (int row = 0; row < habits.size(); row++) {
            Habit habit = habits.get(row);

            // 習慣名稱欄
            Label nameLabel = new Label(habit.getName());
            nameLabel.setPrefWidth(80);
            tableGrid.add(nameLabel, 0, row + 1);

            // 7天打卡按鈕格
            for (int col = 0; col < 7; col++) {
                Button cell = new Button(habit.isChecked(col) ? "✓" : "");
                cell.setPrefWidth(40);
                final int r = row;
                final int c = col;
                cell.setOnAction(e -> {
                    if (c != today) {
                        showAlert("只能打卡今天！");
                        return;
                    }
                    if (habitManager.isChecked(r, c)) {
                        showAlert(habit.getName() + " 今天已經打卡過了！");
                    } else {
                        habitManager.toggleCheckIn(r, c);
                        habitManager.saveHabits();
                        refreshTable();
                        showAlert("打卡成功！");
                    }
                });
                tableGrid.add(cell, col + 1, row + 1);
            }

            // 判斷全勤（7天皆打卡）
            boolean fullAttendance = true;
            if(!habit.isWeeklyAttendence()) fullAttendance = false;


            Label attendanceLabel = new Label(fullAttendance ? "O" : "");
            attendanceLabel.getStyleClass().add("full-attendance");
            attendanceLabel.setFont(Font.font(16));
            attendanceLabel.setAlignment(Pos.CENTER);
            attendanceLabel.setPrefWidth(80);
            tableGrid.add(attendanceLabel, 8, row + 1);
        }

        // 「完美打卡」
        int statusRow = habits.size() + 1;

        Label statusTitle = new Label("完美打卡");
        statusTitle.setFont(Font.font("Arial", FontWeight.BOLD, 9));
        statusTitle.getStyleClass().add("perfect-check");
        tableGrid.add(statusTitle, 0, statusRow);

        for (int col = 0; col < 7; col++) {
            boolean allChecked = true;
            for (Habit habit : habits) {
                if (!habit.isChecked(col)) {
                    allChecked = false;
                    break;
                }
            }
            Label status = new Label(allChecked ? "O" : "");
            status.setFont(Font.font(14));
            status.setPrefWidth(40);
            status.setAlignment(Pos.CENTER);
            status.getStyleClass().add("perfect-check");
            tableGrid.add(status, col + 1, statusRow);
        }

        // 計算並更新打卡率、完美打卡天數、總完成數量
        double rate = habitManager.getCompletionRate() * 100;
        int perfectDays = habitManager.getPerfectClockInDays();
        int totalComplete = habitManager.getCompletionTotal();

        checkRateLabel.setText("打卡率 : " + String.format("%.1f", rate) + "%");
        perfectDaysLabel.setText("完美打卡天數 : " + perfectDays + "天");
        totalCountLabel.setText("總完成數量 : " + totalComplete);
    }    

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("訊息");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
