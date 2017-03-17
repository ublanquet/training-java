package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Computer {
    private long id;
    private long companyId;

    private Company company;
    private String name;
    private LocalDateTime introduced;
    private LocalDateTime discontinued;

    /**
     * basic constructor.
     */
    public Computer() {

    }

    /**
     * constructor.
     * @param id id
     * @param company company
     * @param name name
     * @param introduced date intro
     * @param discontinued date disco
     */
    public Computer(long id, Company company, String name, LocalDateTime introduced, LocalDateTime discontinued) {
        this.id = id;
        this.company = company;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
    }

    /**
     * Constructor.
     * @param id id
     * @param companyId company id
     * @param name name
     * @param introduced date intro
     * @param discontinued date disco
     */
    public Computer(long id, long companyId, String name, LocalDateTime introduced, LocalDateTime discontinued) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
    }

    /**
     * constructor.
     * @param companyId company id
     * @param name name
     * @param introduced date intro
     * @param discontinued date disco
     */
    public Computer(long companyId, String name, LocalDateTime introduced, LocalDateTime discontinued) {
        this.companyId = companyId;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
    }

    /**
     * constructor.
     * @param id id
     * @param companyId company id
     * @param name name
     */
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

    public void setCompany(Company company) {
        this.company = company;
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

    public Timestamp getIntroducedTimestamp() {
        return getIntroduced() == null ? null : Timestamp.valueOf(getIntroduced());
    }

    public void setIntroducedTimestamp(Timestamp introduced) {
        this.introduced = introduced == null ? null : introduced.toLocalDateTime();
    }

    public Timestamp getDiscontinuedTimestamp() {
        return getDiscontinued() == null ? null : Timestamp.valueOf(getDiscontinued());
    }

    public void setDiscontinuedTimestamp(Timestamp discontinued) {
        this.discontinued = discontinued == null ? null : discontinued.toLocalDateTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Computer computer = (Computer) o;

        return getId() == computer.getId();
    }

    @Override
    public String toString() {
        return "Computer{" +
                "id=" + id +
                ", companyId=" + companyId +
                ", companyName=" + company.getName() +
                ", name='" + name + '\'' +
                ", introduced=" + introduced +
                ", discontinued=" + discontinued +
                '}';
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }
}
