package myPackage;

import java.util.ArrayList;
import java.util.List;

public class HabitManager {
    private List<Habit> habits;

    public HabitManager() {
        habits = new ArrayList<>();
    }

    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    public List<Habit> getHabits() {
        return habits;
    }

    public int getHabitCount() {
        return habits.size();
    }

    public String getHabitName(int index) {
        return habits.get(index).getName();
    }

    public boolean isChecked(int habitIndex, int dayIndex) {
        return habits.get(habitIndex).getCheckInStatus().get(dayIndex);
    }

    public void toggleCheckIn(int habitIndex, int dayIndex) {
        Habit habit = habits.get(habitIndex);
        List<Boolean> status = habit.getCheckInStatus();
        status.set(dayIndex, !status.get(dayIndex));
    }

    public void setHabits(List<Habit> newHabits) {
        habits.clear();
        if (newHabits != null) {
            habits.addAll(newHabits);
        }
    }

}
