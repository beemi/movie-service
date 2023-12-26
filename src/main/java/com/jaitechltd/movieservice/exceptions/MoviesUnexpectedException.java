package com.jaitechltd.movieservice.exceptions;

public class MoviesUnexpectedException extends RuntimeException {
    public MoviesUnexpectedException(String message, Exception e) {

        super(message, e);
    }
}
