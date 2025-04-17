package com.minewaku.trilog.search.service;

import java.util.List;

import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.search.document.Post;

public interface IPostService {
	/**
	 * Search posts by content and keyword.
	 *
	 * @param content the content to search for
	 * @param keyword the keyword to search for
	 * @return a list of posts that match the search criteria
	 */
//	List<Post> searchByHashtag(String hashtag, Cursor cursor);
	
	List<Post> searchByContent(String keyword, Cursor cursor);
	List<Post> findByUsername(String username, Cursor cursor);
}
