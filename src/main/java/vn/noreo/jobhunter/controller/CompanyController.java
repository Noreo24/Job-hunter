package vn.noreo.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.noreo.jobhunter.domain.Company;
import vn.noreo.jobhunter.domain.dto.ResultPaginationDTO;
import vn.noreo.jobhunter.service.CompanyService;

@RestController
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company newCompany) {
        Company company = this.companyService.handleCreateCompany(newCompany);
        return ResponseEntity.ok(company);
    }

    @GetMapping("/companies")
    public ResponseEntity<ResultPaginationDTO> fetchAllCompanies(
            @Filter Specification<Company> specification,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.companyService.handleFetchAllCompanies(specification, pageable));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company updatedCompany) {
        Company company = this.companyService.handleUpdateCompany(updatedCompany);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }
}
