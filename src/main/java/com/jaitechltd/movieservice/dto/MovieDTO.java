package com.jaitechltd.movieservice.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jaitechltd.movieservice.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieDTO implements Serializable {
    private Integer movieId;
    private String movieName;
    private String movieGenre;
    private String movieLanguage;
    private String movieReleaseDate;
    private String movieDirector;
    private String movieProducer;
    private String movieCast;
    private String movieDescription;
    private Instant movieCreatedDate;
    private Instant movieUpdatedDate;
    private String movieStatus;
    private String movieRating;
    private String movieDuration;
    private String movieTrailer;
    private String moviePoster;
    private String movieBanner;
    private String movieCountry;


    public static MovieDTO fromMovie(Movie movie) {
        return getMovieDTO(movie);
    }

    public static MovieDTO getMovieDTO(Movie movie) {
        return MovieDTO.builder()
                .movieId(movie.getMovieId())
                .movieName(movie.getMovieName())
                .movieGenre(movie.getMovieGenre())
                .movieLanguage(movie.getMovieLanguage())
                .movieReleaseDate(movie.getMovieReleaseDate())
                .movieDirector(movie.getMovieDirector())
                .movieProducer(movie.getMovieProducer())
                .movieCast(movie.getMovieCast())
                .movieDescription(movie.getMovieDescription())
                .movieCreatedDate(movie.getMovieCreatedDate())
                .movieUpdatedDate(movie.getMovieUpdatedDate())
                .movieStatus(movie.getMovieStatus())
                .movieRating(movie.getMovieRating())
                .movieDuration(movie.getMovieDuration())
                .movieTrailer(movie.getMovieTrailer())
                .moviePoster(movie.getMoviePoster())
                .movieBanner(movie.getMovieBanner())
                .movieCountry(movie.getMovieCountry())
                .build();
    }
}
