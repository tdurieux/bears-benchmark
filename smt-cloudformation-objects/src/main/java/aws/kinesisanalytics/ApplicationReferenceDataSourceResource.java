
package aws.kinesisanalytics;

import java.util.List;
import java.util.Map;
import aws.CreationPolicy;
import aws.DeletionPolicy;
import aws.Resource;
import aws.UpdatePolicy;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * ApplicationReferenceDataSourceResource
 * <p>
 * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-kinesisanalytics-applicationreferencedatasource.html
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Properties"
})
public class ApplicationReferenceDataSourceResource
    extends Resource
{

    /**
     * ApplicationReferenceDataSource
     * <p>
     * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-kinesisanalytics-applicationreferencedatasource.html
     * 
     */
    @JsonProperty("Properties")
    @JsonPropertyDescription("http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-kinesisanalytics-applicationreferencedatasource.html")
    private ApplicationReferenceDataSource properties;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ApplicationReferenceDataSourceResource() {
    }

    /**
     * 
     * @param name
     */
    public ApplicationReferenceDataSourceResource(java.lang.String name) {
        super(name);
    }

    /**
     * ApplicationReferenceDataSource
     * <p>
     * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-kinesisanalytics-applicationreferencedatasource.html
     * 
     */
    public ApplicationReferenceDataSource getProperties() {
        return properties;
    }

    /**
     * ApplicationReferenceDataSource
     * <p>
     * http://docs.aws.amazon.com/AWSCloudFormation/latest/UserGuide/aws-resource-kinesisanalytics-applicationreferencedatasource.html
     * 
     */
    public void setProperties(ApplicationReferenceDataSource properties) {
        this.properties = properties;
    }

    public ApplicationReferenceDataSourceResource withProperties(ApplicationReferenceDataSource properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public ApplicationReferenceDataSourceResource withType(java.lang.String type) {
        super.withType(type);
        return this;
    }

    @Override
    public ApplicationReferenceDataSourceResource withCondition(java.lang.String condition) {
        super.withCondition(condition);
        return this;
    }

    @Override
    public ApplicationReferenceDataSourceResource withCreationPolicy(CreationPolicy creationPolicy) {
        super.withCreationPolicy(creationPolicy);
        return this;
    }

    @Override
    public ApplicationReferenceDataSourceResource withUpdatePolicy(UpdatePolicy updatePolicy) {
        super.withUpdatePolicy(updatePolicy);
        return this;
    }

    @Override
    public ApplicationReferenceDataSourceResource withDeletionPolicy(DeletionPolicy deletionPolicy) {
        super.withDeletionPolicy(deletionPolicy);
        return this;
    }

    @Override
    public ApplicationReferenceDataSourceResource withDependsOn(List<java.lang.String> dependsOn) {
        super.withDependsOn(dependsOn);
        return this;
    }

    @Override
    public ApplicationReferenceDataSourceResource withMetadata(Map<String, Object> metadata) {
        super.withMetadata(metadata);
        return this;
    }

    @Override
    public ApplicationReferenceDataSourceResource withName(java.lang.String name) {
        super.withName(name);
        return this;
    }

    @Override
    public java.lang.String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("properties", properties).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(properties).toHashCode();
    }

    @Override
    public boolean equals(java.lang.Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ApplicationReferenceDataSourceResource) == false) {
            return false;
        }
        ApplicationReferenceDataSourceResource rhs = ((ApplicationReferenceDataSourceResource) other);
        return new EqualsBuilder().appendSuper(super.equals(other)).append(properties, rhs.properties).isEquals();
    }

}
