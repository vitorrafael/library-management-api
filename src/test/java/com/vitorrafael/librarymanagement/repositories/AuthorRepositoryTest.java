package com.vitorrafael.librarymanagement.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.vitorrafael.librarymanagement.models.Author;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AuthorRepositoryTest {

	@Autowired
	private AuthorRepository authorRepository;

	@AfterEach
	public void tearDown() {
		authorRepository.deleteAll();
	}

	@Test
	public void shouldPersistAuthorToDatabase() {
		assertEquals(0, authorRepository.count());

		Author author = authorRepository.save(new Author("George R. R. Martin", "USA", Date.valueOf("1948-09-20")));

		assertEquals(1, authorRepository.count());

		assertNotNull(author.getId());
		assertEquals("George R. R. Martin", author.getName());
		assertEquals("USA", author.getCountry());
		assertEquals(Date.valueOf("1948-09-20").toString(), author.getBirthday().toString());
	}

	@Test
	public void shouldReturnAuthorById() {
		Author author = authorRepository.save(new Author("George R. R. Martin", "USA", Date.valueOf("1948-09-20")));

		Author fetchedAuthor = authorRepository.findById(author.getId()).get();

		assertNotNull(fetchedAuthor);
		assertEquals(author, fetchedAuthor);
	}

	@Test
	public void shouldReturnAllAuthorsForAGivenCountry() {
		List<Author> USAAuthors = Arrays.asList(new Author("George R. R. Martin", "USA", Date.valueOf("1948-09-20")),
				new Author("Rick Riordan", "USA", Date.valueOf("1964-06-19")));

		List<Author> brazilianAuthors = Arrays.asList(new Author("Erico Verissimo", "BR", Date.valueOf("1905-12-17")),
				new Author("Machado de Assis", "BR", Date.valueOf("1839-06-21")));

		brazilianAuthors = (List<Author>) authorRepository.saveAll(brazilianAuthors);
		USAAuthors = (List<Author>) authorRepository.saveAll(USAAuthors);

		List<Author> fetchedUSAAuthors = authorRepository.findAllByCountry("USA");

		assertEquals(USAAuthors, fetchedUSAAuthors);
		assertNotEquals(brazilianAuthors, fetchedUSAAuthors);
	}

}
