package com.jaitechltd.movieservice.controller;

import com.jaitechltd.movieservice.dto.MovieDTO;
import com.jaitechltd.movieservice.exceptions.MovieCreationException;
import com.jaitechltd.movieservice.model.ErrorResponse;
import com.jaitechltd.movieservice.model.Movie;
import com.jaitechltd.movieservice.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;


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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Movie already exists", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)))})
    public ResponseEntity<Object> createMovie(@Valid @RequestBody Movie movieRequest) throws MovieCreationException {

        log.info("Create movie request received: {}", movieRequest);

        final var savedMovie = movieService.createMovie(movieRequest);
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
    }

    @PutMapping("/{movieId}")
    @Operation(summary = "Update an existing movie, given movie id", description = "Update an existing movie, given movie id", tags = {"movies"}, operationId = "updateMovie", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Movie updated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Movie not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "405", description = "Validation exception")})
    public ResponseEntity<Object> updateMovie(@PathVariable Integer movieId, @RequestBody Movie movieRequest) {

        log.info("Update movie request received: {}", movieRequest);

        Optional<MovieDTO> movie = movieService.getMovieByMovieId(movieId);

        if (movie.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            final var updatedMovie = movieService.updateMovie(movieId, movieRequest);
            return ok(updatedMovie);
        }
    }


    @GetMapping("/{movieId}")
    @Operation(summary = "Get a movie by id", description = "Get a movie by id", tags = {"movies"}, operationId = "getMovieById", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Movie found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Movie not found")},
            parameters = {
                    @Parameter(name = "movieId", description = "movie id", required = true, schema = @Schema(type = "integer", defaultValue = "1"))})
    public ResponseEntity<Object> getMovie(@PathVariable Integer movieId) {

        log.info("Get movie request received for id: {}", movieId);

        Optional<MovieDTO> movieResponse = movieService.getMovieByMovieId(movieId);
        return ok(movieResponse);
    }

    @GetMapping("/searchByName")
    @Operation(summary = "Get movies by name", description = "Get movies by name", tags = {"movies"}, operationId = "getMoviesByName", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Movies found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Movies not found")},
            parameters = {
                    @Parameter(name = "movieName", description = "movie name", required = true, schema = @Schema(type = "string", defaultValue = "The Matrix"))})
    public ResponseEntity<Object> getMoviesByName(@RequestParam(value = "movieName", required = true) final String movieName) {

        log.info("Get movies request received for name: {}", movieName);

        List<MovieDTO> movies = movieService.getMoviesByName(movieName);
        return ok(movies);
    }

    @GetMapping("/searchByCountry")
    @Operation(summary = "Get movies by country", description = "Get movies by country", tags = {"movies"}, operationId = "getMoviesByCountry", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Movies found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Movies not found")},
            parameters = {
                    @Parameter(name = "movieCountry", description = "movie country", required = true, schema = @Schema(type = "string", defaultValue = "USA"))})
    public ResponseEntity<Object> getMoviesByCountry(@RequestParam(value = "movieCountry", required = true) final String movieCountry) {

        log.info("Get movies request received for country: {}", movieCountry);

        List<MovieDTO> movies = movieService.getMoviesByCountry(movieCountry);
        return ok(movies);
    }

    @DeleteMapping("/{movieId}")
    @Operation(summary = "Delete a movie by movie id", description = "Delete a movie by movie id", tags = {"movies"}, operationId = "deleteMovieById", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Movie deleted"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid id supplied"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Movie not found")})
    public ResponseEntity<Object> deleteByMovieId(@PathVariable Integer movieId) {

        log.info("Delete movie request received for id: {}", movieId);

        movieService.deleteByMovieId(movieId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    @Operation(summary = "Get movies by name, genre and language", description = "Get movies by name, genre and language", tags = {"movies"}, operationId = "getMovies", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Movies found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Movies not found")},
            parameters = {
                    @Parameter(name = "page", description = "page number", required = false, schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size", description = "page size", required = false, schema = @Schema(type = "integer", defaultValue = "10")),
                    @Parameter(name = "movieName", description = "movie name", required = false, schema = @Schema(type = "string", defaultValue = "The Matrix")),
                    @Parameter(name = "movieGenre", description = "movie genre", required = false, schema = @Schema(type = "string", defaultValue = "Action")),
                    @Parameter(name = "movieLanguage", description = "movie language", required = false, schema = @Schema(type = "string", defaultValue = "English"))})
    public Page<Movie> getMovies(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(value = "movieName", required = false) String movieName,
                                 @RequestParam(value = "movieGenre", required = false) String movieGenre,
                                 @RequestParam(value = "movieLanguage", required = false) String movieLanguage) {
        log.info("Get movies request received for name: {}, genre: {}, language: {}", movieName, movieGenre, movieLanguage);

        final var pageable = PageRequest.of(page, size);

        return movieService.getMovies(pageable, movieName, movieGenre, movieLanguage);
    }

    @GetMapping
    @Operation(summary = "Get all movies", description = "Get all movies with pagination", tags = {"movies"}, operationId = "getAllMovies", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Movies found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Movies not found")},
            parameters = {
                    @Parameter(name = "page", description = "page number", required = false, schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size", description = "page size", required = false, schema = @Schema(type = "integer", defaultValue = "10"))})
    public Page<Movie> getAllMovies(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size) {

        log.info("Get all movies request with page: {}, size: {}", page, size);

        final var pageable = PageRequest.of(page, size);

        return movieService.getAllMovies(pageable);
    }
}
