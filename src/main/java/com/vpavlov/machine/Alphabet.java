package com.vpavlov.machine;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Class represents an alphabet of symbols
 *
 * @author vpavlov
 * @version 20.11.2022
 */
public class Alphabet {

    /**
     * Symbols in the alphabet
     */
    private final Set<String> symbols;

    /**
     * Constructor
     *
     * @param symbols array of symbols in alphabet
     */
    public Alphabet(String... symbols) {
        this.symbols = new HashSet<>();
        this.symbols.addAll(Arrays.asList(symbols));
    }

    /**
     * Constructor
     *
     * @param symbols symbols in CSV format
     */
    public Alphabet(String symbols) {
        this.symbols = new HashSet<>();
        this.symbols.addAll(Arrays.asList(symbols.split(",")));
    }

    /**
     * Deep copy constructor
     *
     * @param alphabet alphabet to copy
     */
    public Alphabet(Alphabet alphabet) {
        this.symbols = new HashSet<>(alphabet.symbols);
    }

    /**
     * Symbols getter
     *
     * @return alphabet symbols
     */
    public Set<String> getSymbols() {
        return this.symbols;
    }

    /**
     * Check if the alphabet contains a specified symbol
     *
     * @param symbol symbol to check
     * @return true if it contains a specified symbol, else false
     */
    public boolean contains(String symbol) {
        return symbols.contains(symbol);
    }

    /**
     * Check if the alphabet contains all specified symbols
     *
     * @param symbols symbols to check
     * @return true if it contains all symbols, else false
     */
    public boolean containsAll(Collection<String> symbols) {
        return this.symbols.containsAll(symbols);
    }
}
