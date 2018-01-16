package net.savantly.shack.web.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.savantly.sprout.core.domain.menu.Menu;
import net.savantly.sprout.core.domain.menu.MenuRepository;
import net.savantly.sprout.core.security.privilege.Privilege;
import net.savantly.sprout.core.security.privilege.PrivilegeRepository;

@Service
public class ShackFixture {
	static final String MENU_ID = "SHACK";
	static final String MENU_NAME = "Shack";
	static final String EDIT_PRIVILEGE = "EDIT_SHACK";
	static final String READ_PRIVILEGE = "READ_SHACK";

	@Autowired
	private MenuRepository menuRepository;
	@Autowired
	private PrivilegeRepository privileges;

	public void install() throws Exception {
		ensureMenuItemsExist();
		ensureWikiPrivilegesExist();
	}
	
	private void ensureWikiPrivilegesExist() {
		if (privileges.findOne(READ_PRIVILEGE) == null) {
			privileges.save(new Privilege(READ_PRIVILEGE));
		}
		if (privileges.findOne(EDIT_PRIVILEGE) == null) {
			privileges.save(new Privilege(EDIT_PRIVILEGE));
		}
	}

	private void ensureMenuItemsExist() {
		Menu menu = menuRepository.findOne(MENU_ID);
		if (menu == null) {
			menu = new Menu();
			menu.setIcon("bookmark");
			menu.setId(MENU_ID);
			menu.setDisplayText(MENU_NAME);
			menu.setUrl("/plugins;id="+ShackModule.BEAN_NAME);
			menuRepository.save(menu);
		}
	}

}