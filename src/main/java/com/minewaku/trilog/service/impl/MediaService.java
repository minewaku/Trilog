package com.minewaku.trilog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.dto.MediaDTO;
import com.minewaku.trilog.entity.Media;
import com.minewaku.trilog.mapper.MediaMapper;
import com.minewaku.trilog.repository.MediaRepository;
import com.minewaku.trilog.service.IMediaService;
import com.minewaku.trilog.util.ErrorUtil;
import com.minewaku.trilog.util.LogUtil;
import com.minewaku.trilog.util.MessageUtil;

@Service
public class MediaService implements IMediaService {

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private MediaMapper mapper;
    
    @Autowired
    private ErrorUtil errorUtil;

    @Override
    public MediaDTO findById(Integer id) {
        try {
            Media file = mediaRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.MEDIA_NOT_FOUND));
            return mapper.entityToDto(file);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public MediaDTO create(MediaDTO file) {
        try {
        	mediaRepository.findById(file.getId()).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.MEDIA_NOT_FOUND));
            Media savedFile = mediaRepository.save(mapper.dtoToEntity(file));
            return mapper.entityToDto(savedFile);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    public MediaDTO update(Integer id, MediaDTO file) {
        try {
        	mediaRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.MEDIA_NOT_FOUND));
            Media savedFile = mediaRepository.save(mapper.dtoToEntity(file));
            return mapper.entityToDto(savedFile);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            mediaRepository.findById(id).orElseThrow(() -> errorUtil.ERROR_DETAILS.get(errorUtil.MEDIA_NOT_FOUND));
            mediaRepository.deleteById(id);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
