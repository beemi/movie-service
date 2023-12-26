package com.jaitechltd.movieservice.exceptions;

public class MovieNotFoundException extends Throwable {

    public MovieNotFoundException(final String message) {

        super(message);
    }

    public MovieNotFoundException(final String message, final Throwable cause) {

        super(message, cause);
    }
}
