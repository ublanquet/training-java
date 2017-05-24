package model.DTO;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ComputerDto {

  private String id;
  private String companyId;

  private String companyName;
  @NotNull
  @Size(min = 2, max = 30)
  private String name;

  @DateTimeFormat(pattern = "dd/MM/yyyy")
  private String introduced;
  @DateTimeFormat(pattern = "dd/MM/yyyy")
  private String discontinued;

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Computer{" +
        "id='" + id + '\'' +
        ", companyId='" + companyId + '\'' +
        ", companyName='" + companyName + '\'' +
        ", name='" + name + '\'' +
        ", introduced='" + introduced + '\'' +
        ", discontinued='" + discontinued + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ComputerDto that = (ComputerDto) o;

    return getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    if (getId() != null) {
      return getId().hashCode();
    } else {
      return 0;
    }
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }

  public String getCompanyName() {
    return companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIntroduced() {
    return introduced;
  }

  public void setIntroduced(String introduced) {
    this.introduced = introduced;
  }

  public String getDiscontinued() {
    return discontinued;
  }

  public void setDiscontinued(String discontinued) {
    this.discontinued = discontinued;
  }


}
