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
import com.minewaku.trilog.util.LogUtil;
import com.minewaku.trilog.util.MessageUtil;

@Service
public class MediaService implements IMediaService {

    @Autowired
    private MediaRepository imageRepository;

    @Autowired
    private MediaMapper mapper;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

//    @Cacheable(value = "media", key = "#id")
    @Override
    public MediaDTO findById(Integer id) {
        try {
            Media file = imageRepository.findById(id).orElseThrow(() -> new RuntimeException(MessageUtil.getMessage("error.get.media")));
            MediaDTO test = (MediaDTO) redisTemplate.opsForValue().get(Integer.toString(id));
            LogUtil.LOGGER.info("MediaDTO from Redis: " + test);
            return mapper.entityToDto(file);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

//    @Cacheable(value = "media", key = "#file.id")
    @Override
    public MediaDTO create(MediaDTO file) {
        try {
            if(file.getId() == null || imageRepository.findById(file.getId()).isEmpty()) {         
                Media savedFile = imageRepository.save(mapper.dtoToEntity(file));
                return mapper.entityToDto(savedFile);
            } else {
                throw new RuntimeException(MessageUtil.getMessage("error.create.media"));
            }
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

//    @CachePut(value = "media", key = "#id")
    @Override
    public MediaDTO update(int id, MediaDTO file) {
        try {
            imageRepository.findById(id).orElseThrow(() -> new RuntimeException(MessageUtil.getMessage("error.get.media")));
            Media savedFile = imageRepository.save(mapper.dtoToEntity(file));
            return mapper.entityToDto(savedFile);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

//    @CacheEvict(value = "media", key = "#id")
    @Override
    public void delete(int id) {
        try {
            imageRepository.findById(id).orElseThrow(() -> new RuntimeException(MessageUtil.getMessage("error.get.media")));
            imageRepository.deleteById(id);
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
