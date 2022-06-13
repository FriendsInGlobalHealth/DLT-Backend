package dlt.dltbackendmaster.domain;
// Generated Jun 13, 2022, 4:04:49 PM by Hibernate Tools 5.2.12.Final

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ReferencesServices generated by hbm2java
 */
@Entity
@Table(name = "references_services", catalog = "dreams_db")
public class ReferencesServices implements java.io.Serializable {

	private ReferencesServicesId id;
	private References references;
	private Services services;
	private String description;
	private int status;
	private int createdBy;
	private Date dateCreated;
	private Integer updatedBy;
	private Date dateUpdated;

	public ReferencesServices() {
	}

	public ReferencesServices(ReferencesServicesId id, References references, Services services, int status,
			int createdBy, Date dateCreated) {
		this.id = id;
		this.references = references;
		this.services = services;
		this.status = status;
		this.createdBy = createdBy;
		this.dateCreated = dateCreated;
	}

	public ReferencesServices(ReferencesServicesId id, References references, Services services, String description,
			int status, int createdBy, Date dateCreated, Integer updatedBy, Date dateUpdated) {
		this.id = id;
		this.references = references;
		this.services = services;
		this.description = description;
		this.status = status;
		this.createdBy = createdBy;
		this.dateCreated = dateCreated;
		this.updatedBy = updatedBy;
		this.dateUpdated = dateUpdated;
	}

	@EmbeddedId

	@AttributeOverrides({
			@AttributeOverride(name = "referenceId", column = @Column(name = "reference_id", nullable = false)),
			@AttributeOverride(name = "serviceId", column = @Column(name = "service_id", nullable = false)) })
	public ReferencesServicesId getId() {
		return this.id;
	}

	public void setId(ReferencesServicesId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reference_id", nullable = false, insertable = false, updatable = false)
	public References getReferences() {
		return this.references;
	}

	public void setReferences(References references) {
		this.references = references;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", nullable = false, insertable = false, updatable = false)
	public Services getServices() {
		return this.services;
	}

	public void setServices(Services services) {
		this.services = services;
	}

	@Column(name = "description", length = 250)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "status", nullable = false)
	public int getStatus() {
		return this.status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Column(name = "created_by", nullable = false)
	public int getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_created", nullable = false, length = 19)
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Column(name = "updated_by")
	public Integer getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_updated", length = 19)
	public Date getDateUpdated() {
		return this.dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

}