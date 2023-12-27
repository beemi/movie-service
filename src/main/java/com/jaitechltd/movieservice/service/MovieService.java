package com.jaitechltd.movieservice.service;

import com.jaitechltd.movieservice.dto.MovieDTO;
import com.jaitechltd.movieservice.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    MovieDTO createMovie(Movie movie);

    Optional<MovieDTO> getMovieByMovieId(Integer movieId);

    List<MovieDTO> getMoviesByName(String movieName);

    void deleteByMovieId(Integer movieId);

    Page<Movie> getAllMovies(Pageable pageable);

    Page<Movie> getMovies(Pageable pageable,String movieName, String movieGenre, String movieLanguage);

    Movie updateMovie(Integer movieId, Movie movie);

    List<MovieDTO> getMoviesByCountry(final String movieCountry);
}
