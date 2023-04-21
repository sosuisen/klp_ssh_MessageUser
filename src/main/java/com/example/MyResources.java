package com.example;

import java.net.URI;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
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

@ApplicationScoped
@Path("/")
public class MyResources {
	@Inject
	private MessagesDAO messageDAO;

	@Inject
	private UsersDAO usersDAO;

	@Inject
	private LoginUser loginUser;

	@Inject
	private Pbkdf2PasswordHash passwordHash;

	private Map<String, String> HASH_PARAMS = Map.of(
			"Pbkdf2PasswordHash.Iterations", "10000",
			"Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512",
			"Pbkdf2PasswordHash.SaltSizeBytes", "128");

	@PostConstruct
	public void prepare() {
		// @Inject した passwordHash が生成されるのは、コンストラクタが呼ばれた後なので、
		// コンストラクタではなく、@PostConstruct というアノテーションのついたメソッドで初期化します。
		passwordHash.initialize(HASH_PARAMS);
	}

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
		UserDTO user = usersDAO.get(userDTO.getName());
		if (user == null) return "ユーザ名またはパスワードが異なります";
		
		if(passwordHash.verify(userDTO.getPassword().toCharArray(), user.getPassword())) {
			// パスワードは正しい
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
		var hash = passwordHash.generate(user.getPassword().toCharArray());
		user.setPassword(hash);
		
		usersDAO.create(user);
		usersDAO.getAll();
		return "";
	}

	@POST
	@Path("user_delete")
	@Template(name = "/users")
	public String deleteMessage(@FormParam("name") String name) {
		usersDAO.delete(name);
		throw new RedirectException("users");
	}
}
