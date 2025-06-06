package com.minewaku.trilog.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.minewaku.trilog.dto.MediaDTO;
import com.minewaku.trilog.dto.RoleDTO;
import com.minewaku.trilog.dto.UserDTO;
import com.minewaku.trilog.dto.request.RegisterRequest;
import com.minewaku.trilog.dto.response.StatusResponse;

public interface IUserService {
    Page<UserDTO> findAll(Pageable pageable, Map<String, String> params);
    Page<UserDTO> search(Map<String, String> params, Pageable pageable);
    
    UserDTO findById(int id);
    List<RoleDTO> getRolesByUserId(int id);
    
    MediaDTO getImage(int id);
    MediaDTO getCover(int id);

    MediaDTO updateImage(int userId, int imageId);
    MediaDTO updateCover(int userId, int coverId);
    
    UserDTO create(RegisterRequest user);
    UserDTO update(int userId, UserDTO user);
    UserDTO patch(int userId, UserDTO user);
    void delete(List<Integer> ids);
}
