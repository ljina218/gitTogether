package project.chat;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.xml.stream.events.StartDocument;

public class JinTalkChatter2 extends JFrame implements Runnable, ActionListener{
	
	JPanel jp_c = new JPanel();
	JPanel jp_e = new JPanel();
	JPanel jp_s = new JPanel();
	JPanel jp_s1 = new JPanel();
	JPanel jp_s2 = new JPanel();
	JTextArea jta_log = new JTextArea();
	JScrollPane jsc_log = new JScrollPane(jta_log, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
													,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	String[] cols = {"현재 접속자"};
	String[][] data = new String[0][1];
	DefaultTableModel dtm = new DefaultTableModel(data,cols);
	JTable jt = new JTable(dtm);
	JScrollPane jsc = new JScrollPane(jt);
	JTableHeader jth = new JTableHeader();
	JLabel jl_m = new JLabel("message");
	JTextField jtf_m = new JTextField();
	JButton jb_exit = new JButton("나가기");
	//
	JFrame jf = new JFrame();
	JPanel jp_north = new JPanel();
	JPanel jp_center = new JPanel();
	JPanel jp_center1 = new JPanel();
	JPanel jp_center2 = new JPanel();
	JPanel jp_center3 = new JPanel();
	JPanel jp_south = new JPanel();
	JLabel jl_id = new JLabel("초대 아이디");
	JLabel jl_port = new JLabel("포트번호");
	JLabel jl_myid = new JLabel("본인 아이디");
	JTextField jtf_id = new JTextField();
	JTextField jtf_port = new JTextField();
	JTextField jtf_myid = new JTextField();
	JButton jb_login = new JButton("방 입장");
	//
	Socket socket = null;
	ObjectOutputStream oos = null;
	ObjectInputStream ois = null;
	//
	int port = 0;
	String myid = null;
	String id = null;
	String myname = null;
	String msg = null;
	//
	boolean stop = false;
	
	
	public static void main(String[] args) {
		new JinTalkChatter2();
	}
	
	public JinTalkChatter2() {
		loginDisplay();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {///////////////////////////////////////////////
		Object ob = e.getSource();
		if(ob==jb_login) {
			port = Integer.parseInt(jtf_port.getText());
			id = jtf_id.getText();
			myid = jtf_myid.getText();
			display("");
			connect(port, id, myid);
		}
		else if(ob==jtf_m) {
			msg = 200+"#"+myname+"#"+jtf_m.getText();
			Thread th = new Thread(this);
			th.start();
			jtf_m.setText("");
		}
		else if(ob==jb_exit) {
			 System.exit(EXIT_ON_CLOSE);
		}
	}	
	
	@Override
	public void run() {
		try {
			oos.writeObject(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void connect(int port, String id, String myid) {
		try {
			socket = new Socket("192.168.0.237",port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			oos.writeObject(100+"#"+id+"#"+port+"#"+myid);
			JinTalkChatterThread2 jcrt2 = new JinTalkChatterThread2(this);
			jcrt2.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loginDisplay() {
		jf.setDefaultCloseOperation(EXIT_ON_CLOSE);
		jf.setTitle("채팅방 로그인 화면2");
		jf.setSize(400,300);
		jf.setVisible(true);
		//
		jf.add("North",jp_north);
		jp_north.setPreferredSize(new Dimension(400,30));
		//
		jf.add("Center",jp_center);
		jp_center.setLayout(new GridLayout(3,1));
		jp_center.add(jp_center3);////
		jp_center3.add(jl_id);
		jl_id.setHorizontalAlignment(SwingConstants.CENTER);
		jl_id.setFont(new Font("견고딕", Font.BOLD, 17));
		jl_id.setPreferredSize(new Dimension(100,50));
		jp_center3.add(jtf_id);
		jtf_id.setPreferredSize(new Dimension(160,30));
		jtf_id.setFont(new Font("견고딕", Font.PLAIN, 17));
		jp_center.add(jp_center2);////
		jp_center2.add(jl_port);
		jl_port.setHorizontalAlignment(SwingConstants.CENTER);
		jl_port.setFont(new Font("견고딕", Font.BOLD, 17));
		jl_port.setPreferredSize(new Dimension(100,50));
		jp_center2.add(jtf_port);
		jtf_port.setPreferredSize(new Dimension(160,30));
		jtf_port.setFont(new Font("견고딕", Font.PLAIN, 17));
		jp_center.add(jp_center1);////
		jp_center1.add(jl_myid);
		jl_myid.setHorizontalAlignment(SwingConstants.CENTER);
		jl_myid.setFont(new Font("견고딕", Font.BOLD, 17));
		jl_myid.setPreferredSize(new Dimension(100,50));
		jp_center1.add(jtf_myid);
		jtf_myid.setPreferredSize(new Dimension(160,30));
		jtf_myid.setFont(new Font("견고딕", Font.PLAIN, 17));
		//
		jf.add("South",jp_south);
		jp_south.setBorder(BorderFactory.createEmptyBorder(15,10,75,10));
		jp_south.setPreferredSize(new Dimension(400,90));
		jp_south.add(jb_login);
		jb_login.setPreferredSize(new Dimension(110,30));
		jb_login.setFont(new Font("견고딕", Font.BOLD, 17));
		//
		jb_login.addActionListener(this);
		jtf_m.addActionListener(this);
	}
	
	public void display(String title) {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setSize(1000,600);
		setTitle(title);
		//
		add("Center",jp_c);
		jp_c.setLayout(new BorderLayout());
		jp_c.setBackground(Color.yellow);
		jp_c.setBorder(BorderFactory.createEmptyBorder(20,10,10,10));
		jp_c.add("Center",jsc_log);
		//
		add("East",jp_e);
		jp_e.setLayout(new BorderLayout());
		jp_e.setBackground(Color.yellow);
		jp_e.setBorder(BorderFactory.createEmptyBorder(20,0,10,10));
		jp_e.setPreferredSize(new Dimension(200,6));
		jp_e.add("Center",jsc);
		jth = jt.getTableHeader();
		jth.setFont(new Font("견고딕", Font.BOLD, 17));
		jt.setFont(new Font("견고딕", Font.PLAIN, 17));
		jt.setRowHeight(25);
		//
		add("South",jp_s);
		jp_s.setLayout(new BorderLayout());
		jp_s.setBackground(Color.yellow);
		jp_s.setBorder(BorderFactory.createEmptyBorder(0,10,10,5));
		jp_s.setPreferredSize(new Dimension(1000,50));
		jp_s.add("Center",jp_s1);
		jp_s1.setLayout(new FlowLayout(FlowLayout.RIGHT));
		jp_s1.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		jp_s1.setBackground(Color.yellow);
		jp_s1.add(jl_m);
		jl_m.setHorizontalAlignment(SwingConstants.CENTER);
		jl_m.setFont(new Font("견고딕", Font.BOLD, 17));
		jl_m.setPreferredSize(new Dimension(100,30));
		jp_s1.add(jtf_m);
		jtf_m.setPreferredSize(new Dimension(650,30));
		jtf_m.setFont(new Font("견고딕", Font.PLAIN, 17));
		jp_s.add("East",jp_s2);
		jp_s1.setLayout(new FlowLayout(FlowLayout.CENTER));
		jp_s2.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		jp_s2.setBackground(Color.yellow);
		jp_s2.setPreferredSize(new Dimension(200,50));
		jp_s2.add(jb_exit);
		jb_exit.setPreferredSize(new Dimension(180,30));
		jb_exit.setFont(new Font("견고딕", Font.BOLD, 17));
	}
	
		
	
		
}
