package com.topekox.pembayaran.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_fcm_token")
@Setter
@Getter
public class UserFCMToken extends BaseEntity {
	
	@NotNull
	@NotEmpty
	@Column(name = "token", nullable = false)
	private String token;
	
	@ManyToOne(cascade = {
			CascadeType.DETACH,
			CascadeType.MERGE, 
			CascadeType.PERSIST,
			CascadeType.REFRESH
	})
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

}
