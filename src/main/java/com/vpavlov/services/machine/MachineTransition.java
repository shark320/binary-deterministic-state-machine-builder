package com.vpavlov.services.machine;

/**
 * Machine transition record. Used for logging machine actions
 *
 * @param symbol transition symbol
 * @param from   from node title
 * @param to     to node title
 * @author vpavlov
 */
public record MachineTransition(String symbol, String from, String to) {

    @Override
    public String toString() {
        return String.format("<%s> --[%s]--> <%s>", from, symbol, to);
    }
}
