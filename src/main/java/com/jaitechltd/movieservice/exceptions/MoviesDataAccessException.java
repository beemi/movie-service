package com.jaitechltd.movieservice.exceptions;

import org.springframework.dao.DataAccessException;

public class MoviesDataAccessException extends RuntimeException {
    public MoviesDataAccessException(String failedToFetchAllMovies, DataAccessException e) {

        super(failedToFetchAllMovies, e);
    }
}
