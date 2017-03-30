package persistance.model;


public class Company {

    private Long id;
    private String name;

    /**
     * Basic constructor.
     */
    public Company() {
    }

    /**
     * basic construcor.
     * @param name name
     */
    public Company(String name) {
        this.name = name;
    }

    /**
     * basic constrcutor.
     * @param id id
     * @param name name
     */
    public Company(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Company company = (Company) o;

        return getId() == company.getId();
    }

    @Override
    public int hashCode() {
        return (int) (getId() ^ (getId() >>> 32));
    }


    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
