package com.minewaku.trilog.search.service;

import com.minewaku.trilog.dto.PostDTO;
import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.dto.response.CursorPage;

public interface ESIPostService {

	CursorPage<PostDTO> searchByHashtag(String hashtag, Cursor cursor);
	CursorPage<PostDTO> searchByContent(String keyword, Cursor cursor);
//	CursorPage<PostDTO> findByUsername(String username, Cursor cursor);
}
