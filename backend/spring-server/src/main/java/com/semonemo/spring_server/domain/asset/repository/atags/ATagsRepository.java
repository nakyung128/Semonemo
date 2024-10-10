package com.semonemo.spring_server.domain.asset.repository.atags;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semonemo.spring_server.domain.asset.model.Atags;

public interface ATagsRepository extends JpaRepository<Atags, Long>,ATagsRepositoryCustom{
	boolean existsByName(String tagname);

	Atags findByName(String tagname);
}
