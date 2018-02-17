/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taskers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author dalemusser
 * 
 * This example uses PropertyChangeSupport to implement
 * property change listeners.
 * 
 */
public class Task3 extends Thread {
    
    private int maxValue, notifyEvery;
    boolean exit = false;
    
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    private States state;
    
    public Task3(int maxValue, int notifyEvery)  {
        this.maxValue = maxValue;
        this.notifyEvery = notifyEvery;
        this.state = States.INACTIVE;
    }
    
    @Override
    public void run() {
        setState(States.ACTIVE);
        doNotify("Task3 is now " + state.name() + ".");
        for (int i = 0; i < maxValue; i++) {
            
            if (i % notifyEvery == 0) {
                doNotify("Task3 is " + state.name() + " at: " + i);
            }
            
            if (exit) {
                updateUser();
                return;
            }
        }
        setState(States.NATURAL_STOP);
        doNotify("Task3 has come to a " + state.name() +".");
        
        setState(States.INACTIVE);
        doNotify("Task3 is now " + state.name() + ".");
        try {
            this.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Task1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateUser() {
        doNotify("Task3 has come to a " + state.name() +".");
        setState(States.INACTIVE);
        doNotify("Task3 is now " + state.name() +".");
    }
    
    public void end() {
        exit = true;
    }
    
    // the following two methods allow property change listeners to be added
    // and removed
    public void addPropertyChangeListener(PropertyChangeListener listener) {
         pcs.addPropertyChangeListener(listener);
     }

     public void removePropertyChangeListener(PropertyChangeListener listener) {
         pcs.removePropertyChangeListener(listener);
     }
    
    private void doNotify(String message) {
        // this provides the notification through the property change listener
        Platform.runLater(() -> {
            // I'm choosing not to send the old value (second param).  Sending "" instead.
            pcs.firePropertyChange("message", "", message);
        });
    }
    
    public void setState(States state) {
        this.state = state;
    }
}