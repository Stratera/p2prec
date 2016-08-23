package com.strateratech.dhs.peerrate.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "user_profile")
public class UserProfile {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_profile_id_seq")
    @SequenceGenerator(name = "user_profile_id_seq", sequenceName = "user_profile_id_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "full_name")
    private String fullName; // VARCHAR(500)

    @NotNull
    @Column(name = "email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$")
    private String email; // VARCHAR(320)

    @Column(name = "version")
    private Integer version; 
    
    @Column(name="job_title")
    private String jobTitle;
    
    @Column(name = "date_of_birth")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date dateOfBirth;
    
    @Column(name="office_street_address")
    private String officeStreetAddress;
    
    @Column(name="office_city")
    private String officeCity;
    @Column(name="office_state_or_prov")
    private String officeStateOrProv;

    @Column(name="office_postal_code")
    private String officePostalCode;

    @Column(name="office_country")
    private String officeCountry;
    
    @Column(name="office_phone")
    private String officePhone;
    
    @Column(name="personal_phone")
    private String personalPhone;
   
    @Column(name="description", columnDefinition="TEXT")
    private String description;
    
    @Basic(fetch = FetchType.LAZY)
    @Column(name="profile_pic")
    private byte[] profilePic;
    

    @Column(name="profile_pic_content_type")
    private String profilePicContentType;

    
    @Column(name = "create_username")
    @CreatedBy
    private String createUsername; // VARCHAR(320)

    @CreatedDate
    @Column(name = "create_ts")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date createTs;

    @LastModifiedBy
    @Column(name = "update_username")
    private String updateUsername; // VARCHAR(320)

    @LastModifiedDate
    @Column(name = "update_ts")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date updateTs;


    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Department.class)
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;

    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sendingUserProfile", targetEntity = Recognition.class)
    private Set<Recognition> mySentRecognitions;


    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "recipientUserProfile", targetEntity = Recognition.class)
    private Set<Recognition> myRecievedRecognitions;


    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }


    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }


    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }


    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }


    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }


    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }


    /**
     * @return the jobTitle
     */
    public String getJobTitle() {
        return jobTitle;
    }


    /**
     * @param jobTitle the jobTitle to set
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }


    /**
     * @return the dateOfBirth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }


    /**
     * @param dateOfBirth the dateOfBirth to set
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    /**
     * @return the officeStreetAddress
     */
    public String getOfficeStreetAddress() {
        return officeStreetAddress;
    }


    /**
     * @param officeStreetAddress the officeStreetAddress to set
     */
    public void setOfficeStreetAddress(String officeStreetAddress) {
        this.officeStreetAddress = officeStreetAddress;
    }


    /**
     * @return the officeCity
     */
    public String getOfficeCity() {
        return officeCity;
    }


    /**
     * @param officeCity the officeCity to set
     */
    public void setOfficeCity(String officeCity) {
        this.officeCity = officeCity;
    }


    /**
     * @return the officeStateOrProv
     */
    public String getOfficeStateOrProv() {
        return officeStateOrProv;
    }


    /**
     * @param officeStateOrProv the officeStateOrProv to set
     */
    public void setOfficeStateOrProv(String officeStateOrProv) {
        this.officeStateOrProv = officeStateOrProv;
    }


    /**
     * @return the officePostalCode
     */
    public String getOfficePostalCode() {
        return officePostalCode;
    }


    /**
     * @param officePostalCode the officePostalCode to set
     */
    public void setOfficePostalCode(String officePostalCode) {
        this.officePostalCode = officePostalCode;
    }


    /**
     * @return the officeCountry
     */
    public String getOfficeCountry() {
        return officeCountry;
    }


    /**
     * @param officeCountry the officeCountry to set
     */
    public void setOfficeCountry(String officeCountry) {
        this.officeCountry = officeCountry;
    }


    /**
     * @return the officePhone
     */
    public String getOfficePhone() {
        return officePhone;
    }


    /**
     * @param officePhone the officePhone to set
     */
    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }


    /**
     * @return the personalPhone
     */
    public String getPersonalPhone() {
        return personalPhone;
    }


    /**
     * @param personalPhone the personalPhone to set
     */
    public void setPersonalPhone(String personalPhone) {
        this.personalPhone = personalPhone;
    }


    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }


    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * @return the profilePic
     */
    public byte[] getProfilePic() {
        return profilePic;
    }


    /**
     * @param profilePic the profilePic to set
     */
    public void setProfilePic(byte[] profilePic) {
        this.profilePic = profilePic;
    }


    /**
     * @return the createUsername
     */
    public String getCreateUsername() {
        return createUsername;
    }


    /**
     * @param createUsername the createUsername to set
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
     * @param createTs the createTs to set
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
     * @param updateUsername the updateUsername to set
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
     * @param updateTs the updateTs to set
     */
    public void setUpdateTs(Date updateTs) {
        this.updateTs = updateTs;
    }


    /**
     * @return the department
     */
    public Department getDepartment() {
        return department;
    }


    /**
     * @param department the department to set
     */
    public void setDepartment(Department department) {
        this.department = department;
    }


    /**
     * @return the mySentRecognitions
     */
    public Set<Recognition> getMySentRecognitions() {
        return mySentRecognitions;
    }


    /**
     * @param mySentRecognitions the mySentRecognitions to set
     */
    public void setMySentRecognitions(Set<Recognition> mySentRecognitions) {
        this.mySentRecognitions = mySentRecognitions;
    }


    /**
     * @return the myRecievedRecognitions
     */
    public Set<Recognition> getMyRecievedRecognitions() {
        return myRecievedRecognitions;
    }


    /**
     * @param myRecievedRecognitions the myRecievedRecognitions to set
     */
    public void setMyRecievedRecognitions(Set<Recognition> myRecievedRecognitions) {
        this.myRecievedRecognitions = myRecievedRecognitions;
    }


    /**
     * @return the profilePicContentType
     */
    public String getProfilePicContentType() {
        return profilePicContentType;
    }


    /**
     * @param profilePicContentType the profilePicContentType to set
     */
    public void setProfilePicContentType(String profilePicContentType) {
        this.profilePicContentType = profilePicContentType;
    }
    
    
    
    
}
