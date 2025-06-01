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


    //設定成這周七天
    public void setThisWeek(){
        thisWeek.clear();
        for(int i=0;i<7;i++){
            thisWeek.add(thisSunday.plusDays(i)); 
        }
    }

    //存檔
    public void saveWeek() {
        try (FileWriter writer = new FileWriter(FILE_NAME)) {
            List<String> weekStrings = thisWeek.stream()
                             .map(LocalDate::toString)
                             .collect(Collectors.toList());
            
            new Gson().toJson(weekStrings, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //讀檔
    public void loadWeek() {
        try (FileReader reader = new FileReader(FILE_NAME)) {
            Type listType = new TypeToken <List<String>>() {}.getType();
            List<String> loaded = new Gson().fromJson(reader, listType);
            
            if (loaded != null && !loaded.isEmpty()) {
                this.thisWeek = loaded.stream()Add commentMore actions
                                 .map(LocalDate::parse) 
                                 .collect(Collectors.toList());                
                if(thisWeek.get(0).equals(thisSunday)){//同一周
                    this.newWeek = false;
                }else{//不同周
                    this.newWeek = true;
                    setThisWeek();
                    saveWeek();
                }
            }else{//第一次生成
                this.newWeek = true;
                setThisWeek();
                saveWeek();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
