package su.hotty.editor.service;
import java.util.List;
import su.hotty.editor.domain.Block;
import su.hotty.editor.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.editor.repository.BlockRepository;
import su.hotty.editor.service.BlockServiceImpl;

public interface BlockService {
    public abstract long countAllBlocks();    
    public abstract void deleteBlock(Block block);    
    public abstract Block findBlock(String id);
    public abstract List<Block> findAllBlocks();    
    public abstract List<Block> findBlockEntries(int firstResult, int maxResults);    
    public abstract void saveBlock(Block block);    
    public abstract Block updateBlock(Block block);
    public abstract void deleteAllBlocks();
    public abstract List<Block> findAllBlocksByParentId(String parentId);
}