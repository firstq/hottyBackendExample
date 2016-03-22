package su.hotty.example.domain;

import flexjson.JSON;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import org.springframework.beans.factory.annotation.Configurable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Version;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.glassfish.jersey.server.JSONP;
import org.hibernate.annotations.Fetch;

@Entity
@Configurable
@PrimaryKeyJoinColumn(name="id")
public class MenuBlock extends Block{

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id")
//    private Long id;
//    
//    @Version
//    @Column(name = "version")
//    private Integer version;
//    
//    public Long getId() {
//        return this.id;
//    }
//    
//    public void setId(Long id) {
//        this.id = id;
//    }
//    
//    public Integer getVersion() {
//        return this.version;
//    }
//    
//    public void setVersion(Integer version) {
//        this.version = version;
//    }
    
    /**
     */
    private Boolean isVertical;

    /**
     */
	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<MenuItem> items = new ArrayList<MenuItem>();
	
    public Boolean getIsVertical() {
        return this.isVertical;
    }
    
    public void setIsVertical(Boolean isVertical) {
        this.isVertical = isVertical;
    }
    
    public List<MenuItem> getItems() {
        return this.items;
    }
    
    public void setItems(List<MenuItem> items) {
        this.items = items;
    }
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("isVertical", "items");
    
//    public static final EntityManager entityManager() {
//        EntityManager em = new MenuBlock().entityManager;
//        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
//        return em;
//    }
    
    public static long countMenuBlocks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MenuBlock o", Long.class).getSingleResult();
    }
    
    public static List<MenuBlock> findAllMenuBlocks() {
        return entityManager().createQuery("SELECT o FROM MenuBlock o", MenuBlock.class).getResultList();
    }
    
    public static List<MenuBlock> findAllMenuBlocks(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM MenuBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, MenuBlock.class).getResultList();
    }
    
    public static MenuBlock findMenuBlock(Long id) {
        if (id == null) return null;
        return entityManager().find(MenuBlock.class, id);
    }
    
    public static List<MenuBlock> findMenuBlockEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MenuBlock o", MenuBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<MenuBlock> findMenuBlockEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM MenuBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, MenuBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            MenuBlock attached = findMenuBlock(this.getId());
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
    public MenuBlock merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MenuBlock merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
