package com.vpavlov.services.machine;

import com.vpavlov.services.machine.exceptions.TransitionsExistException;
import com.vpavlov.visualization.draw_model.MachineNode;
import com.vpavlov.visualization.draw_model.TransitionLine;
import javafx.geometry.Point2D;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public final class MachineServiceFileManager {

    private MachineServiceFileManager(){}

    public static void saveToFile(MachineService machineService, File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writeNodes(writer, machineService);
        writeTransitions(writer,machineService);
        writer.close();
    }

    private static void writeNodes(BufferedWriter writer, MachineService machineService) throws IOException {
        for(MachineNode node : machineService.getNodes().values()){
            String title = node.getTitle();
            Point2D position = node.getPosition();
            boolean isStartNode = node.isStartNode();
            boolean isFinalNode = node.isFinalNode();
            writer.write(String.format("%s,%f,%f,%s,%s;",title, position.getX(), position.getY(),isStartNode, isFinalNode));
        }
        writer.newLine();
    }

    private static void writeTransitions(BufferedWriter writer, MachineService machineService) throws IOException {
        Set<TransitionLine> transitionLines = machineService.getTransitionLines();
        for (TransitionLine line : transitionLines){
            String start = line.getStart().getTitle();
            String end = line.getEnd().getTitle();
            Set<String> titles = line.getTitles();
            String titlesString = titles.toString().replace("[","").replace("]", "").replace(", ","+");
            writer.write(String.format("%s,%s,%s;",start,end, titlesString));
        }
    }

    public static MachineService createFromFile(File file) throws IOException{
        if (file==null) return null;
        MachineService machineService = new MachineService();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> linesList = reader.lines().toList();
        readNodes(machineService, linesList.get(0));
        readTransitions(machineService, linesList.get(1));
        reader.close();
        return machineService;
    }

    private static void readNodes(MachineService machineService, String nodesLine){
        String[] nodesString = nodesLine.split(";");
        for (String nodeString : nodesString){
            String[] params = nodeString.split(",");
            String title = params[0];
            double x = Double.parseDouble(params[1]);
            double y = Double.parseDouble(params[2]);
            boolean isStartNode = Boolean.parseBoolean(params[3]);
            boolean isFinalNode = Boolean.parseBoolean(params[4]);
            machineService.addMachineNode(title, x, y, isStartNode, isFinalNode);
        }
    }

    private static void readTransitions(MachineService machineService, String transitionsLine){
        String[] transitionsString = transitionsLine.split(";");
        for (String transitionString : transitionsString){
            String[] params = transitionString.split(",");
            String from = params[0];
            String to = params[1];
            List<String> symbols = Arrays.asList(params[2].split("\\+"));
            try {
                machineService.addTransitions(symbols, from, to);
            } catch (TransitionsExistException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
