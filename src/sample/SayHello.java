package sample;

import java.util.TimerTask;

/**
 * Created by shestakov.aa on 02.09.2016.
 */
public class SayHello extends TimerTask {
    @Override
    public void run() {
        System.out.println("Timer1!");
    }
}
