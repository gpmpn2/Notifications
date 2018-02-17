/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Button;
import notifcationexamples.NotificationsUIController;

/**
 *
 * @author dalemusser
 * 
 * This example uses an object passed in with a notify()
 * method that gets called when a notification is to occur.
 * To accomplish this the Notifiable interface is needed.
 * 
 */
public class Task1 extends Thread {
    
    private int maxValue, notifyEvery;
    boolean exit = false;
    
    private Notifiable notificationTarget;
    
    private States state;
    
    public Task1(int maxValue, int notifyEvery)  {
        this.maxValue = maxValue;
        this.notifyEvery = notifyEvery;
        this.state = States.INACTIVE;
    }
    
    @Override
    public void run() {
        doNotify("Task1 is now " + state.name() + ".");
        for (int i = 0; i < maxValue; i++) {
            
            if (i % notifyEvery == 0) {
                doNotify("Task1 is " + state.name() + " at: " + i);
            }
            
            if (exit) {
                updateUser();
                return;
            }
        }
        
        setState(States.NATURAL_STOP);
        doNotify("Task1 has come to a " + state.name() +".");
        
        setState(States.INACTIVE);
        doNotify("Task1 is now " + state.name() + ".");
        try {
            this.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Task1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateUser() {
        doNotify("Task1 has come to a " + state.name() +".");
        setState(States.INACTIVE);
        doNotify("Task1 is now " + state.name() +".");
    }
    
    public void end() {
        exit = true;
    }
    
    public void setNotificationTarget(Notifiable notificationTarget) {
        this.notificationTarget = notificationTarget;
    }
    
    private void doNotify(String message) {
        // this provides the notification through a method on a passed in object (notificationTarget)
        if (notificationTarget != null) {
            Platform.runLater(() -> {
                notificationTarget.notify(message);
            });
        }
    }
    
    public void setState(States state) {
        this.state = state;
    }
}
