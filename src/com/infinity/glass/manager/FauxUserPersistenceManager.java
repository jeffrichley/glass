/**
 * 
 */
package com.infinity.glass.manager;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletContext;

import com.infinity.glass.model.UserIdentity;


/**
 * @author kbaumer
 *
 */
public class FauxUserPersistenceManager extends AbstractManager implements UserPersistence {

	private static final Map<String,UserIdentity> users = new HashMap<String,UserIdentity>();
	
	/**
	 * 
	 */
	public FauxUserPersistenceManager(ServletContext context) {
		super();
	}

	@Override
	public UserIdentity create() {
		UserIdentity result = new UserIdentity();
		result.setUserId(UUID.randomUUID().toString());
		result.setEmailAddress("j.doe@navy.mil");
		result.setUserName("John Doe");
		result.setCreated(new Date());
		result.setLastLogin(new Date());
		return result;
	}

	@Override
	public UserIdentity retrieve(String key) {
		UserIdentity ui = users.get(key);
		if(ui == null) {
			ui = create();
			ui.setUserName(key);
			users.put(key, ui);
		}
		return ui;
	}
	
	@Override
	public void save(UserIdentity uid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(UserIdentity uid) {
		// TODO Auto-generated method stub
		
	}

}
