package alphaBank.transactionClosure.service;

import java.net.http.HttpRequest;
import java.util.function.Consumer;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import alphaBank.transactionClosure.dao.AccountMongoRepository;
import alphaBank.transactionClosure.dao.TransferMoneyRepository;
import alphaBank.transactionClosure.dto.TransactionClosureDto;
import alphaBank.transactionClosure.exceptions.TransferNotFoundException;
import alphaBank.transactionClosure.exceptions.UserNotFoundException;
import alphaBank.transactionClosure.model.AccountEntity;
import alphaBank.transactionClosure.model.TransferEntity;

@Service
public class TransactionClosureService {
	private static final String AML_SERVICE_URL = "/alphaBank/amlAprovement";
	private static final String ATA_SERVICE_URL = "/alphaBank/ataAprovement";
	ModelMapper modelMapper;
	TransferMoneyRepository transferMoneyRepository;
	AccountMongoRepository accountMongoRepository;
	RestTemplate restTemplate = new RestTemplate();
	@Value("${app.data.provider.service.host:http://localhost:8888}")
	String dataProviderServiceHostAML;
	@Value("${app.data.provider.service.host:http://localhost:8889}")
	String dataProviderServiceHostATA;
	
	@Autowired
	public TransactionClosureService(ModelMapper modelMapper, TransferMoneyRepository transferMoneyRepository,
			AccountMongoRepository accountMongoRepository) {
		this.modelMapper = modelMapper;
		this.transferMoneyRepository = transferMoneyRepository;
		this.accountMongoRepository = accountMongoRepository;
	}

	@Bean
	Consumer<TransactionClosureDto> closeTransfer() {
		return this::finishTransfer;
	}

	//@Transactional
	private void finishTransfer(TransactionClosureDto transactionClosureDto) {
//		HttpEntity<TransactionClosureDto> httpEntity = new HttpEntity<TransactionClosureDto>(transactionClosureDto);
		ResponseEntity<Boolean> response = restTemplate.exchange(dataProviderServiceHostAML + AML_SERVICE_URL +"/", HttpMethod.GET, null, Boolean.class);
		Boolean amlApproved = response.getBody();
		Boolean ataApproved = restTemplate.exchange(dataProviderServiceHostATA + ATA_SERVICE_URL + "/", HttpMethod.GET, null, Boolean.class).getBody();
		
		TransferEntity transferEntity = transferMoneyRepository.findById(transactionClosureDto.getTransferId())
				.orElseThrow(() -> new TransferNotFoundException(transactionClosureDto.getTransferId()));
		transferEntity.setTransactionApproved(transactionClosureDto.isTransactionApproved());
		transferEntity.setAmlApproved(amlApproved);
		transferEntity.setAtaApproved(ataApproved);
		transferMoneyRepository.save(transferEntity);
		if (transactionClosureDto.isTransactionApproved() && amlApproved && ataApproved) {
			AccountEntity user = accountMongoRepository.findById(transactionClosureDto.getSenderId())
					.orElseThrow(() -> new UserNotFoundException());
			user.credit(transactionClosureDto.getSum());
			accountMongoRepository.save(user);
			AccountEntity user2 = accountMongoRepository.findById(transactionClosureDto.getReceiverId())
					.orElseThrow(() -> new UserNotFoundException());
			user2.debit(transactionClosureDto.getSum());
			accountMongoRepository.save(user2);
		}
	}

}
