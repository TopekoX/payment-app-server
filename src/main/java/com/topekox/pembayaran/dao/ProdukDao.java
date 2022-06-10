package com.topekox.pembayaran.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.topekox.pembayaran.entity.Produk;

public interface ProdukDao extends JpaRepository<Produk, Long> {

}
