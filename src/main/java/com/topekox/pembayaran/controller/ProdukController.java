package com.topekox.pembayaran.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.topekox.pembayaran.entity.Produk;
import com.topekox.pembayaran.service.PembayaranService;

@RestController
@RequestMapping("/api")
public class ProdukController {

	private PembayaranService pembayaranService;

	@Autowired
	public ProdukController(PembayaranService pembayaranService) {
		this.pembayaranService = pembayaranService;
	}
	
	@GetMapping("/produk")
	public List<Produk> semuaProuduk() {
		return pembayaranService.findAll();
	}
	
	@PostMapping("/produk")
	public void simpanProduk(@Valid @RequestBody Produk produk) {
		pembayaranService.simpanProduk(produk);
	}
	
}
