package su.hotty.example.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Configurable
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    /**
     */
    @NotNull
    private String pagePath;

    /**
     */
    @NotNull
    private String title;

    /**
     */
    private String description;

    /**
     */
    @Size(max = 900)
    private String styles;

    /**
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<Block> blocks = new ArrayList<Block>();

    /**
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<FrontClass> frontClases = new ArrayList<FrontClass>();
	
    public String getPagePath() {
        return this.pagePath;
    }
    
    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStyles() {
        return this.styles;
    }
    
    public void setStyles(String styles) {
        this.styles = styles;
    }
    
    public List<Block> getBlocks() {
        return this.blocks;
    }
    
    public void setBlocks(List<Block> blocks) {
        this.blocks = blocks;
    }
    
    public List<FrontClass> getFrontClases() {
        return this.frontClases;
    }
    
    public void setFrontClases(List<FrontClass> frontClases) {
        this.frontClases = frontClases;
    }
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("pagePath", "title", "description", "styles", "blocks", "frontClases");
    
    public static final EntityManager entityManager() {
        EntityManager em = new Page().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countPages() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Page o", Long.class).getSingleResult();
    }
    
    public static List<Page> findAllPages() {
        return entityManager().createQuery("SELECT o FROM Page o", Page.class).getResultList();
    }
    
    public static List<Page> findAllPages(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Page o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Page.class).getResultList();
    }
    
    public static Page findPage(Long id) {
        if (id == null) return null;
        return entityManager().find(Page.class, id);
    }
    
    public static List<Page> findPageEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Page o", Page.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Page> findPageEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Page o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Page.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    @Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }
    
    @Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            Page attached = findPage(this.id);
            this.entityManager.remove(attached);
        }
    }
    
    @Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }
    
    @Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }
    
    @Transactional
    public Page merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Page merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
