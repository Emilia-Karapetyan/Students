package com.example.demo.student.controller;

import com.example.demo.student.model.dto.PublisherDto;
import com.example.demo.student.service.PublisherService;
import com.example.demo.student.util.ContentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/publisher")
public class PublisherController {
    private final PublisherService publisherService;

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping
    public ContentResponse<PublisherDto> getPublisher(Pageable pageable){
        Page<PublisherDto> publisherDtos = publisherService.getPublisher(pageable);
        return new ContentResponse<>(publisherDtos.getTotalElements(),publisherDtos.getContent());
    }

    @PostMapping
    public void addPublisher(@RequestBody PublisherDto publisherDto) throws Exception {
        if (publisherDto == null){
            throw new Exception("Fill all fields");
        }
        publisherService.addPublisher(publisherDto);
    }

    @DeleteMapping("/{id}")
    public void deletePublisher(@PathVariable Long id){
        publisherService.deletePublisherById(id);
    }

}
