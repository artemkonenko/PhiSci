import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.AbstractListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;


public class ResultWindow implements Runnable {

	class RecordComparator implements Comparator<Record>{

		@Override
		public int compare(Record arg0, Record arg1) {
			return -1 * Float.compare(arg0.time, arg1.time);
		}
		
	}
	
	class Record {
		public float time;
		public String name;
		
		public Record (String name, float time) {
			this.name = name;
			this.time = time;
		}
	}
	
	private JFrame frmc;
	private JList list;
	private boolean running;
	
	public static ResultWindow window;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			window = new ResultWindow();
			window.frmc.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		window.start();
		
	}

	public void start() {
		running = true;
		new Thread(this).start();
	}

	public void run() {
		long lastTime = System.currentTimeMillis();
		long delta = 2000;
		
		while (running) {
			delta = System.currentTimeMillis() - lastTime;
			if (delta > 1000 * 1) {
				lastTime = System.currentTimeMillis();
				try {
					update(delta);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//@SuppressWarnings({ "rawtypes", "unchecked" })
	private void update(long delta) throws IOException {
		//label.setText("Ping" + System.currentTimeMillis());
		
		File file = new File("data/record.txt");
		Scanner in = new Scanner(file);
		
		Queue<Record> val = new PriorityQueue<Record>(5, new RecordComparator());
		
		for (String str : in.nextLine().split("::")) {
			if (str.split("-").length == 2) {
				String[] vs = str.split("-");
				val.add(new Record(vs[1], Float.parseFloat(vs[0])));
			}
		}
		in.close();
		
		final List<Record> values = new ArrayList<Record>();
		int l = val.size();
		for (int i = 0; i < l; i++) {
			values.add(0, val.poll());
		}
		
		

		list.setModel(new AbstractListModel() {
			//String[] val = new String[] {" 234234 -23423", "sdf  -234", " 234234 -23423", "sdf  -234", "123 - ghdgd", "123 - ghdgd", " 234234 -23423", "sdf  -234", "123 - ghdgd"};
			
			public int getSize() {
				return values.size();
			}
			public Object getElementAt(int index) {
				Record a = values.get(index);
				return String.format("%1$10.4f - %2$s", a.time, a.name);
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ResultWindow() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmc = new JFrame();
		frmc.setTitle("Топ-лист");
		frmc.setBounds(100, 100, 200, 688);
		frmc.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmc.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		frmc.getContentPane().add(scrollPane);
		
		list = new JList();
		scrollPane.setViewportView(list);
	}
}
