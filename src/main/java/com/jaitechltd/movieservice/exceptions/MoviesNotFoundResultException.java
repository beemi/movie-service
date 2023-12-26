package com.jaitechltd.movieservice.exceptions;

public class MoviesNotFoundResultException extends RuntimeException {
    public MoviesNotFoundResultException(String message) {
        super(message);
    }
}
