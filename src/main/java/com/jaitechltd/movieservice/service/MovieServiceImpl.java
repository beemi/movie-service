package com.jaitechltd.movieservice.service;

import com.jaitechltd.movieservice.config.properties.EventsKafkaProperties;
import com.jaitechltd.movieservice.exceptions.MovieCreationException;
import com.jaitechltd.movieservice.exceptions.UpdateMovieException;
import com.jaitechltd.movieservice.kafka.KafkaProducerService;
import com.jaitechltd.movieservice.metrics.MetricsService;
import com.jaitechltd.movieservice.model.Movie;
import com.jaitechltd.movieservice.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MovieServiceImpl implements MovieService {

    private final EventsKafkaProperties eventsKafkaProperties;
    private final MovieRepository movieRepository;
    private final MongoTemplate mongoTemplate;
    private final MetricsService metricsService;
    private final KafkaProducerService kafkaProducerService;

    public MovieServiceImpl(EventsKafkaProperties eventsKafkaProperties, MovieRepository movieRepository,
                            MongoTemplate mongoTemplate,
                            MetricsService metricsService,
                            KafkaProducerService kafkaProducerService) {
        this.eventsKafkaProperties = eventsKafkaProperties;
        this.movieRepository = movieRepository;
        this.mongoTemplate = mongoTemplate;
        this.metricsService = metricsService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    @Transactional
    public Movie createMovie(Movie movie) throws MovieCreationException {

        movie.setMovieCreatedDate(Instant.now());
        movie.setMovieUpdatedDate(Instant.now());

        try {
            final var savedMovie = movieRepository.save(movie);
            kafkaProducerService.publish(eventsKafkaProperties.getTopic(), savedMovie);
            metricsService.addMovieSuccessCounter();
            return savedMovie;
        } catch (Exception e) {
            log.error("Error while saving movie", e);
            metricsService.addMovieFailureCounter();
            throw new MovieCreationException("Failed to create movie", e);
        }
    }

    @Override
    public Optional<Movie> getMovie(Integer movieId) {
        try {
            metricsService.getMovieSuccessCounter();
            return movieRepository.findByMovieId(movieId);
        } catch (Exception e) {
            log.error("Error while fetching movie", e);
            metricsService.getMovieFailureCounter();
            return Optional.empty();
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

        Optional<Movie> existingMovie = movieRepository.findByMovieId(movieId);

        if (existingMovie.isEmpty()) {
            throw new UpdateMovieException("Movie not found");
        }

        existingMovie.get().setMovieName(movie.getMovieName());
        existingMovie.get().setMovieGenre(movie.getMovieGenre());
        existingMovie.get().setMovieLanguage(movie.getMovieLanguage());
        existingMovie.get().setMovieReleaseDate(movie.getMovieReleaseDate());
        existingMovie.get().setMovieDirector(movie.getMovieDirector());
        existingMovie.get().setMovieProducer(movie.getMovieProducer());
        existingMovie.get().setMovieCast(movie.getMovieCast());
        existingMovie.get().setMovieDescription(movie.getMovieDescription());
        existingMovie.get().setMovieUpdatedDate(Instant.now());
        existingMovie.get().setMovieStatus(movie.getMovieStatus());
        existingMovie.get().setMovieRating(movie.getMovieRating());
        existingMovie.get().setMovieDuration(movie.getMovieDuration());
        existingMovie.get().setMovieTrailer(movie.getMovieTrailer());
        existingMovie.get().setMoviePoster(movie.getMoviePoster());
        existingMovie.get().setMovieBanner(movie.getMovieBanner());
        existingMovie.get().setMovieCountry(movie.getMovieCountry());

        try {
            Movie savedMovie = movieRepository.save(existingMovie.get());
            metricsService.updateMovieSuccessCounter();
            return savedMovie;
        } catch (Exception e) {
            log.error("Error while updating movie", e);
            metricsService.updateMovieFailureCounter();
            throw new UpdateMovieException("Failed to update movie", e);
        }
    }

    @Override
    public List<Movie> getMoviesByName(final String movieName) {
        return movieRepository.findByMovieName(movieName);
    }
}
