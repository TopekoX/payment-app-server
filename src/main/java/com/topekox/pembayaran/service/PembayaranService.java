package com.topekox.pembayaran.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.topekox.pembayaran.dao.ProdukDao;
import com.topekox.pembayaran.dao.UserDao;
import com.topekox.pembayaran.dao.UserFCMTokenDao;
import com.topekox.pembayaran.entity.Produk;
import com.topekox.pembayaran.entity.User;
import com.topekox.pembayaran.entity.UserFCMToken;
import com.topekox.pembayaran.exception.RegisterTokenToTopicFailedException;
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
	public PembayaranService(ProdukDao produkDao, FCMService fcmService, UserDao userDao,
			UserFCMTokenDao userFCMTokenDao) {
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

		Pageable pageRequest = PageRequest.of(0, 1);
		Page<UserFCMToken> tokens = userFcmTokenDao.findByToken(token, pageRequest);

		// Cek kalau token dan usernya sama maka di skip.
		if (tokens.getNumberOfElements() >= 1) {
			UserFCMToken userToken = tokens.getContent().get(0);
			log.info("Cek Data user id {} token {}.", tokens.getContent().get(0).getId(),
					tokens.getContent().get(0).getToken());
			log.info("User ID == Token User ID: {}, User Token == Token: {}",
					user.getId() == userToken.getUser().getId(), userToken.getToken().equals(token));

			if ((user.getId() == userToken.getUser().getId()) && (userToken.getToken().equals(token))) {
				return;
			}
		}

		try {
			if (user != null) {
				fcmService.registerTokenToTopics(token, "produk");
				
				log.info("Find User by Email: " + user.getEmail());
				UserFCMToken userFcmToken = new UserFCMToken();
				userFcmToken.setUser(user);
				userFcmToken.setToken(token);
				log.info("Hapus semua FCM token, untuk user dengan ID: " + user.getId());
				long result = userFcmTokenDao.deleteByUserId(user.getId());
				log.info("Result: " + result);
				userFcmTokenDao.save(userFcmToken);
				log.info("Simpan Token FCM user yang baru, untuk user dengan ID: " + user.getId());
			}
		} catch (RegisterTokenToTopicFailedException e) {
			log.error(e.getMessage());
		}
	}

	@Transactional
	public void simpanProduk(@Valid Produk produk) {
		produkDao.save(produk);
		fcmService.sendMessageToTopic(produk);
	}

}
