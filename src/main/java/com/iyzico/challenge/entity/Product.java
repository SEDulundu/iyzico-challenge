package com.iyzico.challenge.entity;


import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {
	@Id
	@GeneratedValue
	private Long id;
	@Version
	private Long version;  // for optimistic lock
	private BigDecimal price;
	private String name;
	private String description;
	private int stock;

}
