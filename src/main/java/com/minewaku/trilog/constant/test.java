package com.minewaku.trilog.constant;

import java.util.function.Supplier;

public class test {
    public Supplier<String> hallo(String username) {
        return () -> "hallo" + username;
    }
}
