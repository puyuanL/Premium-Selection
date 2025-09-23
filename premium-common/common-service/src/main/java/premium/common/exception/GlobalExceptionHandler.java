package premium.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import premium.model.vo.common.Result;
import premium.model.vo.common.ResultCodeEnum;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Global Exception Process
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error() {
        return Result.build(null, ResultCodeEnum.SYSTEM_ERROR);
    }

    // Custom Exception Process
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Result error(MyException e) {
        return Result.build(null, e.getResultCodeEnum());
    }

}
