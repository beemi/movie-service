package com.jaitechltd.movieservice.repository;

import com.jaitechltd.movieservice.model.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {
    Movie findByMovieId(Integer movieId);

    void deleteByMovieId(Integer movieId);
}
