package com.minewaku.trilog.dto.response;

import java.util.List;

import com.minewaku.trilog.entity.Post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
class NewsfeedResponse {
    private List<Post> posts;
    private Integer nextCursor;
}