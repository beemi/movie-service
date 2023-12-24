package com.jaitechltd.movieservice.exceptions;

public class UpdateMovieException extends RuntimeException {
    public UpdateMovieException(final String message, final Throwable cause) {

        super(message, cause);
    }
}
