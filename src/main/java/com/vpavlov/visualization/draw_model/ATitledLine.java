package com.vpavlov.visualization.draw_model;

import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class ATitledLine extends Region {

    protected final Set<String> titles = new HashSet<>();

    protected final Text title = new Text();

    protected final static double TITLE_PADDING = 2;


    protected ATitledLine(Collection<String> titles){
        this.title.getStyleClass().add("titled-line");
        this.titles.addAll(titles);
        this.getChildren().add(this.title);
        configureTitles();
    }

    protected void configureTitles(){
        String allTitles = titles.toString().replace("[","").replace("]", "");
        title.setText(allTitles);
    }

    protected abstract void calculateTitle();

    public Set<String> getTitles(){
        return titles;
    }

    public void removeTitle(String title){
        titles.remove(title);
        configureTitles();
        calculateTitle();
    }

    public void addTitle(String title){
        titles.add(title);
        configureTitles();
        calculateTitle();
    }

    public void addTitles(Collection<String> titles) {
        this.titles.addAll(titles);
        configureTitles();
        calculateTitle();
    }

    public void removeTitles(Collection<String> titles) {
        this.titles.removeAll(titles);
        configureTitles();
        calculateTitle();
    }
}
