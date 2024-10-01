package com.semonemo.spring_server.domain.elasticsearch.document;

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
@Document(indexName = "users")
public class UserDocument {

	@Id
	private Long id;
	private String nickname;
	private String profileImage;
}