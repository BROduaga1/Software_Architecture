package com.example.gymcrm.repository.specification;

import com.example.gymcrm.domain.Training;
import com.example.gymcrm.dto.TrainingSearchDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class TrainingSpecification {

    public List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Training> root, TrainingSearchDto dto) {
        List<Predicate> predicates = new ArrayList<>();

        if (dto.getIsTrainee() != null && dto.getUsername() != null) {
            String searchByConnectionWith = Boolean.TRUE.equals(dto.getIsTrainee()) ? "trainee" : "trainer";
            predicates.add(cb.equal(root.get(searchByConnectionWith).get("user").get("username"), dto.getUsername()));
        }

        addPredicateIfPresent(dto.getFromDate(),
                fromDate -> cb.greaterThanOrEqualTo(root.get("date"), fromDate), predicates);

        addPredicateIfPresent(dto.getToDate(),
                toDate -> cb.lessThanOrEqualTo(root.get("date"), toDate), predicates);

        return predicates;
    }

    private <T> void addPredicateIfPresent(T value, Function<T, Predicate> predicateFunction, List<Predicate> predicates) {
        Optional.ofNullable(value).map(predicateFunction).ifPresent(predicates::add);
    }
}
