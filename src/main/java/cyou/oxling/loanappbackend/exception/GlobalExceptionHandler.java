package cyou.oxling.loanappbackend.exception;

import cyou.oxling.loanappbackend.dto.common.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntimeException(RuntimeException e) {
        return Result.error(1001, e.getMessage());
    }
    
    /**
     * 处理系统异常
     * @param e 异常
     * @return 响应结果
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        return Result.error(9999, "系统异常，请稍后重试");
    }
} 