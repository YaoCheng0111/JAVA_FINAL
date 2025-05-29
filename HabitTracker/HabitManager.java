package myPackage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HabitManager {
    private List<Habit> habits = new ArrayList<>();
    private static final String FILE_NAME = "JsonData/habits.json";

    public HabitManager() {
        loadHabits();
    }

    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    public void removeHabit(String habitName) {
        habits.removeIf(h -> h.getName().equals(habitName));
    }

    public List<Habit> getHabits() {
        return habits;
    }

    public List<String> getHabitNames() {
        List<String> names = new ArrayList<>();
        for (Habit h : habits) {
            names.add(h.getName());
        }
        return names;
    }

    public String getHabitName(int index) {
        return habits.get(index).getName();
    }

    public int getHabitCount() {
        return habits.size();
    }

    public boolean isChecked(int row, int col) {
        return habits.get(row).isChecked(col);
    }

    public void toggleCheckIn(int row, int col) {
        habits.get(row).toggleCheckIn(col);
    }

    public void saveHabits() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            new Gson().toJson(habits, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadHabits() {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            Type listType = new TypeToken<List<Habit>>() {
            }.getType();
            List<Habit> loaded = new Gson().fromJson(reader, listType);
            if (loaded != null) {
                for (Habit h : loaded) {
                    h.setCheckInStatus(h.getCheckInStatus());
                }
                this.habits = loaded;
            }
        } catch (Exception e) {
            this.habits = new ArrayList<>();
        }
    }
}