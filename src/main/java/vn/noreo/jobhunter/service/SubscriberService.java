package vn.noreo.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.noreo.jobhunter.domain.Skill;
import vn.noreo.jobhunter.domain.Subscriber;
import vn.noreo.jobhunter.repository.SkillRepository;
import vn.noreo.jobhunter.repository.SubscriberRepository;
import vn.noreo.jobhunter.util.error.IdInvalidException;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SkillRepository skillRepository, SubscriberRepository subscriberRepository) {
        this.skillRepository = skillRepository;
        this.subscriberRepository = subscriberRepository;
    }

    public Optional<Subscriber> handleFetchSubscriberById(long id) {
        return this.subscriberRepository.findById(id);
    }

    public Subscriber handleCreateSubscriber(Subscriber newSubscriber) throws IdInvalidException {

        // Check email exists
        boolean isEmailExists = this.subscriberRepository.existsByEmail(newSubscriber.getEmail());
        if (isEmailExists) {
            throw new IdInvalidException("Email " + newSubscriber.getEmail() + " already exists");
        }

        // Check skill exists
        if (newSubscriber.getSkills() != null) {
            List<Long> reqSkills = newSubscriber.getSkills().stream()
                    .map(skill -> skill.getId())
                    .collect(Collectors.toList());
            List<Skill> listSkill = this.skillRepository.findAllById(reqSkills);
            newSubscriber.setSkills(listSkill);
        }

        return this.subscriberRepository.save(newSubscriber);
    }

    public Subscriber handeUpdateSubscriber(Subscriber updatedSubscriber) throws IdInvalidException {
        Optional<Subscriber> subscriberOpt = this.handleFetchSubscriberById(updatedSubscriber.getId());
        if (subscriberOpt.isEmpty()) {
            throw new IdInvalidException("Subscriber with id " + updatedSubscriber.getId() + " not found");
        }
        Subscriber currentSubscriber = subscriberOpt.get();

        if (updatedSubscriber.getSkills() != null) {
            List<Long> reqSkills = updatedSubscriber.getSkills().stream()
                    .map(skill -> skill.getId())
                    .collect(Collectors.toList());
            List<Skill> listSkill = this.skillRepository.findAllById(reqSkills);
            currentSubscriber.setSkills(listSkill);
        }
        return this.subscriberRepository.save(currentSubscriber);
    }
}
