package myPackage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Scanner;

public class TimerTest{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        int startTime = input.nextInt();

        Timer timer = new Timer();

        TimerTask task = new TimerTask(){
            int count = startTime;

            @Override
            public void run(){
                if(count >=0){
                    System.out.println("Count down: " + count);
                    count--;
                }else{
                    System.out.println("Time Over!");
                    timer.cancel();
                }
            }  
        };



        timer.scheduleAtFixedRate(task, 0, 1000);
    }
}