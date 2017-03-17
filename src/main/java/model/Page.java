package model;

import java.util.ArrayList;

public class Page<T> {
    public ArrayList<T> list;


    private int nbEntries;
    private int maxPage;
    public int currentPage;

    public Page (){
    }
    public Page (int nbEntries){
        this.nbEntries = nbEntries;
        this.currentPage = 0;
    }
    public Page (int nbEntries, int currentPage){
        this.nbEntries = nbEntries;
        this.currentPage = currentPage;
    }
    public Page (ArrayList<T> list, int page){
        this.list = list;
        this.currentPage = page;
    }


    public void setNbEntries(int nbEntries) {
        this.nbEntries = nbEntries;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }


    public ArrayList<T> getListPage(){
        return list;
    }

    public void setList(ArrayList<T> list){
        this.list = list;
    }

    public int getNbEntries(){
        return nbEntries;
    }

    public long getFirstEntryIndex(){
        return currentPage * nbEntries;
    }

    public long getLastEntryIndex(){
        return currentPage * nbEntries + nbEntries;
    }

    public int getNextPageIndex(){
        return currentPage < maxPage ? currentPage + 1 : currentPage;
    }

    public int getPrevPageIndex(){
        return currentPage > 0 ? currentPage - 1 : 0;
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public void add(T t){
        list.add(t);
    }


    @Override
    public String toString() {
        String entireString = "";
        for(int i = 0; i<nbEntries; i++){
            entireString += list.get(i).toString() + "\n";
        }
        return entireString;
    }
}
