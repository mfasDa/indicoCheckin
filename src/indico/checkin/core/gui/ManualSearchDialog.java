package indico.checkin.core.gui;

import indico.checkin.core.data.IndicoRegistrant;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ManualSearchDialog extends JDialog implements ListSelectionListener,DocumentListener{

	/**
	 * Dailog for manual search of a registrant. Users can select an entry in the list
	 * of entries.
	 * License: GPLv3 (a copy of the license is provided with the package)
	 * 
	 * @author; Markus Fasel
	 */
	private static final long serialVersionUID = 1L;
	
	private JTable registrantDisplay;
	private JTextField searchField;
	private JButton confirmButton;
	private RegistrantListModel tableModel;
	private IndicoRegistrant selected;
	
	public ManualSearchDialog(JFrame parent, RegistrantListModel dataModel){
		super(parent, "Select registrant", true);
		this.setPreferredSize(new Dimension(700, 400));
		
		JPanel findPanel = new JPanel();
		findPanel.setLayout(new FlowLayout());
		JLabel findLabel = new JLabel("Find by lastname:");
		findPanel.add(findLabel);
		searchField = new JTextField(30);
		searchField.getDocument().addDocumentListener(this);
		findPanel.add(searchField);
				
		tableModel = dataModel;
		registrantDisplay = new JTable(tableModel);
		registrantDisplay.getColumnModel().getColumn(0).setPreferredWidth(200);
		registrantDisplay.getColumnModel().getColumn(1).setPreferredWidth(200);		
		registrantDisplay.getColumnModel().getColumn(2).setPreferredWidth(400);
		registrantDisplay.getColumnModel().getColumn(3).setPreferredWidth(50);	
		registrantDisplay.getSelectionModel().addListSelectionListener(this);
		
		confirmButton = new JButton("Confirm");
		confirmButton.setEnabled(false);
		confirmButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(registrantDisplay.getSelectedRow() > -1){
					try {
						selected = tableModel.getSelectedRegistrantForRow(registrantDisplay.getSelectedRow());
					} catch (EntryListBoundaryException e1) {
						JOptionPane.showMessageDialog(ManualSearchDialog.this, "Error in accessing registrant");
					}
				} 
				// otherwise no entry selected
				// in any case set all entries to selected again
				tableModel.selectAll(); 
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
				selected = null;
				tableModel.selectAll();
				dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(confirmButton);
		buttonPanel.add(cancelButton);
		
		this.getContentPane().add(findPanel, BorderLayout.NORTH);
		this.getContentPane().add(new JScrollPane(registrantDisplay), BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
		this.pack();
	}

	public IndicoRegistrant getSelectedRegistrant(){
		return selected;
	}
	
	public boolean isEntrySelected(){
		return selected != null;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		/*
		 * Enable confirm button only in case an entry was selected		
		 */
		confirmButton.setEnabled(true);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		handleTextfieldChange();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		handleTextfieldChange();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		handleTextfieldChange();
	}
	
	private void handleTextfieldChange(){
		/*
		 *when a textfield changed, update selection in the table model and fire table update
		 */
		System.out.printf("Content of the text field: %s\n", searchField.getText());
		tableModel.UpdateSelected(searchField.getText());
		tableModel.fireTableDataChanged();
	} 
}
