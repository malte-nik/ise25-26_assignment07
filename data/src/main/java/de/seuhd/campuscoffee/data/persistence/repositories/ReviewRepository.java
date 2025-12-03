package de.seuhd.campuscoffee.data.persistence.repositories;

import de.seuhd.campuscoffee.data.persistence.entities.PosEntity;
import de.seuhd.campuscoffee.data.persistence.entities.ReviewEntity;
import de.seuhd.campuscoffee.data.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for persisting review entities.
 */
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>, ResettableSequenceRepository {
    List<ReviewEntity> findAllByPosAndApproved(PosEntity pos, Boolean approved);
    List<ReviewEntity> findAllByPosAndAuthor(PosEntity pos, UserEntity author);
}
