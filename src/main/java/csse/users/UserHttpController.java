package csse.users;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserHttpController {

	private UserService service;
	
	@Autowired
	public UserHttpController(UserService service) {
		this.service = service;
	}
	
	@PostMapping("/signup")
    public String signUp(@RequestBody ApplicationUser user) {
        return service.register(user);
    }
	
	
	@GetMapping("/list")
    public List<ApplicationUser> list() {
        return service.all();
    }

    @GetMapping("/details/{username}")
    ApplicationUser details(@PathVariable(value="username")String username) {
        return service.findByUsername(username);
    }
    
    
    @GetMapping("/search/{EID}")
    ApplicationUser EID(@PathVariable(value="EID")String eid) {
       return service.findByemp(eid);
    }    
        
    @PatchMapping("/update/{username}")
    ApplicationUser update(@PathVariable(value="username")String u, @RequestBody Map<String, String> body) {
    	String username = u;
    	
    	String fn=body.get("firstname");
    	String ln=body.get("lastname");
    	String ad=body.get("address");
    	String con=body.get("phone");
    	String email=body.get("email");
    	
    	return service.editProfile(username, fn, ln, ad, con, email);
    }
    
    @DeleteMapping("/deactivate")
    public void deactivate(@RequestBody List<ApplicationUser> users) {
    	service.deactivate(users);
    }
    
    
    @PatchMapping("/resetpassword/{username}")
    public String resetPassword(@PathVariable(value="username") String username, @RequestBody Map<String, String> body) {
    	String newp=body.get("new");
    	String currentp=body.get("current");
    	String confirmp=body.get("confirm");
    	return service.resetPassword(username, currentp, newp, confirmp);
    }
    
    
    @PatchMapping("/forgotpassword/{username}")
    public String forgotPassword(@PathVariable(value="username") String username, @RequestBody Map<String, String> body) {
    	String np=body.get("new");
    	String confirmp=body.get("confirm");
    	return service.forgotPassword(username, np, confirmp);
    }
}