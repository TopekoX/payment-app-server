package com.topekox.pembayaran.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topekox.pembayaran.dao.ProdukDao;
import com.topekox.pembayaran.entity.Produk;
import com.topekox.pembayaran.fcm.NotificationRequest;

@Service
public class PembayaranService {
	
	private ProdukDao produkDao;
	private FCMService fcmService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PembayaranService.class);
	
	@Autowired
	public PembayaranService(ProdukDao produkDao, FCMService fcmService) {
		this.produkDao = produkDao;
		this.fcmService = fcmService;
	}
	
	@Transactional
	public void save(Produk produk, NotificationRequest request) {
		produkDao.save(produk);
		
		try {
			fcmService.sendMessageAndSaveToAntrian(request);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	
}
