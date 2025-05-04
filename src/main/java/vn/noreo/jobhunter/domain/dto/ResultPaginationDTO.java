package vn.noreo.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDTO {

    private Meta meta;

    // Dữ liệu trả về từ API, có thể là danh sách người dùng hoặc một đối tượng
    // người dùng cụ thể
    private Object dataResult;
}
