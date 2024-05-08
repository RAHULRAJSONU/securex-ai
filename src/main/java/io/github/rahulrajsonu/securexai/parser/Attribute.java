package io.github.rahulrajsonu.securexai.parser;

import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
public enum Attribute {
    OBJECT_RELATION_SEPARATOR("#"),
    RELATION_USER_SEPARATOR("@"),
    NAMESPACE_OBJECT_SEPARATOR(":"),
    PATH_SEPARATOR("/"),
    COMBINER("&"),
    EXCLUDE("!"),
    ALL("*"),
    ;

    private final String symbol;

    Attribute(String value) {
        this.symbol = value;
    }

    public boolean presentIn(@NonNull String str) {
        return str.contains(symbol);
    }

    public boolean equals(@NonNull String str) {
        return str.equals(symbol);
    }

    public List<String> splitToList(@NonNull String str) {
        return List.of(str.split(symbol));
    }

    public String[] split(@NonNull String str) {
        return str.split(symbol);
    }

    public int indexIn(@NonNull String str) {
        return str.indexOf(symbol);
    }

    @Override
    public String toString() {
        return symbol;
    }
}
