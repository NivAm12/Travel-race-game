package TravelRaceGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class MainMenu extends JFrame
{
	// data members:
	private JPanel 		mainMenuPanel;
	private JButton 	startGameButton;
	private JButton 	showScoreboardButton;
	private JButton 	quitGameButton;
	private JLabel 		backgroundImage;
	private Dimension 	buttonResolution;
	private int 		spaceBetweenButtons;
	
	public MainMenu() 
	{	
		super();
		buildMainMenuFrame();
		buildMainMenuPanel();
		setBackgroundImage();
		this.attachEvents();
		this.validate();
	}
	
	// builds the frame components
	private void buildMainMenuFrame()
	{		
		this.setTitle("Travel Race Game");
		this.setSize(800, 600);
		this.setLocationRelativeTo(null); // positing the frame on the center of the screen
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exit button
		this.buttonResolution = new Dimension(141, 25);
		this.setVisible(true);
	}
	
	private void setBackgroundImage()
	{
		this.backgroundImage = new JLabel();
		this.backgroundImage.setIcon(new ImageIcon(getClass().getResource("/Images/MainMenuBackgroundNoButtons.png")));
		this.backgroundImage.setSize(this.getSize());
		this.add(this.backgroundImage);
	}
	
	private void buildMainMenuPanel() 
	{
		this.mainMenuPanel = new JPanel();
		this.spaceBetweenButtons = 30;
		this.mainMenuPanel.setLocation(260, 260);
		this.mainMenuPanel.setOpaque(false);
		this.mainMenuPanel.setSize(buttonResolution.width + 5, (buttonResolution.height + spaceBetweenButtons) * 3);
		buildStartGameButton();
		buildScoreboardButton();
		buildQuitGameButton();
		this.mainMenuPanel.setLayout(new BorderLayout());
		this.add(mainMenuPanel);
	}
	
	private void buildStartGameButton()
	{
		this.startGameButton = new JButton();
		this.buildButtonAndAddToPanel(this.startGameButton, "/Images/MainMenuStartButton.png", new Point(0, 0));
		
	}
	
	private void buildScoreboardButton()
	{
		this.showScoreboardButton = new JButton();
		this.buildButtonAndAddToPanel(this.showScoreboardButton, "/Images/MainMenuScoreBoardButton.png",
				new Point(0, this.spaceBetweenButtons));
	}
	
	private void buildQuitGameButton()
	{
		this.quitGameButton = new JButton();
		this.buildButtonAndAddToPanel(this.quitGameButton, "/Images/MainMenuQuitButton.png", 
				new Point(0, this.showScoreboardButton.getLocation().y + this.spaceBetweenButtons));
	}
	
	private void buildButtonAndAddToPanel(JButton m_Button, String i_ImageSrc, Point i_Location)
	{
		m_Button.setIcon(new ImageIcon(getClass().getResource(i_ImageSrc)));
		m_Button.setSize(buttonResolution);
		m_Button.setVisible(true);
		m_Button.setLocation(i_Location);
		m_Button.setBackground(new Color(235, 189, 52));
		this.mainMenuPanel.add(m_Button);
	}
	
	private void attachEvents()
	{
		// quit button:
		this.quitGameButton.addActionListener(new ActionListener() { 
					
					@Override // quitButton press event:
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
		
		// start game button:
		this.startGameButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				MainWindowFrame gameFrame = new MainWindowFrame(); // create the game frame
				dispose();
			}
		});
	 }
}