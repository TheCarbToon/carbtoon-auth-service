package kr.springboot.dcinside.cartoon.auth.repo;

import kr.springboot.dcinside.cartoon.auth.domain.EmailAuth;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailAuthRepository extends MongoRepository<EmailAuth, String> {

    Optional<EmailAuth> findByUuid(String uuid);
}
