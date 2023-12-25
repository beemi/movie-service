package com.jaitechltd.movieservice.service;

import com.jaitechltd.movieservice.exceptions.MovieCreationException;
import com.jaitechltd.movieservice.exceptions.UpdateMovieException;
import com.jaitechltd.movieservice.metrics.MetricsService;
import com.jaitechltd.movieservice.model.Movie;
import com.jaitechltd.movieservice.repository.MovieRepository;
import com.jaitechltd.movieservice.kafka.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final MongoTemplate mongoTemplate;
    private final MetricsService metricsService;
    private final KafkaProducerService kafkaProducerService;

    public MovieServiceImpl(MovieRepository movieRepository,
                            MongoTemplate mongoTemplate,
                            MetricsService metricsService,
                            KafkaProducerService kafkaProducerService) {
        this.movieRepository = movieRepository;
        this.mongoTemplate = mongoTemplate;
        this.metricsService = metricsService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    @Transactional
    public Movie createMovie(Movie movie) throws MovieCreationException {

        final var date = new Date();
        movie.setMovieCreatedDate(date);
        movie.setMovieUpdatedDate(date);

        try {
            Movie savedMovie = movieRepository.save(movie);
            kafkaProducerService.publish("movie", savedMovie);
            metricsService.addMovieSuccessCounter();
            return savedMovie;
        } catch (Exception e) {
            log.error("Error while saving movie", e);
            metricsService.addMovieFailureCounter();
            throw new MovieCreationException("Failed to create movie", e);
        }
    }

    @Override
    public Movie getMovie(Integer movieId) {
        try {
            metricsService.getMovieSuccessCounter();
            return movieRepository.findByMovieId(movieId);
        } catch (Exception e) {
            log.error("Error while fetching movie", e);
            metricsService.getMovieFailureCounter();
            return null;
        }
    }

    @Override
    public void deleteByMovieId(Integer movieId) {

        try {
            metricsService.deleteMovieSuccessCounter();
            movieRepository.deleteByMovieId(movieId);
        } catch (Exception e) {
            log.error("Error while deleting movie", e);
            metricsService.deleteMovieFailureCounter();
        }
    }

    @Override
    public List<Movie> getAllMovies() {

        try {
            metricsService.getAllMoviesSuccessCounter();
            return movieRepository.findAll();
        } catch (Exception e) {
            log.error("Error while fetching all movies", e);
            metricsService.getAllMoviesFailureCounter();
            return new ArrayList<>();
        }
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

        try {
            Movie savedMovie = movieRepository.save(existingMovie);
            metricsService.updateMovieSuccessCounter();
            return savedMovie;
        } catch (Exception e) {
            log.error("Error while updating movie", e);
            metricsService.updateMovieFailureCounter();
            throw new UpdateMovieException("Failed to update movie", e);
        }
    }
}
