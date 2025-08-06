package com.minewaku.trilog.service;

import com.minewaku.trilog.dto.MediaDTO;
import com.minewaku.trilog.dto.Media.SavedMediaDTO;
import com.minewaku.trilog.entity.Media;
import com.minewaku.trilog.entity.Media;

public interface IMediaService {
    MediaDTO findById(Integer id);
    MediaDTO update(Integer id, MediaDTO file);
    MediaDTO create(SavedMediaDTO file);
    Media createForServices(SavedMediaDTO file);
    void delete(Integer id);
}
