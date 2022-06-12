package com.topekox.pembayaran.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.topekox.pembayaran.entity.UserFCMToken;

@RepositoryRestResource(collectionResourceRel = "userfcmtoken", path = "userfcmtoken")
public interface UserFCMTokenDao extends JpaRepository<UserFCMToken, Long> {
	
	long deleteByToken(String token);
	
	List<UserFCMToken> findByToken(String token);

}