package de.zeroco.main.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

import de.zeroco.main.util.DbUtil;

@Repository
public class UserDao {
	
	public static final List<String> COLUMNS = Arrays.asList("name", "email", "phone", "password");
	public static final List<String> HEADERS = Arrays.asList("pk_id", "name", "email", "phone", "password");
	
	public List<Map<String, Object>> list() {	
		return DbUtil.list("zc_team", "user", HEADERS);
	}
	
	public int delete(Object pk_id) {
		return DbUtil.delete("zc_team", "user", "pk_id", pk_id);
	}
	
	public int save(List<Object> list) {
		return DbUtil.getGeneratedKey("zc_team", "user", COLUMNS, list);
	}
	
	public int update(int pk_id, String name, String email, String phone, String password) {
		return DbUtil.update("zc_team", "user", COLUMNS, Arrays.asList(name, email, phone, password), "pk_id", pk_id);
	}
	
	public boolean findUser(String email, String phone, int pk_id) {
	    return DbUtil.findUser("zc_team", "user", Arrays.asList("email", "phone"), Arrays.asList(email, phone), "pk_id", pk_id);
	}

	public boolean isUserExists(String email, String phone) {
	    return DbUtil.isUserExists("zc_team", "user", Arrays.asList("email", "phone"), Arrays.asList(email, phone));
	}

	public int getId(int pk_id) {
		return DbUtil.getId("zc_team", "user", "pk_id", pk_id);
	}
	
	public Map<String, Object> getUser(int pk_id) {
		return DbUtil.getUser("zc_team", "user", Arrays.asList("pk_id"), Arrays.asList(pk_id));
	}
}
