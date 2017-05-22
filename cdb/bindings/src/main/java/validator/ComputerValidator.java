package validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import model.Computer;


public class ComputerValidator implements Validator {

  @Override
  public boolean supports(Class clazz) {
    return Computer.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    Computer computer = (Computer) target;

    if (computer.getName() == null) {
      errors.rejectValue("name", "name_null");
    }
    if (!Validate.isValidComputer(computer)) {
      errors.reject("invalid_computer");
    }
  }
}
