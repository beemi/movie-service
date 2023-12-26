package com.jaitechltd.movieservice.exceptions;

import com.jaitechltd.movieservice.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovieCreationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicateMovieException(MovieCreationException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleMovieNotFoundException(MovieNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(UpdateMovieException.class)
    public ResponseEntity<ApiResponse<Object>> handleUpdateMovieException(UpdateMovieException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(MoviesNotFoundResultException.class)
    public ResponseEntity<ApiResponse<Object>> handleMoviesNotFoundResultException(MoviesNotFoundResultException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(MoviesDataAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleMoviesDataAccessException(MoviesDataAccessException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(MoviesUnexpectedException.class)
    public ResponseEntity<ApiResponse<Object>> handleMoviesUnexpectedException(MoviesUnexpectedException ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(ex.getMessage()));
    }
}
