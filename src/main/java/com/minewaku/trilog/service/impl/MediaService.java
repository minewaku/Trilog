package com.minewaku.trilog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minewaku.trilog.dto.MediaDTO;
import com.minewaku.trilog.dto.Media.SavedMediaDTO;
import com.minewaku.trilog.entity.Media;
import com.minewaku.trilog.mapper.MediaMapper;
import com.minewaku.trilog.repository.MediaRepository;
import com.minewaku.trilog.service.IMediaService;
import com.minewaku.trilog.service.forServices.IInternalMediaService;
import com.minewaku.trilog.util.ErrorUtil;
import com.minewaku.trilog.util.LogUtil;

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
    public MediaDTO create(SavedMediaDTO file) {
        try {
//        	LogUtil.LOGGER.info(file.toString());
        	Media test = mapper.savedMediaDtoToEntity(file);
//        	LogUtil.LOGGER.info(test.toString());
//        	LogUtil.LOGGER.info(test.getPublicId().length());
//        	LogUtil.LOGGER.info(test.getSecureUrl().length());
            Media savedFile = mediaRepository.save(test);
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
    
    @Override
	public Media createForServices(SavedMediaDTO file) {
		try {
			Media test = mapper.savedMediaDtoToEntity(file);
			return mediaRepository.save(test);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
