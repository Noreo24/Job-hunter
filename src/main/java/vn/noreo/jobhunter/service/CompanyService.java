package vn.noreo.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.noreo.jobhunter.domain.Company;
import vn.noreo.jobhunter.domain.dto.ResultPaginationDTO;
import vn.noreo.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company handleCreateCompany(Company newCompany) {
        return this.companyRepository.save(newCompany);
    }

    public ResultPaginationDTO handleFetchAllCompanies(Specification<Company> specification, Pageable pageable) {
        Page<Company> companyPage = this.companyRepository.findAll(specification, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setCurrentPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setTotalPages(companyPage.getTotalPages());
        meta.setTotalItems(companyPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setDataResult(companyPage.getContent());

        return resultPaginationDTO;
    }

    public Company handleFetchCompanyById(long id) {
        return this.companyRepository.findById(id).orElse(null);
    }

    public Company handleUpdateCompany(Company updatedCompany) {
        // Company currentCompany = this.handleFetchCompanyById(updatedCompany.getId());
        // if (currentCompany != null) {
        // currentCompany.setName(updatedCompany.getName());
        // currentCompany.setDescription(updatedCompany.getDescription());
        // currentCompany.setAddress(updatedCompany.getAddress());
        // currentCompany.setLogo(updatedCompany.getLogo());

        // currentCompany = this.companyRepository.save(currentCompany);
        // }
        // return currentCompany;

        // Hoặc có thể viết như sau (không cần gọi hàm handleFetchCompanyById):
        Optional<Company> currentCompany = this.companyRepository.findById(updatedCompany.getId());
        if (currentCompany.isPresent()) {
            Company company = currentCompany.get();
            company.setName(updatedCompany.getName());
            company.setDescription(updatedCompany.getDescription());
            company.setAddress(updatedCompany.getAddress());
            company.setLogo(updatedCompany.getLogo());
            return this.companyRepository.save(company);
        }
        return null;
    }

    public void handleDeleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }
}
