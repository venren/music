package com.example.music.search.api;
import com.example.music.search.entities.QSong;
import com.example.music.search.entities.Song;
import com.example.music.search.exception.ResourceNotFoundException;
import com.example.music.search.repository.SongRepository;
import com.querydsl.core.BooleanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(value = "/songs" , produces = "application/hal+json")
public class SongResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SongResource.class);

    @Autowired
    private SongRepository songRepository;

    @GetMapping
    public Page<Song> retrieveAllSongs(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        Page<Song> searchResultPage = songRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        LOGGER.info("Found {} songs. Returned page {} contains {} song entries",
                searchResultPage.getTotalElements(),
                searchResultPage.getNumber(),
                searchResultPage.getNumberOfElements()
        );

        return searchResultPage;
    }

    @GetMapping("/{id}")
    public Song getSong(@PathVariable long id)throws ResourceNotFoundException {
        Optional<Song> song = songRepository.findById(id);
        if(!song.isPresent()) {
            LOGGER.error("Song with {} not found", id);
            throw new ResourceNotFoundException(String.format("Song with %s is not found", id));
        }

        return song.get();
    }

    @PostMapping
    public ResponseEntity<Object> addSong(@RequestBody Song song) {
        Song savedSong = songRepository.save(song);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedSong.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @DeleteMapping("/{id}")
    public void deleteSong(@PathVariable long id) {
        songRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSong(@RequestBody Song song, @PathVariable long id) {

        Optional<Song> songOptional = songRepository.findById(id);

        if(!songOptional.isPresent()) {
            LOGGER.error("Song with {} not found", id);
            return ResponseEntity.notFound().build();
        }

        songRepository.save(song);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public Page<Song> searchSongs(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "singer", required = false) String singer,
            @RequestParam(name = "popularity", required = false) Integer popularity,
            @RequestParam(name = "genre", required = false) String genre) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (title != null && !title.isEmpty()) {
            booleanBuilder.and(QSong.song.title.containsIgnoreCase(title));
        }

        if (singer != null && !singer.isEmpty()) {
            booleanBuilder.and(QSong.song.singer.name.containsIgnoreCase(singer));
        }

        if (genre != null && !genre.isEmpty()) {
            booleanBuilder.and(QSong.song.genre.genre.containsIgnoreCase(genre));
        }

        if (popularity != null && popularity > 0) {
            booleanBuilder.and(
                    QSong.song.popularity.goe(popularity));
        }


        Page<Song> response = songRepository.findAll(booleanBuilder.getValue(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));

        LOGGER.info("Found {} songs. Returned page {} contains {} song entries",
                response.getTotalElements(),
                response.getNumber(),
                response.getNumberOfElements()
        );

        return response;
    }

    @GetMapping("/findByArtist")
    public Page<Song> findByArtist(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "singer", required = false) String singer
    ) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (singer != null && !singer.isEmpty()) {
            booleanBuilder.and(QSong.song.singer.name.containsIgnoreCase(singer));
        }

        return songRepository.findAll(booleanBuilder.getValue(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
    }

    @GetMapping("/findByGenre")
    public Page<Song> findByGenre(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "500") int size,
            @RequestParam(name = "genre", required = false) String genre
    ) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (genre != null && !genre.isEmpty()) {
            booleanBuilder.and(QSong.song.genre.genre.containsIgnoreCase(genre));
        }

        return songRepository.findAll(booleanBuilder.getValue(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
    }
}
