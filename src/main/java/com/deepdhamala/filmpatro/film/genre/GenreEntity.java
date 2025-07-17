package com.deepdhamala.filmpatro.film.genre;


import com.deepdhamala.filmpatro.common.AuditableEntity;
import com.deepdhamala.filmpatro.film.movie.MovieEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "genres")
@Audited
public class GenreEntity extends AuditableEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<MovieEntity> movies = new HashSet<>();
}
