package com.topekox.pembayaran.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Setter
@Getter
public class User extends BaseEntity {
	
	@NotNull @NotEmpty
	@Column(nullable = false, unique = true)
	private String email;
	
	@NotNull @NotEmpty
	@Column(nullable = false)
	private String fullName;
	
	@NotNull @NotEmpty
	@Column(nullable = false)
	private String nomorHandphone;

}
