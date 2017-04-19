package com.orange.weather.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

import javax.servlet.Filter;

import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNull;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.orange.weather.enums.TempCategory;
import com.orange.weather.model.DailyNote;
import com.orange.weather.model.User;
import com.orange.weather.service.UserService;
import com.orange.weather.service.WeatherService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:appconfig-root.xml" })
@WebAppConfiguration
public class WeatherTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	private WeatherService weatherService;

	@Autowired
	private UserService userService;

	@Autowired
	private WebApplicationContext ctx;

	private MockMvc mockMvc;

	@Autowired
	private Filter springSecurityFilterChain;
	
	private final String SECURED_URI = "/";
	
	private final String LOGIN_PAGE_URL = "http://localhost/login";

	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
				.addFilters(springSecurityFilterChain)
				.build();
	}

	@Test
	/**
	** Test anonymous user
	**/
	public void itShouldDenyAnonymousAccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(SECURED_URI)).andExpect(MockMvcResultMatchers.redirectedUrl(LOGIN_PAGE_URL));
	}

	
	@Test
	/**
	** Test authorized user
	**/
	public void itShouldAllowAccessToSecuredPage() throws Exception {

		Authentication authentication =
                new UsernamePasswordAuthenticationToken("admin@orange.com", "admin");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext);

        mockMvc.perform(MockMvcRequestBuilders.get(SECURED_URI).session(session))
                .andExpect(MockMvcResultMatchers.status().isOk());
	}

	
	@Test
	@Transactional
	/**
	** Test weather api call
	**/
	public void testGetWeather() {
		assertThat(weatherService, instanceOf(WeatherService.class));

		assertThat(weatherService.getCurrentTemp(), is(Integer.class));
	}
	  
	@Test
	@Transactional
	@Rollback(true)
	/**
	** Test register new user and check if log in success
	**/
	public void testUser() throws Exception {
		assertThat(userService, instanceOf(UserService.class));
		
		User user = new User();
		user.setEmail("test@test.com");
		user.setPassword("test");
		user.setName("Test User");
		user.setMobile("01221231234");
		user.setEnabled(true);
		
		userService.save(user);
		
		Authentication authentication =
                new UsernamePasswordAuthenticationToken("test@test.com", "test");
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext);

        mockMvc.perform(MockMvcRequestBuilders.get(SECURED_URI).session(session))
                .andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Test
	@Transactional
	@Rollback(true)
	/**
	** Test save new daily note
	**/
	public void testDailyNote() throws Exception {
		assertThat(weatherService, instanceOf(WeatherService.class));
		
		DateTime date = new DateTime();
		date = date.yearOfEra().setCopy(1992);
		DateTime startDate = date.millisOfDay().setCopy(3600000);
		DateTime endDate = date.hourOfDay().setCopy(23);
		endDate = endDate.minuteOfHour().setCopy(59);
		endDate = endDate.secondOfMinute().setCopy(59);
		
		DailyNote dailyNote = new DailyNote();
		dailyNote.setDate(startDate.toDate());
		dailyNote.setCategory(TempCategory.TEMP_1_10);
		dailyNote.setNotes("test note");

		this.weatherService.saveDailyNote(dailyNote);
		
		dailyNote = (DailyNote)weatherService.findNoteList(startDate.toDate(), endDate.toDate(), TempCategory.TEMP_1_10).get(0);
		
		assertThat(dailyNote.getNotes(),
				IsEqual.equalTo("test note"));
	}
}
