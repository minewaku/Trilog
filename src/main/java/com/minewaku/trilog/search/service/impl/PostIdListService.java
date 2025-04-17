package com.minewaku.trilog.search.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.minewaku.trilog.dto.model.Cursor;
import com.minewaku.trilog.search.document.PostIdList;
import com.minewaku.trilog.search.repository.PostIdListRepository;
import com.minewaku.trilog.search.service.IPostIdListService;

public class PostIdListService implements IPostIdListService {

	@Autowired
	private PostIdListRepository postIdListRepository;
	
	public List<Integer> geByCursor(String topic, String keyword, Cursor cursor) {
		return postIdListRepository.findByTopicAndCursor(keyword, cursor);
	}
	
	public boolean create(List<Integer> list, String topic) {
        try {
            postIdListRepository.save(PostIdList.builder().topic(topic)
                    .ids(list)
                    .build());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
		
	}
}
