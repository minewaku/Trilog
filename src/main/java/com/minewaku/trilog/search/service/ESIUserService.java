package com.minewaku.trilog.search.service;

import com.minewaku.trilog.dto.User.UserDTO;
import com.minewaku.trilog.dto.common.response.CursorPage;
import com.minewaku.trilog.dto.model.Cursor;

public interface ESIUserService {
	CursorPage<UserDTO> suggestUsers(String keyword, Cursor cursor);
}
