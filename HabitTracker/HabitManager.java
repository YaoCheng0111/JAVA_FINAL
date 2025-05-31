package myPackage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HabitManager {
    private List<Habit> habits = new ArrayList<>();
    private static final String FILE_NAME = "JsonData/habits.json";

    // 從 json 檔抓檔案（預設載入第 0 週）
    public HabitManager() {
        loadHabits(0);
    }

    // add habit to list
    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    // remove habit from list
    public void removeHabit(String habitName) {
        habits.removeIf(h -> h.getName().equals(habitName));
    }

    // get all habit
    public List<Habit> getHabits() {
        return habits;
    }

    // get all habit names
    public List<String> getHabitNames() {
        List<String> names = new ArrayList<>();
        for (Habit h : habits) {
            names.add(h.getName());
        }
        return names;
    }

    // 依照行獲取 habit name
    public String getHabitName(int index) {
        return habits.get(index).getName();
    }

    // get completionTotal
    public int getCompletionTotal() {
        int completionTotal = 0;
        for (Habit h : habits) {
            for (int i = 0; i < 7; i++) {
                if (h.isChecked(i)) {
                    completionTotal++;
                }
            }
        }
        return completionTotal;
    }

    // get habit 總數
    public int getHabitCount() {
        return habits.size();
    }

    // get completionRate
    public double getCompletionRate() {
        if (getHabitCount() == 0) return 0.0;
        return (double) getCompletionTotal() / (getHabitCount() * 7);
    }

    // 依照 table 行列找 isChecked
    public Boolean isChecked(int row, int col) {
        return habits.get(row).isChecked(col);
    }

    // 當日是否為完美打卡
    public Boolean isPerfectClockIn(int dayIndex) {
        for (Habit h : habits) {
            if (!h.isChecked(dayIndex)) {
                return false;
            }
        }
        return true;
    }

    // 完美打卡日數
    public int getPerfectClockInDays() {
        int days = 0;
        for (int i = 0; i < 7; i++) {
            if (isPerfectClockIn(i)) days++;
        }
        return days;
    }

    // 依照 table 行列打勾
    public void toggleCheckIn(int row, int col) {
        habits.get(row).toggleCheckIn(col);
    }

    // 當偵測到是新的一周則執行 reset 所有勾勾
    public void resetAllStatus() {
        for (Habit h : habits) {
            for (int i = 0; i < 7; i++) {
                h.getCheckInStatus().set(i, false);
            }
        }
        saveHabits(0);
    }

    // 存檔
    public void saveHabits(int index) {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            // 先讀原始 JSON 二維陣列
            JsonArray fullJsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            // 準備新的這一列的 habits JSON
            Gson gson = new Gson();
            JsonElement newRow = gson.toJsonTree(this.habits); // 目前 habits 是一維陣列

            // 替換第 index 列
            fullJsonArray.set(index, newRow);

            // 存回檔案
            try (FileWriter writer = new FileWriter(FILE_NAME)) {
                gson.toJson(fullJsonArray, writer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 僅載入第 index 週
    public void loadHabits(int index) {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            JsonArray jsonArray = JsonParser.parseReader(reader).getAsJsonArray();

            List<Habit> loadedHabits = new ArrayList<>();

            for (JsonElement elem : jsonArray) {
                JsonObject obj = elem.getAsJsonObject();

                String name = obj.get("name").getAsString();
                boolean weeklyAttendance = obj.get("weeklyAttendence").getAsBoolean();

                // 取出二維陣列的 checkInStatus
                JsonArray checkIn2D = obj.getAsJsonArray("checkInStatus");

                List<Boolean> oneWeekStatus = new ArrayList<>();

                // 如果 index 合法，就讀那一行；不合法就補 false
                if (index >= 0 && index < checkIn2D.size()) {
                    JsonArray oneWeekArray = checkIn2D.get(index).getAsJsonArray();
                    for (JsonElement statusElem : oneWeekArray) {
                        oneWeekStatus.add(statusElem.getAsBoolean());
                    }
                } else {
                    // 若 index 不在範圍，補 7 個 false
                    for (int i = 0; i < 7; i++) {
                        oneWeekStatus.add(false);
                    }
                }

                Habit habit = new Habit(name);
                habit.setCheckInStatus(oneWeekStatus);
                habit.setWeeklyAttendence(weeklyAttendance);

                loadedHabits.add(habit);
            }

            this.habits = loadedHabits;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
