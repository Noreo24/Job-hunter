package vn.noreo.jobhunter.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Meta {

    // Trang người dùng truyền vào
    private int currentPage;

    // Số lượng bản ghi trên một trang
    private int pageSize;

    // Tổng số trang trong cơ sở dữ liệu
    private int totalPages;

    // Tổng số bản ghi trong cơ sở dữ liệu
    private long totalItems;
}
