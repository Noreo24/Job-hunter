package vn.noreo.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.noreo.jobhunter.domain.Job;
import vn.noreo.jobhunter.domain.Skill;
import vn.noreo.jobhunter.domain.response.ResultPaginationDTO;
import vn.noreo.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.noreo.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.noreo.jobhunter.repository.JobRepository;
import vn.noreo.jobhunter.repository.SkillRepository;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
    }

    public Optional<Job> handleFetchJobById(long id) {
        return this.jobRepository.findById(id);
    }

    public ResCreateJobDTO handleCreateJob(Job newJob) {

        // Check skill exists
        if (newJob.getSkills() != null) {
            List<Long> reqSkills = newJob.getSkills().stream()
                    .map(skill -> skill.getId())
                    .collect(Collectors.toList());
            List<Skill> listSkill = this.skillRepository.findAllById(reqSkills);
            newJob.setSkills(listSkill);
        }

        // Create new job
        Job currentJob = this.jobRepository.save(newJob);

        // Convert to DTO
        ResCreateJobDTO jobDTO = new ResCreateJobDTO();
        jobDTO.setId(currentJob.getId());
        jobDTO.setName(currentJob.getName());
        jobDTO.setSalary(currentJob.getSalary());
        jobDTO.setQuantity(currentJob.getQuantity());
        jobDTO.setLocation(currentJob.getLocation());
        jobDTO.setLevel(currentJob.getLevel());
        jobDTO.setStartDate(currentJob.getStartDate());
        jobDTO.setEndDate(currentJob.getEndDate());
        jobDTO.setActive(currentJob.isActive());
        jobDTO.setCreatedAt(currentJob.getCreatedAt());
        jobDTO.setCreatedBy(currentJob.getCreatedBy());

        if (currentJob.getSkills() != null) {
            List<String> listSkills = currentJob.getSkills().stream()
                    .map(skill -> skill.getName())
                    .collect(Collectors.toList());
            jobDTO.setSkills(listSkills);
        }

        return jobDTO;
    }

    public ResUpdateJobDTO handleUpdateJob(Job updatedJob) {

        // Check skill exists
        if (updatedJob.getSkills() != null) {
            List<Long> reqSkills = updatedJob.getSkills().stream()
                    .map(skill -> skill.getId())
                    .collect(Collectors.toList());
            List<Skill> listSkill = this.skillRepository.findAllById(reqSkills);
            updatedJob.setSkills(listSkill);
        }

        // Update job
        Job currentJob = this.jobRepository.save(updatedJob);

        // Convert to DTO
        ResUpdateJobDTO jobDTO = new ResUpdateJobDTO();
        jobDTO.setId(currentJob.getId());
        jobDTO.setName(currentJob.getName());
        jobDTO.setSalary(currentJob.getSalary());
        jobDTO.setQuantity(currentJob.getQuantity());
        jobDTO.setLocation(currentJob.getLocation());
        jobDTO.setLevel(currentJob.getLevel());
        jobDTO.setStartDate(currentJob.getStartDate());
        jobDTO.setEndDate(currentJob.getEndDate());
        jobDTO.setActive(currentJob.isActive());
        jobDTO.setUpdatedAt(currentJob.getUpdatedAt());
        jobDTO.setUpdatedBy(currentJob.getUpdatedBy());

        if (currentJob.getSkills() != null) {
            List<String> listSkills = currentJob.getSkills().stream()
                    .map(skill -> skill.getName())
                    .collect(Collectors.toList());
            jobDTO.setSkills(listSkills);
        }

        return jobDTO;
    }

    public void handleDeleteJob(long id) {
        this.jobRepository.deleteById(id);
    }

    public ResultPaginationDTO handleFetchAllJobs(Specification<Job> specification, Pageable pageable) {
        Page<Job> jobPage = this.jobRepository.findAll(specification, pageable);

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setCurrentPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setTotalPages(jobPage.getTotalPages());
        meta.setTotalItems(jobPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);

        resultPaginationDTO.setDataResult(jobPage.getContent());
        return resultPaginationDTO;
    }
}