package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PartidaExistenteException;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;
import net.bytebuddy.implementation.bytecode.Throw;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioPartidaImpl implements ServicioPartida {

    private RepositorioPartida respositorioPartida;

    @Autowired
    public ServicioPartidaImpl(RepositorioPartida respositorioPartida){
        this.respositorioPartida=respositorioPartida;

    }

    @Override
    public void crearPartida(){
        try {
            Partida p = buscarPartida();
        }catch (PartidaExistenteException e){
            //Si existe una partida que se encuentra en estadoPartida.APUESTA, la borro
            borrarPartida(respositorioPartida.buscarPartida());
        }
        //instancio la partida
        Partida partidaNueva = getPartidaNueva();
        //la guardo en el repo de Partida
        guardarPartida(partidaNueva);
    }
    @Override
    public Partida getPartidaNueva() {
        Partida partidaNueva = new Partida();
        partidaNueva.cambiarEstadoDeLaPartida(EstadoPartida.APUESTA);
        partidaNueva.setApuesta(0);
        return partidaNueva;
    }

    public void borrarPartida(Partida partidaBorrada) {
        respositorioPartida.borrarPartida(partidaBorrada);
    }


    @Override
    public void guardarPartida(Partida p) {
        respositorioPartida.guardar(p);
    }

    @Override
    public Partida buscarPartida() throws PartidaExistenteException {
        if(respositorioPartida.buscarPartida() != null){
            throw new PartidaExistenteException();
        }
        return null;
    }

}
