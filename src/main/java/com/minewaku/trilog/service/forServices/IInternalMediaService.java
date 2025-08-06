package com.minewaku.trilog.service.forServices;

import com.minewaku.trilog.dto.Media.SavedMediaDTO;
import com.minewaku.trilog.entity.Media;

public interface IInternalMediaService {
    Media createForServices(SavedMediaDTO dto);
}
