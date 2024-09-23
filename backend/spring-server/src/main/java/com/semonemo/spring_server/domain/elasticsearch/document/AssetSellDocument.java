package com.semonemo.spring_server.domain.elasticsearch.document;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import lombok.Data;

@Data
@Document(indexName = "asset_sells")
public class AssetSellDocument {

	@Id
	private Long assetSellId;

	@Field(type = FieldType.Long)
	private Long assetId;

	@Field(type = FieldType.Long)
	private Long creator;

	@Field(type = FieldType.Keyword)
	private String imageUrls;

	@Field(type = FieldType.Long)
	private Long price;

	@Field(type = FieldType.Long)
	private Long hits;

	@Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime createdAt;

	@Field(type = FieldType.Long)
	private Long likeCount;

	@Field(type = FieldType.Nested)
	private List<Tag> tags;

	@Data
	public static class Tag {
		@Field(type = FieldType.Long)
		private Long atagId;

		@MultiField(
				mainField = @Field(type = FieldType.Keyword),
				otherFields = {
						@InnerField(suffix = "text", type = FieldType.Text),
						@InnerField(suffix = "ngram", type = FieldType.Text, analyzer = "ngram_analyzer")
				}
		)
		private String name;
	}
}