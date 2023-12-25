package com.jaitechltd.movieservice.repository;

import com.jaitechltd.movieservice.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MovieRepository extends MongoRepository<Movie, String> {
    Movie findByMovieId(Integer movieId);

    List<Movie> findByMovieName(String movieName);

    void deleteByMovieId(Integer movieId);
}
