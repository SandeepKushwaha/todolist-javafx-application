package com.kushwahatechnologies.todolist;

import com.kushwahatechnologies.todolist.models.TODOData;
import com.kushwahatechnologies.todolist.models.TODOItem;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {

    private List<TODOItem> todoItems;

    @FXML
    private ListView<TODOItem> todoListView;
    @FXML
    private TextArea itemDetailedTextArea;
    @FXML
    private Label deadLineLabel;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu listContextMenu;
    @FXML
    private Button createNewTODOItem;
    @FXML
    private ImageView createNewTODOItemImage;
    @FXML
    private ToggleButton filterToggleButton;
    @FXML
    private ImageView filterTodayItemsImageView;

    private FilteredList<TODOItem> filteredList;

    private Predicate<TODOItem> allItems;
    private Predicate<TODOItem> todayItems;

    public void initialize() {
//        TODOItem item_1 = new TODOItem("Mail birthday card", "Buy a 24th birthday card for Dev", LocalDate.of(2019, Month.NOVEMBER, 24));
//        TODOItem item_2 = new TODOItem("Doctor's appointment", "See Do. Smith at 12 street. Being paperwork", LocalDate.of(2019, Month.OCTOBER, 24));
//        TODOItem item_3 = new TODOItem("Finish design proposal for client", "I promised Ang I'd email website mockups by Monday 30th September", LocalDate.of(2019, Month.SEPTEMBER, 30));
//        TODOItem item_4 = new TODOItem("Pickup Rahul at the train station", "Rahul's arriving on October 2 on the 4:00 train", LocalDate.of(2019, Month.OCTOBER, 2));
//        TODOItem item_5 = new TODOItem("Pick up dry cleaning", "The cloths should be ready by wednesday", LocalDate.of(2019, Month.SEPTEMBER, 25));
//
//        todoItems = new ArrayList<TODOItem>();
//        todoItems.add(item_1);
//        todoItems.add(item_2);
//        todoItems.add(item_3);
//        todoItems.add(item_4);
//        todoItems.add(item_5);
//
//        // create the storage file
//        TODOData.getInstance().setTodoItems(todoItems);

        // method handleClickListView used for the handle the working ListView.
        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(event -> {
            TODOItem item = todoListView.getSelectionModel().getSelectedItem();
            deleteItem(item);
        });
        listContextMenu.getItems().addAll(deleteMenuItem);

        createNewTODOItem.setTooltip(new Tooltip("Create a new TODO Item."));
        createNewTODOItemImage.setImage(new Image("/toolbarButtonGraphics/general/New24.gif"));
        filterTodayItemsImageView.setImage(new Image("/toolbarButtonGraphics/text/AlignLeft24.gif"));

        todoListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                var item = todoListView.getSelectionModel().getSelectedItem();
                itemDetailedTextArea.setWrapText(true);
                itemDetailedTextArea.setText(item.getDetails());
                var dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE MMMM dd, yyyy");
                deadLineLabel.setText(item.getDeadLine().format(dateTimeFormatter));
            }
        });

        allItems = item -> true;
        todayItems = item -> item.getDeadLine().equals(LocalDate.now());

        filteredList = new FilteredList<>(TODOData.getInstance().getTODOItems(), allItems);

//        SortedList<TODOItem> sortedList = new SortedList<>(filteredList, (o1, o2) -> o1.getDeadLine().compareTo(o2.getDeadLine()));
        SortedList<TODOItem> sortedList = new SortedList<>(filteredList, Comparator.comparing(TODOItem::getDeadLine));

//        todoListView.getItems().setAll(todoItems);
//        todoListView.getItems().setAll(TODOData.getInstance().getTODOItems()); // using singleton class TODOData
//        todoListView.setItems(TODOData.getInstance().getTODOItems());
        todoListView.setItems(sortedList);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoListView.getSelectionModel().selectFirst();

        todoListView.setCellFactory(param -> {
            var cell = new ListCell<TODOItem>() {
                @Override
                protected void updateItem(TODOItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item.getShortDesc());
                        if (item.getDeadLine().isBefore(LocalDate.now()) || item.getDeadLine().equals(LocalDate.now())) {
                            setTextFill(Color.RED);
                        } else if (item.getDeadLine().equals(LocalDate.now().plusDays(1))) {
                            setTextFill(Color.FIREBRICK);
                        }
                    }
                }
            };

            cell.emptyProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(listContextMenu);
                }
            });

            return cell;
        });
    }



    @FXML
    public void showNewItemDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add new TODO item");
        dialog.setHeaderText("Use this dialog to create a new TODO item");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todo_item_dialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DialogController controller = fxmlLoader.getController();
            var newItem = controller.processResults();
            // todoListView.getItems().setAll(TODOData.getInstance().getTODOItems());
            if (newItem != null) {
                todoListView.getSelectionModel().select(newItem);
            } else {
                System.out.println("Trying to add Empty Item attempt.");
            }
            System.out.println("OK pressed");
        } else {
            System.out.println("Cancel pressed");
        }
    }

    /**
     * method handleClickListView used for the handle the working ListView.
     */
    @FXML
    public void handleClickListView() {
        var item = todoListView.getSelectionModel().getSelectedItem();
        itemDetailedTextArea.setText(item.getDetails());
        deadLineLabel.setText(item.getDeadLine().toString());
    }

    public void deleteItem(TODOItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete TODO item");
        alert.setHeaderText("Delete item: " + item.getShortDesc());
        alert.setContentText("Are you sure? Press OK to confirm, or cancel to back out.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            TODOData.getInstance().deleteTODOItem(item);
        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        TODOItem selectedItem = todoListView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                deleteItem(selectedItem);
            }
        }
    }

    @FXML
    public void handleFilterButton(ActionEvent event) {
        TODOItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
        if (filterToggleButton.isSelected()) {
            filteredList.setPredicate(todayItems);
            if (filteredList.isEmpty()) {
                itemDetailedTextArea.clear();
                deadLineLabel.setText("");
            } else if (filteredList.contains(selectedItem)) {
                todoListView.getSelectionModel().select(selectedItem);
            } else {
                todoListView.getSelectionModel().selectFirst();
            }
        } else {
            filteredList.setPredicate(allItems);
            if (filteredList.contains(selectedItem)) {
                todoListView.getSelectionModel().select(selectedItem);
            } else {
                todoListView.getSelectionModel().selectLast();
            }
        }
    }

    @FXML
    public void handleExitApplication(ActionEvent event) {
        Platform.exit();
    }
}
