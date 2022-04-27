package alphaBank.transactionClosure.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import alphaBank.transactionClosure.model.AccountEntity;

@Repository
public interface AccountMongoRepository extends MongoRepository<AccountEntity, String>{

}
