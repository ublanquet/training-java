package main.java.model;


import java.time.LocalDateTime;

public class ComputerBuilder {
    private long id = 0;
    private String name = "";
    private LocalDateTime introduced = null;
    private LocalDateTime discontinued = null;
    private Company company;



    public ComputerBuilder(){

    }

    public ComputerBuilder(long id, String name, LocalDateTime introduced, LocalDateTime discontinued, Company company) {
        this.id = id;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
        this.company = company;
    }

    public ComputerBuilder setId(long id) {
        this.id = id;
        return this;
    }
    public ComputerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ComputerBuilder setCompany(Company company) {
        this.company = company;
        return this;
    }

    public ComputerBuilder setIntroduced(LocalDateTime introduced){
        this.introduced = introduced;
        return this;
    }

    public ComputerBuilder setDiscontinued(LocalDateTime discontinued){
        this.discontinued = discontinued;
        return this;
    }

    public Computer build() {
        return new Computer(id,company, name,introduced, discontinued);
    }

}
