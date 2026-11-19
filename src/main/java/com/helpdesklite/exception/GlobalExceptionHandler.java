package com.helpdesklite.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(ResourceNotFoundException.class)
 public ResponseEntity<ApiError> notFound(ResourceNotFoundException e,HttpServletRequest r){
  return build(HttpStatus.NOT_FOUND,e.getMessage(),r,Map.of());
 }

 @ExceptionHandler(InvalidStatusTransitionException.class)
 public ResponseEntity<ApiError> transition(InvalidStatusTransitionException e,HttpServletRequest r){
  return build(HttpStatus.CONFLICT,e.getMessage(),r,Map.of());
 }

 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<ApiError> validation(MethodArgumentNotValidException e,HttpServletRequest r){
  Map<String,String> errors=new LinkedHashMap<>();
  e.getBindingResult().getFieldErrors().forEach(x->errors.putIfAbsent(x.getField(),x.getDefaultMessage()));
  return build(HttpStatus.BAD_REQUEST,"Validation failed",r,errors);
 }

 @ExceptionHandler({HttpMessageNotReadableException.class,MethodArgumentTypeMismatchException.class})
 public ResponseEntity<ApiError> badInput(Exception e,HttpServletRequest r){
  return build(HttpStatus.BAD_REQUEST,"Invalid request value. Check IDs and allowed enum values.",r,Map.of());
 }

 @ExceptionHandler(Exception.class)
 public ResponseEntity<ApiError> unexpected(Exception e,HttpServletRequest r){
  return build(HttpStatus.INTERNAL_SERVER_ERROR,"An unexpected error occurred",r,Map.of());
 }
 private ResponseEntity<ApiError> build(HttpStatus s,String m,HttpServletRequest r,Map<String,String> errors){
  return ResponseEntity.status(s).body(new ApiError(LocalDateTime.now(),s.value(),s.getReasonPhrase(),m,r.getRequestURI(),errors));
 }
}
