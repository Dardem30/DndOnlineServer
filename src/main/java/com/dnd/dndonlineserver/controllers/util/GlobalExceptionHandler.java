package com.dnd.dndonlineserver.controllers.util;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final Log log = LogFactory.getLog(getClass());

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(Exception.class)
    public @ResponseBody ErrorInfo exception(HttpServletRequest req, Exception ex) {
        ErrorInfo errorInfo = null;
        try {
            Throwable rootCause = ex.getCause();
            if (rootCause != null) {
                while (rootCause.getCause() != null && rootCause.getCause() != rootCause)
                    rootCause = rootCause.getCause();
                log.error("The request " + req.getRequestURL() + " produced the Exception in Class: " + rootCause.getStackTrace()[0].getClassName() + ", Method: " + rootCause.getStackTrace()[0].getMethodName() + "", ex);
            } else {
                log.error("The request " + req.getRequestURL() + " produced the Exception", ex);
            }

            errorInfo = new ErrorInfo(req.getRequestURL().toString(), ex);
        } catch (Exception e) {
            log.error("Error during trying to handle exception of the request:" + req.getRequestURL(), e);
        }
        return errorInfo;
    }
    private static class ErrorInfo {
        public String url;
        public String ex;

        public ErrorInfo() {

        }


        public ErrorInfo(String url, Exception ex) {
            this.url = url;
            this.ex = ex.getLocalizedMessage();
        }
    }
}
