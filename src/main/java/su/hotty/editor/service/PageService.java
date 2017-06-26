package su.hotty.editor.service;

import java.util.List;

import su.hotty.editor.domain.Block;
import su.hotty.editor.domain.Page;
import su.hotty.editor.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.editor.repository.PageRepository;
import su.hotty.editor.service.PageServiceImpl;

public interface PageService {
    public abstract long countAllPages();    
    public abstract void deletePage(Page page);    
    public abstract Page findPage(String id);
    public abstract List<Page> findAllPages();    
    public abstract List<Page> findPageEntries(int firstResult, int maxResults);    
    public abstract void savePage(Page page);    
    public abstract Page updatePage(Page page);
    public abstract void savePage(Page page, List<Block> blockEtities);
    public abstract Page findEagerPage(String id);
    public abstract Page findEagerPage(Page page);
    public abstract void deleteAllPages();
    public abstract List<Page> findAllPagesBySiteId(String siteId);
}