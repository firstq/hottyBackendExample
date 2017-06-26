package su.hotty.editor.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.editor.domain.Block;
import su.hotty.editor.repository.BlockRepository;

import java.util.List;

@Service
@Transactional
public class BlockServiceImpl implements BlockService {

    @Autowired
    BlockRepository blockRepository;

    public long countAllBlocks() {
        return blockRepository.count();
    }

    public void deleteBlock(Block block) {
        blockRepository.delete(block);
    }

    public Block findBlock(String id) {
        return blockRepository.findOne(id);
    }

    public List<Block> findAllBlocks() {
        return blockRepository.findAll();
    }

    public List<Block> findBlockEntries(int firstResult, int maxResults) {
        return blockRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

    public void saveBlock(Block block) {
        blockRepository.save(block);
    }

    public Block updateBlock(Block block) {
        return blockRepository.save(block);
    }

    public void deleteAllBlocks() {
        blockRepository.deleteAll();
    }

    @Override
    public List<Block> findAllBlocksByParentId(String parentId) {
        return blockRepository.findAllByParentId(parentId);
    }
}