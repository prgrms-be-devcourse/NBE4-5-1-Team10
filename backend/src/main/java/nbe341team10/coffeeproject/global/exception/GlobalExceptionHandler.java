package nbe341team10.coffeeproject.global.exception;

import nbe341team10.coffeeproject.global.app.AppConfig;
import nbe341team10.coffeeproject.global.dto.RsData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RsData<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult().getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + " : " + fe.getCode() + " : "  + fe.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining("\n"));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        new RsData<>(
                                "400-1",
                                message
                        )
                );
    }


    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<RsData<Void>> ServiceExceptionHandle(ServiceException ex) {

        if(AppConfig.isNotProd()) ex.printStackTrace();

        return ResponseEntity
                .status(ex.getStatusCode())
                .body(
                        new RsData<>(
                                ex.getCode(),
                                ex.getMsg()
                        )
                );
    }

}
