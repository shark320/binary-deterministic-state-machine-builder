package com.vpavlov.services.machine;

import com.vpavlov.services.machine.exceptions.TransitionsExistException;
import com.vpavlov.visualization.draw_model.MachineNode;
import com.vpavlov.visualization.draw_model.TransitionLine;
import javafx.geometry.Point2D;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Machine service file manager static class. Is used for reading and writing machines from files.
 *
 * @author vpavlov
 * @see MachineService
 */
public final class MachineServiceFileManager {

    /**
     * Private empty constructor
     */
    private MachineServiceFileManager() {
    }

    /**
     * Saves given machine service to the file
     *
     * @param machineService machine service to save
     * @param file           file to save into
     * @throws IOException if any
     */
    public static void saveToFile(MachineService machineService, File file) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writeNodes(writer, machineService);
        writeTransitions(writer, machineService);
        writer.close();
    }

    /**
     * Writes given machine service nodes with specific format:<br>
     * [name],[x],[y],[is start node],[is final node]<br>
     * with semicolon delimiters
     *
     * @param writer         writer to write data
     * @param machineService machine service with nodes to save
     * @throws IOException if any
     */
    private static void writeNodes(BufferedWriter writer, MachineService machineService) throws IOException {
        for (MachineNode node : machineService.getNodes().values()) {
            String title = node.getTitle();
            Point2D position = node.getPosition();
            boolean isStartNode = node.isStartNode();
            boolean isFinalNode = node.isFinalNode();
            writer.write(String.format("%s,%f,%f,%s,%s;", title, position.getX(), position.getY(), isStartNode, isFinalNode));
        }
        writer.newLine();
    }

    /**
     * Writes given machine service transitions with specific format:<br>
     * [start node title],[end node title],[transition symbols]<br>
     * with semicolon delimiters
     *
     * @param writer         writer to write data
     * @param machineService machine service with transitions to save
     * @throws IOException if any
     */
    private static void writeTransitions(BufferedWriter writer, MachineService machineService) throws IOException {
        Set<TransitionLine> transitionLines = machineService.getTransitionLines();
        for (TransitionLine line : transitionLines) {
            String start = line.getStart().getTitle();
            String end = line.getEnd().getTitle();
            Set<String> titles = line.getTitles();
            String titlesString = titles.toString().replace("[", "").replace("]", "").replace(", ", "+");
            writer.write(String.format("%s,%s,%s;", start, end, titlesString));
        }
    }

    /**
     * Reads and creates machine service from file
     *
     * @param file file to read from
     * @return created machine service
     * @throws IOException if any
     */
    public static MachineService createFromFile(File file) throws IOException {
        MachineService machineService = new MachineService();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> linesList = reader.lines().toList();
        readNodes(machineService, linesList.get(0));
        if (!readTransitions(machineService, linesList.get(1))) {
            return null;
        }
        reader.close();
        return machineService;
    }

    /**
     * Reads nodes from line with specific format
     * [name],[x],[y],[is start node],[is final node]<br>
     * with semicolon delimiters
     *
     * @param machineService machine service to add nodes to
     * @param nodesLine      string with nodes with specific format
     */
    private static void readNodes(MachineService machineService, String nodesLine) {
        String[] nodesString = nodesLine.split(";");
        for (String nodeString : nodesString) {
            String[] params = nodeString.split(",");
            String title = params[0];
            double x = Double.parseDouble(params[1]);
            double y = Double.parseDouble(params[2]);
            boolean isStartNode = Boolean.parseBoolean(params[3]);
            boolean isFinalNode = Boolean.parseBoolean(params[4]);
            machineService.addMachineNode(title, x, y, isStartNode, isFinalNode);
        }
    }

    /**
     * Reads transitions from line with specific format:<br>
     * [start node title],[end node title],[transition symbols]<br>
     * with semicolon delimiters
     *
     * @param machineService  machine service to add transitions into
     * @param transitionsLine line with data
     * @return true if successful, false otherwise
     */
    private static boolean readTransitions(MachineService machineService, String transitionsLine) {
        String[] transitionsString = transitionsLine.split(";");
        for (String transitionString : transitionsString) {
            String[] params = transitionString.split(",");
            String from = params[0];
            String to = params[1];
            List<String> symbols = Arrays.asList(params[2].split("\\+"));
            try {
                if (!machineService.addTransitions(symbols, from, to)) {
                    return false;
                }
            } catch (TransitionsExistException e) {
                throw new RuntimeException(e);
            }
        }

        return true;
    }
}
