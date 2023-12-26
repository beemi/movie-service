package com.jaitechltd.movieservice.service;

import com.jaitechltd.movieservice.dto.MovieDTO;
import com.jaitechltd.movieservice.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    MovieDTO createMovie(Movie movie);

    Optional<MovieDTO> getMovieByMovieId(Integer movieId);

    List<MovieDTO> getMoviesByName(String movieName);

    void deleteByMovieId(Integer movieId);

    List<Movie> getAllMovies();

    List<Movie> getMovies(String movieName, String movieGenre, String movieLanguage);

    Movie updateMovie(Integer movieId, Movie movie);
}
