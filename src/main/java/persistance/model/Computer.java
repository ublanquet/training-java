package persistance.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class Computer {
    private Long id;
    private Long companyId;
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
     *
     * @param id           id
     * @param company      company
     * @param name         name
     * @param introduced   date intro
     * @param discontinued date disco
     */
    public Computer(Long id, Company company, String name, LocalDateTime introduced, LocalDateTime discontinued) {
        this.id = id;
        this.company = company;
        this.companyId = company.getId();
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
    }

    /**
     * Constructor.
     *
     * @param id           id
     * @param companyId    company id
     * @param name         name
     * @param introduced   date intro
     * @param discontinued date disco
     */
    public Computer(Long id, Long companyId, String name, LocalDateTime introduced, LocalDateTime discontinued) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
    }

    /**
     * constructor.
     *
     * @param companyId    company id
     * @param name         name
     * @param introduced   date intro
     * @param discontinued date disco
     */
    public Computer(Long companyId, String name, LocalDateTime introduced, LocalDateTime discontinued) {
        this.companyId = companyId;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
    }

    /**
     * constructor.
     *
     * @param id        id
     * @param companyId company id
     * @param name      name
     */
    public Computer(Long id, Long companyId, String name) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    /**
     * set company adn companyId.
     *
     * @param company company
     */
    public void setCompany(Company company) {
        this.company = company;
        if (company != null) {
            this.companyId = company.getId();
        }
    }

    public Company getCompany() {
        return company;
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

    //STOP_CHECKSTYLE
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Computer computer = (Computer) o;

        if (Objects.equals(getId(), computer.getId())) {
            return true;
        } else if ( (getId() == null || computer.getId() == null) &&
                    Objects.equals(getName(), computer.getName()) &&
                    Objects.equals(getCompanyId(), computer.getCompanyId()) &&
                    getIntroduced().equals(computer.getIntroduced()) &&
                    getDiscontinued().equals(computer.getDiscontinued())) {
            return true;
        } else {
            return false;
        }
    }
    //START_CHECKSTYLE

    @Override
    public String toString() {
        return "Computer{" +
                "id=" + id +
                ", companyId=" + (company != null ? company.getId() : "null") +
                ", companyName=" + (company != null ? company.getName() : "null") +
                ", name='" + name + '\'' +
                ", introduced=" + introduced +
                ", discontinued=" + discontinued +
                '}';
    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return (int) (getId() ^ (getId() >>> 32));
        } else {
            return 0;
        }
    }
}
