package vn.noreo.jobhunter.domain.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.noreo.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
public class ResCreateUserDTO {

    private long id;
    private String name;
    private String email;
    private int age;
    private GenderEnum gender;
    private String address;
    private Instant createdAt;
}
