package com.medical.core.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalControllerExceptionHandler
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年7月28日 下午4:42:13
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

/*    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse constraintViolationException(ConstraintViolationException ex) {
        return new ApiErrorResponse(500, 5001, ex.getMessage());
    }
    
    @ExceptionHandler(value = { IllegalArgumentException.class })
    @ResponseStatus()
    public BaseResponse IllegalArgumentException(IllegalArgumentException ex) {
        return new ApiErrorResponse(501, 5002, ex.getMessage());
    }

    @ExceptionHandler(value = { NoHandlerFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public BaseResponse noHandlerFoundException(Exception ex) {
        return new ApiErrorResponse(404, 4041, ex.getMessage());
    }

    
    @ExceptionHandler(value = { Exception.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseResponse unknownException(Exception ex) {
        return new ApiErrorResponse(500, 5002, ex.getMessage());
    }*/

}
