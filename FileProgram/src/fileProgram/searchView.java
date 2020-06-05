package fileProgram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class searchView extends JFrame implements Runnable{
		Vector<String> fileName ;
		ArrayList<String> fName;
		ArrayList<String> fSize;
		ArrayList<String> fModified;
		ArrayList<String> fPath;
		DefaultListModel model ;
		static ArrayList<String> deleteName;
		
		JList<String> fileList;
		Container mainContainer;
		JPanel textAreaPanel;
		JPanel btnPanel;
		
		JTable tableFile;
		JButton btnDelete;
		JButton btnCancel;
		
		int flag;
		
		searchView(){
			fileName = new Vector<String>(3);
			fName = new ArrayList<String>();
			fSize = new ArrayList<String>();
			fModified = new ArrayList<String>();
			fPath = new ArrayList<String>();
			model = new DefaultListModel<>();
			deleteName = new ArrayList<String>();
			flag = 0;
			
			setTitle("검색창");
			this.setSize(1000,300);
			this.setLocation(30,30);
			
			mainContainer=this.getContentPane();
			mainContainer.setLayout(new BorderLayout(0,0));
			mainContainer.setBackground(Color.lightGray);
			
			mkGrid();
			
//			subDirList(FileProgramView.location);
		}
		
		void mkGrid() {
			
			textAreaPanel = new JPanel();
			btnPanel = new JPanel();
			btnPanel.setLayout(new FlowLayout(1));
			btnDelete = new JButton("파일삭제");
			btnCancel = new JButton("검색취소");
			
//			fileList = new JList(fileName);
			fileList = new JList(model);
			fileList.setFixedCellWidth(900);
			
			JScrollPane s = new JScrollPane(fileList);
			
			
			
			textAreaPanel.add(s);
			btnPanel.add(btnDelete);
			btnPanel.add(btnCancel);
			
			mainContainer.add(textAreaPanel,BorderLayout.CENTER);
			mainContainer.add(btnPanel,BorderLayout.SOUTH);
			
			
		}


		public synchronized void subDirList(String source, String name){
			SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			File dir = new File(source); 
			String temp = new String();
			int forCheck = 0;
			int index = 0; 
			int tempIndex = 0;
			int tempIndex2 = 0;
			
			if(dir.listFiles() != null) {
			File[] fileList = dir.listFiles(); 
			try{
				for(int i = 0 ; i < fileList.length ; i++){
					if(flag == 1) {
						break;
					}
					File file = fileList[i]; 
					if(file.isFile()){		//파일이 있을 경우
							
						forCheck = 0;
						String[] searchSp = name.split("\\.");
						String[] fileSp = file.getName().split("\\.");
						ArrayList<Integer> indexList = new ArrayList<Integer>();
						
						if(file.getName().equals(name)) {		//ex) a.txt
							forCheck = 1;
						}
						else if(searchSp.length != 0 && searchSp[searchSp.length-1].contains("*")) {	//확장자에 *가 있을 때
							
							if(fileSp.length == searchSp.length) {
								for(int j = 0; j < searchSp.length-1;j++) {
									
									if(!searchSp[j].contains("?") && !searchSp[j].contains("*")) {	//이름에는 *없을 때 
										System.out.println("!? * : " + file.getName());
										if(fileSp[j].equals(searchSp[j])){
											forCheck = 1;
										}
									}
									else if(searchSp[j].contains("*")){		//이름에 *이 있을 때 ex) *a.*
										if(searchSp[j].length()> 0 && fileSp[j].length() > 0) {
											index = searchSp[j].length()-1;
											tempIndex = fileSp[j].length()-1;
										
											if(String.valueOf(fileSp[j].charAt(tempIndex)).equals(String.valueOf(searchSp[j].charAt(index)))) {
											forCheck = 1;
											}
										}
									}
									else if(searchSp[j].contains("?")) {
										tempIndex = 0;
										do {
											tempIndex = searchSp[0].indexOf("?", tempIndex);
											if(tempIndex == -1)
												break;
											indexList.add(tempIndex);
											tempIndex++;
											System.out.println(indexList.get(0));
										}while(tempIndex+1 < searchSp[0].length());
										
										if(searchSp[j].length() == fileSp[j].length()) {
											for(int a = 0; a < indexList.size();a++) {
												if(a == 0 && indexList.get(a) == 0) {
												}
												else if(a != 0 && indexList.get(a)-indexList.get(a-1)-1 != 0) {
													if(!fileSp[j].substring(indexList.get(a-1)+1, indexList.get(a)).equals(
															searchSp[j].substring(indexList.get(a-1)+1, indexList.get(a)))){
														break;
													}
												}
												else if(a != 0 && indexList.get(a)-indexList.get(a-1)-1 == 0) {
												}
												if(a == indexList.size()-1) {											
													forCheck =1;
												}
											}
										}
									}
									
								}

							}
							else {continue;}
						}
						
						
						if(forCheck == 1) {			//조건에 만족하면 파일정보를 가져옴
							Long lastModified = file.lastModified();
							Date date = new Date(lastModified);
							// 파일이 있다면 파일 이름 출력
							fName.add(file.getName());
							temp = file.getName() + "   ";
							fSize.add(String.valueOf(file.length()));
							temp += String.valueOf(file.length())+"   ";
							fModified.add(transFormat.format(date));
							temp += transFormat.format(date)+"   ";
							fPath.add(file.getPath());	
							temp += file.getPath()+"   ";
							fileName.add(temp);
							model.addElement(temp);
							System.out.println("\t 파일 이름 = " + temp);
						}
					}else if(file.isDirectory()){		//하위 디렉토리를 위해 재귀	
						System.out.println("디렉토리 이름 = " + file.getName());
						// 서브디렉토리가 존재하면 재귀적 방법으로 다시 탐색
						subDirList(file.getCanonicalPath().toString(), name); 
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				btnDelete.addActionListener(new ActionListener() {  // 버튼이 눌렸을 때의 작동방식 선언. 여기서는 임시 ActionListener생성
					public void actionPerformed(ActionEvent arg0) { // ActionListener의 구체적인 내용 작성 
						if(flag == 0) {
							JOptionPane.showMessageDialog(null, "검색중입니다.", "오류", JOptionPane.ERROR_MESSAGE);
						}
						else {
							deleteFile();
						}
					}
				});
				
				btnCancel.addActionListener(new ActionListener() {  // 버튼이 눌렸을 때의 작동방식 선언. 여기서는 임시 ActionListener생성
					public void actionPerformed(ActionEvent arg0) { // ActionListener의 구체적인 내용 작성 
						
						flag = 1;
					}
				});
				
			}catch(IOException e){
			}
			}
		}
		
		public synchronized void deleteFile(){
			int num = 0;
			int dFlag = 0;
			String tempStr = new String();
			if(searchView.this.fileList.isSelectionEmpty ()) {	
			}
			else {
				num = searchView.this.fileList.getSelectedIndex();
				searchView.this.model.removeElementAt(num);
				System.out.println(fName.get(num));
				for(int i = 0; i < deleteName.size(); i++) {
					if(fPath.get(num).equals(deleteName.get(i))) {
						dFlag = 1;
						break;
					}
				}
				if(dFlag == 0) {
					File dFile = new File(fPath.get(num));
					if(dFile.exists()) {
						if(dFile.delete()) {
							JOptionPane.showMessageDialog(null, "삭제되었습니다.", "확인", JOptionPane.PLAIN_MESSAGE);
						}
						else {
							JOptionPane.showMessageDialog(null, "삭제에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				else if(dFlag == 1) {
					JOptionPane.showMessageDialog(null, "파일이 존재하지 않습니다.(이미 삭제된 파일입니다.)", "오류", JOptionPane.ERROR_MESSAGE);
				}
				
				deleteName.add(fPath.get(num));
				
				searchView.this.fileList.clearSelection();
			}
		}
		@Override
		public void run() {
			searchView sV = new searchView();
			setVisible(true);

			subDirList(FileProgramView.location, FileProgramView.name);
			if(flag == 0) {
				flag = 1;
				JOptionPane.showMessageDialog(null, "검색이 완료되었습니다.", "종료", JOptionPane.PLAIN_MESSAGE);
			}
		}
	}

