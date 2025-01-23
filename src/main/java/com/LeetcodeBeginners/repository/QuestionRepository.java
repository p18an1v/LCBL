package com.LeetcodeBeginners.repository;

import com.LeetcodeBeginners.entity.Question;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question, ObjectId> {
    List<Question> findByTopicId(ObjectId topicId); // Custom method to fetch questions by topic ID
}

