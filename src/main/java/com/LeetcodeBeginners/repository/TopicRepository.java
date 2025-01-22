package com.LeetcodeBeginners.repository;

import com.LeetcodeBeginners.entity.Topic;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends MongoRepository<Topic, ObjectId> {}
