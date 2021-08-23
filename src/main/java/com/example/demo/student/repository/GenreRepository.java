package com.example.demo.student.repository;

import com.example.demo.student.model.Genre;
import com.example.demo.student.model.dto.GenreDto;
import com.example.demo.student.util.GenreSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("Select name from Genre")
    List<String> findGenres();

    Optional<Genre> findById(Long id);

    List<Genre> findAllByIdIn(Collection<Long> list);

    @Query("select g from Genre g where g.name like concat('%',:#{#searchCriteria.name},'%') group by g.id")
    Page<Genre> searchGenre(@Param("searchCriteria") GenreSearchCriteria searchCriteria, Pageable pageable);

    @Query("select g from Genre g where (:#{#searchCriteria.name} is null) or g.name like concat('%',:#{#searchCriteria.name},'%') group by g.id")
    Page<Genre> getGenresBySearchCriteria(@Param("searchCriteria") GenreSearchCriteria searchCriteria, Pageable pageable);
}
