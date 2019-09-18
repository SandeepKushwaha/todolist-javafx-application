package com.kushwahatechnologies.todolist;

import com.kushwahatechnologies.todolist.models.TODOData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void init() throws Exception {
        try {
            TODOData.getInstance().loadTODOItems();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));
        primaryStage.setTitle("TODO List");
        primaryStage.setScene(new Scene(root, 850, 475));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        try {
            TODOData.getInstance().storeTODOItems();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
