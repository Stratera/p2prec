package com.strateratech.dhs.peerrate.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
/**
 *
 * @author myoung
 * entity for recognition table
 */
@Entity
@Table(name = "recognition")
public class Recognition {
	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recognition_id_seq")
	@SequenceGenerator(name = "recognition_id_seq", sequenceName = "recognition_id_seq", initialValue = 1, allocationSize = 1)
	@Column(name = "id")
	private Long id;

	@Version
	@Column(name = "version")
	private Integer version;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Department.class)
	@JoinColumn(name = "recipient_department_id", referencedColumnName = "id")
	private Department department;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = UserProfile.class)
	@JoinColumn(name = "sending_user_profile_id", referencedColumnName = "id")
	private UserProfile sendingUserProfile;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = UserProfile.class)
	@JoinColumn(name = "recipient_user_profile_id", referencedColumnName = "id")
	private UserProfile recipientUserProfile;


    @NotNull
    @Column(name = "submit_ts")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date submitTs;

    @Column(name="message", columnDefinition="text")
    private String message;
    
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "attachment", length = Integer.MAX_VALUE)
    private byte[] attachment;

    @Column(name = "attachment_content_type")
    private String attachmentContentType;

	@NotNull
	@Column(name = "create_username")
	private String createUsername; // VARCHAR(320)

	@NotNull
	@Column(name = "create_ts")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date createTs;

	@NotNull
	@Column(name = "update_username")
	private String updateUsername; // VARCHAR(320)

	@NotNull
	@Column(name = "update_ts")
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date updateTs;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return department;
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(Department department) {
		this.department = department;
	}

	/**
	 * @return the sendingUserProfile
	 */
	public UserProfile getSendingUserProfile() {
		return sendingUserProfile;
	}

	/**
	 * @param sendingUserProfile
	 *            the sendingUserProfile to set
	 */
	public void setSendingUserProfile(UserProfile sendingUserProfile) {
		this.sendingUserProfile = sendingUserProfile;
	}

	/**
	 * @return the recipientUserProfile
	 */
	public UserProfile getRecipientUserProfile() {
		return recipientUserProfile;
	}

	/**
	 * @param recipientUserProfile
	 *            the recipientUserProfile to set
	 */
	public void setRecipientUserProfile(UserProfile recipientUserProfile) {
		this.recipientUserProfile = recipientUserProfile;
	}

	/**
	 * @return the createUsername
	 */
	public String getCreateUsername() {
		return createUsername;
	}

	/**
	 * @param createUsername
	 *            the createUsername to set
	 */
	public void setCreateUsername(String createUsername) {
		this.createUsername = createUsername;
	}

	/**
	 * @return the createTs
	 */
	public Date getCreateTs() {
		return createTs;
	}

	/**
	 * @param createTs
	 *            the createTs to set
	 */
	public void setCreateTs(Date createTs) {
		this.createTs = createTs;
	}

	/**
	 * @return the updateUsername
	 */
	public String getUpdateUsername() {
		return updateUsername;
	}

	/**
	 * @param updateUsername
	 *            the updateUsername to set
	 */
	public void setUpdateUsername(String updateUsername) {
		this.updateUsername = updateUsername;
	}

	/**
	 * @return the updateTs
	 */
	public Date getUpdateTs() {
		return updateTs;
	}

	/**
	 * @param updateTs
	 *            the updateTs to set
	 */
	public void setUpdateTs(Date updateTs) {
		this.updateTs = updateTs;
	}

    public Date getSubmitTs() {
        return submitTs;
    }

    public void setSubmitTs(Date submitTs) {
        this.submitTs = submitTs;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String messageText) {
        this.message = messageText;
    }

    public byte[] getAttachment() {
        return attachment;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentContentType() {
        return attachmentContentType;
    }

    public void setAttachmentContentType(String attachmentContentType) {
        this.attachmentContentType = attachmentContentType;
    }

	
	

}
