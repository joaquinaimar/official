package org.apache.shiro.web.tags;

import org.apache.shiro.subject.Subject;

public class HasAnyPermissionsTag extends PermissionTag {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7064023875270273991L;
	// Delimeter that separates role names in tag attribute
	private static final String PERMISSION_NAMES_DELIMETER = ",";

	@Override
	protected boolean showTagBody(String permissionNames) {
		boolean hasAnyPermissions = false;

		Subject subject = getSubject();

		if (subject != null) {

			// Iterate through roles and check to see if the user has one of the
			// roles
			for (String permission : permissionNames
					.split(PERMISSION_NAMES_DELIMETER)) {

				if (subject.isPermitted(permission.trim())) {
					hasAnyPermissions = true;
					break;
				}

			}

		}

		return hasAnyPermissions;
	}

}
