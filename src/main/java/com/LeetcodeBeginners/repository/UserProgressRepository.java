package com.LeetcodeBeginners.repository;

import com.LeetcodeBeginners.entity.UserProgress;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProgressRepository extends MongoRepository<UserProgress, ObjectId> {
    Optional<UserProgress> findById(ObjectId id);
}

