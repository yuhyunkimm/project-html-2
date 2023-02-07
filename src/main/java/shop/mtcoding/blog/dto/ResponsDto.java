package shop.mtcoding.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// 전체 매개 변수 받는 것
@AllArgsConstructor
@Getter
@Setter
public class ResponsDto<T> {
    private int code;
    private String msg;
    private T data;

}
