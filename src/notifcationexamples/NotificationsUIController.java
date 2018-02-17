/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notifcationexamples;

import java.beans.PropertyChangeEvent;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import taskers.*;

/**
 * FXML Controller class
 *
 * @author dalemusser
 */
public class NotificationsUIController implements Initializable, Notifiable {

    @FXML
    private TextArea textArea;
    
    private Task1 task1;
    private Task2 task2;
    private Task3 task3;
    
    @FXML
    private Button taskOneButton;
    
    @FXML
    private Button taskTwoButton;
    
    @FXML
    private Button taskThreeButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        taskOneButton.setText("Start Task 1");
        taskTwoButton.setText("Start Task 2");
        taskThreeButton.setText("Start Task 3");
    }
    
    public void start(Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (task1 != null) task1.end();
                if (task2 != null) task2.end();
                if (task3 != null) task3.end();
            }
        });
    }
    
    @FXML
    public void startTask1(ActionEvent event) {
        if (task1 == null) {
            taskOneButton.setText("End Task 1");
            task1 = new Task1(2147483647, 1000000);
            task1.setState(States.ACTIVE);
            task1.setNotificationTarget(this);
            task1.start();
        } else if(task1.isAlive()) {
            task1.setState(States.FORCE_STOP);
            task1.end();
            try {
                task1.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(NotificationsUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void notify(String message) {
        if (message.equals("Task1 is now INACTIVE.")) {
            taskOneButton.setText("Start Task 1");
            task1 = null;
        }
        textArea.appendText(message + "\n");
    }
    
    @FXML
    public void startTask2(ActionEvent event) {
        if (task2 == null) {
            taskTwoButton.setText("End Task 2");
            task2 = new Task2(2147483647, 1000000);
            task2.setOnNotification((String message) -> {
                if (message.equals("Task2 is now INACTIVE.")) {
                    taskTwoButton.setText("Start Task 2");
                    task2 = null;
                }
                textArea.appendText(message + "\n");
            });
            
            task2.start();
        } else if(task2.isAlive()) {
            task2.setState(States.FORCE_STOP);
            task2.end();
            try {
                task2.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(NotificationsUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }    
    }
    
    @FXML
    public void startTask3(ActionEvent event) {
        if (task3 == null) {
            taskThreeButton.setText("End Task 3");
            task3 = new Task3(2147483647, 1000000);
            // this uses a property change listener to get messages
            task3.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                if (((String)evt.getNewValue()).equals("Task3 is now INACTIVE.")) {
                    taskThreeButton.setText("Start Task 3");
                    task3 = null;
                }
                textArea.appendText((String)evt.getNewValue() + "\n");
            });
            
            task3.start();
        } else if(task3.isAlive()) {
            task3.setState(States.FORCE_STOP);
            task3.end();
            try {
                task3.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(NotificationsUIController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
    } 
}
