package com.zefbackend.app.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "build")
public class ErrorDetails {
	
	private LocalDateTime timestamp;
	private List<String> messages;
	private String details;
}
