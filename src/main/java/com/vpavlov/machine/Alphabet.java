package com.vpavlov.machine;

import java.util.Arrays;
import java.util.Collection;
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

    public boolean contains(String symbol){
        return symbols.contains(symbol);
    }

    public boolean containsAll(Collection<String> symbols){
        return this.symbols.containsAll(symbols);
    }

    public void addSymbol(String symbol){
        symbols.add(symbol);
    }
}
