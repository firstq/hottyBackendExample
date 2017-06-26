package su.hotty.editor.repository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import su.hotty.editor.domain.Site;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteRepository extends MongoRepository<Site, String> {
    List<Site> findAll();
}