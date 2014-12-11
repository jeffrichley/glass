/**
 * 
 */
package com.infinity.glass.manager;

import com.infinity.glass.model.UserIdentity;

/**
 * @author kerry.baumer
 *
 */
public interface UserPersistence {

	UserIdentity	create();
	UserIdentity	retrieve(String name);	
	void	save(UserIdentity uid);
	void	delete(UserIdentity uid);
}
