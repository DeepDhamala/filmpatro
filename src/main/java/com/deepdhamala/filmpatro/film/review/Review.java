package com.deepdhamala.filmpatro.film.review;

import com.deepdhamala.filmpatro.common.AuditableEntity;
import com.deepdhamala.filmpatro.film.movie.Movie;
import com.deepdhamala.filmpatro.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "reviews")
@Audited
@SuperBuilder
public class Review extends AuditableEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(nullable = false, length = 2000)
    private String reviewText;

    @Column(nullable = false)
    private Integer rating;
}
