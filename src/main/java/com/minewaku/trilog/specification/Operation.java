package com.minewaku.trilog.specification;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Operation {
	//works as passing data through constructors since enum doens't have key-value structure
    EQUALS(""),
    CONTAINS("like"),
    GREATER_THAN("gt"),
    LESS_THAN("lt"),
    STARTS_WITH("sw"),
    ENDS_WITH("ew");

    private final String symbol;
    
    Operation(String symbol) {
        this.symbol = symbol;
    }
    
    //This map is going to be generated in the runtime right after you run your application. The map will make the search faster since it o(1) for the map and o(n) for integrating through the enum
    public static final Map<String, Operation> SYMBOL_MAP = Stream.of(values())
            .collect(Collectors.toUnmodifiableMap(
                op -> op.symbol,
                op -> op,
                (existing, replacement) -> existing
            ));

    public String getSymbol() {
        return symbol;
    }
}