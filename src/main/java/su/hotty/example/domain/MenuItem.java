package su.hotty.example.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Entity;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Configurable
public class MenuItem {

	public MenuItem() {
	}

	public MenuItem(String name, Boolean toplevel, String link, int priority, MenuItem parentItem, MenuBlock parentBlock) {
		this.name = name;
		this.toplevel = toplevel;
		this.link = link;
		this.priority = priority;
		this.parentItem = parentItem;
		this.parentBlock = parentBlock;
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
	 * Try write to Db value LIKE #<number>
     */
    private String link;
	
	private Long dependSliderBlock;

	public Long getDependSliderBlock() {
		return dependSliderBlock;
	}

	public void setDependSliderBlock(Long dependSliderBlock) {
		this.dependSliderBlock = dependSliderBlock;
	}

    /**
     */
    private int priority;

    /**
     */
    @ManyToOne
    private MenuItem parentItem;

    /**
     */
    @ManyToOne
    private MenuBlock parentBlock;
	
	
	@Transient
	private Boolean isDelete;

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	
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
        return this.parentItem;
    }
    
    public void setParentItem(MenuItem parentItem) {
        this.parentItem = parentItem;
    }
    
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
    
	public static List<MenuItem> findMenuItemsByParent(MenuItem parent) {
        return entityManager().createQuery("SELECT m FROM MenuItem m WHERE m.parentItem = :par ORDER BY priority DESC", MenuItem.class)
				.setParameter("par", parent)
				.getResultList();
    }
	
	public static List<MenuItem> findAllMenuItemsByParentBlockId(Long menuId) {
        return entityManager().createQuery("SELECT o FROM MenuItem o WHERE o.parentBlock.id=?", MenuItem.class).setParameter(1, menuId).getResultList();
    }
	
    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static MenuItem fromJsonToMenuItem(String json) {
        return new JSONDeserializer<MenuItem>()
        .use(null, MenuItem.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<MenuItem> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<MenuItem> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<MenuItem> fromJsonArrayToMenuItems(String json) {
        return new JSONDeserializer<List<MenuItem>>()
        .use("values", MenuItem.class).deserialize(json);
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
