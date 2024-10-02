package com.semonemo.spring_server.domain.elasticsearch.document;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.InnerField;
import org.springframework.data.elasticsearch.annotations.MultiField;

import lombok.Data;

@Data
@Document(indexName = "nft_sells")
public class NFTSellDocument {

	@Id
	private Long nftSellId;

	@Field(type = FieldType.Long)
	private Long nftId;

	@Field(type = FieldType.Long)
	private Long creator;

	@Field(type = FieldType.Long)
	private Long seller;

	@Field(type = FieldType.Keyword)
	private String tokenId;

	@Field(type = FieldType.Long)
	private Long price;

	@Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime createdAt;

	@Field(type = FieldType.Long)
	private int likeCount;

	@Field(type = FieldType.Nested)
	private List<Tag> tags;

	@Data
	public static class Tag {
		@Field(type = FieldType.Long)
		private Long ntagId;

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