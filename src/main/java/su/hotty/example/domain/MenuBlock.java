package su.hotty.example.domain;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Entity;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Configurable
@PrimaryKeyJoinColumn(name="id")
public class MenuBlock extends Block{
	
	@Size(max = 2000)
    private String itemStyles;
	
	@Size(max = 300)
    private String logo;

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getItemStyles() {
		return itemStyles;
	}

	public void setItemStyles(String itemStyles) {
		this.itemStyles = itemStyles;
	}
    /**
     */
    private Boolean isVertical;
	
    /**
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<MenuItem> items = new ArrayList<MenuItem>();

    public Boolean getIsVertical() {
        return this.isVertical;
    }
    
    public void setIsVertical(Boolean isVertical) {
        this.isVertical = isVertical;
    }
    
	public List<MenuItem> getItems() {
		return items;
	}

	public void setItems(List<MenuItem> items) {
		this.items = items;
	}
	
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("isVertical");
    
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
    
    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static MenuBlock fromJsonToMenuBlock(String json) {
        return new JSONDeserializer<MenuBlock>()
        .use(null, MenuBlock.class).deserialize(json);
    }
    
    public static String toMenuBlockJsonArray(Collection<MenuBlock> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String toMenuBlockJsonArray(Collection<MenuBlock> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<MenuBlock> fromJsonArrayToMenuBlocks(String json) {
        return new JSONDeserializer<List<MenuBlock>>()
        .use("values", MenuBlock.class).deserialize(json);
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
