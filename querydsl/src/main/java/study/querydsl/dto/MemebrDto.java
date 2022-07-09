package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemebrDto {

    private String username;
    private int age;

    @QueryProjection
    public MemebrDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
