package alphaBank.transactionClosure.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import alphaBank.transactionClosure.model.TransferEntity;

@Repository
public interface TransferMoneyRepository extends MongoRepository<TransferEntity, String> {

}
