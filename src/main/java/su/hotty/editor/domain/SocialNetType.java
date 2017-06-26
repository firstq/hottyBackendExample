package su.hotty.editor.domain;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;


public class SocialNetType {

    /**
     */
    @NotNull
    private String name;

    /**
     */
    @NotNull
    private String domainUrl;

    /**
     */
    @NotNull
    private String defaultIcon;

    /**
     */
    private Boolean isApproved;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomainUrl() {
        return this.domainUrl;
    }

    public void setDomainUrl(String domainUrl) {
        this.domainUrl = domainUrl;
    }

    public String getDefaultIcon() {
        return this.defaultIcon;
    }

    public void setDefaultIcon(String defaultIcon) {
        this.defaultIcon = defaultIcon;
    }

    public Boolean getIsApproved() {
        return this.isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public String toJson() {
        return new JSONSerializer()
                .exclude("*.class").serialize(this);
    }

    public String toJson(String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(this);
    }

    public static SocialNetType fromJsonToSocialNetType(String json) {
        return new JSONDeserializer<SocialNetType>()
                .use(null, SocialNetType.class).deserialize(json);
    }

    public static String toJsonArray(Collection<SocialNetType> collection) {
        return new JSONSerializer()
                .exclude("*.class").serialize(collection);
    }

    public static String toJsonArray(Collection<SocialNetType> collection, String[] fields) {
        return new JSONSerializer()
                .include(fields).exclude("*.class").serialize(collection);
    }

    public static Collection<SocialNetType> fromJsonArrayToSocialNetTypes(String json) {
        return new JSONDeserializer<List<SocialNetType>>()
                .use("values", SocialNetType.class).deserialize(json);
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