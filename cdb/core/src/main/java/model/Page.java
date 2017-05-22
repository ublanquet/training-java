package model;

import java.util.ArrayList;

public class Page<T> {
    public ArrayList<T> list;
    public int currentPage;
    private int nbEntries;
    private int maxPage;
    private long allPagesItemCount;

    /**
     * basic constructor.
     */
    public Page() {
      this.currentPage = 0;
    }

    /**
     * constrictor.
     * @param nbEntries entries by page
     */
    public Page(int nbEntries) {
        this.nbEntries = nbEntries;
        this.currentPage = 0;
    }

    /**
     * constructor.
     * @param nbEntries entries by page
     * @param currentPage current page
     */
    public Page(int nbEntries, int currentPage) {
        this.nbEntries = nbEntries;
        this.currentPage = currentPage;
    }

    /**
     * constructor.
     * @param list list of objs
     * @param page page number
     */
    public Page(ArrayList<T> list, int page) {
        this.list = list;
        this.nbEntries = list.size();
        this.currentPage = page;
    }

  /**
   * constructor.
   * @param list list of objs
   */
  public Page(ArrayList<T> list) {
    this.list = list;
    this.nbEntries = list.size();
    this.currentPage = 0;

  }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public long getAllPagesItemCount() {
        return allPagesItemCount;
    }

  /**
   * set total numbers of items across all pages, and calc the total number of pages required.
   * @param allPagesItemCount nb of items across all pages
   */
  public void setAllPagesItemCount(long allPagesItemCount) {
        this.allPagesItemCount = allPagesItemCount;
        if (nbEntries > 0) {
            maxPage = (int) allPagesItemCount / nbEntries + (allPagesItemCount % nbEntries == 0 ? 0 : 1);
        }
    }


    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public ArrayList<T> getListPage() {
        return list;
    }

  /**
   * set the list and set page size to it's size.
   * @param list liste
   */
  public void setList(ArrayList<T> list) {
        this.list = list;
        this.nbEntries = list.size();
    }

    public int getNbEntries() {
        return nbEntries;
    }

    public void setNbEntries(int nbEntries) {
        this.nbEntries = nbEntries;
    }

    public Long getFirstEntryIndex() {
        return (long) currentPage * nbEntries;
    }

    public Long getLastEntryIndex() {
        return (long) currentPage * nbEntries + nbEntries;
    }

    public int getNextPageIndex() {
        return currentPage < maxPage ? currentPage + 1 : currentPage;
    }

    public int getPrevPageIndex() {
        return currentPage > 0 ? currentPage - 1 : 0;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * add obj to list.
     * @param t obj to add
     */
    public void add(T t) {
        list.add(t);
    }


    @Override
    public String toString() {
        String entireString = "";
        for (int i = 0; i < nbEntries; i++) {
            entireString += list.get(i).toString() + "\n";
        }
        return entireString;
    }
}
