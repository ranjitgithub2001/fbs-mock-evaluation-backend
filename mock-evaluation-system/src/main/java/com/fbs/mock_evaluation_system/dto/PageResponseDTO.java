package com.fbs.mock_evaluation_system.dto;

import java.util.List;

public class PageResponseDTO<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public PageResponseDTO(List<T> content, int page, int size,
            long totalElements, int totalPages) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    // FIX #14 — added pagination helpers for frontend
    public boolean isHasNext() {
        return page < totalPages - 1;
    }

    public boolean isHasPrevious() {
        return page > 0;
    }
}