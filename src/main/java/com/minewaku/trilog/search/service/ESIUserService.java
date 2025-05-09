package com.minewaku.trilog.search.service;

import java.io.IOException;
import java.util.List;

import com.minewaku.trilog.dto.UserDTO;

public interface ESIUserService {
	List<UserDTO> suggestUsers(String input) throws IOException;
}
