package com.vitorrafael.librarymanagement.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.vitorrafael.librarymanagement.models.Author;

public interface AuthorRepository extends CrudRepository<Author, Long> {
	
	public List<Author> findAllByCountry(String country);
}
