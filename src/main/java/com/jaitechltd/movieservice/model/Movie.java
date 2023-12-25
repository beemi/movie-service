package com.jaitechltd.movieservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Movie")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie {

    @Id
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
}
