/*
 *
 *  * cdoj, UESTC ACMICPC Online Judge
 *  * Copyright (c) 2013 fish <@link lyhypacm@gmail.com>,
 *  * 	mzry1992 <@link muziriyun@gmail.com>
 *  *
 *  * This program is free software; you can redistribute it and/or
 *  * modify it under the terms of the GNU General Public License
 *  * as published by the Free Software Foundation; either version 2
 *  * of the License, or (at your option) any later version.
 *  *
 *  * This program is distributed in the hope that it will be useful,
 *  * but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * along with this program; if not, write to the Free Software
 *  * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */

package cn.edu.uestc.acmicpc.action.user;

import cn.edu.uestc.acmicpc.action.BaseAction;
import cn.edu.uestc.acmicpc.annotation.LoginPermit;
import cn.edu.uestc.acmicpc.db.entity.User;

/**
 * Logout action, remove session about user information.
 *
 * @author <a href="mailto:muziriyun@gmail.com">mzry1992</a>
 * @version 1
 */
@LoginPermit(NeedLogin = true)
public class LogoutAction extends BaseAction {

    private static final long serialVersionUID = -4720877248873990818L;

    public String toLogout() {
        session.remove("userName");
        session.remove("password");
        session.remove("lastLogin");
        session.remove("userType");
        return redirectToRefer("Logout successfully!");
    }

}