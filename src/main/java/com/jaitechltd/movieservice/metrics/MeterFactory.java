package com.jaitechltd.movieservice.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.springframework.stereotype.Component;

import static com.jaitechltd.movieservice.metrics.Constants.*;

@Component
public class MeterFactory {

    private final MeterRegistry meterRegistry;

    public MeterFactory(final MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public Counter addMovieSuccessCounter() {
        return meterRegistry.counter(
                MOVIE_SERVICE_CREATE_MOVIE,
                Tags.of(MOVIE_SERVICE, OUTCOME_TAG_SUCCESS));

    }

    public Counter addMovieFailureCounter() {
        return meterRegistry.counter(
                MOVIE_SERVICE_CREATE_MOVIE,
               Tags.of(MOVIE_SERVICE, OUTCOME_TAG_ERROR));
    }

    public Counter getMovieSuccessCounter() {
        return meterRegistry.counter(
                MOVIE_SERVICE_GET_MOVIE,
                Tags.of(MOVIE_SERVICE, OUTCOME_TAG_SUCCESS));

    }

    public Counter getMovieFailureCounter() {
        return meterRegistry.counter(
                MOVIE_SERVICE_GET_MOVIE,
                Tags.of(MOVIE_SERVICE, OUTCOME_TAG_ERROR));
    }

    public Counter deleteMovieSuccessCounter() {
        return meterRegistry.counter(
                MOVIE_SERVICE_DELETE_MOVIE,
                Tags.of(MOVIE_SERVICE, OUTCOME_TAG_SUCCESS));

    }

    public Counter deleteMovieFailureCounter() {
        return meterRegistry.counter(
                MOVIE_SERVICE_DELETE_MOVIE,
                Tags.of(MOVIE_SERVICE, OUTCOME_TAG_ERROR));
    }

    public Counter getAllMoviesSuccessCounter() {
        return meterRegistry.counter(
                MOVIE_SERVICE_GET_ALL_MOVIES,
                Tags.of(MOVIE_SERVICE, OUTCOME_TAG_SUCCESS));

    }

    public Counter getAllMoviesFailureCounter() {
        return meterRegistry.counter(
                MOVIE_SERVICE_GET_ALL_MOVIES,
                Tags.of(MOVIE_SERVICE, OUTCOME_TAG_ERROR));
    }

    public Counter updateMovieFailureCounter() {
        return meterRegistry.counter(
                MOVIE_SERVICE_UPDATE_MOVIE,
                Tags.of(MOVIE_SERVICE, OUTCOME_TAG_ERROR));
    }

    public Counter updateMovieSuccessCounter() {
        return meterRegistry.counter(
                MOVIE_SERVICE_UPDATE_MOVIE,
                Tags.of(MOVIE_SERVICE, OUTCOME_TAG_SUCCESS));
    }
}
