package study.querydsl.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemebrDto {

    private String username;
    private int age;

    public MemebrDto(String username, int age) {
        this.username = username;
        this.age = age;
    }
}
