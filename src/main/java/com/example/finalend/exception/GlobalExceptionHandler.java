package com.example.finalend.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import com.example.finalend.vo.Resp;
import com.google.protobuf.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static com.example.finalend.vo.Resp.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理HTTP请求格式错误引起的异常
     *
     * @param exception 被处理的异常
     * @return 错误的请求
     */
    @ResponseStatus(code = HttpStatus.OK)
    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class
    })
    public Resp<List<String>> handleDataTypeConvertException(Exception exception) {
        final List<String> messageList = new ArrayList<>();
        if (exception instanceof HttpRequestMethodNotSupportedException) {
            messageList.add("请求方法不匹配");
        } else if (exception instanceof MethodArgumentTypeMismatchException) {
            messageList.add("参数类型不匹配");
        } else if (exception instanceof HttpMessageNotReadableException) {
            messageList.add("请求体的数据格式不匹配");
        }
        return Resp.of(Resp.MALFORMED_REQUEST, "请求格式错误", messageList);
    }

    /**
     * 处理HTTP请求参数校验的异常
     *
     * @param exception 被处理的异常
     * @return 参数校验失败
     */
    @ResponseStatus(code = HttpStatus.OK)
    @ExceptionHandler(value = {ValidationException.class, MethodArgumentNotValidException.class, IllegalArgumentException.class,})
    public Resp<List<String>> handleParameterValidation(Exception exception) {
        final List<String> messageList = new ArrayList<>();
        if (exception instanceof ConstraintViolationException) {
            // controller方法的非对象参数校验
            final ConstraintViolationException e = (ConstraintViolationException) exception;
            final Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            for (ConstraintViolation<?> violation : violations) {
                messageList.add(violation.getMessage());
            }
        } else if (exception instanceof MethodArgumentNotValidException) {
            // controller方法的对象参数内部字段校验
            final MethodArgumentNotValidException e = (MethodArgumentNotValidException) exception;
            final List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                messageList.add(fieldError.getDefaultMessage());
            }
        } else if (exception instanceof IllegalArgumentException) {
            // 使用Assert断言手动校验
            final IllegalArgumentException e = (IllegalArgumentException) exception;
            messageList.add(e.getMessage());
        } else {
            messageList.add(exception.getMessage());
        }
        return Resp.of(PARAMETER_VALIDATION_FAILED, "参数校验失败", messageList);
    }

    /**
     * 处理未登录异常
     *
     * @param exception 未登录异常
     * @return 未登录
     */
    @ResponseStatus(code = HttpStatus.OK)
    @ExceptionHandler(value = {NotLoginException.class})
    public Resp<List<String>> handleNoLoginException(NotLoginException exception) {
        log.info(exception.getLoginType(), exception.getMessage());
        return Resp.of(NO_LOGIN, "未登录", Collections.emptyList());
    }

    /**
     * 处理没有权限的异常
     *
     * @param exception 没有权限的异常
     * @return 没有权限
     */
    @ResponseStatus(code = HttpStatus.OK)
    @ExceptionHandler(value = {NotPermissionException.class})
    public Resp<List<String>> handleNoPermissionException(NotPermissionException exception) {
        log.info(exception.getMessage());
        return Resp.of(NO_PERMISSION, "没有权限", Collections.emptyList());
    }

    /**
     * 处理业务操作失败的异常
     *
     * @param exception 被处理的业务异常
     * @return 操作失败
     */
    @ResponseStatus(code = HttpStatus.OK)
    @ExceptionHandler(value = {ApiException.class})
    public Resp<List<String>> handleApiException(ApiException exception) {
        return Resp.of(FAILURE, exception.getMessage(), Collections.emptyList());
    }

    /**
     * 处理业务操作失败的异常
     *
     * @param exception 被处理的业务异常
     * @return 操作失败
     */
    @ResponseStatus(code = HttpStatus.OK)
    @ExceptionHandler(value = {ServiceException.class})
    public Resp<List<String>> handleServiceException(ServiceException exception) {
        return Resp.of(FAILURE, exception.getMessage(), Collections.emptyList());
    }

    /**
     * 处理其它的异常
     *
     * @param exception 被处理的异常
     * @return 未知的错误
     */
    @ResponseStatus(code = HttpStatus.OK)
    @ExceptionHandler(value = {Exception.class})
    public Resp<List<String>> handleException(Exception exception) {
        exception.printStackTrace();
        return Resp.of(UNKNOWN_ERROR, "未知的错误", Collections.emptyList());
    }

}
