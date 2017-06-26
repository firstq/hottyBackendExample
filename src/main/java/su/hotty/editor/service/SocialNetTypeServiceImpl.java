package su.hotty.editor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.editor.domain.SocialNetType;
import su.hotty.editor.repository.SocialNetTypeRepository;
import java.util.List;

@Service
@Transactional
public class SocialNetTypeServiceImpl implements SocialNetTypeService {

    @Autowired
    SocialNetTypeRepository socialNetTypeRepository;

    public long countAllSocialNetTypes() {
        return socialNetTypeRepository.count();
    }

    public void deleteSocialNetType(SocialNetType socialNetType) {
        socialNetTypeRepository.delete(socialNetType);
    }

    public SocialNetType findSocialNetType(String id) {
        return socialNetTypeRepository.findOne(id);
    }

    public List<SocialNetType> findAllSocialNetTypes() {
        return socialNetTypeRepository.findAll();
    }

    public List<SocialNetType> findSocialNetTypeEntries(int firstResult, int maxResults) {
        return socialNetTypeRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

    public void saveSocialNetType(SocialNetType socialNetType) {
        socialNetTypeRepository.save(socialNetType);
    }

    public SocialNetType updateSocialNetType(SocialNetType socialNetType) {
        return socialNetTypeRepository.save(socialNetType);
    }

    @Override
    public void deleteAllSocialNetTypes() {
        socialNetTypeRepository.deleteAll();
    }

}