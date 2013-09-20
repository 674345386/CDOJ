package cn.edu.uestc.acmicpc.oj.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import cn.edu.uestc.acmicpc.config.MockMVCContext;
import cn.edu.uestc.acmicpc.config.WebMVCConfig;
import cn.edu.uestc.acmicpc.db.dto.impl.UserDTO;
import cn.edu.uestc.acmicpc.db.dto.impl.UserLoginDTO;
import cn.edu.uestc.acmicpc.oj.service.iface.UserService;
import cn.edu.uestc.acmicpc.util.StringUtil;
import cn.edu.uestc.acmicpc.util.exception.AppException;
import cn.edu.uestc.acmicpc.util.exception.FieldException;

import com.alibaba.fastjson.JSON;

/** Mock test for {@link UserController}. */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { MockMVCContext.class, WebMVCConfig.class })
public class UserControllerTest extends ControllerTest {

  private final String URL_LOGIN = "/user/login";
  private final String URL_LOGOUT = "/user/logout";
  private final String URL_REGISTER = "/user/register";

  @Autowired
  private WebApplicationContext context;
  @Autowired
  private UserService userService;

  private MockMvc mockMvc;
  private MockHttpSession session;

  @Before
  public void init() {
    Mockito.reset(userService);
    mockMvc = webAppContextSetup(context).build();
    session = new MockHttpSession(context.getServletContext(), UUID.randomUUID().toString());
  }

