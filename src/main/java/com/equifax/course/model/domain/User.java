package com.equifax.course.model.domain;
// Generated Mar 23, 2020, 12:39:21 AM by Hibernate Tools 5.2.12.Final

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Users generated by hbm2java
 */
@Entity
@Table(name = "Users", catalog = "equifax")
public class User implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Integer idUser;
	private String username;
	private String password;
	private Byte enabled;
	private List<UserRole> userRoles = new ArrayList<UserRole>();

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id_user", unique = true, nullable = false)
	public Integer getId()
	{
		return this.idUser;
	}

	public void setId(Integer idUser)
	{
		this.idUser = idUser;
	}

	@Column(name = "username", unique = true, nullable = false, length = 32)
	public String getUsername()
	{
		return this.username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	@Column(name = "password", nullable = false)
	public String getPassword()
	{
		return this.password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	/**
	 * @deprecated Internal use, use getEnabledView() instead.
	 */
	@Deprecated
	@Column(name = "enabled", nullable = false, insertable = false)
	public Byte getEnabled()
	{
		return this.enabled;
	}

	/**
	 * @deprecated Internal use, use setEnabledView(Boolean enabled) instead.
	 */
	@Deprecated
	public void setEnabled(Byte enabled)
	{
		this.enabled = enabled;
	}

	@Transient
	public boolean getEnabledView()
	{
		return this.enabled != null && this.enabled.equals((byte) 1);
	}

	@Transient
	public void setEnabledView(boolean enabled)
	{
		this.enabled = enabled ? (byte) 1 : (byte) 0;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public List<UserRole> getUserRoles()
	{
		return this.userRoles;
	}

	public void setUserRoles(List<UserRole> userRoles)
	{
		this.userRoles = userRoles;
	}
}
