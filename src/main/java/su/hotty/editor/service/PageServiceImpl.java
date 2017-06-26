package su.hotty.editor.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.editor.domain.Block;
import su.hotty.editor.domain.Page;
import su.hotty.editor.repository.BlockRepository;
import su.hotty.editor.repository.PageRepository;
import java.util.List;

@Service
@Transactional
public class PageServiceImpl implements PageService {

    @Autowired
    PageRepository pageRepository;

    @Autowired
    BlockRepository blockRepository;

    public long countAllPages() {
        return pageRepository.count();
    }

    public void deletePage(Page page) {
        pageRepository.delete(page);
    }

    public Page findPage(String id) {
        return pageRepository.findOne(id);
    }

    public List<Page> findAllPages() {
        return pageRepository.findAll();
    }

    public List<Page> findPageEntries(int firstResult, int maxResults) {
        return pageRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

    public void savePage(Page page) {
        pageRepository.save(page);
    }

    public Page updatePage(Page page) {
        return pageRepository.save(page);
    }

    @Override
    public void savePage(Page page, List<Block> blockEtities) {
        pageRepository.save(page);
        blockRepository.save(blockEtities);
    }

    @Override
    public Page findEagerPage(String id) {
        Page page = pageRepository.findOne(id);
        List<Block> topBlocks = blockRepository.findAllByPageIdAndIsTopOrderByIdAsc(page.getId(), Boolean.TRUE);
        for (Block topBlock : topBlocks) {
            if(topBlock.getTagType().equals("div")) this.findBlockChildrenEager(topBlock);
        }
        page.setBlocks(topBlocks);
        return page;
    }

    @Override
    public Page findEagerPage(Page page) {
        //TODO: !!! по факту вытаскивается только 2 уровня
        List<Block> topBlocks = blockRepository.findAllByPageIdAndIsTopOrderByIdAsc(page.getId(), Boolean.TRUE);
        for (Block topBlock : topBlocks) {
            if(topBlock.getTagType().equals("div")) this.findBlockChildrenEager(topBlock);//topBlock.setChildren(blockRepository.findAllByParentId(topBlock.getId()));
        }
        page.setBlocks(topBlocks);
        return page;
    }

    private Block findBlockChildrenEager(Block block){
        List<Block> children = blockRepository.findAllByParentId(block.getId());
        for (Block child : children){
            findBlockChildrenEager(child);
        }
        block.setChildren(children);
        return block;
    }

    @Override
    public void deleteAllPages() {
        pageRepository.deleteAll();
    }

    @Override
    public List<Page> findAllPagesBySiteId(String siteId) {
        return pageRepository.findAllBySiteId(siteId);
    }

}