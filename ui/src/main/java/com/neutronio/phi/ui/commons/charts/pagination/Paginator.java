package com.neutronio.phi.ui.commons.charts.pagination;

/**
 * An interface for components that use pagination. To be used together
 * with the pagination component.
 */
public interface Paginator { // TODO belongs to util package

    int getMaxPages();
    void setMaxPages(int maxPages);
    void configure(int totalItems, int itemsPerPage);
    void nextPage();
    void previousPage();
    int getCurrentPage();
    void setCurrentPage(int currentPage);
}
