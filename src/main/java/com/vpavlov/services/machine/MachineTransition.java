package com.vpavlov.services.machine;

public record MachineTransition(String symbol, String from, String to) {

    @Override
    public String toString() {
        return String.format("<%s> --[%s]--> <%s>", from, symbol, to);
    }
}
