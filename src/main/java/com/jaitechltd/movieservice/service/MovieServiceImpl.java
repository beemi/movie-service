package com.jaitechltd.movieservice.service;

import com.jaitechltd.movieservice.model.Movie;
import com.jaitechltd.movieservice.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Movie createMovie(Movie movie) {

        final var date = new Date();
        movie.setMovieCreatedDate(date);
        movie.setMovieUpdatedDate(date);

        return movieRepository.save(movie);
    }

    @Override
    public Movie getMovie(Integer movieId) {
        return movieRepository.findByMovieId(movieId);
    }

    @Override
    public void deleteByMovieId(Integer movieId) {
        movieRepository.deleteByMovieId(movieId);
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
}
