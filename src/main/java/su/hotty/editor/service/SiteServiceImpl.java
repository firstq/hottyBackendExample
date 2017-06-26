package su.hotty.editor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.editor.domain.Site;
import su.hotty.editor.repository.SiteRepository;
import java.util.List;

@Service
@Transactional
public class SiteServiceImpl implements SiteService {

    @Autowired
    SiteRepository siteRepository;

    public long countAllSites() {
        return siteRepository.count();
    }

    public void deleteSite(Site site) {
        siteRepository.delete(site);
    }

    public Site findSite(String id) {
        return siteRepository.findOne(id);
    }

    public List<Site> findAllSites() {
        return siteRepository.findAll();
    }

    public List<Site> findSiteEntries(int firstResult, int maxResults) {
        return siteRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

    public void saveSite(Site site) {
        siteRepository.save(site);
    }

    public Site updateSite(Site site) {
        return siteRepository.save(site);
    }

    @Override
    public void deleteAllSites() {
        siteRepository.deleteAll();
    }

}