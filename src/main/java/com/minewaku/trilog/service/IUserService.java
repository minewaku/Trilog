package com.minewaku.trilog.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.minewaku.trilog.dto.MediaDTO;
import com.minewaku.trilog.dto.RoleDTO;
import com.minewaku.trilog.dto.UserDTO;

public interface IUserService {
    Page<UserDTO> findAll(Map<String, String> params, Pageable pageable);
    Page<UserDTO> search(Map<String, String> params, Pageable pageable);
    
    UserDTO findById(int id);
    RoleDTO getRole(int id);
    MediaDTO getImage(int id);
    MediaDTO getCover(int id);

    UserDTO create(UserDTO user);
    UserDTO update(int userId, UserDTO user);
    MediaDTO updateImage(int userId, int imageId);
    MediaDTO updateCover(int userId, int coverId);
    void delete(int id);
}
