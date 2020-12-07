package com.vitorrafael.librarymanagement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Could not find an author for given id")
public class AuthorNotFoundException  extends RuntimeException {
}
