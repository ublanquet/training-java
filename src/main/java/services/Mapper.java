package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import persistance.model.Company;
import persistance.model.Computer;
import persistance.model.DTO.ComputerDto;
import persistance.model.GenericBuilder;
import persistance.model.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Service
public class Mapper {

  static Logger logger = LoggerFactory.getLogger("services.Mapper");

  @Autowired
  CompanyService companyService;
  /**
   * create dto from obj.
   * @param c obj
   * @return dto
   */
  public static ComputerDto createComputerDto(Computer c) {
    logger.debug("create DTO from computer");
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
   * Create computer obj from compîter dto.
   * @param dto .
   * @return .
   */
  public Computer fromDto(ComputerDto dto) {
    logger.debug("Create computer from dto");
    LocalDateTime dateI = null, dateD = null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    try {
      if (dto.getIntroduced() != null && !dto.getIntroduced().isEmpty()) {
        dateI = LocalDate.parse(dto.getIntroduced(), formatter).atStartOfDay();
      }
      if (dto.getDiscontinued() != null && !dto.getDiscontinued().isEmpty()) {
        dateD = LocalDate.parse(dto.getDiscontinued(), formatter).atStartOfDay();
      }
    } catch (Exception ex) {
      logger.error("Error creating computer from DTO : incorrect date - " + ex.getMessage());
    }

    Company comp = null;
    if (Validate.parseLong(dto.getCompanyId()) != null) {
      comp = companyService.getById(Validate.parseLong(dto.getCompanyId()));
    }

    Computer c = GenericBuilder.of(Computer::new)
        .with(Computer::setName, dto.getName())
        .with(Computer::setIntroduced, dateI)
        .with(Computer::setDiscontinued, dateD)
        .with(Computer::setCompany, comp)
        .with(Computer::setId, Validate.parseLong(dto.getId()))
        .build();


    return c;
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
