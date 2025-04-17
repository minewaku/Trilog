package com.minewaku.trilog.search.document;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "post")
public class Post {
	@Id
    private Integer id;
    
	@Field(type = FieldType.Integer)
	private Integer postId;
	
	@MultiField(
        mainField = @Field(type = FieldType.Search_As_You_Type),
        otherFields = {
            @InnerField(suffix = "keyword", type = FieldType.Keyword)
        }
    )
	private String username;  
    
    @Field(type = FieldType.Text)
    private String content;
    
    @Field(type = FieldType.Rank_Feature)
    private Integer likes;
    
    @Field(type = FieldType.Rank_Feature)
    private Integer views;
    
    @Field(type = FieldType.Rank_Feature)
    private Integer comments;
    
    @Field(type = FieldType.Date)
    private LocalDateTime createdDate;
    
    @Field(type = FieldType.Keyword)
    private List<String> hashtags;
    
    @Field(type = FieldType.Text)
    private String status;
    
    @GeoPointField
    private Location location;
    
    public static class Location {
        private double lat;
        private double lon;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }
    
}
