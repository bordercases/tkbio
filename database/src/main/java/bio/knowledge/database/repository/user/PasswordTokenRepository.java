package bio.knowledge.database.repository.user;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import bio.knowledge.model.user.PasswordResetToken;

public interface PasswordTokenRepository extends GraphRepository<PasswordResetToken> {

	@Query("CREATE CONSTRAINT ON (token:PasswordResetToken) ASSERT token.token IS UNIQUE")
	void createUniqueConstraintOnToken();
	
	/**
	 * 
	 * @param token string
	 * @return token node with associated user node
	 */
	@Query("MATCH path = (token:PasswordResetToken) -[*0..1]- ()"
		+ " WHERE token.token = {token}"
		+ " RETURN token, nodes(path), relationships(path)")
	PasswordResetToken findByTokenString(@Param("token") String token);
	
	@Query("MATCH (token:PasswordResetToken)"
		+ " WHERE token.expiryDate < {today}"
		+ " RETURN token")	
	Iterable<PasswordResetToken> findExpiredBefore(@Param("today") long today);
	
}
