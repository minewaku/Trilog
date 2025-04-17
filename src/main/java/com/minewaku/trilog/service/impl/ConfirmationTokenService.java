package com.minewaku.trilog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.entity.ConfirmationToken;
import com.minewaku.trilog.repository.ConfirmationTokenRepository;
import com.minewaku.trilog.service.IConfirmationTokenService;
import com.minewaku.trilog.util.MessageUtil;

@Service
public class ConfirmationTokenService implements IConfirmationTokenService {
	
	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;
	
	@Override
    public ConfirmationToken create(ConfirmationToken confirmationToken) {
        try {
        	return confirmationTokenRepository.save(confirmationToken);
        } catch (Exception e) {
            throw new RuntimeException(MessageUtil.getMessage("error.create.user"), e);
        }
    }
}
