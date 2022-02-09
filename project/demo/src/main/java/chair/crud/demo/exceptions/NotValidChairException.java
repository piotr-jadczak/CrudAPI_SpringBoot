package chair.crud.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Chair is not valid")
public class NotValidChairException extends RuntimeException {

    public NotValidChairException(String message) { super(message);}
}
