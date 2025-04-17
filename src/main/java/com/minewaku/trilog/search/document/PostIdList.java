package com.minewaku.trilog.search.document;

import java.util.List;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(indexName = "post_id_list")

@DynamicUpdate
@Builder
public class PostIdList {
	@Id
    private Integer id;
    
	@Field(type = FieldType.Keyword, name = "topic")
	private String topic;
	
	@Field(type = FieldType.Integer, name = "ids")
	private List<Integer> ids;
}
