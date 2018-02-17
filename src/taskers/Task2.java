/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author dalemusser
 * 
 * This example uses a Notification functional interface.
 * This allows the use of anonymous inner classes or
 * lambda expressions to define the method that gets called
 * when a notification is to be made.
 */
public class Task2 extends Thread {
    
    private int maxValue, notifyEvery;
    boolean exit = false;
    
    private ArrayList<Notification> notifications = new ArrayList<>();
    
    private States state;
    
    public Task2(int maxValue, int notifyEvery)  {
        this.maxValue = maxValue;
        this.notifyEvery = notifyEvery;
        this.state = States.INACTIVE;
    }
    
    @Override
    public void run() {
        setState(States.ACTIVE);
        doNotify("Task2 is now " + state.name() + ".");
        for (int i = 0; i < maxValue; i++) {
            
            if (i % notifyEvery == 0) {
                doNotify("Task2 is " + state.name() + " at: " + i);
            }
            
            if (exit) {
                updateUser();
                return;
            }
        }
        setState(States.NATURAL_STOP);
        doNotify("Task2 has come to a " + state.name() +".");
        
        setState(States.INACTIVE);
        doNotify("Task2 is now " + state.name() + ".");
        try {
            this.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Task1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateUser() {
        doNotify("Task2 has come to a " + state.name() +".");
        setState(States.INACTIVE);
        doNotify("Task2 is now " + state.name() +".");
    }
    
    public void end() {
        exit = true;
    }
    
    // this method allows a notification handler to be registered to receive notifications
    public void setOnNotification(Notification notification) {
        this.notifications.add(notification);
    }
    
    private void doNotify(String message) {
        // this provides the notification through the registered notification handler
        for (Notification notification : notifications) {
            Platform.runLater(() -> {
                notification.handle(message);
            });
        }
    }
    
    public void setState(States state) {
        this.state = state;
    }
}