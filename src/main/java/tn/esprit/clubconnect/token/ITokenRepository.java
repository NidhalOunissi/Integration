package tn.esprit.clubconnect.token;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ITokenRepository extends CrudRepository<Token, Integer> {
    @Query("""
select t from Token t inner join User u on t.user.idU = u.idU where u.idU = :userId and (t.expired = false or t.revoked = false )
""")
    List<Token> findAllValidTokensByUser(int userId);

    Optional<Token> findByToken(String token);
}
