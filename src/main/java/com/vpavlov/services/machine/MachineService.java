package com.vpavlov.services.machine;

import com.vpavlov.App;
import com.vpavlov.machine.Alphabet;
import com.vpavlov.machine.Machine;
import com.vpavlov.services.api.Service;
import com.vpavlov.visualization.draw_model.MachineNode;
import com.vpavlov.visualization.draw_model.TransitionLine;
import javafx.geometry.Point2D;
import javafx.scene.Group;

import java.util.*;

public class MachineService implements Service {

    private final Alphabet alphabet;

    private final Machine machine;

    private final Map<String,MachineNode> nodes = new HashMap<>();

    private final Map<MachineNode, Map<MachineNode, TransitionLine>> transitionsMap = new HashMap<>();

    private final Group machineGraph = new Group();

    private int machineNodesCount = 0;

    public MachineService(){
        alphabet = new Alphabet(App.getProperties().getProperty("alphabet"));
        machine = new Machine(alphabet);
    }


    public boolean markAsStartState(MachineNode node){
        String title = node.getTitle();
        if (machine.setStartState(title)){
            node.setAsStartNode();
            return true;
        }else{
            return false;
        }
    }

    public String makeTransition(String symbol){
        return machine.transition(symbol);
    }

    public void addMachineNode(Point2D point){
        String title = String.valueOf((char)('A' + (machineNodesCount++)));
        MachineNode node = new MachineNode(point.getX(), point.getY(),title);
        machine.addState(title);
        nodes.put(title, node);
        transitionsMap.put(node, new HashMap<>());
        machineGraph.getChildren().add(node);
    }

    public boolean addTransition(String symbol, MachineNode fromNode, MachineNode toNode){
        String from = fromNode.getTitle();
        String to = toNode.getTitle();
        if (machine.addTransition(symbol, from, to)){
            TransitionLine transitionLine = transitionsMap.get(toNode).get(toNode);
            if (transitionLine == null){
                transitionLine = new TransitionLine(fromNode, toNode, symbol);
                machineGraph.getChildren().add(transitionLine);
            }else{
                transitionLine.addTitle(symbol);
            }
            return true;
        }else{
            return false;
        }
    }

    public Map<String, MachineNode> getNodes(){
        return nodes;
    }

    public Group getMachineGraph(){
        return machineGraph;
    }

    public Machine getMachine(){
        return machine;
    }

}
