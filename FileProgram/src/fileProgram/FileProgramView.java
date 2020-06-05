package fileProgram;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class FileProgramView extends JFrame{
	static String location;
	static String name;
	
	private Container mainContainer;
	JPanel firstPanel;
	JPanel secondPanel;
	JPanel thirdPanel;
	
	JTextField locationField;
	JTextField searchField;
	
	JButton btnSearch;
	JButton btnExit;
	
	FileProgramView(String title){
		super(title);
		this.setSize(500,200);
		this.setLocation(100,100);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainContainer=this.getContentPane();
		mainContainer.setLayout(new GridLayout(3,1));
		mainContainer.setBackground(Color.WHITE);
//		this.getRootPane().setBorder(BorderFactory.createMatteBorder(4,4,4,4, Color.BLACK));
		
		mkGrid();
		
	}
	
	public void mkGrid() {
		JLabel labelLocation = new JLabel("경로명 : ");
		JLabel labelSearch = new JLabel("검색어 : ");
		locationField = new JTextField(35);
		searchField = new JTextField(35);
		btnSearch = new JButton("검색");
		btnExit = new JButton("종료");
		btnSearch.setBackground(Color.GRAY);
		btnExit.setBackground(Color.gray);
		

		btnSearch.addActionListener(new ActionListener() {  // 버튼이 눌렸을 때의 작동방식 선언. 여기서는 임시 ActionListener생성
			public void actionPerformed(ActionEvent arg0) { // ActionListener의 구체적인 내용 작성 
				String[] searchSp = searchField.getText().split("\\.");
				
				try {
					if(locationField.getText().isEmpty()) {
						throw new NullFileNameException();
					}
					else if(searchField.getText().isEmpty()) {
						throw new NullSearchNameException();
					}
					else if(locationField.getText().contains("/") || searchField.getText().contains("/") || 
							searchField.getText().contains(":") || 
							locationField.getText().contains("\"") || searchField.getText().contains("\"") || 
							locationField.getText().contains(">") || searchField.getText().contains(">") || 
							locationField.getText().contains("<") || searchField.getText().contains("<") || 
							locationField.getText().contains("|") || searchField.getText().contains("|") || 
							locationField.getText().contains("?")) {
						throw new ErrorSearchNameException();
					}
					else if(searchSp[searchSp.length-1].contains("?")) {
						throw new ErrorFileNameException();
					}
					
					File file = new File(locationField.getText());
					if(!file.isDirectory()) {
						throw new NullDirectoryException();
					}
					
					location = locationField.getText();
					name = searchField.getText();
					
					searchView searchView1 = new searchView();
					Thread searchViewTh = new Thread(searchView1);
					searchViewTh.start();
				}
				catch(NullFileNameException e) {
					JOptionPane.showMessageDialog(null, "입력된 경로가 없습니다.", "오류", JOptionPane.ERROR_MESSAGE);

				}
				catch(NullSearchNameException se) {
					JOptionPane.showMessageDialog(null, "검색할 것을 입력해주십시오.", "오류", JOptionPane.ERROR_MESSAGE);
				}
				catch(ErrorSearchNameException ene) {
					JOptionPane.showMessageDialog(null, "허용하지 않는 문자가 있습니다", "오류", JOptionPane.ERROR_MESSAGE);
				}
				catch(ErrorFileNameException efne) {
					JOptionPane.showMessageDialog(null, "확장자에 ?가 있습니다.", "오류", JOptionPane.ERROR_MESSAGE);
				} 
				catch (NullDirectoryException e) {
					JOptionPane.showMessageDialog(null, "디렉토리가 존재하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
					
			}
		});
		
		btnExit.addActionListener(new ActionListener() {  // 버튼이 눌렸을 때의 작동방식 선언. 여기서는 임시 ActionListener생성
			public void actionPerformed(ActionEvent arg0) { // ActionListener의 구체적인 내용 작성 
				System.out.println("Exit");
				System.exit(0);
			}
		});
		
		firstPanel = new JPanel(); secondPanel = new JPanel(); thirdPanel = new JPanel();
		firstPanel.setBackground(Color.LIGHT_GRAY);
		secondPanel.setBackground(Color.LIGHT_GRAY);
		thirdPanel.setBackground(Color.LIGHT_GRAY);
		
		firstPanel.setLayout(null);
		secondPanel.setLayout(null);
		thirdPanel.setLayout(null);
		
		firstPanel.add(labelLocation); labelLocation.setBounds(0,10,70,20);
		firstPanel.add(locationField); locationField.setBounds(75,10,400,40);
		
		secondPanel.add(labelSearch); labelSearch.setBounds(0,10,70,20);
		secondPanel.add(searchField); searchField.setBounds(75,10,400,40);
		
		thirdPanel.add(btnSearch); btnSearch.setBounds(130,20,100,30);
		thirdPanel.add(btnExit); btnExit.setBounds(250,20,100,30);
		
		
		mainContainer.add(firstPanel);
		mainContainer.add(secondPanel);
		mainContainer.add(thirdPanel);
		
	}
	

	public static void main(String[] args) {
		FileProgramView FPV = new FileProgramView("FileProgram");
		FPV.setVisible(true);
	}
}

class NullFileNameException extends Exception {
	NullFileNameException(){
        super();
    }
}
class NullSearchNameException extends Exception {
	NullSearchNameException(){
        super();
    }
}

class ErrorFileNameException extends Exception {
	ErrorFileNameException(){
        super();
    }
}
class ErrorSearchNameException extends Exception {
	ErrorSearchNameException(){
        super();
    }
}
class NullDirectoryException extends Exception {
	NullDirectoryException(){
        super();
    }
}


