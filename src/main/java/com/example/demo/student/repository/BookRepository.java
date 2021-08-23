package com.example.demo.student.repository;

import com.example.demo.student.model.Book;
import com.example.demo.student.model.dto.BookDto;
import com.example.demo.student.util.SearchCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("Select title from Book")
    List<String> getBookTitle();

    @Query("select b from Book b " +
            "where (:#{#searchCriteria.title} is null) or b.title like concat('%',:#{#searchCriteria.title},'%') and " +
            "(:#{#searchCriteria.publisherId} is null) or b.publisher.id=(:#{#searchCriteria.publisherId}) " +
            "order by b.id")
    Page<Book> getBooksByCriteria(@Param("searchCriteria") SearchCriteria searchCriteria, Pageable pageable);

    boolean existsByTitle(String title);

    @Query("select b from Book b where b.title like concat('%',:#{#searchCriteria.title},'%') order by b.id")
    Page<Book> searchBook(@Param("searchCriteria") SearchCriteria searchCriteria, Pageable pageable);

//    @Query("select new com.example.demo.student.model.dto.BookDto(b.title, b.date, b.publisher.fullName, b.authors,b.genres) " +
//            "from Book b")
//    List<BookDto> getAllBooks();
}
