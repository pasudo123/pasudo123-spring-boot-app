package edu.pasudo123.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * @author pasudo123
 * @since 2019-07-03
 * blog: https://pasudo123.tistory.com/
 * email: oraedoa@gmail.com
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 리소스 미존재 시 잡는 에러
     * @param ex    : 리소스 없는 에러 객체
     * @param webRequest : 요청 객체
     * @return ResponseEntity
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> ResourceNotFoundException(ResourceNotFoundException ex, WebRequest webRequest){

        ErrorMessage errorMessage = ErrorMessage.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.toString())
                .message(ex.getMessage())
                .details(webRequest.getDescription(false))
                .build();

        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }
}
