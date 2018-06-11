package com.ryubai.sgnfexample;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import com.ryubai.sgnf.infoserver.InfoServer;
import com.ryubai.sgnf.scenarioserver.ScenarioServer;
import com.ryubai.sgnf.scenarioserver.UnityType.*;

public class UIExample  extends JFrame
{
	JPanel isp = new JPanel();
	JPanel ssp = new JPanel();

	JLabel apl = new JLabel();
	JLabel bpl = new JLabel();
	
	JButton startIS = new JButton("启动IS");
	JButton stopIS = new JButton("停止IS");
	
	JButton startSS = new JButton("启动SS");
	JButton stopSS = new JButton("停止SS");
	
	JTextField IsPort = new JTextField(10);
	JTextField SsPort = new JTextField(10);
	
	UIExample(InfoServer is,ScenarioServer ss)
	{
		IsPort.setText("9999");
		SsPort.setText("9876");
		
		startIS.addActionListener(new ActionListener(){
			@Override
            public void actionPerformed(ActionEvent e) {
				is.setPort(Integer.parseInt(IsPort.getText()));
                is.startThread();
            }
		});
		stopIS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				is.shut();
			}
		});
		startSS.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ss.setPort(Integer.parseInt(SsPort.getText()));
				ss.startThread();
			}
		});
		stopSS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ss.shut();
			}
		});
		
		Thread pt = new Thread() {// 线程操作
			public void run() {
				while (true) {
					try {
						apl.setText("Player A Position: "+ServerExample.BattleField.playerA.toString());
						bpl.setText("Player B Position: "+ServerExample.BattleField.playerB.toString());
						Thread.sleep(10);
					} catch (Exception e) {
					}
				}
			}
		};
		
		setTitle("some stupid title");
		
		Container container=this.getContentPane();
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.LEFT);
		container.setLayout(flow);
		
        isp.add(new Label("IS端口"));
        
        
        isp.add(IsPort);
        isp.add(startIS);
        isp.add(stopIS);
        
        ssp.add(new Label("SS端口"));
        
        
        ssp.add(SsPort);
        ssp.add(startSS);
        ssp.add(stopSS);
		
        ConsoleTextArea cta = new ConsoleTextArea();
		container.add(isp);
		container.add(ssp);
		
		
		container.add(apl);
		container.add(bpl);
		container.add(cta);

		validate();
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		pt.start();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				is.shut();
				ss.shut();
				pt.stop();
			}
		});
	}

	private class DataRedirection extends OutputStream {
		private final JTextArea destination;

		public DataRedirection(JTextArea destination) {
			if (destination == null)
				throw new IllegalArgumentException("Destination is null");
			this.destination = destination;
		}

		@Override
		public void write(byte[] buffer, int offset, int length) throws IOException {
			final String text = new String(buffer, offset, length);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					destination.append(text);
				}
			});
		}

		@Override
		public void write(int b) throws IOException {
			write(new byte[] { (byte) b }, 0, 1);
		}

	}
	private class ConsoleTextArea extends JTextArea {
		public ConsoleTextArea() {
			DataRedirection out = new DataRedirection(this);
			System.setOut(new PrintStream(out));
			System.setErr(new PrintStream(out));
		}
	}
}















