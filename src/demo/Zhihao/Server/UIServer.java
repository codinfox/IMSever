package demo.Zhihao.Server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * The GUI part of the IMServer project.<br>
 * @author ben
 *
 */
class UIServer extends JFrame {
	private ChatServer server = null;
	private DefaultListModel<String> memberModel = null;
	private JList<String> memberList = null;

	/**
	 * Creates and init the server GUI.
	 * @param server the model of this view.
	 * @param ip the local IP address. 
	 * @param tcpPort the local TCP port.
	 * @param udpPort the lcoal UDP port.
	 */
	public UIServer(ChatServer server, String ip, int tcpPort, int udpPort) {
		super("Chatroom Control Panel");

		this.server = server;
		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.add("Info", new InfoPanel(ip, tcpPort, udpPort));
		JScrollPane sp = new JScrollPane(new MemberPanel());
		tabbedPane.add("Member", sp);

		this.add(tabbedPane);

		setSize(500, 500);
		setVisible(true);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				terminate();
			}
		});
	}

	/**
	 * Adds a new clinet to the list.
	 * @param name the new client's name.
	 */
	public void addClient(String name) {
		memberModel.addElement(name);
	}

	/**
	 * Removes a client from the list.
	 * @param name the client's name.
	 */
	private void removeClient(String name) {
		server.forceQuit(name);
		memberModel.removeElement(name);
	}

	/**
	 * Terminate the thread and quit.
	 */
	private void terminate() {
		server.close();
		System.exit(0);
	}

	private class MemberPanel extends JPanel {
		private int tempIndex = 0;

		public MemberPanel() {
			memberModel = new DefaultListModel<>();
			server.setMemberModel(memberModel);
			this.setLayout(new BorderLayout());
			memberList = new JList<String>(memberModel);
			this.add(memberList);
			memberList.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					memberList.setSelectedIndex(tempIndex = memberList
							.locationToIndex(e.getPoint()));
					if (e.isMetaDown()) {
						JPopupMenu pm = new JPopupMenu();
						JMenuItem deleteItem = new JMenuItem("Force Quit");
						pm.add(deleteItem);
						deleteItem.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String name = memberModel
										.getElementAt(tempIndex);
								removeClient(name);
							}
						});
						pm.show(memberList, e.getX(), e.getY());
					}
				}
			});
		}
	}

	private class InfoPanel extends JPanel {
		private JTextField roomnamefField = new JTextField();
		private JTextField descriptionField = new JTextField();
		private JTextArea bulletinArea = new JTextArea();

		private String roomName = "Unknown";
		private String roomDes = "This is a room";
		private String billboard = "Nothing";

		public InfoPanel(String ip, int tcpPort, int udpPort) {
			// basic info
			this.setLayout(new BorderLayout());
			JPanel basicInfo = new JPanel(new GridLayout(3, 2));
			basicInfo.setBorder(BorderFactory.createTitledBorder("Basic Info"));
			basicInfo.setPreferredSize(new Dimension(500, 100));
			basicInfo.add(new JLabel("IP address:"));
			basicInfo.add(new JLabel(ip));
			basicInfo.add(new JLabel("TCP Port/UDP Port:"));
			basicInfo.add(new JLabel(Integer.toString(tcpPort) + " / "
					+ Integer.toString(udpPort)));
			JButton terminateButton = new JButton("Terminate");
			basicInfo.add(terminateButton);
			terminateButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					terminate();
				}
			});
			this.add(basicInfo, BorderLayout.NORTH);

			// name, description and bulletin
			JPanel detailInfo = new JPanel();
			detailInfo.setLayout(null);
			JLabel roomname = new JLabel("Room Name: ");
			roomname.setBounds(10, 10, 100, 20);
			roomnamefField.setBounds(120, 10, 350, 20);
			JLabel description = new JLabel("Description: ");
			description.setBounds(10, 40, 100, 20);
			descriptionField.setBounds(120, 40, 350, 20);
			JLabel bulletin = new JLabel("Bulletin: ");
			bulletin.setBounds(10, 70, 100, 20);
			bulletinArea.setBounds(120, 70, 350, 200);
			JButton updateButton = new JButton("Update");
			updateButton.setBounds(190, 280, 100, 40);
			bulletinArea.setLineWrap(true);
			
			detailInfo.add(roomname);
			detailInfo.add(description);
			detailInfo.add(bulletin);
			detailInfo.add(roomnamefField);
			detailInfo.add(descriptionField);
			detailInfo.add(bulletinArea);
			detailInfo.add(updateButton);
			this.add(detailInfo, BorderLayout.CENTER);
			

			updateButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if ((roomnamefField.getText().indexOf('|') != -1)
							|| (descriptionField.getText().indexOf("|") != -1)
							|| (bulletinArea.getText().indexOf("|") != -1)) {
						illegalMsg();
						return;
					}
					roomName = roomnamefField.getText();
					roomDes = descriptionField.getText();
					billboard = bulletinArea.getText();
					billboard.replaceAll("\n", " ");
					updateInfo();
				}
			});
		}
		
		private void illegalMsg() {
			JOptionPane.showMessageDialog(this, "Illegal Character");
		}

		public void updateInfo() {
			server.updateInfo(roomName+"|"+roomDes+"|"+billboard+"\n");
		}
	}
}
