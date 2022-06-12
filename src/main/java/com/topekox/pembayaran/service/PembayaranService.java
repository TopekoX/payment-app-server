package com.topekox.pembayaran.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topekox.pembayaran.dao.ProdukDao;
import com.topekox.pembayaran.dao.UserDao;
import com.topekox.pembayaran.dao.UserFCMTokenDao;
import com.topekox.pembayaran.entity.Produk;
import com.topekox.pembayaran.entity.User;
import com.topekox.pembayaran.entity.UserFCMToken;
import com.topekox.pembayaran.fcm.NotificationRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PembayaranService {
	
	private ProdukDao produkDao;
	private FCMService fcmService;
	private UserDao userDao;
	private UserFCMTokenDao userFcmTokenDao;
	
	@Autowired
	public PembayaranService(ProdukDao produkDao, FCMService fcmService,
			UserDao userDao, UserFCMTokenDao userFCMTokenDao) {
		this.produkDao = produkDao;
		this.fcmService = fcmService;
		this.userDao = userDao;
		this.userFcmTokenDao = userFCMTokenDao;
	}
	
	@Transactional
	public void save(Produk produk, NotificationRequest request) {
		produkDao.save(produk);
		
		try {
			fcmService.sendMessageAndSaveToAntrian(request);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	@Transactional
	public List<Produk> findAll() {
		return produkDao.findAll();
	}
	
	@Transactional
	public void updateToken(String email, String token) {
		User user = userDao.findByEmail(email);
		
		if (user != null) {
			log.info("Find User: " + user);
			UserFCMToken userFcmToken = new UserFCMToken();
			userFcmToken.setUser(user);
			userFcmToken.setToken(token);
			userFcmTokenDao.deleteByToken(token);
			log.info("Hapus semua FCM token, untuk user dengan ID: " + user.getId());
			userFcmTokenDao.save(userFcmToken);
			log.info("Simpan Token FCM user yang baru, untuk user dengan ID: " + user.getId());
		}
	}
		
}
