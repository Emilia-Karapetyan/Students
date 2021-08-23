package com.example.demo.student.util;

import java.util.List;

public class ContentResponse<T> {
    private Long totalElement;
    private List<T> content;

    public ContentResponse(Long totalElement, List<T> content) {
        this.totalElement = totalElement;
        this.content = content;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public Long getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(Long totalElement) {
        this.totalElement = totalElement;
    }
}
