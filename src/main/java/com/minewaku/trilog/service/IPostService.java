package com.minewaku.trilog.service;

import com.minewaku.trilog.dto.PostDTO;
import com.minewaku.trilog.dto.response.StatusResponse;

public interface IPostService {
	 PostDTO create(PostDTO post);
	 public PostDTO update(int id, PostDTO Post);
	 public StatusResponse delete(int[] ids);
}
