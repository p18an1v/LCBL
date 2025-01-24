package com.LeetcodeBeginners.repository;

import com.LeetcodeBeginners.entity.Pattern;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface PatternRepository extends MongoRepository<Pattern, ObjectId> {
}
