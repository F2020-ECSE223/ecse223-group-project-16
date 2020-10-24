package ca.mcgill.ecse.flexibook.controller;

@SuppressWarnings("serial")
public class InvalidUserException extends Exception {
	public InvalidUserException(String message) {
		super(message);
	}
}
