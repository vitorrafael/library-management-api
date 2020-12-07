package com.vitorrafael.librarymanagement.controllers;



import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vitorrafael.librarymanagement.exceptions.AuthorNotFoundException;
import com.vitorrafael.librarymanagement.exceptions.InvalidRequestBodyException;
import com.vitorrafael.librarymanagement.models.Author;
import com.vitorrafael.librarymanagement.repositories.AuthorRepository;

@RestController
@RequestMapping(path = AuthorController.PATH)
public class AuthorController {

	public static final String PATH = "/api/v1/author";

	@Inject
	private AuthorRepository authorRepository;

	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public @ResponseBody Author getAuthor(@PathVariable("id") @Min(0) Long id) {
		Author author = authorRepository.findById(id).orElse(null);

		if (author == null)
			throw new AuthorNotFoundException();

		return author;
	}
	
	@GetMapping(value = "/country/{country}")
	@ResponseStatus(code = HttpStatus.OK)
	public @ResponseBody List<Author> getAuthorsByCountry(@PathVariable("country") String country) {	
		return authorRepository.findAllByCountry(country);
	}
	
	@GetMapping(value = "/")
	public @ResponseBody List<Author> getAllAuthors() {
		return (List<Author>) authorRepository.findAll();
	}
	
	@PostMapping(value = "/")
	@ResponseStatus(code = HttpStatus.CREATED)
	public @ResponseBody Author registerAuthor(@Valid @RequestBody Author author) {
		
		if(author.getId() != null)
			throw new InvalidRequestBodyException();
		
		return authorRepository.save(author);		
	}
	
	@PutMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public @ResponseBody Author updateAuthor(@PathVariable("id") @Min(0) Long id, @Valid @RequestBody Author author) {
		
		if(author.getId() == null || author.getId() != id)
			throw new InvalidRequestBodyException();
		
		return authorRepository.save(author);
	}
	
	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public @ResponseBody ResponseEntity<Author> deleteAuthor(@PathVariable("id") @Min(0) Long id) {
		
		if(!authorRepository.existsById(id))
			throw new AuthorNotFoundException();
		
		authorRepository.deleteById(id);
		
		return null;
	}
}
