package com.minewaku.trilog.service;

import com.minewaku.trilog.dto.MediaDTO;

public interface IMediaService {
    public MediaDTO findById(Integer id);
    public MediaDTO create(MediaDTO file);
    public MediaDTO update(int id, MediaDTO file);
    public void delete(int id);
}
