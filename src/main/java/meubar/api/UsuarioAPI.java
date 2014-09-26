package meubar.api;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ws.rs.CookieParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import meubar.api.base.BaseAPIImpl;
import meubar.aspects.Permissoes;
import meubar.cadastro.json.pojo.UsuarioJson;
import meubar.cadastro.servico.ServicoUsuario;
import meubar.json.pojo.Messagem;

import com.google.gson.Gson;
 

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioAPI extends BaseAPIImpl {

	@EJB
	ServicoUsuario servicoUsuario;
 
    public UsuarioAPI() {
    }

	@PermitAll
	@OPTIONS
	public Response doOptions() {
		return Response.status(Status.OK).build();
	};

	@Permissoes(values = { "Administrador", "Gerente" })
	@GET
	public Response doGet(@CookieParam("auth_token") String token) {
		List<UsuarioJson> list = servicoUsuario.getAll();
		Gson gson = new Gson();
		String result = gson.toJson(list);
		return Response.status(Status.OK).entity(result).build();
	}

	@Permissoes(values = { "Administrador", "Gerente" })
	@GET
	@Path("/{id: [0-9]*}")
	public Response doGet(@CookieParam("auth_token") String token,
			@PathParam("id") String id) {
		UsuarioJson usuario = servicoUsuario.getById(id);
		Gson gson = new Gson();
		String result = gson.toJson(usuario);
		return Response.status(Status.OK).entity(result).build();
	}

	@Permissoes(values = { "Administrador", "Gerente" })
	@POST
	public Response doPost(@CookieParam("auth_token") String token, String json) {
		Object result;
		Gson gson = new Gson();
		
		try {

			UsuarioJson usuarioJson = gson.fromJson(json, UsuarioJson.class);
			usuarioJson.setUsuarioId(getUsuarioIdFromToken(token));
			result = servicoUsuario.cadastrar(usuarioJson);

		} catch (Exception e) {
			result = new Messagem(e.getMessage());
		}
		
		return Response.status(Status.ACCEPTED).entity(result).build();
	}

	@Permissoes(values = { "Administrador", "Gerente" })
	@DELETE
	@Path("/{id: [0-9]*}")
	public Response doDelete(@CookieParam("auth_token") String token,
			@PathParam("id") String id) {

		Status result = Status.NOT_FOUND;
		boolean deleted = servicoUsuario.deletar(id);
		if (deleted) {
			result = Status.ACCEPTED;
		}

		return Response.status(result).build();
	}

	@Permissoes(values = { "Administrador", "Gerente" })
	@PUT
	@Path("/{id: [0-9]*}")
	public Response doPut(@CookieParam("auth_token") String token,
			@PathParam("id") String id, String json) {
		Object resultObj = null;
		Status result = Status.NOT_FOUND;
		boolean updated = false;
		Gson gson = new Gson();

		try {

			UsuarioJson usuarioJson = gson.fromJson(json, UsuarioJson.class);
			usuarioJson.setUsuarioId(getUsuarioIdFromToken(token));
			updated = servicoUsuario.update(id, usuarioJson);

		} catch (Exception e) {
			resultObj = new Messagem(e.getMessage());
		}

		if (updated) {
			result = Status.ACCEPTED;
		}

		return Response.status(result).entity(resultObj).build();
	}
}