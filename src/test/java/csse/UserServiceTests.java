package csse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import csse.users.ApplicationUser;
import csse.users.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = {"test"})
public class UserServiceTests {
	
	@Autowired
	UserService service;
	
	//ApplicationUser testUser;
	
	private Logger logger = LoggerFactory.getLogger(UserServiceTests.class);
	private List<ApplicationUser> testUserList = new ArrayList<>();
    private ApplicationUser testAdmin;
    private ApplicationUser testUser;
	
    @Before
    public void setUp() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<ApplicationUser> typeReference = new TypeReference<ApplicationUser>() {};
        InputStream adminInput = TypeReference.class.getResourceAsStream("/json/adminUser.json");
        testAdmin  = mapper.readValue(adminInput, typeReference);
        logger.info("loaded create Admin User");

        InputStream userInput = TypeReference.class.getResourceAsStream("/json/user.json");
        testUser = mapper.readValue(userInput, typeReference);
        logger.info("loaded create User type User");
    }
    
    @After
    public void tearDown() {
        service.cleanDatabase();
        logger.info("database renewed");
    }
    
    @Test
    public void setsUserIdOnSaveTest() throws Exception {
        logger.info("Running setsUserIdOnSave");
        String savedUser = service.register(testAdmin);
        Assert.assertNotNull("After creation UserID should not be null", testAdmin.get_id());
    }
    
//    @Test
//    public void getAllUsersTest() throws Exception {
//        logger.info("Running setsUserIdOnSave");
//        //String savedUser1 = service.register(testAdmin);
//        String savedAdmin1 = service.register(testUser);
//
//        List<ApplicationUser> list = service.all();
//        Assert.assertEquals("user size should be 2", 2, list.size());
//
//    }
    
//    @Test
//    public void getUserByUsernameTest() throws Exception {
//        logger.info("Running getUserByUsername");
//        ApplicationUser user = service.findByUsername("Mathew99");
//        
//        Assert.assertEquals("user's Address Attribute shud be Negombo", "Negombo", user.getAddress());
//
//    }
    
//    @Test
//    public void getUserByEmpIDTest() throws Exception {
//        logger.info("Running getUserByEmpID");
//        ApplicationUser user = service.findByemp("A5099");
//        
//        Assert.assertEquals("user's Address Attribute shud be Negombo", "Negombo", user.getAddress());
//
//    }
   
//   @Test
//  public void getUserByEmpIDFailTest() throws Exception {
//      logger.info("Running fail getUserByEmpID");
//      ApplicationUser user = service.findByemp("A5088");
//      
//      Assert.assertEquals("user shud be null", null, user);
//
//  }
//    
//    @Test
//    public void resetPasswordPassedTest() throws Exception {
//        logger.info("Running pass resetPassword");
//        String response = service.resetPassword("Sam99", "Sam99", "sam99", "sam99");       
//        Assert.assertEquals("user's pwd is reset", "Password reset successfully", response);
//
//    }
    
//  @Test
//  public void resetPasswordFailTest() throws Exception {
//      logger.info("Running fail resetPassword");
//      String response = service.resetPassword("Sam99", "Sam99", "sam88", "sam99");       
//      Assert.assertEquals("user's pwd is not reset", "The new passwords don't match", response);
//
//  }
    
//  @Test
//  public void resetPasswordFailTest() throws Exception {
//      logger.info("Running fail resetPassword");
//      String response = service.resetPassword("Sam99", "Sam9999", "sam99", "sam99");       
//      Assert.assertEquals("user's pwd is not reset", "Current password is incorrect!", response);
//
//  }
    
//
//    @Test
//    public void forgotPasswordPassTest() throws Exception {
//        logger.info("Running pass forgotPassword");
//        String response = service.forgotPassword("Mathew99", "math", "math");
//        Assert.assertEquals("user's pwd is reset", "Password reset successfully", response);
//
//    }
    
//    @Test
//  public void forgotPasswordFailTest() throws Exception {
//      logger.info("Running fail forgotPassword");
//      String response = service.forgotPassword("Mathew99", "math123", "math");
//      Assert.assertEquals("user's pwd is not reset", "Password reset failed", response);
//
//  }
    
//    
//    @Test
//    public void editProfileTest() throws Exception{
//    	logger.info("Running editProfile");
//    	ApplicationUser user = service.editProfile("Mathew99", "math" , "mathtest", "kandy", "0764565789", "math123@gmail.com");       
//        Assert.assertEquals("user's profile is reset","math123@gmail.com" , user.getEmail());
//    }
//    
//    @Test
//    public void deactivatePassTest() throws Exception{
//    	logger.info("Running pass deactivate");
//    	String response = service.deactivate("A5001");       
//        Assert.assertEquals("user is deleted","Successfully deactivated" ,response);
//    }
//    
    
//   @Test
//  public void deactivateFailTest() throws Exception{
//  	logger.info("Running fail deactivate");
//  	String response = service.deactivate("A9001");       
//      Assert.assertEquals("user is not deleted","User doesn't exist" ,response);
//  }
}
