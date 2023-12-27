package com.jaitechltd.movieservice.service;

import com.jaitechltd.movieservice.config.properties.EventsKafkaProperties;
import com.jaitechltd.movieservice.dto.MovieDTO;
import com.jaitechltd.movieservice.exceptions.*;
import com.jaitechltd.movieservice.kafka.KafkaProducerService;
import com.jaitechltd.movieservice.metrics.MetricsService;
import com.jaitechltd.movieservice.model.Movie;
import com.jaitechltd.movieservice.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public MovieDTO createMovie(Movie movieRequest) {

        Optional<Movie> existingMovie = movieRepository.findByMovieId(movieRequest.getMovieId());
        if (existingMovie.isPresent()) {
            throw new MovieCreationException("Movie already exists with id: " + movieRequest.getMovieId());
        }

        movieRequest.setMovieCreatedDate(Instant.now());
        movieRequest.setMovieUpdatedDate(Instant.now());

        try {
            final var savedMovie = movieRepository.save(movieRequest);
            kafkaProducerService.publish(eventsKafkaProperties.getTopic(), savedMovie);
            metricsService.addMovieSuccessCounter();
            return MovieDTO.fromMovie(savedMovie);
        } catch (Exception e) {
            log.error("Error while saving movie", e);
            metricsService.addMovieFailureCounter();
            throw new MovieCreationException("Failed to create movie", e);
        }
    }

    @Override
    @Cacheable(value = "movies", key = "#movieId")
    public Optional<MovieDTO> getMovieByMovieId(final Integer movieId) {

        Optional<Movie> movie = movieRepository.findByMovieId(movieId);
        if (movie.isEmpty()) {
            metricsService.getMovieFailureCounter();
            throw new MovieNotFoundException("Movie not found with id: " + movieId);
        }
        metricsService.getMovieSuccessCounter();
        return Optional.of(MovieDTO.fromMovie(movie.get()));
    }

    @Override
    public List<MovieDTO> getMoviesByName(final String movieName) {
        List<Movie> movies = movieRepository.findByMovieName(movieName);

        if (movies.isEmpty()) {
            metricsService.getMovieFailureCounter();
            return Collections.emptyList();
        }
        metricsService.getMovieSuccessCounter();
        return movies.stream()
                .map(MovieDTO::fromMovie)
                .collect(Collectors.toList());
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
    public Page<Movie> getAllMovies(Pageable pageable) {
        try {
            metricsService.getAllMoviesSuccessCounter();
            return movieRepository.findAll(pageable);
        } catch (DataAccessException e) {
            log.error("Error while fetching all movies", e);
            metricsService.getAllMoviesFailureCounter();
            throw new MoviesDataAccessException("Failed to fetch all movies", e);
        } catch (Exception e) {
            log.error("Unexpected error while fetching all movies", e);
            metricsService.getAllMoviesFailureCounter();
            throw new MoviesUnexpectedException("Unexpected error occurred", e);
        }
    }

    @Override
    public Page<Movie> getMovies(Pageable pageable, String movieName, String movieGenre, String movieLanguage) {

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

        long total = mongoTemplate.count(query, Movie.class);
        query.with(pageable);
        List<Movie> movies = mongoTemplate.find(query, Movie.class);

        return new PageImpl<>(movies, pageable, total);
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
    public List<MovieDTO> getMoviesByCountry(final String movieCountry) {

        List<Movie> movies = movieRepository.findByMovieCountry(movieCountry);
        if (movies.isEmpty()) {
            metricsService.getMovieFailureCounter();
            return Collections.emptyList();
        }
        metricsService.getMovieSuccessCounter();
        return movies.stream()
                .map(MovieDTO::fromMovie)
                .collect(Collectors.toList());
    }
}
