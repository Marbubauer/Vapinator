/*author: Marco Conforto
 * version 1.3 beta
 * 
 * README
 * Bugfix: 
 * - niente
 * 
 * Cose aggiunte o modificate:
 * - ricerca live tramite il textField sotto la tabella
 * - sorting degli elementi delle colonne in ordine alfabetico
 * - aggiunti gli ID per prossima addizione del salvataggio su DB
 * 
 */

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Vector;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Image;


public class Vapinator extends JFrame implements ActionListener, WindowListener, KeyListener{

	private static final long serialVersionUID = 1L;

	private String versione = "v1.3 beta";

	private JLabel lblMlDiBase;
	private JFrame frameModifica;
	private JTable table;
	private DefaultTableModel model, model1, model2;
	private TableRowSorter<TableModel> rowSorter = new TableRowSorter<>();
	private JButton btnAggiungi, aggiungi1;
	private JButton btnModifica, modifica1;
	private JButton btnRimuovi, btnMake;
	private JButton copia;
	private JComboBox<String> comboBox, comboBox1, comboBox2, comboBox3;
	private JTextField textField, textField_1, textField_2, textField_3;
	private JTextField textField4, textField5, textField6, textField7;
	private JTextField textField8, textField9, textField10;
	private Vector<Aroma> fruttati = new Vector<Aroma>(1);
	private Vector<Aroma> cremosi = new Vector<Aroma>(1);
	private Vector<Aroma> tabaccosi = new Vector<Aroma>(1);
	private File f, c, t;
	private String user = System.getProperty("user.name");
	private int tempRow = -1;
	private JLabel lblNomeAroma;
	private JTextField jtfilter;

	public Vapinator() {
		super("Vapinator v1.3 beta");

		setSize(909,480);
		getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 42, 604, 308);
		getContentPane().add(scrollPane);

		try {
			f = new File("C:\\Users\\" + user + "\\Documents\\fruttati.txt");
			if(f.createNewFile() == true) {}
			c = new File("C:\\Users\\" + user + "\\Documents\\cremosi.txt");
			if(c.createNewFile() == true) {}
			t = new File("C:\\Users\\" + user + "\\Documents\\tabaccosi.txt");
			if(t.createNewFile() == true) {}
		}catch(Exception e) {
			e.printStackTrace();
		}

		JLabel lblKeyWord = new JLabel("Inserisci la parola chiave:");
		lblKeyWord.setBounds(20, 360, 209, 14);
		getContentPane().add(lblKeyWord);

		jtfilter = new JTextField();
		jtfilter.setBounds(171, 357, 443, 20);
		getContentPane().add(jtfilter);
		jtfilter.setColumns(10);

