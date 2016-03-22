package su.hotty.example.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Configurable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContextType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Configurable
@PersistenceContext(type = PersistenceContextType.EXTENDED)
public class MenuItem {

	public MenuItem() {
	}

	public MenuItem(Integer version, String name, Boolean toplevel, String link, int priority, MenuItem parentItem, MenuBlock parentBlock) {
		this.version = version;
		this.name = name;
		this.toplevel = toplevel;
		this.link = link;
		this.priority = priority;
		this.parentItem = parentItem;
		this.parentBlock = parentBlock;
	}

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
    private String name;

    /**
     */
    private Boolean toplevel;

    /**
     */
    private String link;

    /**
     */
    private int priority;

    /**
     */
    @ManyToOne
	@JoinColumn(name= "parent_item")
    private MenuItem parentItem;

    /**
     */
	@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name= "parent_block")
    private MenuBlock parentBlock = null;
	
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Boolean getToplevel() {
        return this.toplevel;
    }
    
    public void setToplevel(Boolean toplevel) {
        this.toplevel = toplevel;
    }
    
    public String getLink() {
        return this.link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public MenuItem getParentItem() {
		if(parentItem != null) parentItem.setParentItem(null);
        return this.parentItem;
    }
    
    public void setParentItem(MenuItem parentItem) {
        this.parentItem = parentItem;
    }
    
	@JsonIgnore(value = true)
    public MenuBlock getParentBlock() {
        return this.parentBlock;
    }
    
    public void setParentBlock(MenuBlock parentBlock) {
        this.parentBlock = parentBlock;
    }
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "toplevel", "link", "priority", "parentItem", "parentBlock");
    
    public static final EntityManager entityManager() {
        EntityManager em = new MenuItem().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countMenuItems() {
        return entityManager().createQuery("SELECT COUNT(o) FROM MenuItem o", Long.class).getSingleResult();
    }
    
    public static List<MenuItem> findAllMenuItems() {
        return entityManager().createQuery("SELECT o FROM MenuItem o", MenuItem.class).getResultList();
    }
	
	public static List<MenuItem> findAllMenuItemsByParentBlockId(Long menuId) {
        return entityManager().createQuery("SELECT o FROM MenuItem o WHERE o.parentBlock.id=?", MenuItem.class).setParameter(1, menuId).getResultList();
    }
    
    public static List<MenuItem> findAllMenuItems(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM MenuItem o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, MenuItem.class).getResultList();
    }
    
    public static MenuItem findMenuItem(Long id) {
        if (id == null) return null;
        return entityManager().find(MenuItem.class, id);
    }
    
    public static List<MenuItem> findMenuItemEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM MenuItem o", MenuItem.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<MenuItem> findMenuItemEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM MenuItem o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, MenuItem.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            MenuItem attached = findMenuItem(this.id);
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
    public MenuItem merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        MenuItem merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
