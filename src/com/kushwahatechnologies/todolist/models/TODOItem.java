package com.kushwahatechnologies.todolist.models;

import java.time.LocalDate;
/**
 * Data Class for the TODOItem
 * which provide the sample data structure
 */
public class TODOItem {
    private String shortDesc;
    private String details;
    private LocalDate deadLine;

    /**
     * Constructor for TODOItem initialization
     *
     * @param shortDesc used for short Description
     *                      of TODOTask typed String
     * @param details   used for the Details typed String
     * @param deadLine  used for local date typed LocalDate
     *
     */
    public TODOItem(String shortDesc, String details, LocalDate deadLine) {
        this.shortDesc = shortDesc;
        this.details = details;
        this.deadLine = deadLine;
    }

    /**
     * Setters and Getters of the shortDesc, details and deadLine.
     * */
    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getDetails() {
        return details;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    /**
     * Overriding method toString for
     * default object view via toString() method
     * */
    @Override
    public String toString() {
        return this.shortDesc;
    }
}
