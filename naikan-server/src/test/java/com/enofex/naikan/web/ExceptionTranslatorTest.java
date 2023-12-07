package com.enofex.naikan.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ResponseStatus;

class ExceptionTranslatorTest {

  @Test
  void shouldHandleExceptionInternalServerError() {
    ExceptionTranslator translator = new ExceptionTranslator();
    Exception ex = new RuntimeException("Test exception");

    ProblemDetail result = translator.handleException(ex);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getStatus());
  }

  @Test
  void shouldHandleExceptionWithResponseStatus() {
    ExceptionTranslator translator = new ExceptionTranslator();
    AnnotatedTestException ex = new AnnotatedTestException();

    ProblemDetail result = translator.handleException(ex);

    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatus());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  private static class AnnotatedTestException extends RuntimeException {

  }
}
