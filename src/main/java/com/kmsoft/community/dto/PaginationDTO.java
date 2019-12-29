package com.kmsoft.community.dto;

import java.util.LinkedList;
import java.util.List;

public class PaginationDTO {
    private List<QuestionDTO> questionDTOS;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private Integer totalPage;
    private List<Integer> pages = new LinkedList<>();


    public void setPagination(Integer totalPage, Integer page, Integer size) {
        this.totalPage = totalPage;
        pages.add(page);
        for(int i = 1; i < 4; i++){
            if(page - i > 0)
                pages.add(0, page - i);
            if(page + i <= totalPage)
                pages.add(page + i);
        }
        if(page < 1) page = 1;
        if(page > totalPage) page = totalPage;
        this.page = page;
        //是否展示上一页
        showPrevious = page != 1;
        //是否展示下一页
        showNext = page != totalPage;
        //是否展示首页
        showFirstPage = !pages.contains(1);
        //是否展示尾页
        showEndPage = !pages.contains(totalPage);
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public List<QuestionDTO> getQuestionDTOS() {
        return questionDTOS;
    }

    public void setQuestionDTOS(List<QuestionDTO> questionDTOS) {
        this.questionDTOS = questionDTOS;
    }

    public boolean isShowPrevious() {
        return showPrevious;
    }

    public void setShowPrevious(boolean showPrevious) {
        this.showPrevious = showPrevious;
    }

    public boolean isShowFirstPage() {
        return showFirstPage;
    }

    public void setShowFirstPage(boolean showFirstPage) {
        this.showFirstPage = showFirstPage;
    }

    public boolean isShowNext() {
        return showNext;
    }

    public void setShowNext(boolean showNext) {
        this.showNext = showNext;
    }

    public boolean isShowEndPage() {
        return showEndPage;
    }

    public void setShowEndPage(boolean showEndPage) {
        this.showEndPage = showEndPage;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }


}
