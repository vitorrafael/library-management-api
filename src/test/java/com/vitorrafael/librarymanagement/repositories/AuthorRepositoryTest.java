package com.vitorrafael.librarymanagement.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.vitorrafael.librarymanagement.models.Author;

@SpringBootTest
@AutoConfigureTestDatabase( connection = EmbeddedDatabaseConnection.H2 )
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
		
		assertEquals(1,  authorRepository.count());
		
		assertNotNull(author.getId());
		assertEquals("George R. R. Martin", author.getName());
		assertEquals("USA", author.getCountry());
		assertEquals(Date.valueOf("1948-09-20").toString(), author.getBirthday().toString());
	}
	
}
