package org.serratec.backend.projeto_service_dto.service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.serratec.backend.projeto_service_dto.config.MailConfig;
import org.serratec.backend.projeto_service_dto.domain.Perfil;
import org.serratec.backend.projeto_service_dto.domain.Usuario;
import org.serratec.backend.projeto_service_dto.domain.UsuarioPerfil;
import org.serratec.backend.projeto_service_dto.dto.UsuarioDTO;
import org.serratec.backend.projeto_service_dto.dto.UsuarioInserirDTO;
import org.serratec.backend.projeto_service_dto.exception.EmailException;
import org.serratec.backend.projeto_service_dto.exception.SenhaException;
import org.serratec.backend.projeto_service_dto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private PerfilService perfilService;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Autowired
	private MailConfig mailConfig;
	
	public List<UsuarioDTO> findAll(){
		List<Usuario> usuarios = usuarioRepository.findAll();
		
		//cria uma lista vazia para converter os usuarios em dto
//		List<UsuarioDTO> usuariosDTO = new ArrayList<>();
//		for(Usuario usuario: usuarios) {
//			usuariosDTO.add(new UsuarioDTO(usuario));
//		}
		
		List<UsuarioDTO> usuariosDTO = usuarios.stream().map(UsuarioDTO::new).toList();
		
		return usuariosDTO;	
	}
	
	public Optional<Usuario> buscar(Long id) {
		return usuarioRepository.findById(id);
	}
	
	@Transactional
	public UsuarioDTO inserir(UsuarioInserirDTO usuarioInserir) throws EmailException, SenhaException {
		if(!usuarioInserir.getSenha().equals(usuarioInserir.getConfirmaSenha())) {
			throw new SenhaException("Senha e confirmaSenha não são iguais");
		} 
		
		if(usuarioRepository.findByEmail(usuarioInserir.getEmail()) != null) {
			throw new EmailException("Email já existente");
		}
		
		Usuario usuario = new Usuario();
		usuario.setNome(usuarioInserir.getNome());
		usuario.setEmail(usuarioInserir.getEmail());
		//criptografa a senha do usuario
		usuario.setSenha(encoder.encode(usuarioInserir.getSenha()));
		
		//optional
		Set<UsuarioPerfil> perfis = new HashSet<>();
		for(Perfil perfil : usuarioInserir.getPerfis()) {
			perfil = perfilService.buscar(perfil.getId());
			UsuarioPerfil usuarioPerfil = new UsuarioPerfil(usuario, perfil, LocalDate.now());
			perfis.add(usuarioPerfil);
		}
		
		usuario.setUsuarioPerfis(perfis);
		
		usuario = usuarioRepository.save(usuario);
		
		mailConfig.sendEmail(usuario.getEmail(), "Cadastro de usuario", usuario.toString());
		
		return new UsuarioDTO(usuario);
	}
}
