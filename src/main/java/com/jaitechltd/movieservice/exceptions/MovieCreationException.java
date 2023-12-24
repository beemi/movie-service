package com.jaitechltd.movieservice.exceptions;

public class MovieCreationException extends RuntimeException {
    public MovieCreationException(final String message, final Throwable cause) {

        super(message, cause);
    }
}
