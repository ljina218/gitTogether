package project.chat;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;

public class JinTalkChatterThread2 extends Thread{
	
	private boolean stop;
	
	JinTalkChatter2 jcr2 = null;
	
	public JinTalkChatterThread2(JinTalkChatter2 jinTalkChatter2) {
		jcr2 = jinTalkChatter2;
		this.stop = false;
	}
	
	@Override
	public void run() {
		String msg = null;
		while(!stop) {
			try {
				msg = (String) jcr2.ois.readObject();
				StringTokenizer st = null;
				int protocol = 0;
				if(msg!=null) {
					st = new StringTokenizer(msg, "#");
					protocol = Integer.parseInt(st.nextToken());
				}
				switch(protocol) {
				case 100:{
					int num = Integer.parseInt(st.nextToken());
					String rname = st.nextToken();
					String name = st.nextToken();
					if(jcr2.myname==null&&num==1) {
						jcr2.myname = name;
					}
					String title =jcr2.myname+"님, "+rname+" 방문을 환영합니다.";
					jcr2.jf.dispose();
					//
					jcr2.setTitle(title);
					jcr2.jta_log.append("["+name+"님 입장]\n");
					Vector v = new Vector();
					v.add(name);
					jcr2.dtm.addRow(v);
					}break;
				case 110:{
					String loginfail = st.nextToken();
					JOptionPane.showMessageDialog(jcr2, loginfail);
					jcr2.jf.dispose();
					this.stop = stop;
					}break;
				case 200:{
					String name = st.nextToken();
					String message = st.nextToken();
					jcr2.jta_log.append(name+": "+message+"\n");
				}break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
