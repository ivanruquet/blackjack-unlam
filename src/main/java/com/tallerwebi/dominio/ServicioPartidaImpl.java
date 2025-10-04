package com.tallerwebi.dominio;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioPartidaImpl implements ServicioPartida {
    Partida partida;

    @Override
    public Boolean crearPartida() {
        Partida partida = new Partida();
        partida.cambiarEstadoDeLaPartida(EstadoPartida.APUESTA);
        partida.setApuesta(0);
        this.partida = partida;
        //y la deberia guardar en el repositorio
        if(partida!=null){
            return true;
        }
        return false;
    }


    public Partida getPartida(){
        return this.partida;
    }




}
