package com.minewaku.trilog.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.minewaku.trilog.dto.Comment.CommentDTO;
import com.minewaku.trilog.dto.Comment.SavedCommentDTO;
import com.minewaku.trilog.dto.Post.PostDTO;
import com.minewaku.trilog.dto.Post.SavedPostDTO;
import com.minewaku.trilog.dto.Post.UpdatedPostDTO;
import com.minewaku.trilog.dto.common.response.StatusResponse;

public interface IPostService {
	 PostDTO findById(int id);
	 Page<PostDTO> findAll(Map<String, String> params, Pageable pageable);
	 
	 PostDTO create(SavedPostDTO post);
	 PostDTO update(int id, UpdatedPostDTO Post);
	 void delete(List<Integer> ids);
	 
	 StatusResponse addMedia(Integer id, List<MultipartFile> files);
	 StatusResponse deleteMedia(Integer postId, List<Integer> mediaIds);
	 
	 CommentDTO addComment(Integer id, SavedCommentDTO savedCommentDTO);
}
