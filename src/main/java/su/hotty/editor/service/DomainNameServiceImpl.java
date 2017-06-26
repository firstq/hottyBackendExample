package su.hotty.editor.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.editor.domain.DomainName;
import su.hotty.editor.repository.DomainNameRepository;

import java.util.List;


@Service
@Transactional
public class DomainNameServiceImpl implements DomainNameService {

    @Autowired
    DomainNameRepository domainNameRepository;

    public long countAllDomainNames() {
        return domainNameRepository.count();
    }

    public void deleteDomainName(DomainName domainName) {
        domainNameRepository.delete(domainName);
    }

    public DomainName findDomainName(String id) {
        return domainNameRepository.findOne(id);
    }

    public List<DomainName> findAllDomainNames() {
        return domainNameRepository.findAll();
    }

    public List<DomainName> findDomainNameEntries(int firstResult, int maxResults) {
        return domainNameRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

    public void saveDomainName(DomainName domainName) {
        domainNameRepository.save(domainName);
    }

    public DomainName updateDomainName(DomainName domainName) {
        return domainNameRepository.save(domainName);
    }

    @Override
    public void deleteAllDomainNames() {
        domainNameRepository.deleteAll();
    }

}