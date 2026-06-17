package com.awbd.ironplate.service.impl;

import com.awbd.ironplate.domain.TrainingProgram;
import com.awbd.ironplate.domain.TrainingProgram.DifficultyLevel;
import com.awbd.ironplate.exception.ResourceNotFoundException;
import com.awbd.ironplate.repository.TrainingProgramRepository;
import com.awbd.ironplate.service.TrainingProgramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainingProgramServiceImpl implements TrainingProgramService {

    private final TrainingProgramRepository trainingProgramRepository;

    @Override
    @Transactional
    public TrainingProgram save(TrainingProgram trainingProgram) {
        log.debug("Saving training program: {}", trainingProgram.getName());
        return trainingProgramRepository.save(trainingProgram);
    }

    @Override
    @Transactional(readOnly = true)
    public TrainingProgram findById(Long id) {
        return trainingProgramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TrainingProgram", id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingProgram> findAll(Pageable pageable) {
        return trainingProgramRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrainingProgram> findByUserId(Long userId, Pageable pageable) {
        return trainingProgramRepository.findByUserId(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrainingProgram> findByDifficultyLevel(DifficultyLevel difficultyLevel) {
        return trainingProgramRepository.findByDifficultyLevel(difficultyLevel);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("Deleting training program with id: {}", id);
        if (!trainingProgramRepository.existsById(id)) {
            throw new ResourceNotFoundException("TrainingProgram", id);
        }
        trainingProgramRepository.deleteById(id);
    }
}
