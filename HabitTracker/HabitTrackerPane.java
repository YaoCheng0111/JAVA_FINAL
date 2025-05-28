package myPackage;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class HabitTrackerPane extends BorderPane {
    private final HabitManager habitManager;
    private final GridPane tableGrid;

    public HabitTrackerPane(HabitManager habitManager) {
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
        setCenter(tableGrid);

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        Button addButton = new Button("+ Add Habit");
        Button deleteButton = new Button("- Delete Habit");
        buttonBox.getChildren().addAll(addButton, deleteButton);
        setBottom(buttonBox);

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

        tableGrid.add(new Label(""), 0, 0);
        for (int i = 0; i < 7; i++) {
            Label dayLabel = new Label(DayOfWeek.of((i + 7 - 1) % 7 + 1).name().substring(0, 3));
            dayLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            tableGrid.add(dayLabel, i + 1, 0);
        }

        List<Habit> habits = habitManager.getHabits();
        int today = LocalDate.now().getDayOfWeek().getValue() % 7;
        for (int row = 0; row < habits.size(); row++) {
            Habit habit = habits.get(row);
            Label nameLabel = new Label(habit.getName());
            tableGrid.add(nameLabel, 0, row + 1);

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
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("訊息");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
