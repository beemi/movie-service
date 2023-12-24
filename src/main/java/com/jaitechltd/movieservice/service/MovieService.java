package com.jaitechltd.movieservice.service;

import com.jaitechltd.movieservice.exceptions.MovieCreationException;
import com.jaitechltd.movieservice.model.Movie;

import java.util.List;

public interface MovieService {

    Movie createMovie(Movie movie) throws MovieCreationException;

    Movie getMovie(Integer movieId);

    void deleteByMovieId(Integer movieId);

    List<Movie> getAllMovies();

    List<Movie> getMovies(String movieName, String movieGenre, String movieLanguage);

    Movie updateMovie(Integer movieId, Movie movie);
}
