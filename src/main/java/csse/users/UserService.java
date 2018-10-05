package csse.users;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private UserDAO repo;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public UserService(UserDAO repo, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.repo = repo;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}	
	
	private List<ApplicationUser> users;
	
	//register
	public String register(ApplicationUser user) {
		
		users=repo.findAll();
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String d=dateFormat.format(date);
		//System.out.println(d); //2016/11/16 12:08:43
		
		if((!user.getAddress().isEmpty()) && (!user.getEmail().isEmpty()) && (!user.getEmp_ID().isEmpty()) && (!user.getEmp_type().isEmpty()) 
				&& (!user.getEmp_type().isEmpty()) && (!user.getFirstname().isEmpty()) && (!user.getLastname().isEmpty()) && (!user.getPassword().isEmpty()) 
				&& (!user.getPhone().isEmpty()) && (!user.getUsername().isEmpty()) && (user.getAuthorities()!=null) && (!user.getAuthorities().isEmpty())) {
		
			String usid=user.getEmp_ID();
			String usemail=user.getEmail();
			String ususn=user.getUsername();		
			
			if(!(users.toString().matches("\\[.*\\b" + usid + "\\b.*]"))) {
				
				if(!(users.toString().matches("\\[.*\\b" + usemail + "\\b.*]"))) {
					
					if(!(users.toString().matches("\\[.*\\b" + ususn + "\\b.*]"))) {
						
						user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
						user.setCreatedDate(d);
						user.setlastLogin("00/00/0000 00:00:00");
						user.setModifiedDate(d);
						repo.save(user);
						
						return user.toString();
							
					} else{ return "Username exists";}
				
				} else{ return "Email exists";}			
				
			} else{ return "EmployeeID exists";}
					
		} else{ return "fill all fields";}
	}
	
	//get all users
	public List<ApplicationUser> all(){
		return repo.findAll();
	}
	
	//get user by username
	//@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public ApplicationUser findByUsername(String username) {
		return repo.findByUsername(username);
	}
	
	//get user by emp_ID
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ApplicationUser findByemp(String ID) {
		users=repo.findAll();
		for(ApplicationUser u: users) {
			if(u.getEmp_ID().equals(ID)) {
				return u;
			}
		}
		return null;	
	}
		
	//reset password through profile
	//@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public String resetPassword(String username, String cpwd, String npwd, String confirm) {
        ApplicationUser u=repo.findByUsername(username);
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String d=dateFormat.format(date);
        		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();  
		  
       // if(u.getPassword().equals(cpwd)) {
		if(encoder.matches(cpwd, u.getPassword())) {
        	if(npwd.equals(confirm)) {
        		u.setPassword(bCryptPasswordEncoder.encode(npwd));
                //u.setPassword(npwd);
				u.setModifiedDate(d);
				
                repo.save(u);
                return "Password reset successfully";
            }
        	return "The new passwords don't match";
        }
        return "Current password is incorrect!";
    }
	
	//reset password through forgot passWord
	//@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public String forgotPassword(String username, String np, String confirm) {
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String d=dateFormat.format(date);
		
		ApplicationUser u= repo.findByUsername(username);
        if(np.equals(confirm)) {
        	
        	u.setPassword(bCryptPasswordEncoder.encode(np));
            
            //u.setPassword(np);
			u.setModifiedDate(d);
			
            repo.save(u);
            return "Password reset successfully";
        }
        return "Password reset failed";
    }
	
	//edit user profile
	//@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	public ApplicationUser editProfile(String username, String firstname, String lastname, String address,
			String phone, String email) {
		users=repo.findAll();
		
		ApplicationUser user = repo.findByUsername(username);
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date date = new Date();
		String d=dateFormat.format(date);
		
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setAddress(address);
		user.setPhone(phone);
		
		if(!(users.toString().matches("\\[.*\\b" + email + "\\b.*]"))) {
			user.setEmail(email);
		}		
		
		user.setModifiedDate(d);
		
		repo.save(user);
		return user;
	}
	
	//deactivate user
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void deactivate(List<ApplicationUser> u) {
		repo.deleteAll(u);
	}
	
	public void cleanDatabase() {
		repo.deleteAll();
	}
}