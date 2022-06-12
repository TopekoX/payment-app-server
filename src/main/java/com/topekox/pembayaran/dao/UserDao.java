package com.topekox.pembayaran.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.topekox.pembayaran.entity.User;

public interface UserDao extends JpaRepository<User, Long> {

	User findByEmail(String email);

}
