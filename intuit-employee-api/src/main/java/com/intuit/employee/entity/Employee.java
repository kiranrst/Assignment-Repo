package com.intuit.employee.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.SQLDelete;

import lombok.Data;

@Data
@Entity
@Table(name = "EMPLOYEE")
@SQLDelete(sql = "UPDATE Employee SET is_active=false WHERE id=?")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;


	@Column(name = "designtion")
	private String designtion;

	@Column(name = "address_line1", nullable = false)
	private String addressLine1;

	@Column(name = "address_line2", nullable = true)
	private String addressLine2;

	@Column(name = "city", nullable = false)
	private String city;

	@Column(name = "state_code", nullable = false)
	private String state;

	@Column(name = "country_code", nullable = false)
	private String country;

	@Column(name = "pin_code", nullable = false)
	private String pincode;
	
	@Column(name="is_active")
	private Boolean isActive = true;

}
