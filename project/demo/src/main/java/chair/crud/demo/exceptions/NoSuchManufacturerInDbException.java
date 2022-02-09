package chair.crud.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such chair in db")
public class NoSuchManufacturerInDbException extends RuntimeException {

    public NoSuchManufacturerInDbException(String message) { super(message);}
}
