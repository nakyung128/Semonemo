package com.semonemo.spring_server.domain.nft.repository.ntags;

import com.semonemo.spring_server.domain.nft.entity.Ntags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NTagRepository extends JpaRepository<Ntags, Long>, NTagRepositoryCustom {
}
