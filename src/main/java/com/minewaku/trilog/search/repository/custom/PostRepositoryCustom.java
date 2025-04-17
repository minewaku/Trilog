package com.minewaku.trilog.search.repository.custom;

import java.io.IOException;
import java.util.List;

import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.search.document.Post;

public interface PostRepositoryCustom {

	//stored again in the PostIdList then do cursor-based pagination
	List<Integer> searchByHashtag(String hashtag) throws IOException;
	
	// cursor-based pagination directly by using after_search
	List<Post> searchByContent(String keyword, Cursor cursor) throws IOException;
	List<Post> findByUsernameKeywordOrderByCreatedDateDesc (String username, Cursor cursor) throws IOException;
}
