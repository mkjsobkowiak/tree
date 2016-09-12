package com.bit4mation.web.exception;

import com.bit4mation.core.exception.IllegalDataException;
import com.bit4mation.core.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * Class used for exception handling and set response with proper http status
 */
@ControllerAdvice
@Slf4j
public class WebExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class, IllegalDataException.class})
    public ResponseEntity<CustomError> badRequests(HttpServletRequest req, Exception ex) {
        return new ResponseEntity<>(createCustomError(ex), BAD_REQUEST);
    }

    private CustomError createCustomError(Exception ex) {
        log.error("Returned CustomError:", ex);
        CustomError customError = new CustomError();
        customError.setCreateDate(System.currentTimeMillis());
        customError.setExceptionClassName(ex.getClass().getName());
        customError.setMessage(ex.getMessage());
        return customError;
    }
}
