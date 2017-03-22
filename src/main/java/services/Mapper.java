package services;

import model.Computer;
import model.DTO.ComputerDto;
import model.GenericBuilder;
import model.Page;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Mapper {
  /**
   * create dto from obj.
   * @param c obj
   * @return dto
   */
  public static ComputerDto createComputerDto(Computer c) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    String formattedDateIntro = c.getIntroduced() != null ? c.getIntroduced().format(formatter) : "";
    String formattedDateDisco = c.getDiscontinued() != null ? c.getDiscontinued().format(formatter) : "";

    String companyId = "", companyName = "";
    if (c.getCompany() != null) {
      companyId = c.getCompanyId().toString();
      companyName = c.getCompany().getName();
    }

    ComputerDto dto = GenericBuilder.of(ComputerDto::new)
        .with(ComputerDto::setId, c.getId().toString())
        .with(ComputerDto::setName, c.getName())
        .with(ComputerDto::setCompanyId, companyId)
        .with(ComputerDto::setIntroduced, formattedDateIntro)
        .with(ComputerDto::setDiscontinued, formattedDateDisco)
        .with(ComputerDto::setCompanyName, companyName)
        .build();

    return dto;
  }

  /**
   * convert a computer page to a computer dto Page.
   * @param page computer page
   * @return dto page
   */
  public static Page<ComputerDto> convertPageDto(Page<Computer> page) {
    ArrayList<ComputerDto> dtoList = new ArrayList<ComputerDto>();
    for (Computer c : page.list) {
      dtoList.add(createComputerDto(c));
    }
    Page<ComputerDto> pageDto = new Page<>(page.getNbEntries(), page.currentPage);
    pageDto.list = dtoList;
    return pageDto;
  }
}
