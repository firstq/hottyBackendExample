package su.hotty.editor.service;

import java.util.List;
import su.hotty.editor.domain.DomainName;
import su.hotty.editor.service.DomainNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su.hotty.editor.repository.DomainNameRepository;
import su.hotty.editor.service.DomainNameServiceImpl;

public interface DomainNameService {
    public abstract long countAllDomainNames();
    public abstract void deleteDomainName(DomainName domainName);    
    public abstract DomainName findDomainName(String id);
    public abstract List<DomainName> findAllDomainNames();    
    public abstract List<DomainName> findDomainNameEntries(int firstResult, int maxResults);    
    public abstract void saveDomainName(DomainName domainName);    
    public abstract DomainName updateDomainName(DomainName domainName);
    public abstract void deleteAllDomainNames();
}