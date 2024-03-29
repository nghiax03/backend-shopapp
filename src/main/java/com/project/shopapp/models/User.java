package com.project.shopapp.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseEntity implements UserDetails{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "fullname", length = 100)
	private String fullName;
	
	@Column(name = "phone_number", length = 20, nullable = false)
	private String phoneNumber;
	
	@Column(name = "address" ,length = 200)
	private String address;
	
	@Column(name = "password", length = 100, nullable = false)
	private String password;
	
	@Column(name = "is_active")
	private boolean active;
	
	@Column(name = "date_of_birth")
	private Date dateOfBirth;
	
	@Column(name = "facebook_account_id")
	private int facebookAccountId;
	
	@Column(name = "google_account_id")
	private int googleAccountId;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private com.project.shopapp.models.Role role;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority("ROLE_"+getRole().getName().toUpperCase()));
		//authorityList.add(new SimpleGrantedAuthority("ADMIN"));
		return authorityList;
	}

	@Override
	public String getUsername() {
		return phoneNumber;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
}
