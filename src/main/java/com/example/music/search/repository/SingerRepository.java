package com.example.music.search.repository;

import com.example.music.search.entities.Singer;
import com.example.music.search.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SingerRepository extends JpaRepository<Singer,Long>, JpaSpecificationExecutor<Song>, QuerydslPredicateExecutor<Song> {

}
