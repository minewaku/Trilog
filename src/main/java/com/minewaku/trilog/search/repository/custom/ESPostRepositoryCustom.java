package com.minewaku.trilog.search.repository.custom;

import java.io.IOException;
import java.util.List;

import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.dto.response.CursorPage;
import com.minewaku.trilog.search.document.ESPost;

//only be used for search purpose and only return ids of data
public interface ESPostRepositoryCustom {
	CursorPage<Integer> searchByHashtag(String keyword, Cursor cursor) throws IOException;
	CursorPage<Integer> searchByContent(String keyword, Cursor cursor) throws IOException;
//	CursorPage<Integer> findByUsernameKeywordOrderByCreatedDateDesc (String keyword, Cursor cursor) throws IOException;
}
