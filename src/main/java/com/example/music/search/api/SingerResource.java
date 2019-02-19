package com.example.music.search.api;

import com.example.music.search.entities.QSinger;
import com.example.music.search.entities.QSong;
import com.example.music.search.entities.Singer;
import com.example.music.search.entities.Song;
import com.example.music.search.exception.ResourceNotFoundException;
import com.example.music.search.repository.SingerRepository;
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
@RequestMapping("/singers")
public class SingerResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingerResource.class);

    @Autowired
    private SingerRepository singerRepository;

    @GetMapping
    public Page<Singer> retrieveAllSingers(@RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "10") int size){
        Page<Singer> searchResultPage = singerRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
        LOGGER.info("Found {} singers. Returned page {} contains {} singer entries",
                searchResultPage.getTotalElements(),
                searchResultPage.getNumber(),
                searchResultPage.getNumberOfElements()
        );

        return searchResultPage;
    }

    @GetMapping("/{id}")
    public Singer getSinger(@PathVariable long id)throws ResourceNotFoundException {
        Optional<Singer> singer = singerRepository.findById(id);
        if(!singer.isPresent()) {
            LOGGER.error("Singer with {} not found", id);
            throw new ResourceNotFoundException(String.format("Singer with %s is not found", id));
        }

        return singer.get();
    }

    @PostMapping
    public ResponseEntity<Object> addSinger(@RequestBody Singer singer) {
        Singer savedStudent = singerRepository.save(singer);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedStudent.getId()).toUri();

        return ResponseEntity.created(location).build();

    }

    @DeleteMapping("/{id}")
    public void deleteSinger(@PathVariable long id) {
        singerRepository.deleteById(id);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSinger(@RequestBody Singer singer, @PathVariable long id) {

        Optional<Singer> singerOptional = singerRepository.findById(id);

        if (!singerOptional.isPresent()) {
            LOGGER.error("Singer with {} not found", id);
            return ResponseEntity.notFound().build();
        }

        singerRepository.save(singer);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public Page<Song> searchSinger(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "name", required = false) String name) {

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (name != null && !name.isEmpty()) {
            booleanBuilder.and(QSinger.singer.name.containsIgnoreCase(name));
        }

        return singerRepository.findAll(booleanBuilder.getValue(),
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id")));
    }
}
