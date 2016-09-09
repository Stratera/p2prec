package com.strateratech.dhs.peerrate.entity;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @author myoung Entity for department table
 *
 */
@Entity
@Table(name = "department")
public class Department {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_id_seq")
    @SequenceGenerator(name = "department_id_seq", sequenceName = "department_id_seq", initialValue = 1, allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name; // VARCHAR(20)

    @Column(name = "version")
    private Integer version;

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

    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "department", targetEntity = Recognition.class)
    private Set<Recognition> recognitions;

    @OrderBy("id")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "department", targetEntity = UserProfile.class)
    private Set<UserProfile> userProfiles;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
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

    /**
     * @return the recognitions
     */
    public Set<Recognition> getRecognitions() {
        if (recognitions == null) {
            recognitions = new TreeSet<>();
        }
        return recognitions;
    }

    /**
     * @param recognitions
     *            the recognitions to set
     */
    public void setRecognitions(Set<Recognition> recognitions) {
        this.recognitions = recognitions;
    }

    /**
     * @return the userProfiles
     */
    public Set<UserProfile> getUserProfiles() {
        if (userProfiles == null) {
            userProfiles = new TreeSet<>();
        }
        return userProfiles;
    }

    /**
     * @param userProfiles
     *            the userProfiles to set
     */
    public void setUserProfiles(Set<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

}
