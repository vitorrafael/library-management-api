package com.vitorrafael.librarymanagement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Invalid RequestBody for this HTTP Verb")
public class InvalidRequestBodyException extends RuntimeException {

}
