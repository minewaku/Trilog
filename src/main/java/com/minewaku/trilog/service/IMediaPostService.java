package com.minewaku.trilog.service;

import java.util.List;

import com.minewaku.trilog.dto.Media.MediaPostDTO;
import com.minewaku.trilog.dto.Media.SavedMediaPostDTO;

public interface IMediaPostService {
	MediaPostDTO save(SavedMediaPostDTO media);
	void delete(List<Integer> ids);
}
