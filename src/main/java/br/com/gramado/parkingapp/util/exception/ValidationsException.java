package br.com.gramado.parkingapp.util.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidationsException extends Exception {

    public ValidationsException(String message) {
        super(message);
    }
}
