package com.jaitechltd.movieservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "Movie")
public class Movie {

    @Id
    @Indexed(unique = true)
    @Positive(message = "Movie ID must be positive")
    private Integer movieId;

    @Indexed
    @NotBlank(message = "Movie name cannot be blank")
    private String movieName;
    @Indexed
    private String movieGenre;
    @Indexed
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
    @Indexed
    private String movieCountry;
}
