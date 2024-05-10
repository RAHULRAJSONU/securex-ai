package io.github.rahulrajsonu.securexai.data.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Setter
@Getter
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "auth_client")
public class Client extends AbstractPersistable{
	@Column(unique = true, nullable = false, updatable = false)
	private String clientId;
	private String clientSecret;
	@Column(unique = true)
	private String clientName;
	@OneToOne(cascade = CascadeType.ALL)
	@MapsId
	private AuthProvider authProvider;
}