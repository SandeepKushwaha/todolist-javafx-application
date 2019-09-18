package com.kushwahatechnologies.todolist.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class TODOData {
    private static TODOData instance = new TODOData();
    private static String filename = "TODOListItems.txt";

    private ObservableList<TODOItem> todoItems;
    private DateTimeFormatter formatter;

    public static TODOData getInstance() {
        return instance;
    }

    private TODOData() {
        formatter = DateTimeFormatter.ofPattern("EEEE MMMM dd, yyyy");
    }

    public ObservableList<TODOItem> getTODOItems() {
        return todoItems;
    }

//    public void setTodoItems(List<TODOItem> todoItems) {
//        this.todoItems = todoItems;
//    }

    public void addTODOItem(TODOItem item) {
        todoItems.add(item);
    }

    public void loadTODOItems() throws IOException {
        todoItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader bufferedReader = Files.newBufferedReader(path);

        String input;

        try {
            while ((input = bufferedReader.readLine()) != null) {
                String[] itemPieces = input.split("\t");

                String shortDesc = itemPieces[0];
                String details = itemPieces[1];
                String dateString = itemPieces[2];

                LocalDate date = LocalDate.parse(dateString, formatter);

                TODOItem item = new TODOItem(shortDesc, details, date);
                todoItems.add(item);
            }
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    }

    public void storeTODOItems() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter bufferedWriter = Files.newBufferedWriter(path);

        try {
            Iterator<TODOItem> iter = todoItems.iterator();
            while (iter.hasNext()) {
                TODOItem item = iter.next();
                bufferedWriter.write(String.format("%s\t%s\t%s",
                        item.getShortDesc(),
                        item.getDetails(),
                        item.getDeadLine().format(formatter)));
                bufferedWriter.newLine();
            }
        } finally {
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
        }
    }

    public void deleteTODOItem(TODOItem item) {
        todoItems.remove(item);
    }
}
