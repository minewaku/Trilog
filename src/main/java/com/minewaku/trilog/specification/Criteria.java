package com.minewaku.trilog.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Criteria {
    private String field;
    private Operation operation;
    private String value;
}