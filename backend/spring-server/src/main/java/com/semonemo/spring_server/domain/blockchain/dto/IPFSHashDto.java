package com.semonemo.spring_server.domain.blockchain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigInteger;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class IPFSHashDto {
    String title;
    String content;
    String image;
}