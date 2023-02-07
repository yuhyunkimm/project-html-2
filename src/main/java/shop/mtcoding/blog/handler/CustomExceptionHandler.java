package shop.mtcoding.blog.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import shop.mtcoding.blog.dto.ResponsDto;
import shop.mtcoding.blog.handler.ex.CustomException;
import shop.mtcoding.blog.handler.ex.CustomApiException;
import shop.mtcoding.blog.util.Script;

@RestControllerAdvice
public class CustomExceptionHandler {

    // NullPointException <- RuntimeException
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customException(CustomException e) {
        // Status = 400 으로 확인
        String responseBody = Script.back(e.getMessage());
        return new ResponseEntity<>(Script.back(e.getMessage()), e.getStatus());

    }

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> CustomApiException(CustomException e) {
        return new ResponseEntity<>(new ResponsDto<>(-1, e.getMessage(), null), e.getStatus());

    }
}
