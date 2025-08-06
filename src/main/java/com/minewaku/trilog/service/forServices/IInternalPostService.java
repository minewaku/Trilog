package com.minewaku.trilog.service.forServices;

import com.minewaku.trilog.dto.Post.SavedPostDTO;
import com.minewaku.trilog.entity.Post;

public interface IInternalPostService {
	Post createForServices(SavedPostDTO dto);
}
