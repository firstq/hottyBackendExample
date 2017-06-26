package su.hotty.editor.service;

import java.util.List;
import su.hotty.editor.domain.Site;
import su.hotty.editor.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.editor.repository.SiteRepository;
import su.hotty.editor.service.SiteServiceImpl;

public interface SiteService {
    public abstract long countAllSites();
    public abstract void deleteSite(Site site);    
    public abstract Site findSite(String id);
    public abstract List<Site> findAllSites();    
    public abstract List<Site> findSiteEntries(int firstResult, int maxResults);    
    public abstract void saveSite(Site site);    
    public abstract Site updateSite(Site site);
    public abstract void deleteAllSites();
}