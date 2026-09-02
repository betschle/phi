package com.neutronio.phi.ui.commons.charts.pagination;

import com.badlogic.gdx.math.MathUtils;

/**
 * A logical controller for pagination
 */
public class DefaultPaginator implements Paginator {

    private int currentPage = 1;
    private int maxPages = 1;

    public void setCurrentPage(int currentPage) {
        this.currentPage = MathUtils.clamp( currentPage, 1, this.maxPages);
    }

    @Override
    public int getMaxPages() {
        return maxPages;
    }

    public void setMaxPages(int maxPages) {
        this.maxPages = Math.abs(maxPages);
        this.currentPage = MathUtils.clamp( this.currentPage, 1, this.maxPages);
    }

    @Override
    public void configure(int totalItems, int itemsPerPage) {
        if( totalItems == 0) this.maxPages = 1;
        else this.maxPages = Math.abs(itemsPerPage > 0 ? (int) Math.ceil( totalItems / (float) itemsPerPage ) : 1);
        this.currentPage = MathUtils.clamp( this.currentPage, 1, this.maxPages);
    }

    @Override
    public void nextPage() {
        this.currentPage = MathUtils.clamp( this.currentPage + 1, 1, this.maxPages );
    }

    @Override
    public void previousPage() {
        this.currentPage = MathUtils.clamp( this.currentPage - 1, 1, this.maxPages );
    }

    @Override
    public int getCurrentPage() {
        return this.currentPage;
    }
}
