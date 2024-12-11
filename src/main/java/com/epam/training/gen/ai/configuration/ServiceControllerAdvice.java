package com.epam.training.gen.ai.configuration;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class ServiceControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    @SneakyThrows
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        val message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ":" + error.getDefaultMessage())
                .collect(Collectors.joining(System.lineSeparator()));
        val problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setDetail(message);
        ErrorResponseException errorResponseException = new ErrorResponseException(HttpStatus.BAD_REQUEST, problem, null);
        return super.handleException(errorResponseException, request);
    }
}
