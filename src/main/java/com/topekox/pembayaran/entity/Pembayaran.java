package com.topekox.pembayaran.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pembayaran")
@Setter
@Getter
public class Pembayaran extends BaseEntity {

}
