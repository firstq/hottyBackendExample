package su.hotty.editor.repository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import su.hotty.editor.domain.DomainName;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainNameRepository extends MongoRepository<DomainName, String> {
    List<DomainName> findAll();
}