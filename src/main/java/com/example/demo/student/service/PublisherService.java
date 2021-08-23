package com.example.demo.student.service;

import com.example.demo.student.model.Publisher;
import com.example.demo.student.model.dto.PublisherDto;
import com.example.demo.student.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public Page<PublisherDto> getPublisher(Pageable pageable){
        return publisherRepository.findAll(pageable).map(PublisherDto::toDto);
    }

    public void addPublisher(PublisherDto publisherDto){
        List<String> publisherList = publisherRepository.findPublisherName();
        if (!publisherList.contains(publisherDto.getFullName())){
            Publisher publisher = PublisherDto.toEntity(publisherDto);
            publisherRepository.save(publisher);
        }else{
            throw new IllegalStateException("Publisher has already exist");
        }

    }

    public void deletePublisherById(Long id) {
        if (!publisherRepository.existsById(id)){
            throw new IllegalStateException("Publisher with id " + id + "doesn't exist");
        }
        publisherRepository.deleteById(id);
    }
}
