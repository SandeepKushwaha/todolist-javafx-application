package com.kushwahatechnologies.todolist;

import com.kushwahatechnologies.todolist.models.TODOData;
import com.kushwahatechnologies.todolist.models.TODOItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DialogController {
    @FXML
    private TextField shortDescription;

    @FXML
    private TextArea detailedArea;

    @FXML
    private DatePicker deadLinePicker;

    public void initialize() {
        detailedArea.setWrapText(true);

    }

    public TODOItem processResults() {
        var shortDesc = shortDescription.getText().trim();
        var detail = detailedArea.getText().trim();
        var deadline = deadLinePicker.getValue();

        if ((shortDesc != null && !shortDesc.isEmpty()) && (detail != null && !shortDesc.isEmpty()) && !deadline.equals(null)) {
            var newItem = new TODOItem(shortDesc, detail, deadline);
            TODOData.getInstance().addTODOItem(newItem);
            return newItem;
        }

        return null;
    }
}
