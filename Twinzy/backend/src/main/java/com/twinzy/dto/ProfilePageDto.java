package com.twinzy.dto;

import java.util.List;

public class ProfilePageDto {

    private List<ProfileSummaryDto> items;
    private int page;
    private int size;
    private long total;
    private int totalPages;

    public ProfilePageDto(List<ProfileSummaryDto> items, int page, int size, long total) {
        this.items = items;
        this.page = page;
        this.size = size;
        this.total = total;
        this.totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);
    }

    public List<ProfileSummaryDto> getItems() {
        return items;
    }

    public void setItems(List<ProfileSummaryDto> items) {
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
