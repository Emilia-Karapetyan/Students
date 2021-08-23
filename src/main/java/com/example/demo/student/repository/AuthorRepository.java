package com.example.demo.student.repository;

import com.example.demo.student.model.Author;
import com.example.demo.student.model.dto.AuthorDto;
import com.example.demo.student.util.AuthorSearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("Select fullName from Author")
    List<String> findAuthors();
    List<Author> findAllByIdIn(Collection<Long> list);
    Optional<Author> findById(Long id);

    @Query("select a from Author a where a.fullName like concat('%',:#{#searchCriteria.fullName},'%') order by a.id ")
    Page<Author> searchAuthors(@Param("searchCriteria") AuthorSearchCriteria searchCriteria, Pageable pageable);

    @Query("Select a from Author a where (:#{#searchCriteria.fullName} is null) or a.fullName like concat('%',:#{#searchCriteria.fullName},'%') order by a.id  ")
    Page<Author> getAuthorsBySearchCriteria(@Param("searchCriteria") AuthorSearchCriteria searchCriteria, Pageable pageable);
}
