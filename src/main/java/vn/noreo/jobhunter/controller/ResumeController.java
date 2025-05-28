package vn.noreo.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.noreo.jobhunter.domain.Resume;
import vn.noreo.jobhunter.domain.response.ResultPaginationDTO;
import vn.noreo.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.noreo.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.noreo.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.noreo.jobhunter.service.ResumeService;
import vn.noreo.jobhunter.util.annotation.ApiMessage;
import vn.noreo.jobhunter.util.error.IdInvalidException;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create new resume")
    public ResponseEntity<ResCreateResumeDTO> createNewResume(@Valid @RequestBody Resume newResume)
            throws IdInvalidException {
        // Check if the resume already exists for the user and job
        boolean isIdExists = this.resumeService.checkResumeExistsByUserAndJob(newResume);
        if (!isIdExists) {
            throw new IdInvalidException("User or Job not found for the provided Resume");
        }

        // Create the new resume
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.handleCreateResume(newResume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume updatedResume)
            throws IdInvalidException {
        Optional<Resume> currentResumeOpt = this.resumeService.handleFetchResumeById(updatedResume.getId());
        if (currentResumeOpt.isEmpty()) {
            throw new IdInvalidException("Resume with id " + updatedResume.getId() + " not found");
        }
        Resume currentResume = currentResumeOpt.get();
        currentResume.setState(updatedResume.getState());

        return ResponseEntity.ok().body(this.resumeService.handleUpdateResume(updatedResume));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete resume by id")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> currentResumeOpt = this.resumeService.handleFetchResumeById(id);
        if (!currentResumeOpt.isPresent()) {
            throw new IdInvalidException("Resume with id " + id + " not found");
        }
        this.resumeService.handleDeleteResume(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch resume by id")
    public ResponseEntity<ResFetchResumeDTO> fetchResumeById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> currentResumeOpt = this.resumeService.handleFetchResumeById(id);
        if (!currentResumeOpt.isPresent()) {
            throw new IdInvalidException("Resume with id " + id + " not found");
        }
        return ResponseEntity.ok().body(this.resumeService.convertToResFetchResumeDTO(currentResumeOpt.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resumes")
    public ResponseEntity<ResultPaginationDTO> fetchAllResumes(
            @Filter Specification<Resume> specification,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.resumeService.handleFetchAllResumes(specification, pageable));
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Fetch resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumesByUser(Pageable pageable) {
        return ResponseEntity.ok().body(this.resumeService.handleFetchResumesByUser(pageable));
    }
}
