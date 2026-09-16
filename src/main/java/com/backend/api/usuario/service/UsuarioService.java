package com.backend.api.usuario.service;

import com.backend.api.usuario.dto.UsuarioRequest;
import com.backend.api.usuario.dto.UsuarioResponse;
import com.backend.api.usuario.entity.Usuario;
import com.backend.api.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioResponse crear(UsuarioRequest request) {

        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();

        Usuario guardado = usuarioRepository.save(usuario);

        return convertirAResponse(guardado);
    }

    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .toList();
    }

    public UsuarioResponse obtenerPorId(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuario no encontrado")
                );

        return convertirAResponse(usuario);
    }
    public UsuarioResponse actualizar(Long id, UsuarioRequest request) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("Usuario no encontrado")
                );

        usuario.setNombre(request.getNombre());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(request.getPassword());

        Usuario actualizado = usuarioRepository.save(usuario);

        return convertirAResponse(actualizado);
    }

    public void eliminar(Long id) {

        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        usuarioRepository.deleteById(id);
    }

    private UsuarioResponse convertirAResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail()
        );
    }
}