package com.jaitechltd.movieservice.metrics;


import org.springframework.stereotype.Component;

@Component
public class MetricsService {

    private final MeterFactory meterFactory;

    public MetricsService(final MeterFactory meterFactory) {
        this.meterFactory = meterFactory;
    }

    public void addMovieSuccessCounter() {
        meterFactory.addMovieSuccessCounter().increment();
    }

    public void addMovieFailureCounter() {
        meterFactory.addMovieFailureCounter().increment();
    }

    public void getMovieSuccessCounter() {
        meterFactory.getMovieSuccessCounter().increment();
    }

    public void getMovieFailureCounter() {
        meterFactory.getMovieFailureCounter().increment();
    }

    public void deleteMovieSuccessCounter() {
        meterFactory.deleteMovieSuccessCounter().increment();
    }

    public void deleteMovieFailureCounter() {
        meterFactory.deleteMovieFailureCounter().increment();
    }

    public void updateMovieSuccessCounter() {
        meterFactory.updateMovieSuccessCounter().increment();
    }

    public void updateMovieFailureCounter() {
        meterFactory.updateMovieFailureCounter().increment();
    }

    public void getAllMoviesSuccessCounter() {
        meterFactory.getAllMoviesSuccessCounter().increment();
    }

    public void getAllMoviesFailureCounter() {
        meterFactory.getAllMoviesFailureCounter().increment();
    }
}