  @Test
  public void testLogin_successful() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName("admin")
        .setPassword("password")
        .build();
    UserDTO userDTO = UserDTO.builder()
        .setUserName("admin")
        .setPassword("password")
        .build();
    when(userService.login(Mockito.<UserLoginDTO> any())).thenReturn(userDTO);
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("success")));
    Assert.assertEquals(userDTO, session.getAttribute("currentUser"));
  }

  @Test
  public void testLogin_invalidUserName_null() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName(null)
        .setPassword("password")
        .build();
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("userName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userLoginDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter your user name.")));
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testLogin_invalidUserName_empty() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName("")
        .setPassword("password")
        .build();
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("userName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userLoginDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage",
            is("Please enter 4-24 characters consist of A-Z, a-z, 0-9 and '_'.")));
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testLogin_invalidUserName_tooShort() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName(StringUtil.repeat("a", 3))
        .setPassword("password")
        .build();
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("userName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userLoginDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage",
            is("Please enter 4-24 characters consist of A-Z, a-z, 0-9 and '_'.")));
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testLogin_invalidUserName_tooLong() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName(StringUtil.repeat("a", 25))
        .setPassword("password")
        .build();
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("userName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userLoginDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage",
            is("Please enter 4-24 characters consist of A-Z, a-z, 0-9 and '_'.")));
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testLogin_invalidUserName_invalid() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName("%$#@5%")
        .setPassword("password")
        .build();
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("userName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userLoginDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage",
            is("Please enter 4-24 characters consist of A-Z, a-z, 0-9 and '_'.")));
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testLogin_invalidPassword_null() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName("admin")
        .setPassword(null)
        .build();
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("password")))
        .andExpect(jsonPath("$.field[0].objectName", is("userLoginDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter your password.")));
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testLogin_invalidPassword_empty() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName("admin")
        .setPassword("")
        .build();
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("password")))
        .andExpect(jsonPath("$.field[0].objectName", is("userLoginDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter 6-20 characters.")));
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testLogin_invalidPassword_tooShort() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName("admin")
        .setPassword(StringUtil.repeat("a", 5))
        .build();
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("password")))
        .andExpect(jsonPath("$.field[0].objectName", is("userLoginDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter 6-20 characters.")));
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testLogin_invalidPassword_tooLong() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName("admin")
        .setPassword(StringUtil.repeat("a", 21))
        .build();
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("password")))
        .andExpect(jsonPath("$.field[0].objectName", is("userLoginDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter 6-20 characters.")));
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testLogin_failed_wrongUserNameOrPassword() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName("admin")
        .setPassword("wrongPassword")
        .build();
    when(userService.login(Mockito.<UserLoginDTO> any()))
        .thenThrow(new FieldException("password", "User or password is wrong, please try again"));
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("password")))
        .andExpect(jsonPath("$.field[0].objectName", is("password")))
        .andExpect(jsonPath("$.field[0].defaultMessage",
            is("User or password is wrong, please try again")));
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testLogin_failed_bothUserNameAndPassword_null() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName(null)
        .setPassword(null)
        .build();
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(2)))
        .andExpect(jsonPath("$.field[*].field", containsInAnyOrder("password", "userName")))
        .andExpect(jsonPath("$.field[*].objectName",
            containsInAnyOrder("userLoginDTO", "userLoginDTO")))
        .andExpect(jsonPath("$.field[*].defaultMessage", containsInAnyOrder(
            "Please enter your password.",
            "Please enter your user name.")));
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testLogin_failed_bothUserNameAndPassword_empty() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName("")
        .setPassword("")
        .build();
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(2)))
        .andExpect(jsonPath("$.field[*].field", containsInAnyOrder("password", "userName")))
        .andExpect(jsonPath("$.field[*].objectName",
            containsInAnyOrder("userLoginDTO", "userLoginDTO")))
        .andExpect(jsonPath("$.field[*].defaultMessage", containsInAnyOrder(
            "Please enter 6-20 characters.",
            "Please enter 4-24 characters consist of A-Z, a-z, 0-9 and '_'.")));
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testLogin_failed_serviceError() throws Exception {
    UserLoginDTO userLoginDTO = UserLoginDTO.builder()
        .setUserName("admin")
        .setPassword("password")
        .build();
    when(userService.login(Mockito.<UserLoginDTO> any()))
        .thenThrow(new AppException("service error"));
    mockMvc.perform(post(URL_LOGIN)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userLoginDTO))
        .session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("error")))
        .andExpect(jsonPath("$.error_msg", is("service error")));
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testLogout_successful() throws Exception {
    UserDTO userDTO = UserDTO.builder().build();
    session.putValue("currentUser", userDTO);
    mockMvc.perform(post(URL_LOGOUT)
        .contentType(APPLICATION_JSON_UTF8)
        .session(session))
        .andExpect(status().isOk());
    Assert.assertNull(session.getAttribute("currentUser"));
  }

  @Test
  public void testRegister_successfully() throws Exception {
    UserDTO userDTO = UserDTO.builder().build();
    when(userService.register(userDTO)).thenReturn(userDTO);
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("success")));
  }

  @Test
  public void testRegister_failed_userName_null() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setUserName(null)
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("userName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter your user name.")));
  }

  @Test
  public void testRegister_failed_userName_empty() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setUserName("")
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("userName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage",
            is("Please enter 4-24 characters consist of A-Z, a-z, 0-9 and '_'.")));
  }

  @Test
  public void testRegister_failed_userName_whiteSpaces() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setUserName(StringUtil.repeat(" ", 10))
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("userName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage",
            is("Please enter 4-24 characters consist of A-Z, a-z, 0-9 and '_'.")));
  }

  @Test
  public void testRegister_failed_userName_tooShort() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setUserName(StringUtil.repeat("a", 3))
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("userName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage",
            is("Please enter 4-24 characters consist of A-Z, a-z, 0-9 and '_'.")));
  }

  @Test
  public void testRegister_failed_userName_tooLong() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setUserName(StringUtil.repeat("a", 25))
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("userName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage",
            is("Please enter 4-24 characters consist of A-Z, a-z, 0-9 and '_'.")));
  }

  @Test
  public void testRegister_failed_userName_invalid() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setUserName("#userName")
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("userName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage",
            is("Please enter 4-24 characters consist of A-Z, a-z, 0-9 and '_'.")));
  }

  @Test
  public void testRegister_failed_password_null() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setPassword(null)
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("password")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter your password.")));
  }

  @Test
  public void testRegister_failed_password_empty() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setPassword("")
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("password")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter 6-20 characters.")));
  }

  @Test
  public void testRegister_failed_password_tooShort() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setPassword(StringUtil.repeat("a", 5))
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("password")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter 6-20 characters.")));
  }

  @Test
  public void testRegister_failed_password_tooLong() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setPassword(StringUtil.repeat("a", 21))
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("password")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter 6-20 characters.")));
  }

  @Test
  public void testRegister_failed_passwordRepeat_null() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setPasswordRepeat(null)
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("passwordRepeat")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please repeat your password.")));
  }

  @Test
  public void testRegister_failed_passwordRepeat_empty() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setPasswordRepeat("")
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("passwordRepeat")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter 6-20 characters.")));
  }

  @Test
  public void testRegister_failed_passwordRepeat_tooShort() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setPasswordRepeat(StringUtil.repeat("a", 5))
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("passwordRepeat")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter 6-20 characters.")));
  }

  @Test
  public void testRegister_failed_passwordRepeat_tooLong() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setPasswordRepeat(StringUtil.repeat("a", 21))
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("passwordRepeat")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter 6-20 characters.")));
  }

  @Test
  public void testRegister_failed_nickName_null() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setNickName(null)
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("nickName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter your nick name.")));
  }

  @Test
  public void testRegister_failed_nickName_empty() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setNickName("")
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("nickName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter 2-20 characters.")));
  }

  @Test
  public void testRegister_failed_nickName_whiteSpaces() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setNickName(StringUtil.repeat(" ", 10))
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("nickName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter 2-20 characters.")));
  }

  @Test
  public void testRegister_failed_nickName_tooShort() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setNickName(StringUtil.repeat("a", 1))
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("nickName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter 2-20 characters.")));
  }

  @Test
  public void testRegister_failed_nickName_tooLong() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setNickName(StringUtil.repeat("a", 21))
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("nickName")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage", is("Please enter 2-20 characters.")));
  }

  @Test
  public void testRegister_failed_email_null() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setEmail(null)
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("email")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage",
            is("Please enter a validation email address.")));
  }

  @Test
  public void testRegister_failed_email_invalid() throws Exception {
    UserDTO userDTO = UserDTO.builder()
        .setEmail(null)
        .build();
    mockMvc.perform(post(URL_REGISTER)
        .contentType(APPLICATION_JSON_UTF8)
        .content(JSON.toJSONBytes(userDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.result", is("field_error")))
        .andExpect(jsonPath("$.field", hasSize(1)))
        .andExpect(jsonPath("$.field[0].field", is("email")))
        .andExpect(jsonPath("$.field[0].objectName", is("userDTO")))
        .andExpect(jsonPath("$.field[0].defaultMessage",
            is("Please enter a validation email address.")));
  }
}