package com.minewaku.trilog.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.dto.UserDTO;
import com.minewaku.trilog.entity.User;
import com.minewaku.trilog.mapper.UserMapper;
import com.minewaku.trilog.repository.UserRepository;
import com.minewaku.trilog.search.repository.custom.ESUserRepositoryCustom;
import com.minewaku.trilog.search.service.ESIUserService;

@Service
public class ESUserService implements ESIUserService {
	
	@Autowired
	private ESUserRepositoryCustom esUserRepositoryCustom;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserMapper userMapper;
	
	
	public List<UserDTO> suggestUsers(String input) {
		try {
			List<Integer> esResult =  esUserRepositoryCustom.suggestUsers(input);
			List<User> result = userRepository.findAllById(esResult);
			
			return result.stream().map(userMapper::entityToDto).toList();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
}
