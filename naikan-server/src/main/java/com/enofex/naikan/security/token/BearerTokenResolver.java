package com.enofex.naikan.security.token;


import jakarta.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

final class BearerTokenResolver {

  private static final String AUTHORIZATION_HEADER = "Authorization";

  private static final String BEARER_TYPE = "Bearer";
  private static final String ACCESS_TOKEN = "access_token";

  private final Log logger = LogFactory.getLog(getClass());

  String resolve(HttpServletRequest request) {
    return extractToken(request);
  }

  private String extractToken(HttpServletRequest request) {
    String token = extractHeaderToken(request);

    if (token == null) {
      this.logger.debug("Token not found in headers. Trying request parameters.");
      token = request.getParameter(ACCESS_TOKEN);

      if (token == null) {
        this.logger.debug("Token not found in request parameters. Not an Token request.");
      }
    }

    return token;
  }

  private String extractHeaderToken(HttpServletRequest request) {
    Enumeration<String> headers = request.getHeaders(AUTHORIZATION_HEADER);

    while (headers.hasMoreElements()) {
      String value = headers.nextElement();

      if (value.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
        String authHeaderValue = value.substring(BEARER_TYPE.length()).trim();

        int commaIndex = authHeaderValue.indexOf(',');
        if (commaIndex > 0) {
          authHeaderValue = authHeaderValue.substring(0, commaIndex);
        }
        return authHeaderValue;
      }
    }

    return null;
  }

}