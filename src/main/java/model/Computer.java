package main.java.model;

import java.time.*;

public class Computer {

    private long id;



    private long companyId;
    private Company company;
    private String name;
    private LocalDateTime introduced;
    private LocalDateTime discontinued;

    public Computer(){

    }

    public Computer(long id, Company company, String name, LocalDateTime introduced, LocalDateTime discontinued) {
        this.id = id;
        this.company = company;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
    }

    public Computer(long id, long companyId, String name, LocalDateTime introduced, LocalDateTime discontinued) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
    }

    public Computer(long companyId, String name, LocalDateTime introduced, LocalDateTime discontinued) {
        this.companyId = companyId;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
    }

    public Computer(long id, long companyId, String name) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getIntroduced() {
        return introduced;
    }

    public void setIntroduced(LocalDateTime introduced) {
        this.introduced = introduced;
    }

    public LocalDateTime getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(LocalDateTime discontinued) {
        this.discontinued = discontinued;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Computer computer = (Computer) o;

        return getId() == computer.getId();
    }

    @Override
    public String toString() {
        return "Computer{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", name='" + name + '\'' +
                ", introduced=" + introduced +
                ", discontinued=" + discontinued +
                '}';
    }
}
