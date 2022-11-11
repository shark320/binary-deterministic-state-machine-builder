package com.vpavlov.machine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Alphabet {

    private final Set<String> symbols = new HashSet<>();

    public Alphabet (String... symbols){
        this.symbols.addAll(Arrays.asList(symbols));
    }

    public Alphabet(String symbols){
        this.symbols.addAll(Arrays.asList(symbols.split(",")));
    }

    public Set<String> getSymbols(){
        return this.symbols;
    }

    public void addSymbol(String symbol){
        symbols.add(symbol);
    }
}
