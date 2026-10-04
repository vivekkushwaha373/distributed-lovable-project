package com.agenticIde.distributed_lovable.comman_lib.error;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
//    private final static Logger log1 = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex){
          ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
          log.error(apiError.toString(),ex);
          return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleBadRequest(ResourceNotFoundException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getResourceName()+" with id "+ ex.getResourceId());
        log.error(apiError.toString(),ex);
        return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiError> handleInputValidationError(MethodArgumentNotValidException ex){
//        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage());
//        log.error(apiError.toString(),ex);
//        return ResponseEntity.status(apiError.httpStatus()).body(apiError);
//
//    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleInputValidationError(MethodArgumentNotValidException ex){

        List<ApiFieldError> errors = ex.getBindingResult().getFieldErrors().stream().map(x->new ApiFieldError(x.getField(),x.getDefaultMessage())).toList();

        ApiError apiError = new ApiError( HttpStatus.BAD_REQUEST,"Field Validation Error",errors);
        log.error(apiError.toString(),ex);
        return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNameNotFoundException(UsernameNotFoundException ex){
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,"username not found with username: "+ex.getMessage());
        log.error(apiError.toString(),ex);
        return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex){
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED,"Authentication failes : "+ex.getMessage());
        log.error(apiError.toString(),ex);
        return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiError> handleJwtException(JwtException ex){
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED,"Invalid JWT TOKEN : "+ex.getMessage());
        log.error(apiError.toString(),ex);
        return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex){
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN,"Access Denied: Insufficient Permissions "+ex.getMessage());
        log.error(apiError.toString(),ex);
        return ResponseEntity.status(apiError.httpStatus()).body(apiError);
    }

}
