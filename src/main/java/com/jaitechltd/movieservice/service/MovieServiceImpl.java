package com.jaitechltd.movieservice.service;

import com.jaitechltd.movieservice.model.Movie;
import com.jaitechltd.movieservice.repository.MovieRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final MongoTemplate mongoTemplate;

    public MovieServiceImpl(MovieRepository movieRepository, MongoTemplate mongoTemplate) {
        this.movieRepository = movieRepository;
        this.mongoTemplate = mongoTemplate;
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

    @Override
    public List<Movie> getMovies(String movieName, String movieGenre, String movieLanguage) {

        Query query = new Query();
        List<Criteria> criteria = new ArrayList<>();

        if (movieName != null) {
            criteria.add(Criteria.where("movieName").is(movieName));
        }

        if (movieGenre != null) {
            criteria.add(Criteria.where("movieGenre").is(movieGenre));
        }

        if (movieLanguage != null) {
            criteria.add(Criteria.where("movieLanguage").is(movieLanguage));
        }

        if (!criteria.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[0])));
        }

        Pageable pageable = PageRequest.of(0, 10);

        query.with(pageable);

        return mongoTemplate.find(query, Movie.class);
    }

    @Override
    public Movie updateMovie(Integer movieId, Movie movie) {

        final var existingMovie = movieRepository.findByMovieId(movieId);

        existingMovie.setMovieName(movie.getMovieName());
        existingMovie.setMovieGenre(movie.getMovieGenre());
        existingMovie.setMovieLanguage(movie.getMovieLanguage());
        existingMovie.setMovieReleaseDate(movie.getMovieReleaseDate());
        existingMovie.setMovieDirector(movie.getMovieDirector());
        existingMovie.setMovieProducer(movie.getMovieProducer());
        existingMovie.setMovieCast(movie.getMovieCast());
        existingMovie.setMovieDescription(movie.getMovieDescription());
        existingMovie.setMovieUpdatedDate(new Date());
        existingMovie.setMovieStatus(movie.getMovieStatus());
        existingMovie.setMovieRating(movie.getMovieRating());
        existingMovie.setMovieDuration(movie.getMovieDuration());
        existingMovie.setMovieTrailer(movie.getMovieTrailer());
        existingMovie.setMoviePoster(movie.getMoviePoster());
        existingMovie.setMovieBanner(movie.getMovieBanner());
        existingMovie.setMovieCountry(movie.getMovieCountry());

        return movieRepository.save(existingMovie);
    }
}
