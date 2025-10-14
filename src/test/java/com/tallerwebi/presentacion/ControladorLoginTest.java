package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorLoginTest {

	private ControladorLogin controladorLogin;
	private Usuario usuarioMock;
	private DatosLogin datosLoginMock;
	private HttpServletRequest requestMock;
	private HttpSession sessionMock;
	private ServicioLogin servicioLoginMock;


	@BeforeEach
	public void init(){
		datosLoginMock = new DatosLogin("dami@unlam.com", "123");
		usuarioMock = mock(Usuario.class);
		when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
		requestMock = mock(HttpServletRequest.class);
		sessionMock = mock(HttpSession.class);
		servicioLoginMock = mock(ServicioLogin.class);
        controladorLogin = new ControladorLogin(servicioLoginMock);
	}

	@Test
	public void loginConUsuarioYPasswordInorrectosDeberiaLlevarALoginNuevamente(){
		// preparacion
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(null);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario o clave incorrecta"));
	}

    @Test
    public void loginConUsuarioYPasswordCorrectosDeberiaLlevarASala() {
        // preparación
        Usuario usuarioEncontradoMock = mock(Usuario.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(usuarioEncontradoMock);

        // ejecución
        ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

        // validación
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/sala"));
        verify(sessionMock, times(1)).setAttribute("usuario", usuarioEncontradoMock);
    }

    @Test public void registrameSiUsuarioNoExisteDeberiaCrearUsuarioYVolverAlLogin() throws UsuarioExistente {

        // ejecucion
        ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock);
        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(servicioLoginMock, times(1)).registrar(usuarioMock); }

    @Test public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
        // preparacion
        doThrow(UsuarioExistente.class).when(servicioLoginMock).registrar(usuarioMock);
        // ejecucion
        ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock);
        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El usuario ya existe")); }

    @Test public void errorEnRegistrarmeDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {

        // preparacion
        doThrow(RuntimeException.class).when(servicioLoginMock).registrar(usuarioMock);
        // ejecucion
        ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock);
        // validacion
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
        assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al registrar el nuevo usuario"));
    }

    @Test
    public void alRegistrarUsuarioDeberiaGuardarTodosSusAtributos() throws UsuarioExistente {
        Usuario nuevo = new Usuario();
        nuevo.setEmail("nuevo@unlam.com");
        nuevo.setPassword("123");
        nuevo.setNombre("Usr");
        nuevo.setApellido("Usuario");
        nuevo.setUsername("usuario1");
        nuevo.setFechaNacimiento(LocalDate.of(2000, 5, 10));

        ModelAndView mv = controladorLogin.registrarme(nuevo);

        assertThat(mv.getViewName(), equalToIgnoringCase("redirect:/login"));
        verify(servicioLoginMock, times(1)).registrar(nuevo);
    }
}

