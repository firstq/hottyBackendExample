package su.hotty.editor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.editor.domain.SocialNet;
import su.hotty.editor.repository.SocialNetRepository;
import java.util.List;

@Service
@Transactional
public class SocialNetServiceImpl implements SocialNetService {

    @Autowired
    SocialNetRepository socialNetRepository;

    public long countAllSocialNets() {
        return socialNetRepository.count();
    }

    public void deleteSocialNet(SocialNet socialNet) {
        socialNetRepository.delete(socialNet);
    }

    public SocialNet findSocialNet(String id) {
        return socialNetRepository.findOne(id);
    }

    public List<SocialNet> findAllSocialNets() {
        return socialNetRepository.findAll();
    }

    public List<SocialNet> findSocialNetEntries(int firstResult, int maxResults) {
        return socialNetRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

    public void saveSocialNet(SocialNet socialNet) {
        socialNetRepository.save(socialNet);
    }

    public SocialNet updateSocialNet(SocialNet socialNet) {
        return socialNetRepository.save(socialNet);
    }

    @Override
    public void deleteAllSocialNets() {
        socialNetRepository.deleteAll();
    }

}