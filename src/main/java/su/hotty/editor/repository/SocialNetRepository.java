package su.hotty.editor.repository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import su.hotty.editor.domain.SocialNet;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialNetRepository extends MongoRepository<SocialNet, String> {
    List<SocialNet> findAll();
}