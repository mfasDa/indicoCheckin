package indico.checkin.core.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import indico.checkin.core.data.IndicoEventRegistrantList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ManualSearchDialog extends JDialog {

	/**
	 * Dailog for manual search of a registrant
	 */
	private static final long serialVersionUID = 1L;
	
	private JTable registrantDisplay;
	private boolean entrySelected;
	
	public ManualSearchDialog(JFrame parent, RegistrantListModel dataModel){
		super(parent, "Select registrant", true);
		entrySelected = false;
		
		registrantDisplay = new JTable(dataModel);
		
		JButton confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.printf("Row selected: %s\n", registrantDisplay.getSelectedRow());
				if(registrantDisplay.getSelectedRow() > -1){
					entrySelected = true;
					System.out.println("A row was selected");
				} 
				// otherwise no entry selected();
				dispose();
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * Cancel button, no entry selected
				 */
				entrySelected = false;
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);
		
		this.getContentPane().add(new JScrollPane(registrantDisplay), BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
		this.pack();
	}

	public boolean isEntrySelected() {
		return entrySelected;
	}

	public void setEntrySelected(boolean entrySelected) {
		this.entrySelected = entrySelected;
	}
	
	public int getSelectedRow(){
		return registrantDisplay.getSelectedRow();
	}
}
