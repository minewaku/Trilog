package com.minewaku.trilog.search.repository.custom;

import java.util.List;

import com.minewaku.trilog.search.document.ESUser;

import java.io.IOException;

public interface ESUserRepositoryCustom {
	List<Integer> suggestUsers(String input) throws IOException;
}
