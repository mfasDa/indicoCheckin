package indico.checkin.core.gui;

import indico.checkin.core.data.IndicoRegistrantSocialEvent;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SocialEventInfoDialog extends JDialog {

	/**
	 * Dialog showing the events booked by the registrant
	 * License: GPLv3 (a copy of the license is provided with the package)
	 * 
	 * @author: Markus Fasel
	 */
	private static final long serialVersionUID = 1L;

	public SocialEventInfoDialog(JFrame parent, List<IndicoRegistrantSocialEvent> events){
		super(parent, true);
		
		JTable infotable = new JTable(new SocialEventInfoModel(events));
		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		getContentPane().add(new JScrollPane(infotable), BorderLayout.CENTER);
		getContentPane().add(closeButton, BorderLayout.SOUTH);
		pack();
	}
}
