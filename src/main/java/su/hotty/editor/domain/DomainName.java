package su.hotty.editor.domain;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import su.hotty.editor.domain.DomainName;
import su.hotty.editor.domain.Site;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Persistent
public class DomainName {

    /**
     */
    @NotNull
    private String domainName;

    /**
     */
    private String siteId;

    /**
     */
    @NotNull
    private Long clientId;

    public String getDomainName() {
        return this.domainName;
    }
    
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
    
    public String getSite() {
        return this.siteId;
    }
    
    public void setSite(String siteId) {
        this.siteId = siteId;
    }
    
    public Long getClientId() {
        return this.clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static DomainName fromJsonToDomainName(String json) {
        return new JSONDeserializer<DomainName>()
        .use(null, DomainName.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<DomainName> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<DomainName> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<DomainName> fromJsonArrayToDomainNames(String json) {
        return new JSONDeserializer<List<DomainName>>()
        .use("values", DomainName.class).deserialize(json);
    }

    @Id
    private String id;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
}