package alphaBank.transactionClosure.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionClosureDto {
	String approvementId;
	String transferId;
	String senderId;
	String receiverId;
	int sum;
	boolean transactionApproved;
	

}
