package com.vpavlov.visualization.draw_model;

import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract class represents a titled line
 *
 * @author vpavlov
 */
public abstract class ATitledLine extends Region {

    /**
     * Title padding
     */
    protected final static double TITLE_PADDING = 2;

    /**
     * Line titles
     */
    protected final Set<String> titles = new HashSet<>();

    /**
     * Title element
     */
    protected final Text title = new Text();

    /**
     * Constructor
     *
     * @param titles titles
     */
    protected ATitledLine(Collection<String> titles) {
        this.title.getStyleClass().add("titled-line");
        this.titles.addAll(titles);
        this.getChildren().add(this.title);
        configureTitles();
    }

    /**
     * Sets all titles to the title element with comma delimiter
     */
    protected void configureTitles() {
        String allTitles = titles.toString().replace("[", "").replace("]", "");
        title.setText(allTitles);
    }

    /**
     * Calculates title placement
     */
    protected abstract void calculateTitle();

    /**
     * Titles getter
     *
     * @return set of line titles
     */
    public Set<String> getTitles() {
        return titles;
    }

    /**
     * Removes title from titles list
     *
     * @param title title to remove
     */
    public void removeTitle(String title) {
        titles.remove(title);
        configureTitles();
        calculateTitle();
    }

    /**
     * Add title to the line
     *
     * @param title title to add
     */
    public void addTitle(String title) {
        titles.add(title);
        configureTitles();
        calculateTitle();
    }

    /**
     * Add titles to the line
     *
     * @param titles titles to add
     */
    public void addTitles(Collection<String> titles) {
        this.titles.addAll(titles);
        configureTitles();
        calculateTitle();
    }

    /**
     * Remove all specified titles
     *
     * @param titles titles to remove
     */
    public void removeTitles(Collection<String> titles) {
        this.titles.removeAll(titles);
        configureTitles();
        calculateTitle();
    }
}
