package com.semonemo.spring_server.domain.asset.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AssetRequestDto {
	private Long creator;
	private String imageUrl;
}
