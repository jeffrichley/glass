package com.infinity.glass.manager;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.SecurityContext;

import com.infinity.glass.model.UserIdentity;

public interface IdentityManager {

	UserIdentity getUserIdentity(HttpServletRequest req) throws IllegalStateException;

	UserIdentity getUserIdentity(SecurityContext sc);

}