package com.example.demo.student.model.dto;

import com.example.demo.student.model.Publisher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PublisherDto {
    private String fullName;

    public static PublisherDto toDto(Publisher publisher){
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setFullName(publisher.getFullName());
        return publisherDto;
    }

    public static Publisher toEntity(PublisherDto publisherDto){
        Publisher publisher = new Publisher();
        publisher.setFullName(publisherDto.getFullName());
        return publisher;
    }
}
