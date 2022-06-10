package com.topekox.pembayaran.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.topekox.pembayaran.entity.AntrianFCM;
import com.topekox.pembayaran.entity.StatusAntrian;

public interface AntrianFCMDao extends JpaRepository<AntrianFCM, Long> {
	
	Page<AntrianFCM> findByStatusAntrian(StatusAntrian statusAntrian, Pageable pageRequest);

}
