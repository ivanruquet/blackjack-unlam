package com.tallerwebi.dominio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioTerminosImpl implements ServicioTerminos {

    private final RepositorioUsuario repositorioUsuario;

    @Autowired
    public ServicioTerminosImpl(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public boolean verificarTerminosAceptados(String email) {
        Usuario usuario = repositorioUsuario.buscar(email);
        return usuario != null && Boolean.TRUE.equals(usuario.getTerminosAceptados());
    }

    @Override
    public void aceptarTerminos(String email) {
        Usuario usuario = repositorioUsuario.buscar(email);
        if (usuario != null) {
            usuario.setTerminosAceptados(true);
            repositorioUsuario.modificar(usuario);
        }
    }
}
