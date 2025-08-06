package com.minewaku.trilog.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.minewaku.trilog.dto.MediaDTO;
import com.minewaku.trilog.dto.User.UpdatedUserDTO;
import com.minewaku.trilog.dto.User.UserDTO;
import com.minewaku.trilog.dto.common.request.RegisterRequest;

public interface IUserService {
    
    UserDTO findById(Integer id);
    Page<UserDTO> findAll(Map<String, String> params, Pageable pageable);
    
    List<String> getRolesByUserId(Integer id);
    
    MediaDTO getImage(Integer id);
    MediaDTO getCover(Integer id);

    MediaDTO updateImage(Integer userId, Integer imageId);
    MediaDTO updateCover(Integer userId, Integer coverId);
    
    UserDTO create(RegisterRequest user);
    UserDTO update(Integer userId, UpdatedUserDTO user);
    void delete(List<Integer> ids);
}
