package vn.noreo.jobhunter.service;

import org.springframework.stereotype.Service;

import vn.noreo.jobhunter.domain.Company;
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
}
