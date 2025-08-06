package com.minewaku.trilog.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.dto.User.UserDTO;
import com.minewaku.trilog.dto.common.response.CursorPage;
import com.minewaku.trilog.dto.model.Cursor;
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
	
	
	public CursorPage<UserDTO> suggestUsers(String keyword, Cursor cursor) {
		try {
			CursorPage<Integer> esResult =  esUserRepositoryCustom.suggestUsers(keyword, cursor);
			List<User> result = userRepository.findAllById(esResult.getRecords());
			
			return CursorPage.<UserDTO>builder()
					.after(esResult.getAfter())
					.before(esResult.getBefore())
					.limit(esResult.getLimit()).total(result.size())
					.records(result.stream().map(userMapper::entityToDto).toList())
					.build();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}
}
