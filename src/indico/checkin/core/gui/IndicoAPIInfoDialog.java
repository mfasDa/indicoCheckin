package indico.checkin.core.gui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class IndicoAPIInfoDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public IndicoAPIInfoDialog(Frame owner, String apikey, String apisecret, String server, int event){
		super(owner);
				
		JPanel pnlinfo = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();
		cs.fill = GridBagConstraints.HORIZONTAL;

		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		cs.insets = new Insets(10,10,3,3);
		pnlinfo.add(getAdjustedLabel("URL:"), cs);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;
		cs.insets = new Insets(10,3,3,10);
		pnlinfo.add(getAdjustedLabel(server), cs);
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		cs.insets = new Insets(3,10,3,3);
		pnlinfo.add(getAdjustedLabel("Event:"), cs);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;
		cs.insets = new Insets(3,3,3,10);
		pnlinfo.add(getAdjustedLabel(String.format("%d", event)), cs);
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		cs.insets = new Insets(3,10,3,3);
		pnlinfo.add(getAdjustedLabel("API key:"), cs);
		cs.gridx = 1;
		cs.gridy = 2;
		cs.gridwidth = 2;
		cs.insets = new Insets(3,3,3,10);
		pnlinfo.add(getAdjustedLabel(apikey), cs);
		cs.gridx = 0;
		cs.gridy = 3;
		cs.gridwidth = 1;
		cs.insets = new Insets(3,10,3,3);
		pnlinfo.add(getAdjustedLabel("API secret:"), cs);
		cs.gridx = 1;
		cs.gridy = 3;
		cs.gridwidth = 2;
		cs.insets = new Insets(3,3,3,10);
		pnlinfo.add(getAdjustedLabel(apisecret), cs);
		
		JButton btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		
		JPanel pnlbtn = new JPanel();
		pnlbtn.add(btnOK);
		
		this.getContentPane().add(pnlinfo, BorderLayout.CENTER);
		this.getContentPane().add(pnlbtn, BorderLayout.PAGE_END);
		this.pack();
		this.setLocationRelativeTo(owner);
	}

	private JLabel getAdjustedLabel(String message){
		JLabel lbl = new JLabel(message);
		lbl.setHorizontalAlignment(SwingConstants.LEFT);
		return lbl;
	}
}
