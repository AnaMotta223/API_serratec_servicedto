package org.serratec.backend.projeto_service_dto.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Usuario implements UserDetails, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long id;

	private String nome;

	private String email;

	private String senha;
	
	//mappedby mostra o caminho ate chegar no usuarip - id -> usuario
	//EAGER mostra a lista preenchida quando buscar
	@OneToMany(mappedBy = "id.usuario", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<UsuarioPerfil> usuarioPerfis = new HashSet<>();

	public Usuario() {
	}

	public Usuario(Long id, String nome, String email, String senha) {
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.senha = senha;
	}
	
	public Set<UsuarioPerfil> getUsuarioPerfis() {
		return usuarioPerfis;
	}

	public void setUsuarioPerfis(Set<UsuarioPerfil> usuarioPerfis) {
		this.usuarioPerfis = usuarioPerfis;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return Objects.equals(id, other.id);
	}

	
	//qualquer tipo de classe herda de grantedauthority
	//grantedauthority sao permissoes do usuario (perfil no caso)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		//para cada perfil em UsuarioPerfil pega os perfis
		for(UsuarioPerfil usuarioPerfil : usuarioPerfis) {
			//pega a chave composta id (Perfil e Usuario) - a partir do id pega o perfil (classe Perfil) e dai pega o nome do perfil
			authorities.add(new SimpleGrantedAuthority(usuarioPerfil.getId().getPerfil().getNome()));
		}
		
		return authorities;
	}

	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	//pega o nome do perfil de cada usuario e os separa por ,
	//alem de mostrar as informacoes basicas
	@Override
	public String toString() {
		return "* Código: " + id +
				"\n* Nome: " + nome + 
				"\n* Email: " + email + 
				"\n* Perfis: " + 
				usuarioPerfis
					.stream()
					.map(up -> up.getId().getPerfil().getNome())
					.collect(Collectors.joining(", "));
	}

}