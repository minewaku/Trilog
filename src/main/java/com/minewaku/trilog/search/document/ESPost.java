package com.minewaku.trilog.search.document;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "post")
public class ESPost {
    
	@Field(type = FieldType.Integer)
	private Integer id;
	
	@Field(type = FieldType.Integer, name = "user_id")
	private Integer userId;
	
	@MultiField(
        mainField = @Field(type = FieldType.Search_As_You_Type),
        otherFields = {
            @InnerField(suffix = "keyword", type = FieldType.Keyword)
        }
    )
	private String username;  
    
    @Field(type = FieldType.Text)
    private String content;
    
    @Field(type = FieldType.Integer)
    private Integer like_count;
    
    @Field(type = FieldType.Integer)
    private Integer comment_count;
    
    @Field(type = FieldType.Integer)
    private Integer view_count;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Field(type = FieldType.Date, name = "created_date")
    private LocalDateTime createdDate;
    
    @Field(type = FieldType.Integer)
    private Integer status;
    
    @GeoPointField
    private Location location;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Location implements Serializable {
        private Double lat;
        private Double lon;
    }
}
