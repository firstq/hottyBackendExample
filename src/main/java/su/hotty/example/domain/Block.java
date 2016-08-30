package su.hotty.example.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Configurable
@Inheritance(strategy=InheritanceType.JOINED)
public class Block {

	public Block() {
	}

	public Block(String name, String styles, Block parent, Boolean topLevel, Page page) {
		this.name = name;
		this.styles = styles;
		this.parent = parent;
		this.topLevel = topLevel;
		this.page = page;
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
    
	
	@Transient
	private Boolean isDelete;

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	
    /**
     */
    @NotNull
    private String name;

    /**
     */
    @Size(max = 900)
    private String styles;

    /**
     */
    @ManyToOne
    private Block parent;

    /**
     */
    private Boolean topLevel;

    /**
     */
    @ManyToOne
    private Page page;

    /**
     */
    @ManyToOne
    private StaticBlock staticBlock;

    /**
     */
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<FrontClass> frontClases = new ArrayList<FrontClass>();
	
	private String frontClases;
	
	/**
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<Block> children = new ArrayList<Block>();
	
	public List<Block> getChildren() {
		return children;
	}

	public void setChildren(List<Block> children) {
		this.children = children;
	}
	
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getStyles() {
        return this.styles;
    }
    
    public void setStyles(String styles) {
        this.styles = styles;
    }
    
    public Block getParent() {
        return this.parent;
    }
    
    public void setParent(Block parent) {
        this.parent = parent;
    }
    
    public Boolean getTopLevel() {
        return this.topLevel;
    }
    
    public void setTopLevel(Boolean topLevel) {
        this.topLevel = topLevel;
    }
    
    public Page getPage() {
        return this.page;
    }
    
    public void setPage(Page page) {
        this.page = page;
    }
    
    public StaticBlock getStaticBlock() {
        return this.staticBlock;
    }
    
    public void setStaticBlock(StaticBlock staticBlock) {
        this.staticBlock = staticBlock;
    }
    
    public String getFrontClases() {
        return this.frontClases;
    }
    
    public void setFrontClases(String frontClases) {
        this.frontClases = frontClases;
    }
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "styles", "parent", "topLevel", "page", "staticBlock", "frontClases");
    
    public static final EntityManager entityManager() {
        EntityManager em = new Block().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countBlocks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM Block o", Long.class).getSingleResult();
    }
    
    public static List<Block> findAllBlocks() {
        return entityManager().createQuery("SELECT o FROM Block o", Block.class).getResultList();
    }
    
    public static List<Block> findAllBlocks(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Block o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Block.class).getResultList();
    }
    
    public static Block findBlock(Long id) {
        if (id == null) return null;
        return entityManager().find(Block.class, id);
    }
    
    public static List<Block> findBlockEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM Block o", Block.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<Block> findBlockEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM Block o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, Block.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            Block attached = findBlock(this.id);
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
    public Block merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        Block merged = this.entityManager.merge(this);
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
    
    public static Block fromJsonToBlock(String json) {
        return new JSONDeserializer<Block>()
        .use(null, Block.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<Block> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<Block> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<Block> fromJsonArrayToBlocks(String json) {
        return new JSONDeserializer<List<Block>>()
        .use("values", Block.class).deserialize(json);
    }
	
    @Transient
	 private String blockType = this.getClass().toString().substring(this.getClass().toString().lastIndexOf(".")+1);

	public String getBlockType() {
		return blockType;
	}

	public void setBlockType(String blockType) {
		this.blockType = blockType;
	}
	
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
