package com.minewaku.trilog.search.repository.custom;

import java.io.IOException;

import com.minewaku.trilog.dto.common.response.CursorPage;
import com.minewaku.trilog.dto.model.Cursor;

public interface ESUserRepositoryCustom {
	CursorPage<Integer> suggestUsers(String keywrord, Cursor cursor) throws IOException;
}
