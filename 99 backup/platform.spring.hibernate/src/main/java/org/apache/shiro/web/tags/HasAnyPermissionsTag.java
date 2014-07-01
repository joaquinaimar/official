package org.apache.shiro.web.tags;

import org.apache.shiro.subject.Subject;

public class HasAnyPermissionsTag extends PermissionTag {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7064023875270273991L;

	private static final String PERMISSION_NAMES_DELIMETER = ",";

	@Override
	protected boolean showTagBody(String permissionNames) {
		boolean hasAnyPermissions = false;

		Subject subject = getSubject();

		if (subject != null) {

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
