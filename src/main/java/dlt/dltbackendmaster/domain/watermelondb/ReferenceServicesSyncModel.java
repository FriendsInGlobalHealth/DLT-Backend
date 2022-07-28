package dlt.dltbackendmaster.domain.watermelondb;

public class ReferenceServicesSyncModel {

	private String id;
    private Integer reference_id;
    private Integer service_id;
    private String description;
    private String date_created;
    private Integer status;
    private String _status;
    private String _changed;
    private String online_id;
    
	public ReferenceServicesSyncModel() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getReference_id() {
		return reference_id;
	}

	public void setReference_id(Integer reference_id) {
		this.reference_id = reference_id;
	}

	public Integer getService_id() {
		return service_id;
	}

	public void setService_id(Integer service_id) {
		this.service_id = service_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate_created() {
		return date_created;
	}

	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String get_status() {
		return _status;
	}

	public void set_status(String _status) {
		this._status = _status;
	}

	public String get_changed() {
		return _changed;
	}

	public void set_changed(String _changed) {
		this._changed = _changed;
	}

	public String getOnline_id() {
		return online_id;
	}

	public void setOnline_id(String online_id) {
		this.online_id = online_id;
	}
}