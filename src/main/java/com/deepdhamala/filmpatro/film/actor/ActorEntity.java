package com.deepdhamala.filmpatro.film.actor;


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
@Table(name = "actors")
@Audited
public class ActorEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToMany(mappedBy = "actors")
    private Set<MovieEntity> movies = new HashSet<>();
}
