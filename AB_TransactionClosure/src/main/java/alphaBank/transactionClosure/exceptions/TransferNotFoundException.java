package alphaBank.transactionClosure.exceptions;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class TransferNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5152085909060747196L;
	
	public TransferNotFoundException(String transferId) {
		super("Transfer " + transferId + " not found");
	}

}
