package com.example.demo.student.repository;

import com.example.demo.student.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublisherRepository extends JpaRepository<Publisher, Long> {
    @Query("Select fullName from Publisher")
    List<String> findPublisherName();


    Publisher getPublisherById(Long id);
}
