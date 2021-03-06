package TravelRaceGame.Controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.Timer;
import TravelRaceGame.Model.*;
import TravelRaceGame.View.*;


public class GameInstance implements IApplicationController
{
	private Model m_Model;
	private View m_View;
	
	public GameInstance(Model i_Model, View i_View)
	{
		m_Model = i_Model;
		m_View = i_View;
		
		InitilaizeGame();
	}
	
	private void InitilaizeGame()
	{
		m_Model.InitializeGame();
		m_View.Initilize();
		startCurrentPlayerTurn();
	}
	
	@Override
	public void update(Observable i_UpdateSender, Object arg)
	{
		if (arg instanceof GameBoardUi.eNotificationType)
		{
			switch((GameBoardUi.eNotificationType)arg)
			{
			case CardClicked:
				onCardClicked();
				break;
			case DiceClicked:
				onDiceClicked();
				break;
			}
		}
	}
	
	private void startCurrentPlayerTurn()
	{
		m_View.SetCardsInHandAndEnableEvents(cardsInHandEnumToString(m_Model.GetCurrentPlayer().GetHand()));
		m_View.GetBoard().SetInstuctionText(String.format("%s turn<br/>Round %d of %d",
				m_Model.GetCurrentPlayer().GetName(), m_Model.GetCurrentPlayer().GetCurrentRound(), m_Model.GetMaxRoundSize()));
		
		m_View.EnablePlayButtons(true);
	}
	
	private void onCardClicked()
	{
		m_Model.UseCard(m_View.GetCardClickedIndex()); // update model
		m_View.SetCardsInHandAndEnableEvents(cardsInHandEnumToString(m_Model.GetCurrentPlayer().GetHand())); // update view
	}
	
	private void onDiceClicked()
	{
		m_Model.RollDice();
		m_View.GetBoard().GetDiceButton().RollDice(m_Model.GetDiceScore());
		m_Model.PlayTurn();
		delayThenMovePlayerAndChangeTurn();
	}
	
	private void delayThenMovePlayerAndChangeTurn() // also check if the game ended
	{
		Timer delayAfterDiceRoll = new Timer(1400, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				m_View.GetBoard().SetPlayersLocation(m_Model.GetPlayerOne().GetCurrentLocation(), m_Model.GetPlayerTwo().GetCurrentLocation());
				
				if (!checkIfGameEnded())
				{
					m_Model.EndTurn();
					startCurrentPlayerTurn();
				}
			}
		});
		
		delayAfterDiceRoll.setRepeats(false);
		delayAfterDiceRoll.start();
	}
	
	private boolean checkIfGameEnded()
	{
		boolean isEnded = false;
		
		if (m_Model.CheckIfPlayerWon())
		{
			if (m_View.AskReplayGame(m_Model.GetCurrentPlayer().GetName()))
			{
				InitilaizeGame();
			}
			else
			{
				m_View.EndGameUi();
			}
			
			isEnded = true;
		}
		
		return isEnded;
	}
	
	
	private ArrayList<String> cardsInHandEnumToString(ArrayList<Card> i_CardsInHands)
	{
		ArrayList<String> cardsInHandStr = new ArrayList<String>();
		
		for (Card currentCard : i_CardsInHands)
		{
			cardsInHandStr.add(currentCard.GetType().name());
		}
		
		return cardsInHandStr;
	}
}
