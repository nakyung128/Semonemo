package com.semonemo.spring_server.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CursorResult<T> {

	private List<T> content;
	private Long nextCursor;
	private boolean hasNext;
}
