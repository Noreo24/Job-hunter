package vn.noreo.jobhunter.service;

import java.util.List;
import java.util.Optional;

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

    public List<Company> handleFetchAllCompanies() {
        return this.companyRepository.findAll();
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
