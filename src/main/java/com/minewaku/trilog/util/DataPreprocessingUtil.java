package com.minewaku.trilog.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DataPreprocessingUtil {
	
	public static List<Integer> parseCommaSeparatedIds(String ids) {
        if (ids == null || ids.trim().isEmpty()) {
            return Collections.emptyList();
        }

        return Arrays.stream(ids.split(","))
                     .filter(s -> s != null && !s.trim().isEmpty())
                     .map(s -> {
                         try {
                             return Integer.parseInt(s.trim());
                         } catch (NumberFormatException e) {
                             // Log the error if needed, e.g., logger.warn("Invalid integer: {}", s);
                             return null;
                         }
                     })
                     .filter(Objects::nonNull)
                     .collect(Collectors.toList());
    }
}
