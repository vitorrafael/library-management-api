package com.vitorrafael.librarymanagement.controllers;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.util.Arrays;

import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vitorrafael.librarymanagement.models.Author;
import com.vitorrafael.librarymanagement.repositories.AuthorRepository;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AuthorControllerTest {

	private MockMvc mockMvc;

	@Inject
	private WebApplicationContext applicationContext;

	@Inject
	private AuthorRepository authorRepository;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
	}
	
	@AfterEach
	public void tearDown() {
		authorRepository.deleteAll();
	}
	
	@Test
	public void shouldReturnAuthorForAValidIdd() throws Exception {
		Author author = authorRepository
				.save(new Author("George R. R. Martin", "USA", Date.valueOf("1948-09-20"), "georgegot@fab.com"));

		mockMvc.perform(get(AuthorController.PATH + "/" + author.getId().intValue()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(author.getId().intValue())))
				.andExpect(jsonPath("$.name", is("George R. R. Martin")))
				.andExpect(jsonPath("$.country", is("USA")))
				.andExpect(jsonPath("$.birthday", is("1948-09-20")))
				.andExpect(jsonPath("$.email", is("georgegot@fab.com")));
	}

	@Test
	public void shouldReturn404ForAnInvalidId() throws Exception {
		int invalidId = 2;

		mockMvc.perform(get(AuthorController.PATH + "/" + invalidId))
			.andExpect(status().isNotFound());
	}
	
	@Test
	public void shouldReturnAuthorsForCountryWithRegisteredAuthors() throws Exception {
		authorRepository.saveAll(Arrays.asList(new Author("George R. R. Martin", "USA", Date.valueOf("1948-09-20")),
				new Author("Rick Riordan", "USA", Date.valueOf("1964-06-19")),
				new Author("Machado de Assis", "BR", Date.valueOf("1839-06-21"))));
		
		String country = "USA";
		
		mockMvc.perform(get(AuthorController.PATH + "/country/" + country))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()", is(2)));
	}
	
	@Test
	public void shouldReturnEmptyAuthorListForCountryWithNoRegisteredAuthors() throws Exception {
		String country = "USA";
		
		mockMvc.perform(get(AuthorController.PATH + "/country/" + country))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.length()", is(0)));
	}
	
	@Test
	public void shouldReturnAllRegisteredAuthors() throws Exception {
		authorRepository.saveAll(Arrays.asList(new Author("George R. R. Martin", "USA", Date.valueOf("1948-09-20")),
				new Author("Rick Riordan", "USA", Date.valueOf("1964-06-19")),
				new Author("Machado de Assis", "BR", Date.valueOf("1839-06-21"))));
		
		mockMvc.perform(get(AuthorController.PATH + "/"))
			.andExpect(jsonPath("$.length()", is(3)));
	}
	
	@Test
	public void shouldRegisterAuthorWithValidData() throws Exception {
		Author author = new Author("George R. R. Martin", "USA", Date.valueOf("1948-09-20"), "georgegot@fab.com");
		
		mockMvc.perform(post(AuthorController.PATH + "/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(author)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id", is(not(equalTo(null)))))
			.andExpect(jsonPath("$.name", is("George R. R. Martin")))
			.andExpect(jsonPath("$.country", is("USA")))
			.andExpect(jsonPath("$.birthday", is("1948-09-20")))
			.andExpect(jsonPath("$.email", is("georgegot@fab.com")));
	}
	
	@Test
	public void shouldNotRegisterAuthorDueToInvalidData() throws Exception {
		Author author = new Author("George R. R. Martin", "USA", Date.valueOf("1948-09-20"), "georgegot@fab.com");
		
		author.setId(2L);
		
		mockMvc.perform(post(AuthorController.PATH + "/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(author)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void shouldUpdateValidAuthor() throws Exception {
		Author author = authorRepository.save(new Author("George R. R. Martin", "USA", Date.valueOf("1948-09-20"), "georgegot@fab.com"));
		
		author.setCountry("GB");
		
		mockMvc.perform(put(AuthorController.PATH + "/" + author.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(author)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id", is(author.getId().intValue())))
			.andExpect(jsonPath("$.name", is("George R. R. Martin")))
			.andExpect(jsonPath("$.country", is("GB")))
			.andExpect(jsonPath("$.birthday", is("1948-09-20")))
			.andExpect(jsonPath("$.email", is("georgegot@fab.com")));
	}
	
	@Test
	public void shouldNotUpdateAuthorWithInconsistentId() throws Exception {
		Author author = authorRepository.save(new Author("George R. R. Martin", "USA", Date.valueOf("1948-09-20"), "georgegot@fab.com"));
		
		author.setCountry("GB");
		
		mockMvc.perform(put(AuthorController.PATH + "/" + author.getId() + 2)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(author)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void shouldNotUpdateAuthorWithInvalidId() throws Exception {
		Author author = new Author("George R. R. Martin", "USA", Date.valueOf("1948-09-20"), "georgegot@fab.com");
		
		author.setCountry("GB");
		
		mockMvc.perform(put(AuthorController.PATH + "/" + 2)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(author)))
			.andExpect(status().isBadRequest());
	}
	
	@Test
	public void shouldDeletePersistedAuthor() throws Exception {
		Author author = authorRepository.save(new Author("George R. R. Martin", "USA", Date.valueOf("1948-09-20"), "georgegot@fab.com"));
				
		mockMvc.perform(delete(AuthorController.PATH + "/" + author.getId()))
			.andExpect(status().isNoContent());
		
		assertEquals(false, authorRepository.existsById(author.getId()));
	}
	
	@Test
	public void shouldThrowErrorWhenAuthorIsNotPersisted() throws Exception {			
		mockMvc.perform(delete(AuthorController.PATH + "/" + 2))
			.andExpect(status().isNotFound());
	}
}
