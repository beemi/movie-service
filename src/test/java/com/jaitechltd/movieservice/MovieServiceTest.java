package com.jaitechltd.movieservice;

import com.jaitechltd.movieservice.model.Movie;
import com.jaitechltd.movieservice.repository.MovieRepository;
import com.jaitechltd.movieservice.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @InjectMocks
    private MovieService movieService;

    @Test
    public void testCreateMovie() {

        final var movie = Movie.builder()
                .movieId(1)
                .movieName("Test Movie")
                .movieDescription("Test Movie Description")
                .movieRating("5")
                .build();

        when(movieRepository.save(movie)).thenReturn(movie);

        movieService.createMovie(movie);

        assertThat(movieRepository.save(movie), any(Movie.class));
    }
}
