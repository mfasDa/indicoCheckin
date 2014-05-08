/****************************************************************************
 *  Copyright (C) 2014  Markus Fasel <markus.fasel@cern.ch>                 *
 *                      Steffen Weber <s.weber@gsi.de>                      *
 *                                                                          * 
 *  This program is free software: you can redistribute it and/or modify    *
 *  it under the terms of the GNU General Public License as published by    *
 *  the Free Software Foundation, either version 3 of the License, or       *
 *  (at your option) any later version.                                     *
 *                                                                          *
 *  This program is distributed in the hope that it will be useful,         *
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 *  GNU General Public License for more details.                            *
 *                                                                          *
 *  You should have received a copy of the GNU General Public License       *
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.   *
 ****************************************************************************/

package indico.checkin.core.gui;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Timed beeping
 * @author Markus Fasel
 */
public class Beeper extends TimerTask {
	
	Timer timer;
	int beepCounter;
	int numberBeeps;
	
	public Beeper(int numberBeeps){
		timer = new Timer();
		timer.schedule(this, 0, 500);
		beepCounter = 0;
		this.numberBeeps = numberBeeps;
	}

	@Override
	public void run() {
		java.awt.Toolkit.getDefaultToolkit().beep();
		beepCounter++;
		if(beepCounter >= numberBeeps){
			// Stop timer when beeps are done
			timer.cancel();
		}
	}

}
