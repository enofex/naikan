package com.enofex.naikan.security;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
class ExceptionTranslator {

  private final Log logger = LogFactory.getLog(getClass());

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleException(Exception ex) {
    ResponseStatus responseStatus = ex != null
        ? AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class)
        : null;
    this.logger.warn(ex);

    if (responseStatus != null) {
      return ProblemDetail.forStatus(responseStatus.value());
    }

    return ProblemDetail.forStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
  }
}
