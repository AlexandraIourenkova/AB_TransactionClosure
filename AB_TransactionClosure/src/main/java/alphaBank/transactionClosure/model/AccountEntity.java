package alphaBank.transactionClosure.model;

import org.springframework.data.annotation.Id;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountEntity {
		@Id
		String id;
		String firstName;
		String lastName;
		String maritialStatus;
		String employmentStatus;
		Address address;
		ContactDetails contactDetails;
		int balance;
		
		
		public void debit(int sum) {
			balance = balance+sum;
		}
		
		public void credit(int sum) {
			balance = balance - sum;
		}
		
	}
