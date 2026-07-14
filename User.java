package testing6;

public class User {
	private String username;
	private String password;
	private String name;
	private String phoneNum;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPhoneNum() {
		return phoneNum;
	}
	
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	
	public User(String username, String password, String name, String phoneNum) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.phoneNum = phoneNum;
	}
}
