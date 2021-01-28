package ReadingNoteBook;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class start extends JFrame{
	JLabel totalLabel = new JLabel("0");
	DateFormat sdFormat = new SimpleDateFormat("yyyy.MM.dd.E����");
	Date nowDate = new Date();
	String date = sdFormat.format(nowDate);
	
	JLabel titleLabel = new JLabel("����");
	JLabel genreLabel = new JLabel("�о�");
	JLabel authorLabel = new JLabel("�۰�");
	JCheckBox finish = new JCheckBox("�ϵ�");
	JCheckBox repeat = new JCheckBox("�絶");
	JLabel publisherLabel = new JLabel("���ǻ�");
	JLabel pageLabel = new JLabel("������ ��");
	JLabel startL = new JLabel("�б� ������ ��");
	JLabel endL = new JLabel("�� ���� ��");
	JLabel [] sentenceL = new JLabel[3];	
	//String g = new String("null");
	//String fin = new String("null");
	//String re= new String("null");
	String g ="";
	String fin = "";
	String re = "";
	String [] c = new String [13]; //�ε� �Ҷ� ����ϴ� �迭 (����, �о�, �۰�, ���ǻ�, ������ ��, ������, ���� ��, �ϵ�, �絶, ����3��, ����)
	String lp; int lpn;
	String st = "";
	int totalpage;
	int savetp;
	String delFileName;
	String [] genre = {"�����ϼ���","��,�Ҽ�,���", "SF", "�ι���", "������", "��ȸ����", "����", "����/�濵", "��Ÿ"};
	JComboBox<String> lgenrecb = new JComboBox<String>(genre);
	JTextField ltitle = new JTextField(15);
	JTextField lauthor = new JTextField(13);
	JTextField lpublisher = new JTextField(10);
	JTextField lpage = new JTextField(6);
	JTextArea [] lsentenceArray = new JTextArea[3];
	
	JTextField lstartdate = new JTextField(8);
	JTextField lenddate = new JTextField(8);
	
	JTextArea lnote = new JTextArea(18, 32);
	
	JCheckBox lfinish = new JCheckBox("�ϵ�");
	JCheckBox lrepeat = new JCheckBox("�絶");
	
	String lg = "";
	String lfin = "";
	String lre = "";
	int tmp;
	File pf = new File("totalpage.txt");
	
	public start() {
		setTitle("������Ʈ"); setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		c.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		c.setBackground(new Color(231, 225, 217));
		
		JLabel mainTitle = new JLabel("������Ʈ ���α׷�");
		mainTitle.setFont(new Font("���� ���", Font.PLAIN, 30));
		
		ImageIcon imgbook = new ImageIcon("images/book.png");
		JButton book = new JButton ("����ϱ�", imgbook);
		book.setFont(new Font ("���� ���", Font.PLAIN, 20));
		book.setBackground(new Color(247, 215, 130));
		book.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				write w = new write();
			}
		});		
		JLabel totalMessage = new JLabel("���ݱ��� ���� ������ :");
		totalMessage.setFont(new Font ("���� ���", Font.PLAIN, 23));
		totalLabel.setFont(new Font ("���� ���", Font.PLAIN, 23));
		JLabel total = new JLabel("");
		total.setFont(new Font ("���� ���", Font.PLAIN, 20));

		int n; String str = "";
		try {
			FileReader pfr = new FileReader(pf);
			BufferedReader pbr = new BufferedReader(pfr);
			
			for( ; ; ) {
				n = pfr.read();
				if(n == -1) break;
				str += (char)n;
			}
			totalLabel.setText(str);
		}	
		catch (IOException e1) {
			e1.printStackTrace();
		}
		
		totalpage = Integer.parseInt(str);
		
		JButton loading  = new JButton ("�ҷ�����");
		loading.setFont(new Font ("���� ���", Font.PLAIN, 20));
		loading.setBackground(new Color(247, 215, 130));
		listListener listener = new listListener();
		loading.addActionListener(listener);
		loading.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				list l = new list();
			}
		});
		
		JButton exit = new JButton ("�����ϱ�");
		exit.setFont(new Font("���� ���", Font.PLAIN, 20));
		exit.setBackground(new Color(190, 173, 143));
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try {
					FileWriter pfw = new FileWriter(pf);
					BufferedWriter pbw = new BufferedWriter(pfw);
					pbw.write(Integer.toString(totalpage)); pbw.flush();
					
				} 
				catch (IOException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}	
		});
		
		
		JButton timerB = new JButton ("Ÿ�̸�");
		timerB.setFont(new Font("���� ���", Font.PLAIN, 20));
		timerB.setBackground(new Color(247, 215, 130));	
		timerB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				timer t = new timer();
			}
		});
		
		c.add(mainTitle);
		c.add(book);
		c.add(totalMessage);
		c.add(totalLabel);
		c.add(total);
		c.add(loading);
		add(timerB);
		c.add(exit);
		
		setSize(400, 850); setVisible(true);
		
	}
	
	 public class timer extends JFrame{
		JTextField timetf = new JTextField(3);

		Clip clip ;
		public timer() {
			setTitle("Ÿ�̸�"); 
			Container c = getContentPane();
			c.setLayout(null);
			c.setBackground(new Color(231, 225, 217));
			
			timetf.setFont(new Font("���� ���", Font.PLAIN, 15));
			timetf.setBounds(105, 25, 80, 50); 
			
			JLabel label = new JLabel("��");
			label.setFont(new Font("���� ���", Font.PLAIN, 20));
			label.setBounds(185, 25, 100, 50); 
			
			JButton start = new JButton("����");
			start.setFont(new Font("���� ���", Font.PLAIN, 15));
			start.setBounds(225, 25, 65, 50); 
			start.setBackground(new Color(247, 215, 130));
			timerRun l = new timerRun();
			start.addActionListener(l);
			
			ImageIcon imgclock = new ImageIcon("images/clock.png");
			Image img = imgclock.getImage();
			Image changedimg = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
			ImageIcon changedicon = new ImageIcon(changedimg);
			
			JLabel clock = new JLabel(changedicon);
			clock.setBounds(100, 80, 200, 200);
			
			JButton close = new JButton ("�ݱ�");
			close.setFont(new Font("���� ���", Font.PLAIN, 15));
			close.setBounds(150, 275, 100, 50);
			close.setBackground(new Color(247, 215, 130));	
			close.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});

			c.add(timetf);
			c.add(label);
			c.add(start);
			c.add(clock);
			c.add(close);
			setSize(400, 400); setVisible(true);
		}
		public class timerRun implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				int tm = Integer.parseInt(timetf.getText());
				int ts = tm * 60;
				int sm ; int ss;
				int i = 0 ;;
				
				try {
					clip = AudioSystem.getClip();
					File audioFile = new File("audio/alarm.wav");
					AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
					clip.open(audioStream);
				} 
				catch (LineUnavailableException e2) {e2.printStackTrace();} 
				catch (UnsupportedAudioFileException e1) {e1.printStackTrace();}
				catch (IOException e1) {e1.printStackTrace();}
				
				for(i = ts; i >= 0; i--) {
					sm = i/60;
					ss = i%60;
					System.out.println(sm + ":" + ss);
					
					try {
						Thread.sleep(1000);
						
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				clip.start();
			}
			
		}
	}
	
	public class write extends JFrame {
		
		JComboBox<String> genrecb = new JComboBox<String>(genre);
		JTextField title = new JTextField(15);
		JTextField author = new JTextField(13);
		JTextField publisher = new JTextField(10);
		
		JTextField page = new JTextField(6);
		
		JTextArea [] sentenceArray = new JTextArea[3];
		
		JTextField startdate = new JTextField(8);
		JTextField enddate = new JTextField(8);
		
		JTextArea note = new JTextArea(18, 32);
		
		
		public write(){
			setTitle("���â"); 
			Container c = getContentPane(); 
			c.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
			
			JLabel today = new JLabel(date);
			today.setForeground(Color.black);
			today.setFont(new Font("���� ���", Font.BOLD, 40));
			c.add(today);
			
			genrecb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					g = genrecb.getSelectedItem().toString();
				}
			});
			
			JButton save = new JButton("����");
			saveListener listener = new saveListener();
			save.addActionListener(listener);
			save.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			finish.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					fin = "�ϵ�";
				}
			});
			repeat.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					re = "�絶";
					st = "(�絶) ";
				}
			});			
			
			JButton exit = new JButton("�ݱ�");
			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					int result = JOptionPane.showConfirmDialog(null,
							"����� �׸��νðڽ��ϱ�?", "Confirm", JOptionPane.YES_NO_OPTION);
					if(result == JOptionPane.YES_OPTION) 
						dispose();
				}
			});
			
			c.add(titleLabel);
			c.add(title);
			c.add(genreLabel);
			c.add(genrecb);
			c.add(authorLabel);
			c.add(author);
			c.add(publisherLabel);
			c.add(publisher);
			c.add(pageLabel);
			c.add(page);
			c.add(startL);
			c.add(startdate);
			c.add(endL);
			c.add(enddate);
			c.add(finish);
			c.add(repeat);
			
			for(int i = 0; i < sentenceArray.length; i++) {
				c.add(sentenceL[i] = new JLabel("������ ��� ����"));
				c.add(new JScrollPane(sentenceArray[i] = new JTextArea(3, 32)));
			}
			c.add(new JLabel("��Ʈ"));
			c.add(new JScrollPane(note));
			c.add(save);
			c.add(exit);
			
			setSize(400, 900); setVisible(true);
		}
		
		public class saveListener extends JFrame implements ActionListener{
			
			public void actionPerformed (ActionEvent e) { 
				st += title.getText();
			    String sa = author.getText();
			    String spub = publisher.getText();
			    String sp = page.getText();
			    int spn = Integer.parseInt(sp);
				String ssd = startdate.getText();
				String sed = enddate.getText();
				
				totalpage += spn;
				totalLabel.setText(Integer.toString(totalpage));
				
				FileWriter fw; 
				try {
					fw = new FileWriter("notes/" + st + ".txt");
					fw.write(st); fw.write("//"); // ����
					fw.write(g); fw.write("//"); // �о�
					fw.write(sa); fw.write("//"); // �۰�
					fw.write(spub);fw.write("//"); // ���ǻ�
					fw.write(sp);fw.write("//"); // ������ ��
					fw.write(ssd);fw.write("//"); // �б� ������ ��¥
					fw.write(sed);fw.write("//"); // �� ���� ��¥
					fw.write(fin); fw.write("//"); // �ϵ� ����
					fw.write(re); fw.write("//"); // �絶 ����
					
					// ������ ��� ���� 1
					String impression_1 = sentenceArray[0].getText();
					fw.write(impression_1); fw.write("//"); 
					impression_1 = impression_1.replace("\n","\r\n");	
					
					// ������ ��� ���� 2	
					String impression_2 = sentenceArray[1].getText();
					fw.write(impression_2); fw.write("//");
					impression_2 = impression_2.replace("\n","\r\n");	
					
					// ������ ��� ���� 3
					String impression_3 = sentenceArray[2].getText();
					fw.write(impression_3); fw.write("//");
					impression_3 = impression_3.replace("\n","\r\n");	
					
					String n = note.getText();
					fw.write(n); 
					n = n.replace("\n","\r\n");	
					
					fw.close(); 
					
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				st = "";
				finish.setSelected(false);
				repeat.setSelected(false);
				fin = ""; re = ""; 
				JOptionPane.showConfirmDialog(null, "����Ǿ����ϴ�.", 
						"Message", JOptionPane.PLAIN_MESSAGE);
				
			}
			
		}
	}
	
	public class list extends JFrame {
		
		public list() {
			setTitle("�ҷ�����"); Container c = getContentPane(); 
			c.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
			
			JLabel today = new JLabel(date);
			today.setForeground(Color.black);
			today.setFont(new Font("���� ���", Font.BOLD, 40));
			c.add(today);
			
			JComboBox<String> genrecb = new JComboBox<String>(genre);
			genrecb.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					lg = genrecb.getSelectedItem().toString();
				}
			});
			
			finish.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					lfin = "�ϵ�";
				}
			});
			repeat.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					lre = "�絶";
				}
			});			
			JButton revise = new JButton("����");
			reviseListener relsn = new reviseListener();
			revise.addActionListener(relsn);
			revise.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			JButton exit = new JButton("�ݱ�");
			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					dispose();
					ltitle.setText("");
					lgenrecb.setSelectedIndex(0);
					lauthor.setText("");
					lpublisher.setText("");
					lpage.setText("");
					lfin = "";
					lre = "";
					lstartdate.setText("");
					lenddate.setText("");
					lfinish.setSelected(false);
					lrepeat.setSelected(false);
					lnote.setText("");
				}
			});
			
			c.add(titleLabel);
			c.add(ltitle);
			c.add(genreLabel);
			c.add(lgenrecb);
			c.add(authorLabel);
			c.add(lauthor);
			c.add(publisherLabel);
			c.add(lpublisher);
			c.add(pageLabel);
			c.add(lpage);
			c.add(startL);
			c.add(lstartdate);
			c.add(endL);
			c.add(lenddate);
			c.add(lfinish);
			c.add(lrepeat);
			
			for(int i = 0; i < lsentenceArray.length; i++) {
				c.add(sentenceL[i] = new JLabel("������ ��� ����"));
				c.add(new JScrollPane(lsentenceArray[i] = new JTextArea(3, 32)));
			}
			c.add(new JLabel("��Ʈ"));
			c.add(new JScrollPane(lnote));
			c.add(revise);
			c.add(exit);
			
			setSize(400, 900); setVisible(true);
		}
		
		public class reviseListener extends JFrame implements ActionListener{
		
			public void actionPerformed (ActionEvent e) {
			
				listListener l = new listListener();
			
				String lt = ltitle.getText();
				String la = lauthor.getText();
				String lpub = lpublisher.getText();
				lp = lpage.getText();
				lpn = Integer.parseInt(lp);
				totalpage = totalpage + lpn - tmp;
				String rp = Integer.toString(totalpage);
				totalLabel.setText(rp);
				String lsd = lstartdate.getText();
				String led = lenddate.getText();
				
			
				FileWriter fw; 
				File file = new File("notes/" + delFileName);
			
				if(file.exists())
					file.delete();	
			
				try {
					fw = new FileWriter("notes/" + lt + ".txt");
					fw.write(lt); fw.write("//"); // ����
					fw.write(lg); fw.write("//"); // �о�
					fw.write(la); fw.write("//"); // �۰�
					fw.write(lpub);fw.write("//"); // ���ǻ�
					fw.write(lp);fw.write("//"); // ������ ��
					fw.write(lsd);fw.write("//"); // �б� ������ ��¥
					fw.write(led);fw.write("//"); // �� ���� ��¥
					fw.write(lfin); fw.write("//"); // �ϵ� ����
					fw.write(lre); fw.write("//"); // �絶 ����
				
					// ������ ��� ���� 1
					String impression_1 = lsentenceArray[0].getText();
					fw.write(impression_1); fw.write("//"); 
					impression_1 = impression_1.replace("\n","\r\n");	
				
					// ������ ��� ���� 2	
					String impression_2 = lsentenceArray[1].getText();
					fw.write(impression_2); fw.write("//");
					impression_2 = impression_2.replace("\n","\r\n");	
				
					// ������ ��� ���� 3
					String impression_3 = lsentenceArray[2].getText();
					fw.write(impression_3); fw.write("//");
					impression_3 = impression_3.replace("\n","\r\n");	
				
					String n = lnote.getText();
					fw.write(n); 
					n = n.replace("\n","\r\n");	
				
					fw.close(); 
				
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			
				JOptionPane.showConfirmDialog(null,
						"�����Ǿ����ϴ�.", "Message", JOptionPane.PLAIN_MESSAGE);
				tmp = 0;
			}
		}
	}
	
	class listListener implements ActionListener {
		private JFileChooser chooser;
		
		public listListener() {
			chooser = new JFileChooser();
		}
	
		public void actionPerformed(ActionEvent e) {
			FileNameExtensionFilter filter 
					= new FileNameExtensionFilter("txt", "txt");

			chooser.setFileFilter(filter);
			
			int ret = chooser.showOpenDialog(null);
			if(ret != JFileChooser.APPROVE_OPTION) {
				JOptionPane.showMessageDialog(null,
						"������ �������� �ʾҽ��ϴ�.","���", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			String path = chooser.getSelectedFile().getPath();
			delFileName = chooser.getSelectedFile().getName();
			
			String [] c = new String [13]; 
			//����, �о�, �۰�, ���ǻ�, ������ ��, ������, ���� ��, �ϵ�, �絶, ����3��, ����
			
			int k; String s = "";
			
			try {
				FileReader fr = new FileReader(path);
				BufferedReader br = new BufferedReader(fr);
				
				for( ; ; ) {
		               k = fr.read();
		               if( k == -1) break;
		               s += (char)k;
		        }
				
				String[] splited = s.split("//");
				for(int i = 0; i < splited.length; i++) {
					c[i] = splited [i];
				}
				
				ltitle.setText(c[0]);
				String gnstr = c[1];
				switch(gnstr) {
				case "null":
					lgenrecb.setSelectedIndex(0); break;
				case "��,�Ҽ�,���" :
					lgenrecb.setSelectedIndex(1); break;
				case "SF":
					lgenrecb.setSelectedIndex(2); break;
				case "�ι���":
					lgenrecb.setSelectedIndex(3); break;
				case "������":
					lgenrecb.setSelectedIndex(4); break;
				case "��ȸ����":
					lgenrecb.setSelectedIndex(5); break;
				case "����":
					lgenrecb.setSelectedIndex(6); break;
				case "����/�濵":
					lgenrecb.setSelectedIndex(7); break;
				case "��Ÿ":
					lgenrecb.setSelectedIndex(8); break;
				
				}
				lauthor.setText(c[2]);
				lpublisher.setText(c[3]);
				lpage.setText(c[4]);
				
				String pl = c[4];
				tmp = Integer.parseInt(pl);
				
				lstartdate.setText(c[5]);
				lenddate.setText(c[6]);
				if(c[7].equals("�ϵ�"))
					lfinish.setSelected(true);
				if(c[8].equals("�絶"))
					lrepeat.setSelected(true);
				for(int i = 0; i < 3; i++) {
					lsentenceArray[i].setText(c[9+i]);
				}
				lnote.setText(c[12]);
				
				fr.close(); 
			 }

			 catch(Exception e2) {
				 e2.printStackTrace();
			 }
			
		}
		
	}
	
	public static void main(String[] args) {
		new start();
	}
}



