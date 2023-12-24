package com.jaitechltd.movieservice.controller;

import com.jaitechltd.movieservice.exceptions.MovieCreationException;
import com.jaitechltd.movieservice.model.Movie;
import com.jaitechltd.movieservice.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    @Operation(summary = "Create a new movie", description = "Create a new movie", tags = {"movies"}, operationId = "createMovie", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Movie created"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Movie already exists")})
    public ResponseEntity<Object> createMovie(@RequestBody Movie movie) throws MovieCreationException {
        log.info("Create movie request received: {}", movie);

        final var savedMovie = movieService.createMovie(movie);
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
    }

    @PutMapping("/{movieId}")
    @Operation(summary = "Update an existing movie, given movie id", description = "Update an existing movie, given movie id", tags = {"movies"}, operationId = "updateMovie", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Movie updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Movie not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "405", description = "Validation exception")})
    public ResponseEntity<Object> updateMovie(@PathVariable Integer movieId, @RequestBody Movie movie) {

        log.info("Update movie request received: {}", movie);
        final var existingMovie = movieService.getMovie(movieId);
        if (existingMovie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            final var updatedMovie = movieService.updateMovie(movieId, movie);
            return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
        }
    }


    @GetMapping("/{movieId}")
    @Operation(summary = "Get a movie by id", description = "Get a movie by id", tags = {"movies"}, operationId = "getMovieById", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Movie found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Movie not found")})
    public ResponseEntity<Object> getMovie(@PathVariable Integer movieId) {

        final var movie = movieService.getMovie(movieId);
        if (movie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "Get movies by name, genre and language", description = "Get movies by name, genre and language", tags = {"movies"}, operationId = "getMovies", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Movies found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Movies not found")})
    public ResponseEntity<Object> getMovies(@RequestParam(value = "movieName", required = false) String movieName,
                                            @RequestParam(value = "movieGenre", required = false) String movieGenre,
                                            @RequestParam(value = "movieLanguage", required = false) String movieLanguage) {

        List<Movie> movies = movieService.getMovies(movieName, movieGenre, movieLanguage);
        if (movies == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    @DeleteMapping("/{movieId}")
    @Operation(summary = "Delete a movie by movie id", description = "Delete a movie by movie id", tags = {"movies"}, operationId = "deleteMovieById", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Movie deleted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Movie not found")})
    public ResponseEntity<Object> deleteByMovieId(@PathVariable Integer movieId) {
        movieService.deleteByMovieId(movieId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @Operation(summary = "Get all movies", description = "Get all movies", tags = {"movies"}, operationId = "getAllMovies", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Movies found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Movies not found")})
    public ResponseEntity<Object> getAllMovies() {
        return new ResponseEntity<>(movieService.getAllMovies(), HttpStatus.OK);
    }
}
