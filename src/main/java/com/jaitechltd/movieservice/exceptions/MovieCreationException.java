package com.jaitechltd.movieservice.exceptions;

public class MovieCreationException extends RuntimeException {

    public MovieCreationException(final String message) {

        super(message);
    }

    public MovieCreationException(final String message, Exception e) {

        super(message, e);
    }
}
