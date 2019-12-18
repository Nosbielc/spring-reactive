package com.nosbielc.reactive.products.config;

import com.nosbielc.reactive.products.exceptions.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestControllerHandler {

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<String> handleNotFound(final ProductNotFoundException ex) {
    log.error(ex.getMessage(), ex);
    //final ErrorResponse error = new ErrorResponse();
    //error.setMsg("not found");
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
  }

}