		String[] colonne = {"Nome", "Gusto", "Marca", "ml", "ID"};
		table = new JTable();
		table.setFont(new Font("Arial", Font.PLAIN, 15));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);

		model = new NonEditableTableModel();
		model1 = new NonEditableTableModel();
		model2 = new NonEditableTableModel();
		model.setColumnIdentifiers(colonne);
		model1.setColumnIdentifiers(colonne);
		model2.setColumnIdentifiers(colonne);
		table.setModel(model);
		scrollPane.setViewportView(table);

		copia = new JButton("Copy to clipboard");
		copia.addActionListener(this);
		copia.setBounds(436, 8, 178, 26);
		getContentPane().add(copia);

		btnAggiungi = new JButton("Aggiungi");
		btnAggiungi.addActionListener(this);
		btnAggiungi.setBounds(10, 385, 135, 39);
		getContentPane().add(btnAggiungi);

		btnModifica = new JButton("Modifica");
		btnModifica.addActionListener(this);
		btnModifica.setBounds(223, 385, 169, 39);
		getContentPane().add(btnModifica);

		btnRimuovi = new JButton("Rimuovi");
		btnRimuovi.addActionListener(this);
		btnRimuovi.setBounds(479, 385, 135, 39);
		getContentPane().add(btnRimuovi);

		JLabel lblScegliIlTipo = new JLabel("Scegli il tipo di aroma:");
		lblScegliIlTipo.setFont(new Font("Arial", Font.PLAIN, 16));
		lblScegliIlTipo.setBounds(10, 11, 219, 23);
		getContentPane().add(lblScegliIlTipo);

		comboBox = new JComboBox();
		comboBox.addItem("Fruttati");
		comboBox.addItem("Cremosi");
		comboBox.addItem("Tabaccosi");
		comboBox.addActionListener(this);
		comboBox.setBounds(223, 10, 203, 23);
		getContentPane().add(comboBox);

		textField9 = new JTextField();
		textField9.setBounds(777, 47, 99, 20);
		getContentPane().add(textField9);
		textField9.setColumns(10);

		textField10 = new JTextField();
		textField10.setColumns(10);
		textField10.setBounds(777, 72, 99, 20);
		getContentPane().add(textField10);

		JLabel lblConcentrazioneFinale = new JLabel("Concentrazione finale:");
		lblConcentrazioneFinale.setBounds(624, 50, 153, 14);
		getContentPane().add(lblConcentrazioneFinale);

		JLabel lblNewLabel = new JLabel("Quantit� finale:");
		lblNewLabel.setBounds(624, 75, 153, 14);
		getContentPane().add(lblNewLabel);

		JLabel lblConcentrazioneBaseNic = new JLabel("Concentrazione base nic:");
		lblConcentrazioneBaseNic.setBounds(624, 106, 153, 14);
		getContentPane().add(lblConcentrazioneBaseNic);

		btnMake = new JButton("Make a Liquid");
		btnMake.addActionListener(this);
		btnMake.setBounds(624, 186, 252, 39);
		getContentPane().add(btnMake);

		lblMlDiBase = new JLabel("ml di base nic:");
		lblMlDiBase.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblMlDiBase.setBounds(624, 131, 194, 39);
		getContentPane().add(lblMlDiBase);

		comboBox3 = new JComboBox();
		comboBox3.addItem("25 mg/ml");
		comboBox3.addItem("18 mg/ml");
		comboBox3.setBounds(777, 103, 99, 20);
		getContentPane().add(comboBox3);

		lblNomeAroma = new JLabel("Nome aroma:");
		lblNomeAroma.setBounds(624, 20, 143, 14);
		getContentPane().add(lblNomeAroma);

		textField8 = new JTextField();
		textField8.setBounds(777, 17, 99, 20);
		getContentPane().add(textField8);
		textField8.setColumns(10);

		//importa tutti i dati dai file .txt
		leggiDaFile();
		
		//imposta il rowSorter alla tabella e il modello al rowSorter
		rowSorter.setModel(model);
		table.setRowSorter(rowSorter);

		//ricerca live dal textField "jtfiler"
		jtfilter.getDocument().addDocumentListener(new DocumentListener(){

			@Override
			public void insertUpdate(DocumentEvent e) {
				String text = jtfilter.getText();

				if (text.trim().length() == 0) {
					rowSorter.setRowFilter(null);
				} else {
					rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String text = jtfilter.getText();

				if (text.trim().length() == 0) {
					rowSorter.setRowFilter(null);
				} else {
					rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				throw new UnsupportedOperationException("Not supported yet."); 
			}

		});

		setDefaultLookAndFeelDecorated(false);
		setIconImage(new ImageIcon("vapeicon.ico").getImage());
		setResizable(false);
		setLocationRelativeTo(null);
		addWindowListener(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}


	public void finestraAggiungi() {
		JFrame frameAggiungi = new JFrame("Vapinator " + versione);

		frameAggiungi.getContentPane().setLayout(null);

		aggiungi1 = new JButton("Aggiungi");
		aggiungi1.addActionListener(this);
		aggiungi1.setBounds(178, 139, 110, 23);
		frameAggiungi.getContentPane().add(aggiungi1);

		comboBox1 = new JComboBox();
		comboBox1.addItem("Fruttati");
		comboBox1.addItem("Cremosi");
		comboBox1.addItem("Tabaccosi");
		comboBox1.setBounds(10, 140, 147, 20);
		frameAggiungi.getContentPane().add(comboBox1);

		textField_1 = new JTextField();
		textField_1.setBounds(122, 42, 168, 20);
		frameAggiungi.getContentPane().add(textField_1);
		textField_1.setColumns(10);

		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(122, 73, 168, 20);
		frameAggiungi.getContentPane().add(textField_2);

		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(122, 104, 168, 20);
		frameAggiungi.getContentPane().add(textField_3);

		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(122, 11, 168, 20);
		frameAggiungi.getContentPane().add(textField);

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(10, 14, 110, 14);
		frameAggiungi.getContentPane().add(lblNome);

		JLabel lblGusto = new JLabel("Gusto");
		lblGusto.setBounds(10, 45, 110, 14);
		frameAggiungi.getContentPane().add(lblGusto);

		JLabel lblMarca = new JLabel("Marca:");
		lblMarca.setBounds(10, 76, 110, 14);
		frameAggiungi.getContentPane().add(lblMarca);

		JLabel lblMl = new JLabel("ml");
		lblMl.setBounds(10, 107, 110, 14);
		frameAggiungi.getContentPane().add(lblMl);

		textField.addKeyListener(this);
		textField_1.addKeyListener(this);
		textField_2.addKeyListener(this);
		textField_3.addKeyListener(this);
		aggiungi1.addKeyListener(this);
		frameAggiungi.addKeyListener(this);

		frameAggiungi.setLocationRelativeTo(null);
		frameAggiungi.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frameAggiungi.setSize(314, 212);
		frameAggiungi.setVisible(true);
	}

	public void finestraModifica() {
		frameModifica = new JFrame("Vapinator " + versione);

		frameModifica.getContentPane().setLayout(null);

		modifica1 = new JButton("Modifica");
		modifica1.addActionListener(this);
		modifica1.setBounds(178, 139, 110, 23);
		frameModifica.getContentPane().add(modifica1);

		comboBox2 = new JComboBox();
		comboBox2.addItem("Fruttati");
		comboBox2.addItem("Cremosi");
		comboBox2.addItem("Tabaccosi");
		comboBox2.setBounds(10, 140, 147, 20);
		frameModifica.getContentPane().add(comboBox2);

		textField5 = new JTextField();
		textField5.setBounds(122, 42, 168, 20);
		frameModifica.getContentPane().add(textField5);
		textField5.setColumns(10);

		textField6 = new JTextField();
		textField6.setColumns(10);
		textField6.setBounds(122, 73, 168, 20);
		frameModifica.getContentPane().add(textField6);

		textField7 = new JTextField();
		textField7.setColumns(10);
		textField7.setBounds(122, 104, 168, 20);
		frameModifica.getContentPane().add(textField7);

		textField4 = new JTextField();
		textField4.setColumns(10);
		textField4.setBounds(122, 11, 168, 20);
		frameModifica.getContentPane().add(textField4);

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(10, 14, 110, 14);
		frameModifica.getContentPane().add(lblNome);

		JLabel lblGusto = new JLabel("Gusto");
		lblGusto.setBounds(10, 45, 110, 14);
		frameModifica.getContentPane().add(lblGusto);

		JLabel lblMarca = new JLabel("Marca:");
		lblMarca.setBounds(10, 76, 110, 14);
		frameModifica.getContentPane().add(lblMarca);

		JLabel lblMl = new JLabel("ml");
		lblMl.setBounds(10, 107, 110, 14);
		frameModifica.getContentPane().add(lblMl);


		frameModifica.addWindowListener(this);
		frameModifica.setLocationRelativeTo(null);
		frameModifica.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frameModifica.setSize(314, 212);
		frameModifica.setVisible(true);
	}



	@Override
	public void actionPerformed(ActionEvent e) {

		//cambia scheda in riferimento al tipo di aroma scelto dalla combobox principale
		if(e.getSource() == comboBox) {
			if(comboBox.getSelectedItem().equals("Fruttati")) {
				table.setModel(model);
				table.updateUI();
				rowSorter.setModel(model);
				tempRow = -1;
			}else if(comboBox.getSelectedItem().equals("Cremosi")) {
				table.setModel(model1);
				table.updateUI();
				rowSorter.setModel(model1);
				tempRow = -1;
			}else if(comboBox.getSelectedItem().equals("Tabaccosi")) {
				table.setModel(model2);
				table.updateUI();
				rowSorter.setModel(model2);
				tempRow = -1;
			}
		}
		//--------------------------------------------------------------
		//apre la finestra per aggiungere un aroma
		else if(e.getSource() == btnAggiungi) {
			finestraAggiungi();
		}
		//aggiunge un aroma usando il tasto aggiungi1 dentro finestraAggiungi()
		else if(e.getSource() == aggiungi1) {
			aggiungiAroma();
		}
		//-----------------------------------------------------------------------------------
		//apre la finestra per modificare un aroma e ci inserisce le informazioni dell'aroma selezionato sulla tabella
		else if(e.getSource() == btnModifica) {
			tempRow = table.getSelectedRow();
			table.setEnabled(false);
			if(tempRow >= 0) {
				finestraModifica();
				tempRow = table.getSelectedRow();
				comboBox.setEnabled(false);
				if(comboBox.getSelectedItem().equals("Fruttati")) {
					getAromaForModifica(0);
				}else if(comboBox.getSelectedItem().equals("Cremosi")) {
					getAromaForModifica(1);
				}else if(comboBox.getSelectedItem().equals("Tabaccosi")) {
					getAromaForModifica(2);
				}
			}else {
				JOptionPane.showMessageDialog(new JFrame(), "Devi prima selezionare una riga della tabella!");
				table.setEnabled(true);
			}
		}
		//--------------------------------------------------------------------------------------------
		//modifica definitivamente l'aroma selezionato e puo anche spostarlo da una scheda all'altra se si vuole
		else if(e.getSource() == modifica1) {
			try {
				Object[] temp = new Object[5];
				int tempid = (int)table.getValueAt(tempRow, 4);
				comboBox.setEnabled(true);
				//se la scheda in cui si � e quella selezionata nella finestra di modifica sono uguali 
				//l'aroma viene semplicemente modificato e lasciato al suo posto originario
				if(comboBox2.getSelectedItem().equals("Fruttati") && comboBox.getSelectedItem().equals("Fruttati")) {

					for(int i = 0; i < fruttati.size(); i++) {
						if(fruttati.get(i).getID() == tempid) {
							fruttati.get(i).setNome(textField4.getText());
							fruttati.get(i).setGusto(textField5.getText());
							fruttati.get(i).setMarca(textField6.getText());
							fruttati.get(i).setMl(Integer.parseInt(textField7.getText()));

							table.setValueAt(textField4.getText(), tempRow, 0);
							table.setValueAt(textField5.getText(), tempRow, 1);
							table.setValueAt(textField6.getText(), tempRow, 2);
							table.setValueAt(textField7.getText(), tempRow, 3);
							break;
						}
					}

				} 

				//---------------------------------------------------------------------------------------------				
				//se si sceglie di spostare l'aroma in un'altra scheda viene eliminato sia dal vector che dalla scheda
				//in cui si � e ne viene creato uno nuovo con i dati inseriti nella scheda selezionata nella combobox2
				if(!comboBox2.getSelectedItem().equals("Fruttati") && comboBox.getSelectedItem().equals("Fruttati")) {

					if(comboBox2.getSelectedItem().equals("Cremosi")) {
						for(int i = 0; i < fruttati.size(); i++) {
							if(fruttati.get(i).getID() == tempid) {
								fruttati.remove(i);
								fruttati.trimToSize();
								break;
							}
						}

						int idNuovo = controllaID(cremosi);
						cremosi.add(new Aroma(idNuovo, textField4.getText(),textField5.getText(),textField6.getText(),Integer.parseInt(textField7.getText())));
						model.removeRow(tempRow);

						temp[0] = textField4.getText();
						temp[1] = textField5.getText();
						temp[2] = textField6.getText();
						temp[3] = textField7.getText();
						temp[4] = idNuovo;

						model1.addRow(temp);
						table.updateUI();
					}else if(comboBox2.getSelectedItem().equals("Tabaccosi")) {

						for(int i = 0; i < fruttati.size(); i++) {
							if(fruttati.get(i).getID() == tempid) {
								fruttati.remove(i);
								fruttati.trimToSize();
								break;
							}
						}

						int idNuovo = controllaID(tabaccosi);
						tabaccosi.add(new Aroma(idNuovo, textField4.getText(), textField5.getText(), textField6.getText(),Integer.parseInt(textField7.getText())));
						model.removeRow(tempRow);

						temp[0] = textField4.getText();
						temp[1] = textField5.getText();
						temp[2] = textField6.getText();
						temp[3] = textField7.getText();
						temp[4] = idNuovo;

						model2.addRow(temp);
						table.updateUI();
					}
				}

				if(comboBox2.getSelectedItem().equals("Cremosi") && comboBox.getSelectedItem().equals("Cremosi")) {

					for(int i = 0; i < cremosi.size(); i++) {
						if(cremosi.get(i).getID() == tempid) {
							cremosi.get(i).setNome(textField4.getText());
							cremosi.get(i).setGusto(textField5.getText());
							cremosi.get(i).setMarca(textField6.getText());
							cremosi.get(i).setMl(Integer.parseInt(textField7.getText()));

							table.setValueAt(textField4.getText(), tempRow, 0);
							table.setValueAt(textField5.getText(), tempRow, 1);
							table.setValueAt(textField6.getText(), tempRow, 2);
							table.setValueAt(textField7.getText(), tempRow, 3);
							break;
						}
					}

				}
				else if(!comboBox2.getSelectedItem().equals("Cremosi") && comboBox.getSelectedItem().equals("Cremosi")) {

					if(comboBox2.getSelectedItem().equals("Fruttati")) {
						for(int i = 0; i < cremosi.size(); i++) {
							if(cremosi.get(i).getID() == tempid) {
								cremosi.remove(i);
								cremosi.trimToSize();
								break;
							}
						}

						int idNuovo = controllaID(fruttati);
						fruttati.add(new Aroma(idNuovo, textField4.getText(),textField5.getText(),textField6.getText(),Integer.parseInt(textField7.getText())));
						model1.removeRow(tempRow);

						temp[0] = textField4.getText();
						temp[1] = textField5.getText();
						temp[2] = textField6.getText();
						temp[3] = textField7.getText();
						temp[4] = idNuovo;

						model.addRow(temp);
						table.updateUI();
					}else if(comboBox2.getSelectedItem().equals("Tabaccosi")) {

						for(int i = 0; i < cremosi.size(); i++) {
							if(cremosi.get(i).getID() == tempid) {
								cremosi.remove(i);
								cremosi.trimToSize();
								break;
							}
						}

						int idNuovo = controllaID(tabaccosi);
						tabaccosi.add(new Aroma(idNuovo, textField4.getText(),textField5.getText(),textField6.getText(),Integer.parseInt(textField7.getText())));
						model1.removeRow(tempRow);

						temp[0] = textField4.getText();
						temp[1] = textField5.getText();
						temp[2] = textField6.getText();
						temp[3] = textField7.getText();
						temp[4] = idNuovo;

						model2.addRow(temp);
						table.updateUI();
					}
				}

				if(comboBox2.getSelectedItem().equals("Tabaccosi") && comboBox.getSelectedItem().equals("Tabaccosi")) {

					for(int i = 0; i < tabaccosi.size(); i++) {
						if(tabaccosi.get(i).getID() == tempid) {
							tabaccosi.get(i).setNome(textField4.getText());
							tabaccosi.get(i).setGusto(textField5.getText());
							tabaccosi.get(i).setMarca(textField6.getText());
							tabaccosi.get(i).setMl(Integer.parseInt(textField7.getText()));

							table.setValueAt(textField4.getText(), tempRow, 0);
							table.setValueAt(textField5.getText(), tempRow, 1);
							table.setValueAt(textField6.getText(), tempRow, 2);
							table.setValueAt(textField7.getText(), tempRow, 3);
							break;

						}
					}
				}
				else if(!comboBox2.getSelectedItem().equals("Tabaccosi") && comboBox.getSelectedItem().equals("Tabaccosi")) {

					if(comboBox2.getSelectedItem().equals("Cremosi")) {
						for(int i = 0; i < tabaccosi.size(); i++) {
							if(tabaccosi.get(i).getID() == tempid) {
								tabaccosi.remove(i);
								tabaccosi.trimToSize();
								break;
							}
						}
						int idNuovo = controllaID(cremosi);
						cremosi.add(new Aroma(idNuovo, textField4.getText(),textField5.getText(),textField6.getText(),Integer.parseInt(textField7.getText())));
						model2.removeRow(tempRow);

						temp[0] = textField4.getText();
						temp[1] = textField5.getText();
						temp[2] = textField6.getText();
						temp[3] = textField7.getText();
						temp[4] = idNuovo;

						model1.addRow(temp);
						table.updateUI();
					}else if(comboBox2.getSelectedItem().equals("Fruttati")) {
						for(int i = 0; i < tabaccosi.size(); i++) {
							if(tabaccosi.get(i).getID() == tempid) {
								tabaccosi.remove(i);
								tabaccosi.trimToSize();
								break;
							}
						}

						int idNuovo = controllaID(fruttati);
						fruttati.add(new Aroma(fruttati.size(), textField4.getText(),textField5.getText(),textField6.getText(),Integer.parseInt(textField7.getText())));
						model2.removeRow(tempRow);

						temp[0] = textField4.getText();
						temp[1] = textField5.getText();
						temp[2] = textField6.getText();
						temp[3] = textField7.getText();
						temp[4] = idNuovo;

						model.addRow(temp);
						table.updateUI();
					}
				}

				table.setEnabled(true);
				frameModifica.dispose();
			}catch(NumberFormatException exc) {
				if(textField7.getText().equals("")) {
					JOptionPane.showMessageDialog(new JFrame(), "Devi inserire un numero nell'ultima casella!");
				}else {
					JOptionPane.showMessageDialog(new JFrame(), "Devi inserire un numero nell'ultima casella!");
				}
			}
		}
		//---------------------------------------------------------------------------------------
		//elimina l'aroma selezionato
		else if(e.getSource() == btnRimuovi) {
			tempRow = table.getSelectedRow();

			if(tempRow >= 0) {
				btnAggiungi.setEnabled(false);
				btnModifica.setEnabled(false);
				btnRimuovi.setEnabled(false);
				comboBox.setEnabled(false);
				tempRow = table.getSelectedRow();
				if (JOptionPane.showConfirmDialog(new JFrame(), "Sei sicuro di voler cancellare questo aroma?", "Attenzione",
						JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					if(comboBox.getSelectedIndex() == 0) {
						int tempID = (int)model.getValueAt(tempRow, 4);
						model.removeRow(tempRow);
						for(int i = 0; i < fruttati.size(); i++) {
							if(fruttati.get(i).getID() == tempID)
								tempID = i;
						}
						fruttati.remove(tempID);
						fruttati.trimToSize();
					}else if(comboBox.getSelectedIndex() == 1) {
						int tempID = (int)model1.getValueAt(tempRow, 4);
						model1.removeRow(tempRow);
						for(int i = 0; i < cremosi.size(); i++) {
							if(cremosi.get(i).getID() == tempID)
								tempID = i;
						}
						cremosi.remove(tempID);
						cremosi.trimToSize();
					}else {
						int tempID = (int)model2.getValueAt(tempRow, 4);
						model2.removeRow(tempRow);
						for(int i = 0; i < tabaccosi.size(); i++) {
							if(tabaccosi.get(i).getID() == tempID)
								tempID = i;
						}
						tabaccosi.remove(tempID);
						tabaccosi.trimToSize();
					}
					btnAggiungi.setEnabled(true);
					btnModifica.setEnabled(true);
					btnRimuovi.setEnabled(true);
					comboBox.setEnabled(true);
				} else {
					btnAggiungi.setEnabled(true);
					btnModifica.setEnabled(true);
					btnRimuovi.setEnabled(true);
					comboBox.setEnabled(true);
				}
			}else {
				JOptionPane.showMessageDialog(new JFrame(), "Devi prima selezionare una riga della tabella!");
			}
		}

		//-----------------------------------------------------------
		//fa una copia dei soli gusti sulla clipboard del computer
		if(e.getSource() == copia) {
			copyToClipboard();
		}	

		//--------------------------------------------------------
		//calcola gli ml di base con nic da usare nel processo di creazione del liquido e
		//toglie gli ml di aroma usati dall'aroma presente nel database
		if(e.getSource() == btnMake) {
			makeLiquid();
		}

	}

	//-----------------------------------------------------------------------------------------------

	//scrive gli aromi su 3 file diversi con le caratteristiche di ogni aroma divise da ";"
	public void scriviSuFile() {
		try {

			FileWriter fw = new FileWriter(f);
			PrintWriter pw = new PrintWriter(fw);

			for(int i = 0; i < fruttati.size(); i++) {
				pw.println(fruttati.get(i).getNome() + ";" + fruttati.get(i).getGusto() + ";" + fruttati.get(i).getMarca() + ";" + fruttati.get(i).getMl());

			}
			pw.close();
			fw.close();

			FileWriter fw1 = new FileWriter(c);
			PrintWriter pw1 = new PrintWriter(fw1);

			for(int i = 0; i < cremosi.size(); i++) {
				pw1.println(cremosi.get(i).getNome() + ";" + cremosi.get(i).getGusto() + ";" + cremosi.get(i).getMarca() + ";" + cremosi.get(i).getMl());
			}
			pw1.close();
			fw1.close();


			FileWriter fw2 = new FileWriter(t);
			PrintWriter pw2 = new PrintWriter(fw2);

			for(int i = 0; i < tabaccosi.size(); i++) {
				pw2.println(tabaccosi.get(i).getNome() + ";" + tabaccosi.get(i).getGusto() + ";" + tabaccosi.get(i).getMarca() + ";" + tabaccosi.get(i).getMl());
			}
			pw2.close();
			fw2.close();

		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	//------------------------------------------------------------------------------------

	public void leggiDaFile() {
		try {
			Object[] temp = new Object[5];
			FileInputStream fin;
			String linea = new String();

			fin = new FileInputStream(f);
			Scanner in = new Scanner(fin);

			int id = 0;

			while(in.hasNextLine())
			{
				linea = in.nextLine();
				String elementi[] = linea.split(";");
				fruttati.add(new Aroma(id, elementi[0], elementi[1], elementi[2], Integer.parseInt(elementi[3])));
				temp[0] = elementi[0];
				temp[1] = elementi[1];
				temp[2] = elementi[2];
				temp[3] = elementi[3];
				temp[4] = id;

				model.addRow(temp);
				id++;
			}
			in.close();


			FileInputStream fin1;
			String linea1 = new String();

			fin1 = new FileInputStream(c);
			Scanner in1 = new Scanner(fin1);
			id = 0;
			while(in1.hasNextLine())
			{
				linea1 = in1.nextLine();
				String elementi[] = linea1.split(";");
				cremosi.add(new Aroma(id, elementi[0], elementi[1], elementi[2], Integer.parseInt(elementi[3])));
				temp[0] = elementi[0];
				temp[1] = elementi[1];
				temp[2] = elementi[2];
				temp[3] = elementi[3];
				temp[4] = id;

				model1.addRow(temp);
				id++;
			}
			
			in1.close();
			
			FileInputStream fin2;
			String linea2 = new String();

			fin2 = new FileInputStream(t);
			Scanner in2 = new Scanner(fin2);
			id = 0;
			while(in2.hasNextLine())
			{
				linea2 = in2.nextLine();
				String elementi[] = linea2.split(";");
				tabaccosi.add(new Aroma(id, elementi[0], elementi[1], elementi[2], Integer.parseInt(elementi[3])));
				temp[0] = elementi[0];
				temp[1] = elementi[1];
				temp[2] = elementi[2];
				temp[3] = elementi[3];
				temp[4] = id;

				model2.addRow(temp);
				id++;
			}

			in2.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Errore, file non trovato");
		} 

	}

	//------------------------------------------------------------------------
	//metodo per copiare tutti gli aromi sulla clipboard
	public void copyToClipboard() {
		String aromiCompleti = new String();

		aromiCompleti += "FRUTTATI:\n";
		for(int i = 0; i < fruttati.size(); i++) {
			aromiCompleti += fruttati.get(i).getGusto() + "\n";
		}
		aromiCompleti += "CREMOSI:\n";
		for(int i = 0; i < cremosi.size(); i++) {
			aromiCompleti += cremosi.get(i).getGusto() + "\n";
		}
		aromiCompleti += "TABACCOSI:\n";
		for(int i = 0; i < tabaccosi.size(); i++) {
			aromiCompleti += tabaccosi.get(i).getGusto() + "\n";
		}

		StringSelection stringSelection = new StringSelection(aromiCompleti);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}

	//-------------------------------------------------------------------------------------	

	public void aggiungiAroma() {
		Object[] temp = new Object[5];

		//aggiunge l'aroma dentro la scheda scelta con la combobox1
		//prende ogni scritta dai textField e le inserisce in un array oggetto temporaneo per poi aggiungerle 
		//all'Vector e alla tabella tramite il model apposito
		try {
			if(comboBox1.getSelectedItem().equals("Fruttati")) {
				int idNuovo = controllaID(fruttati);
				temp[0] = textField.getText().substring(0, 1).toUpperCase() + textField.getText().substring(1);
				temp[1] = textField_1.getText().substring(0, 1).toUpperCase() + textField_1.getText().substring(1);
				temp[2] = textField_2.getText().substring(0, 1).toUpperCase() + textField_2.getText().substring(1);
				temp[3] = textField_3.getText().substring(0, 1).toUpperCase() + textField_3.getText().substring(1);
				temp[4] = idNuovo;


				fruttati.add(new Aroma(idNuovo, (String)temp[0], (String)temp[1], (String)temp[2], Integer.parseInt((String)temp[3])));

				model.addRow(temp);
				table.updateUI();
			}else if(comboBox1.getSelectedItem().equals("Cremosi")) {
				int idNuovo = controllaID(cremosi);
				temp[0] = textField.getText().substring(0, 1).toUpperCase() + textField.getText().substring(1);
				temp[1] = textField_1.getText().substring(0, 1).toUpperCase() + textField_1.getText().substring(1);
				temp[2] = textField_2.getText().substring(0, 1).toUpperCase() + textField_2.getText().substring(1);
				temp[3] = textField_3.getText().substring(0, 1).toUpperCase() + textField_3.getText().substring(1);
				temp[4] = idNuovo;

				cremosi.add(new Aroma(idNuovo, (String)temp[0], (String)temp[1], (String)temp[2], Integer.parseInt((String)temp[3])));

				model1.addRow(temp);
				table.updateUI();
			}else if(comboBox1.getSelectedItem().equals("Tabaccosi")) {
				int idNuovo = controllaID(tabaccosi);
				temp[0] = textField.getText().substring(0, 1).toUpperCase() + textField.getText().substring(1);
				temp[1] = textField_1.getText().substring(0, 1).toUpperCase() + textField_1.getText().substring(1);
				temp[2] = textField_2.getText().substring(0, 1).toUpperCase() + textField_2.getText().substring(1);
				temp[3] = textField_3.getText().substring(0, 1).toUpperCase() + textField_3.getText().substring(1);
				temp[4] = idNuovo;

				tabaccosi.add(new Aroma(idNuovo, (String)temp[0], (String)temp[1], (String)temp[2], Integer.parseInt((String)temp[3])));

				model2.addRow(temp);
				table.updateUI();
			}	

			//resetta i textField 
			textField.setText("");
			textField_1.setText("");
			textField_2.setText("");
			textField_3.setText("");
		}catch(NumberFormatException e) {
			if(textField_3.getText().equals("")) {
				JOptionPane.showMessageDialog(new JFrame(), "Devi inserire un numero nell'ultima casella!");
			}else {
				JOptionPane.showMessageDialog(new JFrame(), "Devi inserire un numero nell'ultima casella!");
			}
		}

	}

	//---------------------------------------------------------------------------------

	//salva gli aromi su file quando viene chiuso il programma
	@Override
	public void windowClosing(WindowEvent e) {
		if(e.getSource() == this)
			scriviSuFile();
		else if(e.getSource() == frameModifica) {
			comboBox.setEnabled(true);
			table.setEnabled(true);
		}

	}

	//usato nella finestra aggiungi aroma con invio
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			aggiungiAroma();
		}

	}

	//--------------------------------------------------------------------------------------------------------------
	public void makeLiquid() {
		try {
			if(!textField9.getText().equals("") && !textField10.getText().equals("")) {

				for(int i = 0; i < fruttati.size(); i++) {
					if(textField8.getText().equalsIgnoreCase((String)model.getValueAt(i, 0))) {
						if((fruttati.get(i).getMl() - (Integer.parseInt(textField10.getText())) / 10 > 0)) {
							model.setValueAt((fruttati.get(i).getMl() - (Integer.parseInt(textField10.getText()) / 10)), i, 3);
							fruttati.get(i).setMl((int)(fruttati.get(i).getMl() - (Integer.parseInt(textField10.getText()) / 10)));
						}else if((fruttati.get(i).getMl() - (Integer.parseInt(textField10.getText())) / 10 == 0)){
							model.setValueAt((fruttati.get(i).getMl() - (Integer.parseInt(textField10.getText()) / 10)), i, 3);
							fruttati.get(i).setMl((int)(fruttati.get(i).getMl() - (Integer.parseInt(textField10.getText()) / 10)));
							if (JOptionPane.showConfirmDialog(new JFrame(), "Hai finito l'aroma, vuoi rimuoverlo?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
								fruttati.remove(i);
								model.removeRow(i);
							}
						}else {
							JOptionPane.showMessageDialog(new JFrame(), "Non ne hai abbastanza!");
						}
						break;
					}
				}

				for(int i = 0; i < cremosi.size(); i++) {
					if(textField8.getText().equalsIgnoreCase(cremosi.get(i).getNome())) {
						if((cremosi.get(i).getMl() - (Integer.parseInt(textField10.getText())) / 10 > 0)) {
							model1.setValueAt((cremosi.get(i).getMl() - (Integer.parseInt(textField10.getText()) / 10)), i, 3);
							cremosi.get(i).setMl((int)(cremosi.get(i).getMl() - (Integer.parseInt(textField10.getText()) / 10)));
						}else if((cremosi.get(i).getMl() - (Integer.parseInt(textField10.getText())) / 10 == 0)){
							model1.setValueAt((cremosi.get(i).getMl() - (Integer.parseInt(textField10.getText()) / 10)), i, 3);
							cremosi.get(i).setMl((int)(cremosi.get(i).getMl() - (Integer.parseInt(textField10.getText()) / 10)));
							if (JOptionPane.showConfirmDialog(new JFrame(), "Hai finito l'aroma, vuoi rimuoverlo?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
								cremosi.remove(i);
								model1.removeRow(i);
							}
						}else {
							JOptionPane.showMessageDialog(new JFrame(), "Non ne hai abbastanza!");
						}


					}
				}

				for(int i = 0; i < tabaccosi.size(); i++) {
					if(textField8.getText().equalsIgnoreCase(tabaccosi.get(i).getNome())) {
						if((tabaccosi.get(i).getMl() - (Integer.parseInt(textField10.getText())) / 10 > 0)) {
							model2.setValueAt((tabaccosi.get(i).getMl() - (Integer.parseInt(textField10.getText()) / 10)), i, 3);
							tabaccosi.get(i).setMl((int)(tabaccosi.get(i).getMl() - (Integer.parseInt(textField10.getText()) / 10)));
						}else if((tabaccosi.get(i).getMl() - (Integer.parseInt(textField10.getText())) / 10 == 0)){
							model2.setValueAt((tabaccosi.get(i).getMl() - (Integer.parseInt(textField10.getText()) / 10)), i, 3);
							tabaccosi.get(i).setMl((int)(tabaccosi.get(i).getMl() - (Integer.parseInt(textField10.getText()) / 10)));
							if (JOptionPane.showConfirmDialog(new JFrame(), "Hai finito l'aroma, vuoi rimuoverlo?", "WARNING", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
								tabaccosi.remove(i);
								model2.removeRow(i);
							}
						}else {
							JOptionPane.showMessageDialog(new JFrame(), "Non ne hai abbastanza!");
						}


					}
				}


				double quantBase = 0;
				String temp[] = ((String)comboBox3.getSelectedItem()).split(" ");
				quantBase = (Double.parseDouble(textField9.getText()) * Double.parseDouble(textField10.getText()) / Double.parseDouble(temp[0]));

				lblMlDiBase.setText("ml di base nic: " + quantBase + "ml");

				table.updateUI();
			} else {
				JOptionPane.showMessageDialog(new JFrame(), "Riempi tutti i campi!");
			}
		}catch(NumberFormatException exc) {
			JOptionPane.showMessageDialog(new JFrame(), "Devi inserire dei numeri!");
		}
	}

	public int controllaID(Vector<Aroma> aroma) {
		int max = 0;
		for(int i = 0; i < aroma.size(); i++) {
			if(aroma.get(i).getID() > max) {
				max = aroma.get(i).getID();
			}
		}

		return (max + 1);
	}

	public void getAromaForModifica(int n) {
		textField4.setText((String)table.getValueAt(table.getSelectedRow(), 0));
		textField5.setText((String)table.getValueAt(table.getSelectedRow(), 1));
		textField6.setText((String)table.getValueAt(table.getSelectedRow(), 2));
		textField7.setText((String)table.getValueAt(table.getSelectedRow(), 3));
		comboBox2.setSelectedIndex(0);
	}


	//classe per fare in modo che le celle della tabella non siano modificabili con il doppio click
	public class NonEditableTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 1L;

		public NonEditableTableModel() {

		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	}



	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowClosed(WindowEvent arg0) {}
	@Override
	public void windowDeactivated(WindowEvent arg0) {}
	@Override
	public void windowDeiconified(WindowEvent arg0) {}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	@Override
	public void windowOpened(WindowEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
	@Override
	public void keyReleased(KeyEvent arg0) {}

	public static void main(String[] args) {

		new Vapinator();

	}
}
