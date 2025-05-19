package de.zeroco.main.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.zeroco.main.dao.UserDao;
import de.zeroco.main.util.Util;

@Service
public class UserService {
	
	@Autowired
	UserDao dao;
	
	public List<Map<String, Object>> list() {
		List<Map<String, Object>> list = dao.list();
		return Util.hasData(list) ? list : new ArrayList<Map<String, Object>>();
	}
	
	public int delete(Object pk_id) {
		return dao.delete(pk_id);
	}
	
	public int save(String name, String email, String phone, String password) {
	    boolean result  = dao.isUserExists(email, phone);
	    if (result) {
	        return dao.save(Arrays.asList(name, email, phone, password));
	    }
	    return 0; 
	}
	
	public int update(int pk_id, String name, String email, String phone, String password) {
		int result = dao.getId(pk_id);
		if (result == 1) {
			boolean status  = dao.findUser(email, phone, pk_id);
			if (status) {
				return dao.update(pk_id, name, email, phone, password);
			}
		}
		return 0;
	}
	
	public Map<String, Object> getUser(int pk_id) {
		return Util.isBlank(dao.getUser(pk_id)) ? new HashMap<String, Object>() : dao.getUser(pk_id);
	}
}
