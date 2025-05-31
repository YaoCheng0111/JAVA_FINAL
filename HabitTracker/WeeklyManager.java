package myPackage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WeeklyManager {
    private Boolean newWeek;
    private LocalDate today;
    private LocalDate thisSunday;
    private List<LocalDate> thisWeek = new ArrayList<>();
    private List<List<LocalDate>> allWeeks = new ArrayList<>();
    private static final String FILE_NAME = "JsonData/week.json";

    //從json檔抓檔案
    public WeeklyManager() {
        today = LocalDate.now();
        thisSunday = today.minusDays(today.getDayOfWeek().getValue() % 7);
        newWeek = false;
        loadWeek();
    }

    //獲取每次打開程式時是否為新的一周
    public Boolean isNewWeek(){
        return newWeek;
    }

    //獲取當周第一天
    public LocalDate getFirsDate(){
        return thisWeek.get(0);
    }

    //獲取當周最後一天
    public LocalDate getLasDate(){
        return thisWeek.get(6);
    }

    private List<LocalDate> generateWeekStartingFrom(LocalDate sunday) {
        List<LocalDate> week = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            week.add(sunday.plusDays(i));
        }
        return week;
    }

    //設定成這周七天
    private void setThisWeek(){
        thisWeek.clear();
        for(int i=0;i<7;i++){
            thisWeek.add(thisSunday.plusDays(i)); 
        }
    }

    //存檔
    public void saveWeek() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            List<List<String>> raw = allWeeks.stream()
                             .map(week -> week.stream().map(LocalDate::toString).collect(Collectors.toList()))
                             .collect(Collectors.toList());
            
            new Gson().toJson(raw, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //讀檔
    public void loadWeek() {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            Type listType = new TypeToken<List<List<String>>>() {}.getType();
            List<List<String>> raw = new Gson().fromJson(reader, listType);
            
            if (raw != null && !raw.isEmpty()) {
                allWeeks = raw.stream()
                                 .map(week -> week.stream().map(LocalDate::parse).collect(Collectors.toList())) 
                                 .collect(Collectors.toList()); 
                
                List<LocalDate> lastSaveWeek = allWeeks.get(allWeeks.size()-1);
                LocalDate lastSunday = lastSaveWeek.get(0);
                
                if(!lastSunday.equals(thisSunday)){
                    newWeek = true;

                    LocalDate nextSunday = lastSunday.plusWeeks(1);
                    while(!nextSunday.isAfter(thisSunday)){
                        allWeeks.add(generateWeekStartingFrom(nextSunday));
                        nextSunday = nextSunday.plusWeeks(1);
                    }

                    thisWeek = generateWeekStartingFrom(nextSunday);
                    allWeeks.add(thisWeek);

                    saveWeek();
                }else{
                    newWeek = false;
                    thisWeek = lastSaveWeek;
                }
            }else{//第一次生成
                this.newWeek = true;
                thisWeek = generateWeekStartingFrom(thisSunday);
                allWeeks.add(thisWeek);
                saveWeek();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
