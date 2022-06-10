package com.topekox.pembayaran.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "antrian_fcm")
@Setter
@Getter
@ToString
public class AntrianFCM extends BaseEntity {
	
	@Column(name = "waktu_masuk", nullable = false)
	@CreationTimestamp
	private Date waktuMasuk = new Date();
	
	@Column(name = "token", nullable = false)
	private String tokenTujuan;
	
	@Column(name = "message", nullable = false)
	private String message;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "topic", nullable = false)
	private String topic;

	
	@Column(name = "waktu_kirim")
	@Temporal(TemporalType.TIMESTAMP)
	private Date waktuKirim = new Date();
	
	@Column(name="status_antrian", nullable = false)
	@Enumerated(EnumType.STRING)
	private StatusAntrian statusAntrian = StatusAntrian.BARU;
	
	@Column(name = "keterangan")
	private String keterangan;
	
	public AntrianFCM() {
		
	}

	public AntrianFCM(Date waktuMasuk, String tokenTujuan, 
			String message, String title, Date waktuKirim,
			StatusAntrian statusAntrian, String keterangan) {
		this.waktuMasuk = waktuMasuk;
		this.tokenTujuan = tokenTujuan;
		this.message = message;
		this.title = title;
		this.waktuKirim = waktuKirim;
		this.statusAntrian = statusAntrian;
		this.keterangan = keterangan;
	}	

}
