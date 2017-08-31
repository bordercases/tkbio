package bio.knowledge.service.user;

import java.util.Calendar;

import org.neo4j.ogm.exception.CypherException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bio.knowledge.database.repository.user.PasswordTokenRepository;
import bio.knowledge.database.repository.user.UserRepository;
import bio.knowledge.model.user.PasswordResetToken;
import bio.knowledge.model.user.User;

@Service
public class PasswordTokenService {
	
	@Autowired
	PasswordTokenRepository tokenRepo;
	
	@Autowired
	UserRepository userRepo;
	
	/**
	 * Puts a newly generated token in the database
	 * Also deletes any expired tokens that are found
	 * 
	 * @param email address
	 * @return a token object associated to the user
	 */
	public PasswordResetToken generateToken(String email) {
		
		Iterable<PasswordResetToken> expired = tokenRepo.findExpiredBefore(Calendar.getInstance().getTimeInMillis());
		tokenRepo.delete(expired);
		
		User user = userRepo.findByEmail(email);
		if (user == null) {
			return null;
		}
		
		PasswordResetToken token = null;
		while (token == null) {
			try {
				token = new PasswordResetToken(user);
			} catch (CypherException e) {
				// token already in use
			}
		}
		
		tokenRepo.save(token);
		return token;
	}
	
	/**
	 * 
	 * @param tokenString a UUID
	 * @return the token object, unless it has expired
	 */
	public PasswordResetToken findByTokenString(String tokenString) {
		
		PasswordResetToken token = tokenRepo.findByTokenString(tokenString);
		
		if (token == null || token.isValid()) {
			return token;
		} else {
			tokenRepo.delete(token); // expired tokens can (and should) be deleted
			return null;
		}
	}
	
	public void delete(PasswordResetToken token) {
		tokenRepo.delete(token);
	}

}
