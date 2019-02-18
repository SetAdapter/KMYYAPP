package com.kmwlyy.patient.kdoctor.bean;

import java.util.List;

/**
 * @Description描述:
 * @Author作者: hx
 * @Date日期: 2016/8/24
 */
public class SearchBean {
    private boolean last;
    private int totalPages;
    private int totalElements;
    private int size;
    private int number;
    private int numberOfElements;
    private boolean first;
    private String sort;
    private List<SearchItemBean> content;

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<SearchItemBean> getSearchItemList() {
        return content;
    }

    public void setSearchItemList(List<SearchItemBean> searchItemList) {
        this.content = searchItemList;
    }

}
