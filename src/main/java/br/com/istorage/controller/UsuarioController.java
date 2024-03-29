package br.com.istorage.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.istorage.dto.UsuarioDTO;
import br.com.istorage.dto.UsuarioSenhaDTO;
import br.com.istorage.model.Usuario;
import br.com.istorage.repository.UsuarioRepository;
import br.com.istorage.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	private final PasswordEncoder encoder;

	public UsuarioController(UsuarioService usuarioService, PasswordEncoder encoder) {
		super();
		this.usuarioService = usuarioService;
		this.encoder = encoder;
	}

	@GetMapping
	public ResponseEntity<List<UsuarioDTO>> findAll() {
		List<Usuario> list = this.usuarioService.consultarTodosUsuarios();
		List<UsuarioDTO> listDTO = list.stream().map(objeto -> new UsuarioDTO(objeto)).collect(Collectors.toList());

		return ResponseEntity.ok().body(listDTO);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Usuario> consultarUsuario(@PathVariable int id) {
		Usuario usuario = this.usuarioService.consultarUsuarioId(id);
		return ResponseEntity.ok().body(usuario);
	}

	   @GetMapping("/validarSenha")
	    public ResponseEntity<Boolean> validarSenha(@RequestParam String login,
	                                                @RequestParam String password) {

	        Optional<Usuario> optUsuario = usuarioRepository.findByUsuario(login);
	        if (optUsuario.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
	        }

	        Usuario usuario = optUsuario.get();
	        boolean valid = encoder.matches(password, usuario.getSenha());

	        HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
	        return ResponseEntity.status(status).body(valid);

	    }

	@PostMapping
	public ResponseEntity<Usuario> salvarUsuario(@RequestBody Usuario usuario) {
		usuario.setSenha(encoder.encode(usuario.getSenha()));
		Usuario user = this.usuarioService.salvarUsuario(usuario);
		return ResponseEntity.ok().body(user);
	}

	@PatchMapping(value = "{id}")
	public ResponseEntity<UsuarioDTO> atualizarUsuario(@PathVariable int id, @RequestBody UsuarioSenhaDTO usuario) {
		Usuario user = this.usuarioService.atualizarUsuario(id, usuario);
		UsuarioDTO userDto = user.toDto();
		return ResponseEntity.ok().body(userDto);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable int id) {
		this.usuarioService.delete(id);
	}

}
