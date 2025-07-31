package com.minewaku.trilog.service;

import com.minewaku.trilog.dto.MediaDTO;

public interface IMediaService {
    MediaDTO findById(Integer id);
    MediaDTO update(Integer id, MediaDTO file);
    MediaDTO create(MediaDTO file);
    void delete(Integer id);
}
