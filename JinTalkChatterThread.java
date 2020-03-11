package project.chat;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;

public class JinTalkChatterThread extends Thread{
	
	private boolean stop;
	
	JinTalkChatter jcr = null;
	
	public JinTalkChatterThread(JinTalkChatter jinTalkChatter) {
		jcr = jinTalkChatter;
		this.stop = false;
	}
	
	@Override
	public void run() {
		String msg = null;
		while(!stop) {
			try {
				msg = (String) jcr.ois.readObject();
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
					if(jcr.myname==null&&num==1) {
						jcr.myname = name;
					}
					String title =jcr.myname+"님, "+rname+" 방문을 환영합니다.";
					jcr.jf.dispose();
					//
					jcr.setTitle(title);
					jcr.jta_log.append("["+name+"님 입장]\n");
					Vector v = new Vector();
					v.add(name);
					jcr.dtm.addRow(v);
					}break;
				case 110:{
					String loginfail = st.nextToken();
					JOptionPane.showMessageDialog(jcr, loginfail);
					jcr.jf.dispose();
					this.stop = stop;
					}break;
				case 200:{
					String name = st.nextToken();
					String message = st.nextToken();
					jcr.jta_log.append(name+": "+message+"\n");
				}break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
