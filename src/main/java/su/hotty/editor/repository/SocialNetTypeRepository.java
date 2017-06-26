package su.hotty.editor.repository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import su.hotty.editor.domain.SocialNetType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialNetTypeRepository extends MongoRepository<SocialNetType, String> {
    List<SocialNetType> findAll();
}