package com.minewaku.trilog.search.service;

import com.minewaku.trilog.dto.Post.PostDTO;
import com.minewaku.trilog.dto.common.response.CursorPage;
import com.minewaku.trilog.dto.model.Cursor;

public interface ESIPostService {

	CursorPage<PostDTO> searchByHashtag(String hashtag, Cursor cursor);
	CursorPage<PostDTO> searchByContent(String keyword, Cursor cursor);
	CursorPage<PostDTO> findByUserId(Integer userId, Cursor cursor);
}
