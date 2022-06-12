package com.topekox.pembayaran.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.topekox.pembayaran.entity.UserFCMToken;

@RepositoryRestResource(collectionResourceRel = "userfcmtoken", path = "userfcmtoken")
public interface UserFCMTokenDao extends JpaRepository<UserFCMToken, Long> {
	
	long deleteByToken(String token);
	
	Page<UserFCMToken> findByToken(String token, Pageable pageable);

}
