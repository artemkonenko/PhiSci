import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Screen extends Canvas implements Runnable {
	private static final long serialVersionUID = 1L;
	public Screen screen;
	public static int WIDTH = 1200;
	public static int HEIGHT = 600;
	public static int SCALE = 50;
	public static String NAME = "Фестиваль науки. Лабиринты.";
	
	private static NameDialog nd;
	
	
	private static Action action;
	
	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean topPressed = false;
	private boolean bottomPressed = false;
	private boolean fcPressed = false;
	
	public static Sprite player;
	private static JLabel lTime;
	private static JButton btnRestart;
	private static JFrame frame;
	
	private boolean running;
	long delta;
	private long stopTime, startTime;
	
	public int px, py;
	public Spot[][] aMap;
	public Way way = new Way();
	
	ArrayList<IDrawble> toDraw = new ArrayList<IDrawble>(1);
	
	public void addDrawble(IDrawble toDrawble) {
		toDraw.add(toDrawble);
	}
	
	public void start() {
		running = true;
		startTime = System.currentTimeMillis();
		new Thread(this).start();
	}

	public void run() {
		long lastTime = System.currentTimeMillis();

		init();
		
		while (running) {
			delta = System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();
			
			render();
			update(delta);
		}
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(2);
			requestFocus();
			return;
		}

		Graphics g = bs.getDrawGraphics(); 
		
		g.setColor(Color.lightGray); 
		g.fillRect(0, 0, getWidth(), getHeight()); 
		
		g.setColor(Color.blue);
		
		for (IDrawble toDrawble : toDraw) {
			toDrawble.draw(g, SCALE);
		}
		
		/*way.draw(g, SCALE);*/
		
		if ( (aMap[py][px].cType & (char)16) == (char)16 ) {
			if (lTime.getText().startsWith("Time")) {
				stopTime = System.currentTimeMillis();
			}
			
			if (nd == null) {
				NameDialog nd = new NameDialog((float)(stopTime - startTime) / 1000, aMap[way.get(0).y][way.get(0).x].dijkstraDistance, way.size()-1);
				nd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				nd.setVisible(true);
				
				Spot st = way.get(0);
				px = st.x;
				py = st.y;
				way.clear();
				way.add(st);
				
				nd = null;
			}
		}
		else if ( (aMap[py][px].cType & (char)32) != (char)32 ) {
			lTime.setText("Time: " + (float)( System.currentTimeMillis() - startTime ) / 1000);
		}
		else {
			startTime = System.currentTimeMillis();
			lTime.setText("Time: 0");
		}
		
		player.draw(g, px * SCALE, py * SCALE, SCALE);
		
		g.dispose();
		bs.show(); // показать
	}
	
	public void update(long delta) {
		/*if (leftPressed == true) {
			px--;
		}
		if (rightPressed == true) {
			px++;
		}
		if (topPressed == true) {
			py--;
		}
		if (bottomPressed == true) {
			py++;
		}*/
	}


	public void init() {
		addKeyListener(new KeyInputHandler());
		
		way.add(aMap[py][px]);
		//addDrawble(way);
		
		player = getSprite("assets/hero1.png");
		
		Spot.solid = getSprite("assets/stone1.png");
		//Spot.solid = getSprite("solid.png");
		Spot.floor = getSprite("assets/floor.png");
		Spot.start = getSprite("assets/start1.png");
		Spot.finish = getSprite("assets/finish1.png");
	}
	
	public void reinit(String name) {
		Map map;
		toDraw.clear();
		
		map = new Map(name); // minimap

		
		Screen screen = new Screen(map.getSize()[1], map.getSize()[0]);
		
		screen.aMap = map.aMap;
		screen.px = map.start.x;
		screen.py = map.start.y;
		
		screen.addDrawble(map);
		screen.start();
	}
	
	public Screen(int width, int height) {
		frame = new JFrame(Screen.NAME);
		Dimension dim = frame.getToolkit().getScreenSize();
		
		Screen.WIDTH = width;
		Screen.HEIGHT = height;
		Screen.SCALE = Math.round(Math.min((dim.width - 300) / width, (dim.height - 100) / height));
		
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(this, BorderLayout.CENTER);
		
		action = this.new SwingAction();
		
		btnRestart = new JButton("Restart");
		btnRestart.setAction(action);
		btnRestart.setFont(new Font("Tahoma", Font.PLAIN, 20));
		//btnRestart.setBounds(580, 400, 40, 80);
		frame.add(btnRestart, BorderLayout.SOUTH);
		
		lTime = new JLabel("Time: ");
		lTime.setFont(new Font("Tahoma", Font.PLAIN, 20));
		//lTime.setBounds(0, 0, 40, 200);
		frame.add(lTime, BorderLayout.NORTH);
		frame.pack();
		//frame.setResizable(false);
		frame.setVisible(true);
		//this.start();

		screen = this;
	}

	public Sprite getSprite(String path) {
		BufferedImage sourceImage = null;

		try {
			Image image = ImageIO.read(new File(path));
			sourceImage = (BufferedImage) image;
		} catch (IOException e) {
			e.printStackTrace();
		}

		Sprite sprite = new Sprite(Toolkit.getDefaultToolkit().createImage(sourceImage.getSource()));
		return sprite;
	}
	
	
	private class KeyInputHandler extends KeyAdapter {
		public void keyPressed(KeyEvent e) {			  
			try {
				if (e.getKeyCode() == KeyEvent.VK_LEFT && !leftPressed && aMap[py][px].left != null) {
					leftPressed = true;
					px--;
					way.add(aMap[py][px]);
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT && !rightPressed && aMap[py][px].right != null) {
					rightPressed = true;
					px++;
					way.add(aMap[py][px]);
				}
				if (e.getKeyCode() == KeyEvent.VK_UP && !topPressed && aMap[py][px].top != null) {
					topPressed = true;
					py--;
					way.add(aMap[py][px]);
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN && !bottomPressed && aMap[py][px].bottom != null) {
					bottomPressed = true;
					py++;
					way.add(aMap[py][px]);
				}
				
				if (e.getKeyCode() == KeyEvent.VK_F3 && !fcPressed) {
					fcPressed = true;
					JFileChooser fileopen = new JFileChooser("./data");
					int ret = fileopen.showDialog(null, "Открыть файл");               
					if (ret == JFileChooser.APPROVE_OPTION) {
					    File file = fileopen.getSelectedFile();
					    
					    reinit(file.getName().split("\\.")[0] );
					    
					}
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				topPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				bottomPressed = false;
			}
			
			if (e.getKeyCode() == KeyEvent.VK_F3) {
				fcPressed = false;
			}
		}
	}
	
	private class SwingAction extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public SwingAction() {
			putValue(NAME, "Restart!");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			Spot st = way.get(0);
			px = st.x;
			py = st.y;
			way.clear();
			way.add(st);
			
			screen.requestFocus();
		}
	}
}
