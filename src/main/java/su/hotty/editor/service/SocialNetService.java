package su.hotty.editor.service;

import java.util.List;
import su.hotty.editor.domain.SocialNet;
import su.hotty.editor.service.SocialNetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.editor.repository.SocialNetRepository;
import su.hotty.editor.service.SocialNetServiceImpl;

public interface SocialNetService {
    public abstract long countAllSocialNets();
    public abstract void deleteSocialNet(SocialNet socialNet);    
    public abstract SocialNet findSocialNet(String id);
    public abstract List<SocialNet> findAllSocialNets();    
    public abstract List<SocialNet> findSocialNetEntries(int firstResult, int maxResults);    
    public abstract void saveSocialNet(SocialNet socialNet);    
    public abstract SocialNet updateSocialNet(SocialNet socialNet);
    public abstract void deleteAllSocialNets();
}