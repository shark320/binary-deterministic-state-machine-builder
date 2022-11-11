package com.vpavlov.visualization.draw_model;

import com.vpavlov.visualization.controller.PrimaryController;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class MachineNode extends Region {

    private static final Color DEFAULT_NODE_COLOR = Color.AQUA;

    private static final Color CURRENT_NODE_COLOR = Color.RED;

    private static final Color START_NODE_BORDER_COLOR = Color.YELLOW;

    private static final Color END_NODE_BORDER_COLOR = Color.ORANGE;

    private static final double DEFAULT_NODE_RADIUS = 20;

    private static final double SPECIAL_NODE_BORDER_WIDTH = DEFAULT_NODE_RADIUS*0.2;

    private Circle circle;

    private Text title;

    public MachineNode(double x, double y, String title){
        circle = new Circle(x, y, DEFAULT_NODE_RADIUS, DEFAULT_NODE_COLOR);
        this.title = createTitle(x,y,title);
        this.getChildren().addAll(circle,this.title);
        this.setOnMouseClicked(PrimaryController.machineNodeMouseClickHandler);
    }

    private Text createTitle(double x, double y, String title){
        Text t = new Text(title);
        double width = t.getLayoutBounds().getWidth();
        double height = t.getLayoutBounds().getHeight();
        t.setX(x-width/2);
        t.setY(y+height/4);
        //t.setY(y);
        return t;
    }

    public void setAsStartNode(){
        circle.setStrokeWidth(SPECIAL_NODE_BORDER_WIDTH);
        circle.setStroke(START_NODE_BORDER_COLOR);
    }
}
