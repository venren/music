package com.example.music.search.entities;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

@Entity
@Table(name = "song")
public class Song implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name = "genreId", nullable = false)
    private Genre genre;

    @Column(name = "popularity", nullable = false)
    @Min(0)
    @Max(10)
    private int popularity;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="singerId")
    private Singer singer;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Singer getSinger() {
        return singer;
    }

    public Genre getGenre() {
        return genre;
    }

    public int getPopularity() {
        return popularity;
    }
}
