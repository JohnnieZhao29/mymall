package com.zjy.mymall.product.exception;

import com.zjy.common.exception.BizCodeEnume;
import com.zjy.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zjy
 * @version 1.0
 */

@Slf4j
//@ResponseBody
@RestControllerAdvice(basePackages = "com.zjy.mymall.product.controller")
public class MymallExceptionControllerAdvice {


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{}，异常类型{}", e.getMessage(), e.getClass());
        BindingResult result = e.getBindingResult();
        Map<String, String > errorMap = new HashMap<>();
        result.getFieldErrors().forEach((item) -> {
            String message = item.getDefaultMessage();

            String field = item.getField();
            errorMap.put(field, message);

        });
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(),
                BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data",errorMap);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handlerException(Throwable throwable){
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(),
                BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
}
