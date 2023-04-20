package com.example;

import java.net.URI;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.server.mvc.Template;

import com.example.model.LoginUser;
import com.example.model.MessageDTO;
import com.example.model.MessagesDAO;
import com.example.model.UserDTO;
import com.example.model.UsersDAO;

@Path("/")
public class MyResources {
	@Inject
	private MessagesDAO messageDAO;

	@Inject
	private UsersDAO usersDAO;

	@Inject
	private LoginUser loginUser;

	@GET
	@Path("")
	@Template(name = "/index")
	public String home() {
		return "";
	}

	@GET
	@Path("login")
	@Template(name = "/login")
	public String getLogin() {
		loginUser.setName(null);
		return "";
	}

	@POST
	@Path("login")
	@Template(name = "/login")
	public String postLogin(@BeanParam UserDTO userDTO) {
		if (userDTO.getName().equals("kcg") && userDTO.getPassword().equals("foo")) {
			// login.jsp の中で条件分岐してlistへリダイレクトします。
			loginUser.setName(userDTO.getName());
			throw new RedirectException("list");
		}
		return "ユーザ名またはパスワードが異なります";
	}

	@GET
	@Path("list")
	@Template(name = "/message")
	public String getMessage() {
		if (loginUser.getName() == null) {
			// 認証に成功していない場合は、loginへリダイレクト
			throw new RedirectException("login");
		}
		messageDAO.getAll();
		return "";
	}

	@POST
	@Path("list")
	@Template(name = "/message")
	public String postMessage(@BeanParam MessageDTO mes) {
		mes.setName(loginUser.getName());
		messageDAO.create(mes);
		messageDAO.getAll();
		return "";
	}

	@GET
	@Path("clear")
	public void clearMessage() {
		messageDAO.deleteAll();
		throw new RedirectException("list");
	}

	@lombok.Getter
	@lombok.Setter
	@lombok.AllArgsConstructor
	public static class RedirectException extends RuntimeException {
		private String redirectTo;
	}

	@Provider
	public static class RedirectExceptionMapper implements ExceptionMapper<RedirectException> {
		@Override
		public Response toResponse(RedirectException exception) {
			return Response.seeOther(URI.create(exception.redirectTo)).build();
		}
	}
	

	@POST
	@Path("search")
	@Template(name = "/message")
	public String postSearch(@FormParam("keyword") String keyword) {
		messageDAO.search(keyword);
		return "";
	}

	@GET
	@Path("users")
	@Template(name = "/users")
	public String getUsers() {
		usersDAO.getAll();
		return "";
	}

	@POST
	@Path("users")
	@Template(name = "/users")
	public String postMessage(@BeanParam UserDTO user) {
		usersDAO.create(user);
		usersDAO.getAll();
		return "";
	}

}
