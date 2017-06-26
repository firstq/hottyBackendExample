package su.hotty.editor.repository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import su.hotty.editor.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PageRepository extends MongoRepository<Page, String> {
    List<Page> findAll();
    List<Page> findAllBySiteId(String siteId);
}