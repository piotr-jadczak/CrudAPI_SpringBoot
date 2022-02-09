package chair.crud.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such chair in db")
public class NoSuchChairInDbException extends RuntimeException {

    public NoSuchChairInDbException(String message) { super(message);}
}
