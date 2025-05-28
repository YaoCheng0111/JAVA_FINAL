package myPackage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HabitManager {
    private int completionTotal;
    private double completionRate;
    private Boolean[] perfectClockIn = {false,false,false,false,false,false,false};
    private List<Habit> habits = new ArrayList<>();
    private static final String FILE_NAME = "habits.json";

    //從json檔抓檔案
    public HabitManager() {
        loadHabits();
    }

    //add habit to list
    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    //remove habit from list
    public void removeHabit(String habitName) {
        habits.removeIf(h -> h.getName().equals(habitName));
    }

    //get all habit
    public List<Habit> getHabits() {
        return habits;
    }

    //get all habit names
    public List<String> getHabitNames() {
        List<String> names = new ArrayList<>();
        for (Habit h : habits) {
            names.add(h.getName());
        }
        return names;
    }

    //依照行獲取habit name
    public String getHabitName(int index) {
        return habits.get(index).getName();
    }

    //get completionTotal
    public int getCompletionTotal(){
        completionTotal = 0;
        for(Habit h:habits){
            for(int i=0;i<7;i++){
                if(h.isChecked(i)){
                    completionTotal++;
                }
            }
        }
        return completionTotal;
    }

    //get habit 總數
    public int getHabitCount() {
        return habits.size();
    }

    //get completionRate
    public double getCompletionRate(){
        completionRate = getCompletionTotal() / getHabitCount()*7;
        return completionRate;
    }

    //依照table行列找isChecked
    public Boolean isChecked(int row, int col) {
        return habits.get(row).isChecked(col);
    }

    //當日是否為完美打卡
    public Boolean isPerfectClockIn(int dayIndex){
        for(Habit h:habits){
            if(!h.isChecked(dayIndex)){
                perfectClockIn[dayIndex]= false;
                return false;
            }
        }
        perfectClockIn[dayIndex] = true;
        return true;
    }

    //完美打卡日數
    public int getPerfectClockInDays(){
        int days=0;
        for(int i=0;i<7;i++){
            if(isPerfectClockIn(i)) days++;
        }
        return days;
    }

    //依照table行列打勾
    public void toggleCheckIn(int row, int col) {
        habits.get(row).toggleCheckIn(col);
    }

    //存檔
    public void saveHabits() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            new Gson().toJson(habits, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //讀檔
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
