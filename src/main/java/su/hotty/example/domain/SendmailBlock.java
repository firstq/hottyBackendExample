package su.hotty.example.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Configurable
@PrimaryKeyJoinColumn(name="id")
public class SendmailBlock extends Block {

    /**
     */
    @Size(max = 100)
    private String sendTo;

    /**
     */
    @Size(max = 100)
    private String sendFrom;

    /**
     */
    private Boolean capchaEnable;
	
    public String getSendTo() {
        return this.sendTo;
    }
    
    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }
    
    public String getSendFrom() {
        return this.sendFrom;
    }
    
    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }
    
    public Boolean getCapchaEnable() {
        return this.capchaEnable;
    }
    
    public void setCapchaEnable(Boolean capchaEnable) {
        this.capchaEnable = capchaEnable;
    }
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("sendTo", "sendFrom", "capchaEnable");
    
//    public static final EntityManager entityManager() {
//        EntityManager em = new SendmailBlock().entityManager;
//        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
//        return em;
//    }
    
    public static long countSendmailBlocks() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SendmailBlock o", Long.class).getSingleResult();
    }
    
    public static List<SendmailBlock> findAllSendmailBlocks() {
        return entityManager().createQuery("SELECT o FROM SendmailBlock o", SendmailBlock.class).getResultList();
    }
    
    public static List<SendmailBlock> findAllSendmailBlocks(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SendmailBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SendmailBlock.class).getResultList();
    }
    
    public static SendmailBlock findSendmailBlock(Long id) {
        if (id == null) return null;
        return entityManager().find(SendmailBlock.class, id);
    }
    
    public static List<SendmailBlock> findSendmailBlockEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SendmailBlock o", SendmailBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<SendmailBlock> findSendmailBlockEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SendmailBlock o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SendmailBlock.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            SendmailBlock attached = findSendmailBlock(this.getId());
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
    public SendmailBlock merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SendmailBlock merged = this.entityManager.merge(this);
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
    
    public static SendmailBlock fromJsonToSendmailBlock(String json) {
        return new JSONDeserializer<SendmailBlock>()
        .use(null, SendmailBlock.class).deserialize(json);
    }
//    
//    public static String toJsonArray(Collection<SendmailBlock> collection) {
//        return new JSONSerializer()
//        .exclude("*.class").serialize(collection);
//    }
//    
//    public static String toJsonArray(Collection<SendmailBlock> collection, String[] fields) {
//        return new JSONSerializer()
//        .include(fields).exclude("*.class").serialize(collection);
//    }
    
    public static Collection<SendmailBlock> fromJsonArrayToSendmailBlocks(String json) {
        return new JSONDeserializer<List<SendmailBlock>>()
        .use("values", SendmailBlock.class).deserialize(json);
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
