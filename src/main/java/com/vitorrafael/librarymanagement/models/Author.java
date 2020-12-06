package com.vitorrafael.librarymanagement.models;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Author {
	
	@Id
	private long id;
	
	@NotEmpty
	private String name;
	
	@NotEmpty
	private String country;
	
	@NotEmpty
	@DateTimeFormat
	private Date birthday;

	public Author() {
	
	}

	public Author(String name, String country, Date birthday) {
		this.name = name;
		this.country = country;
		this.birthday = birthday;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	
	
}
