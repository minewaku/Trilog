package com.minewaku.trilog.search.repository.custom;

import java.io.IOException;

import com.minewaku.trilog.dto.common.response.CursorPage;
import com.minewaku.trilog.dto.model.Cursor;

//only be used for search purpose and only return ids of data
public interface ESPostRepositoryCustom {
	CursorPage<Integer> searchByHashtag(String keyword, Cursor cursor) throws IOException;
	CursorPage<Integer> searchByContent(String keyword, Cursor cursor) throws IOException;
	CursorPage<Integer> findByUserIdOrderByCreatedDateDesc (Integer userId, Cursor cursor) throws IOException;
}
