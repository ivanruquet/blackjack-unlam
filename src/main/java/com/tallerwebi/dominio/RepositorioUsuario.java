package com.tallerwebi.dominio;

public interface RepositorioUsuario {

    Usuario buscarUsuario(String email, String password);
    void guardar(Usuario usuario);
    Usuario buscar(String email);
    void guardarModificaciones(Usuario usuario);
    void actualizar(Usuario usuario);
    Usuario buscarPorId(Long userId);


}

