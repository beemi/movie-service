package com.jaitechltd.movieservice.exceptions;

public class MovieNotFoundException extends RuntimeException {

    public MovieNotFoundException(final String message) {

        super(message);
    }
}
