package com.example.gymcrm.repository.impl;

import com.example.gymcrm.domain.Training;
import com.example.gymcrm.dto.TrainingSearchDto;
import com.example.gymcrm.repository.TrainingRepository;
import com.example.gymcrm.repository.specification.TrainingSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TrainingRepositoryImpl implements TrainingRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final TrainingSpecification trainingSpecification;

    @Override
    @Transactional
    public Training save(Training training) {
        if (training.getId() == null) {
            entityManager.persist(training);
            return training;
        } else {
            return entityManager.merge(training);
        }
    }

    @Override
    public Optional<Training> findById(long id) {
        return Optional.ofNullable(entityManager.find(Training.class, id));
    }

    @Override
    public List<Training> findAllByCriteria(TrainingSearchDto trainingSearchDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> query = cb.createQuery(Training.class);
        Root<Training> root = query.from(Training.class);

        List<Predicate> predicates = trainingSpecification.buildPredicates(cb, root, trainingSearchDto);

        query.select(root).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(query).getResultList();
    }
}
