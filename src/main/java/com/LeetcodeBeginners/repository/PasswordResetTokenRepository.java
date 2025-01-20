package com.LeetcodeBeginners.repository;

import com.LeetcodeBeginners.dto.PasswordResetToken;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, ObjectId> {

    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUserId(String userId); // You can delete tokens by userId if needed

}
