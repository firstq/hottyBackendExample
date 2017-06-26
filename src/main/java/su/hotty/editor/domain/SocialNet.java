package su.hotty.editor.domain;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.ManyToOne;
import su.hotty.editor.domain.Block;
import su.hotty.editor.domain.SocialNet;
import su.hotty.editor.domain.SocialNetType;
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
public class SocialNet {

    /**
     */
    @NotNull
    private String netName;

    /**
     */
    @NotNull
    @Column(unique = true)
    private String clientPage;

    /**
     */
    @NotNull
    private String clientComunity;

    /**
     */
    @NotNull
    private String authKey;

    /**
     */
    @NotNull
    private Long clientId;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss a")
    private Date dateCreated;

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm:ss a")
    private Date dateUpdated;

    /**
     */
    @NotNull
    private String clientNetIcon;

    /**
     */
    @ManyToOne
    private String socialNetTypeId;

    /**
     */
    @ManyToOne
    private String blockId;
    
    public String getNetName() {
        return this.netName;
    }
    
    public void setNetName(String netName) {
        this.netName = netName;
    }
    
    public String getClientPage() {
        return this.clientPage;
    }
    
    public void setClientPage(String clientPage) {
        this.clientPage = clientPage;
    }
    
    public String getClientComunity() {
        return this.clientComunity;
    }
    
    public void setClientComunity(String clientComunity) {
        this.clientComunity = clientComunity;
    }
    
    public String getAuthKey() {
        return this.authKey;
    }
    
    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }
    
    public Long getClientId() {
        return this.clientId;
    }
    
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    
    public Date getDateCreated() {
        return this.dateCreated;
    }
    
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public Date getDateUpdated() {
        return this.dateUpdated;
    }
    
    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
    
    public String getClientNetIcon() {
        return this.clientNetIcon;
    }
    
    public void setClientNetIcon(String clientNetIcon) {
        this.clientNetIcon = clientNetIcon;
    }
    
    public String getSocialNetTypeId() {
        return this.socialNetTypeId;
    }
    
    public void setSocialNetTypeId(String socialNetTypeId) {
        this.socialNetTypeId = socialNetTypeId;
    }
    
    public String getBlockId() {
        return this.blockId;
    }
    
    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String toJson() {
        return new JSONSerializer()
        .exclude("*.class").serialize(this);
    }
    
    public String toJson(String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(this);
    }
    
    public static SocialNet fromJsonToSocialNet(String json) {
        return new JSONDeserializer<SocialNet>()
        .use(null, SocialNet.class).deserialize(json);
    }
    
    public static String toJsonArray(Collection<SocialNet> collection) {
        return new JSONSerializer()
        .exclude("*.class").serialize(collection);
    }
    
    public static String toJsonArray(Collection<SocialNet> collection, String[] fields) {
        return new JSONSerializer()
        .include(fields).exclude("*.class").serialize(collection);
    }
    
    public static Collection<SocialNet> fromJsonArrayToSocialNets(String json) {
        return new JSONDeserializer<List<SocialNet>>()
        .use("values", SocialNet.class).deserialize(json);
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