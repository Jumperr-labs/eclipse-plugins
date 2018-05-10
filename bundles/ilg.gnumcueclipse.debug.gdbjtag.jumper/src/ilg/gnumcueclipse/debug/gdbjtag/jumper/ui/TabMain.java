/*******************************************************************************
 * Copyright (c) 2007 - 2010 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial implementation
 *     Liviu Ionescu - ARM version
 *******************************************************************************/

package ilg.gnumcueclipse.debug.gdbjtag.jumper.ui;

import org.eclipse.cdt.launch.ui.CMainTab2;

import ilg.gnumcueclipse.debug.gdbjtag.jumper.Activator;

public class TabMain extends CMainTab2 {

	public TabMain() {
		super((Activator.getInstance().getDefaultPreferences().getTabMainCheckProgram() ? 0
				: CMainTab2.DONT_CHECK_PROGRAM) | CMainTab2.INCLUDE_BUILD_SETTINGS);
	}
}
