package myPackage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HabitTrackerApp {
    private static final String JSON_FILE = "habits.json";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HabitManager habitManager = new HabitManager();

            //讀取JSON檔案，預設讀取習慣資料
            File jsonFile = new File(JSON_FILE);
            if (jsonFile.exists()) {
                try (Reader reader = new FileReader(jsonFile)) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Habit>>() {
                    }.getType();
                    List<Habit> loadedHabits = gson.fromJson(reader, listType);

                    if (loadedHabits != null) {
                        for (Habit habit : loadedHabits) {
                            habit.getCheckInStatus();
                        }
                        habitManager.setHabits(loadedHabits);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "讀取習慣資料失敗：" + e.getMessage());
                }
            }

            HabitTrackerGUI gui = new HabitTrackerGUI(habitManager);
            gui.setVisible(true);

            //當程式關閉時儲存JSON資料
            gui.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    saveHabits(habitManager);
                    System.exit(0);
                }
            });
        });
    }

    // 儲存習慣清單到JSON
    private static void saveHabits(HabitManager habitManager) {
        try (Writer writer = new FileWriter(JSON_FILE)) {
            Gson gson = new Gson();
            gson.toJson(habitManager.getHabits(), writer);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "儲存習慣資料失敗：" + e.getMessage());
        }
    }
}
