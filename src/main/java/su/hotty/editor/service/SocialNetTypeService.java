package su.hotty.editor.service;

import java.util.List;
import su.hotty.editor.domain.SocialNetType;
import su.hotty.editor.service.SocialNetTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.editor.repository.SocialNetTypeRepository;
import su.hotty.editor.service.SocialNetTypeServiceImpl;

public interface SocialNetTypeService {

    public abstract long countAllSocialNetTypes();    
    public abstract void deleteSocialNetType(SocialNetType socialNetType);    
    public abstract SocialNetType findSocialNetType(String id);
    public abstract List<SocialNetType> findAllSocialNetTypes();    
    public abstract List<SocialNetType> findSocialNetTypeEntries(int firstResult, int maxResults);    
    public abstract void saveSocialNetType(SocialNetType socialNetType);    
    public abstract SocialNetType updateSocialNetType(SocialNetType socialNetType);
    public abstract void deleteAllSocialNetTypes();

}