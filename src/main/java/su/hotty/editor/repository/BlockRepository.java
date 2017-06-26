package su.hotty.editor.repository;
import java.util.List;

import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.data.mongodb.repository.MongoRepository;
import su.hotty.editor.domain.Block;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends MongoRepository<Block, String> {
    List<Block> findAll();
    List<Block> findAllByPageIdAndIsTopOrderByIdAsc(String pageId, Boolean isTop);
    List<Block> findAllByParentId(String parentId);

}