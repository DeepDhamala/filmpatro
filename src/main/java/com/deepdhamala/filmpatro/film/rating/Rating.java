package com.deepdhamala.filmpatro.film.rating;

import com.deepdhamala.filmpatro.common.AuditableEntity;
import com.deepdhamala.filmpatro.film.movie.MovieEntity;
import com.deepdhamala.filmpatro.user.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "ratings")
@Audited
public class Rating extends AuditableEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id", nullable = false)
    private MovieEntity movie;

    @Column(nullable = false)
    private Integer stars; // 1 to 5 or any other scale
}
