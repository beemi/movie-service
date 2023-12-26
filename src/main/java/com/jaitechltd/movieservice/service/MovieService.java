package com.jaitechltd.movieservice.service;

import com.jaitechltd.movieservice.exceptions.MovieCreationException;
import com.jaitechltd.movieservice.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    Movie createMovie(Movie movie) throws MovieCreationException;

    Optional<Movie> getMovie(Integer movieId);

    void deleteByMovieId(Integer movieId);

    List<Movie> getAllMovies();

    List<Movie> getMovies(String movieName, String movieGenre, String movieLanguage);

    Movie updateMovie(Integer movieId, Movie movie);

    List<Movie> getMoviesByName(String movieName);
}
