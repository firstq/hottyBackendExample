package su.hotty.example.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;
import java.util.Calendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.Collection;
import javax.persistence.ManyToOne;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Configurable
public class SendedMessage {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Version
    @Column(name = "version")
    private Integer version;
    
    @NotNull
    @Size(min = 1)
    private String theme;
	
	@NotNull
    @Size(min = 1)
    private String captcha;
	
	@Size(max = 2000)
    private String targetMessage;

    @NotNull
    @Pattern(regexp = "[\\w.]+@[\\w.]+\\.\\w+")
    private String email;

    @ManyToOne
    private SendmailBlock parent;

	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss a")
    private Calendar dateCreate;
	
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getTargetMessage() {
		return targetMessage;
	}

	public void setTargetMessage(String targetMessage) {
		this.targetMessage = targetMessage;
	}

	public SendmailBlock getParent() {
		return parent;
	}

	public void setParent(SendmailBlock parent) {
		this.parent = parent;
	}
    
    public Integer getVersion() {
        return this.version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Calendar getDateCreate() {
        return this.dateCreate;
    }
    
    public void setDateCreate(Calendar dateCreate) {
        this.dateCreate = dateCreate;
    }
    
    @PersistenceContext
    transient EntityManager entityManager;
    
    public static final List<String> fieldNames4OrderClauseFilter = java.util.Arrays.asList("name", "lastname", "fathername", "email", "login", "password", "dateCreate", "dateUpdate", "dateLastVisit", "enabled");
    
    public static final EntityManager entityManager() {
        EntityManager em = new SendedMessage().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }
    
    public static long countSendedMessages() {
        return entityManager().createQuery("SELECT COUNT(o) FROM SendedMessage o", Long.class).getSingleResult();
    }
    
    public static List<SendedMessage> findAllSendedMessages() {
        return entityManager().createQuery("SELECT o FROM SendedMessage o", SendedMessage.class).getResultList();
    }
    
    public static List<SendedMessage> findAllSendedMessages(String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SendedMessage o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SendedMessage.class).getResultList();
    }
    
    public static SendedMessage findSendedMessage(Long id) {
        if (id == null) return null;
        return entityManager().find(SendedMessage.class, id);
    }
    
    public static List<SendedMessage> findSendedMessageEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM SendedMessage o", SendedMessage.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }
    
    public static List<SendedMessage> findSendedMessageEntries(int firstResult, int maxResults, String sortFieldName, String sortOrder) {
        String jpaQuery = "SELECT o FROM SendedMessage o";
        if (fieldNames4OrderClauseFilter.contains(sortFieldName)) {
            jpaQuery = jpaQuery + " ORDER BY " + sortFieldName;
            if ("ASC".equalsIgnoreCase(sortOrder) || "DESC".equalsIgnoreCase(sortOrder)) {
                jpaQuery = jpaQuery + " " + sortOrder;
            }
        }
        return entityManager().createQuery(jpaQuery, SendedMessage.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            SendedMessage attached = SendedMessage.findSendedMessage(this.id);
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
    public SendedMessage merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        SendedMessage merged = this.entityManager.merge(this);
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
    
    public static SendedMessage fromJsonToSendedMessage(String json) {
        return new JSONDeserializer<SendedMessage>()
        .use(null, SendedMessage.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<SendedMessage> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<SendedMessage> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<SendedMessage> fromJsonArrayToSendedMessages(String json) {
        return new JSONDeserializer<List<SendedMessage>>()
        .use("values", SendedMessage.class).deserialize(json);
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}
