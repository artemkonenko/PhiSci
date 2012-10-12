import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class NameDialog extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JLabel uTime;
	private float time;
	private Frame parent;
	
	public NameDialog(float time, int ideal, int real) {
		setType(Type.UTILITY);
		setResizable(false);
		setTitle("Финиш достигнут!");
		setBounds(100, 100, 407, 243);
		getContentPane().setLayout(null);
		
		JLabel label = new JLabel("Ваше время:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label.setBounds(12, 13, 105, 22);
		getContentPane().add(label);
		
		JLabel label_1 = new JLabel("Количество шагов:");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_1.setBounds(12, 48, 156, 22);
		getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("Лишних из них:");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_2.setBounds(12, 83, 131, 22);
		getContentPane().add(label_2);
		
		JLabel label_3 = new JLabel("Ваше имя:");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label_3.setBounds(12, 118, 87, 22);
		getContentPane().add(label_3);
		
		textField = new JTextField();
		textField.setText("Василиса Премудрая");
		textField.setFont(new Font("Tahoma", Font.PLAIN, 17));
		textField.setBounds(101, 116, 286, 28);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		JButton button = new JButton("Добавиться в таблицу рекордов");
		button.setFont(new Font("Tahoma", Font.PLAIN, 17));
		button.setBounds(12, 170, 375, 28);
		button.addActionListener(this);
		getContentPane().add(button);
		
		uTime = new JLabel( String.valueOf(time) );
		uTime.setFont(new Font("Tahoma", Font.PLAIN, 18));
		uTime.setBounds(129, 13, 258, 22);
		getContentPane().add(uTime);
		
		JLabel uSteps = new JLabel( String.valueOf(real) );
		uSteps.setFont(new Font("Tahoma", Font.PLAIN, 18));
		uSteps.setBounds(180, 48, 207, 22);
		getContentPane().add(uSteps);
		
		JLabel uOddStep = new JLabel( String.valueOf(real - ideal) );
		uOddStep.setBounds(155, 83, 232, 22);
		getContentPane().add(uOddStep);
	}
	
	public void actionPerformed(ActionEvent ae) {
		FileWriter fw;
		
		try {
			fw = new FileWriter("data/record.txt", true);
			
			fw.write("::" + uTime.getText() + "-" + textField.getText());
			fw.flush();
			
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		dispose();
		
		/*ResultDialog rd = new ResultDialog(this.parent);
		rd.setVisible(true);
		rd = null;*/
		
		
	}
}
