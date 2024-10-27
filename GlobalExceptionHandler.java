package com.zefbackend.core.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.zefbackend.app.model.ErrorDetails;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> haveNotPermissionException(AccessDeniedException exception, WebRequest request){
		List<String> messages = new ArrayList<>();
		messages.add(exception.getMessage());
		
		for(String message : messages) {
			log.error("Permission issue:  {}", message);
		}
		
		ErrorDetails error = ErrorDetails.build(LocalDateTime.now(), messages, request.getDescription(false));
		return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> duplicateException(DataIntegrityViolationException exception, WebRequest request){
		List<String> messages = new ArrayList<>();
		messages.add(exception.getMessage());
		
		for(String message : messages) {
			log.info("Duplication issue:  {}", message);
		}
		
		ErrorDetails error = ErrorDetails.build(LocalDateTime.now(), messages, request.getDescription(false));
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleInvalidArgument(MethodArgumentNotValidException exception, WebRequest request) {
		List<String> messages = new ArrayList<>();
		
		exception.getBindingResult().getFieldErrors().forEach(error -> {
			messages.add(error.getDefaultMessage());
		});
		
		for(String message : messages) {
			log.error("Bad request issue:  {}", message);
		}
		
		ErrorDetails error = ErrorDetails.build(LocalDateTime.now(), messages, request.getDescription(false));
		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> notFoundExceptionHandler(NotFoundException exception, WebRequest request){
		List<String> messages = new ArrayList<>();
		messages.add(exception.getMessage());
		ErrorDetails error = ErrorDetails.build(LocalDateTime.now(), messages, request.getDescription(false));
		
		for(String message : messages) {
			log.error("Error message:  {}", message);
		}
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ClientErrorException.class)
	public ResponseEntity<?> keycloakConflictHandler(ClientErrorException exception, WebRequest request){
		List<String> messages = new ArrayList<>();
		messages.add(exception.getMessage());
		ErrorDetails error = ErrorDetails.build(LocalDateTime.now(), messages, request.getDescription(false));
		
		for(String message : messages) {
			log.error("Error message:  {}", message);
		}
		
		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandler(Exception exception, WebRequest request){
		List<String> messages = new ArrayList<>();
		messages.add(exception.getMessage());
		ErrorDetails error = ErrorDetails.build(LocalDateTime.now(), messages, request.getDescription(false));
		
		for(String message : messages) {
			log.error("Error message:  {}", message);
		}
		
		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
