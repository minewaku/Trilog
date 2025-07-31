package com.minewaku.trilog.mapper;

import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.minewaku.trilog.dto.Media.MediaPostDTO;
import com.minewaku.trilog.dto.Post.PostDTO;
import com.minewaku.trilog.dto.Post.SavedPostDTO;
import com.minewaku.trilog.entity.MediaPost;
import com.minewaku.trilog.entity.Post;
import com.minewaku.trilog.search.document.ESPost;

@Mapper(componentModel = "spring", 
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "content", source = "content")
	@Mapping(source = "userId", target = "user.id")
	@Mapping(target = "viewCount", ignore = true)
	@Mapping(target = "likeCount", ignore = true)
	@Mapping(target = "commentCount", ignore = true)
	@Mapping(target = "createdDate", ignore = true)
	@Mapping(target = "modifiedDate", ignore = true)
	@Mapping(target = "media", ignore = true)
	@Mapping(target = "comments", ignore = true)
	@Mapping(source = "lat", target = "lat")
	@Mapping(source = "lon", target = "lon")
	@Mapping(target = "user", ignore = true)
	Post dtoToEntity(PostDTO dto);
	
	@Mapping(source = "userId", target = "user.id")
	@Mapping(target = "content", source = "content")
	Post savedPostDtoToEntity(SavedPostDTO dto);
	
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "lat", target = "lat")
    @Mapping(source = "lon", target = "lon")
    @Mapping(source = "media", target = "thumbnails", qualifiedByName = "mediaPostsToThumbnails")
    PostDTO entityToDto(Post entity);
	
	
	ESPost dtoToESPost(PostDTO dto);
	
	@Named("mediaPostsToThumbnails")
    default MediaPostDTO[] mediaPostsToThumbnails(Set<MediaPost> mediaPosts) {
        if (mediaPosts == null) {
            return new MediaPostDTO[0];
        }
        return mediaPosts.stream()
                .filter(MediaPost::getIsThumbnail)
                .map(mediaPost -> {
                    MediaPostDTO dto = new MediaPostDTO();
                    dto.setPublicId(mediaPost.getMedia().getPublicId()); 
                    dto.setSecureUrl(mediaPost.getMedia().getSecureUrl()); 
                    dto.setDisplayOrder(mediaPost.getDisplayOrder());
                    dto.setIsThumbnail(mediaPost.getIsThumbnail());
                    return dto;
                })
                .toArray(MediaPostDTO[]::new);
    }
	
}


