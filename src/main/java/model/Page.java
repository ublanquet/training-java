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
        this.currentPage = page;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public ArrayList<T> getListPage() {
        return list;
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
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
