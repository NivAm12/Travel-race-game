package TravelRaceGame.Model;
import java.util.Observable;
import TravelRaceGame.Model.Card.eCardType;
import TravelRaceGame.Model.Player.ePlayerState;


public class LogicBoard extends Observable implements Model
{
	private final int f_TilesNumber = 28;
	private final int f_FirstPlayerHandSize = 3;
	private final int f_MaxRoundsToWin = 3;
	private final boolean f_IsSpecialTile = true;
	private int m_DiceScore;
	private Player m_PlayerOne;
	private Player m_PlayerTwo;
	private Player m_CurrentPlayer;
	private boolean[] m_Tiles;
	private CardDeck m_Deck;
	
	
	public LogicBoard(String i_PlayerOneName, String i_PlayerTwoName)
	{
		m_PlayerOne = new Player(i_PlayerOneName);
		m_PlayerTwo = new Player(i_PlayerTwoName);	
		m_Tiles = new boolean[f_TilesNumber];
		m_Deck = new CardDeck();
		setSpecialTiles();
	}
	
	
	// Getters/Setters:
	public int GetDiceScore()
	{
		return m_DiceScore;
	}
	
	public Player GetCurrentPlayer()
	{
		return m_CurrentPlayer;
	}
	
	
	// Methods:
	public void InitilaizeGame()
	{
		m_Deck.CreateAndShuffleDeck();
		initilizePlayer(m_PlayerOne);
		initilizePlayer(m_PlayerTwo);
		m_CurrentPlayer = m_PlayerOne;
	}
	
	public boolean CheckIfPlayerWon()
	{
		return (m_CurrentPlayer.GetCurrentRound() >= f_MaxRoundsToWin);
	}
	
	public void UseCard(int i_CardInHandIndex)
	{
		Card cardToUse = m_CurrentPlayer.RemoveAndReturnCard(i_CardInHandIndex);
		switch(cardToUse.GetType().GetCategory())
		{
		case CurrentBuff:
			m_CurrentPlayer.AddCurrentBuff(cardToUse);
			break;
		case QuededBuff:
			m_CurrentPlayer.AddQuededBuff(cardToUse);
			break;
		case OtherPlayerState:
			changeOtherPlayerState(cardToUse);
			break;
		}	
	}
	
	public void RollDice()
	{
		m_DiceScore = Dice.GetResult();
	}
	
	public void PlayTurn()
	{
		int numberOfSteps = 0;
		int diceScoreToAdd = (m_CurrentPlayer.getCurrentPlayerState() == ePlayerState.ZeroDice ? 0 : m_DiceScore);
		
		for (Card buff : m_CurrentPlayer.GetCurrentBuffs())
		{
			switch (buff.GetType())
			{
			case DicePlusOne:
				numberOfSteps = diceScoreToAdd + 1;
				break;
			case DicePlusTwo:
				numberOfSteps = diceScoreToAdd + 2;
				break;
			case DicePlusThree:
				numberOfSteps = diceScoreToAdd + 3;
			case DiceNextTurnPlusThree:
				numberOfSteps = diceScoreToAdd + 3;
				break;
			case DiceMultiTwo:
				numberOfSteps = diceScoreToAdd * 2;
				break;
			case DiceNextTurnMultiThree:
				numberOfSteps = diceScoreToAdd * 3;
				break;
			default:
				numberOfSteps = diceScoreToAdd;
				break;
			}
			
		}
		
		movePlayer(numberOfSteps);
		
		// check special tile:
		if (m_Tiles[m_CurrentPlayer.GetCurrentLocation()])
		{
			m_CurrentPlayer.AddCard(m_Deck.Draw());
		}
	}
	
	private void movePlayer(int i_NumerOfSteps)
	{
		if (m_CurrentPlayer.getCurrentPlayerState() == ePlayerState.Revert)
		{
			i_NumerOfSteps *= -1;
		}
		else if (m_CurrentPlayer.getCurrentPlayerState() == ePlayerState.Frozen)
		{
			i_NumerOfSteps = 0;
		}
		
		if ((m_CurrentPlayer.GetCurrentLocation() + i_NumerOfSteps) >= f_TilesNumber)
		{
			m_CurrentPlayer.IncreamentRound();
		}
		else if (m_CurrentPlayer.GetCurrentLocation() + i_NumerOfSteps < 0)
		{
			m_CurrentPlayer.DecrementRound();
		}
		
		m_CurrentPlayer.SetCurrentLocation((m_CurrentPlayer.GetCurrentLocation() + i_NumerOfSteps) % f_TilesNumber);
		m_CurrentPlayer.AddScore((int)(i_NumerOfSteps * 1.2 + 3)); // The highest the steps are, the highest the score became
	}
	
	public void EndTurn()
	{
		m_CurrentPlayer.GetCurrentBuffs().clear();
		m_CurrentPlayer.TransferBuffs();
		m_CurrentPlayer.ChangePlayerState(ePlayerState.Normal);
		m_CurrentPlayer = (m_CurrentPlayer == m_PlayerOne ? m_PlayerTwo : m_PlayerTwo);		
	}
	
	private void initilizePlayer(Player i_PlayerToInitilize)
	{
		i_PlayerToInitilize.Initilize();
		
		for (int i = 0; i <= f_FirstPlayerHandSize; i++)
		{
			i_PlayerToInitilize.AddCard(m_Deck.Draw());
		}
	}
	
	private void setSpecialTiles()
	{
		for (int i = 5; i < f_TilesNumber - 1; i += 7)
		{
			m_Tiles[i] = f_IsSpecialTile;
		}
	}
	
	private void changeOtherPlayerState(Card i_CardToUse)
	{
		Player opponentPlayer = (m_CurrentPlayer == m_PlayerOne ? m_PlayerTwo : m_PlayerOne); // Choose the opponent player
		
		if (!opponentPlayer.GetCurrentBuffs().contains(new Card(eCardType.Immune)))
		{
			if (i_CardToUse.GetType() == eCardType.FreezeOtherPlayer)
			{	
				opponentPlayer.ChangePlayerState(ePlayerState.Frozen);
			}
			else if (i_CardToUse.GetType() == eCardType.DiceRevertOtherPlayer)
			{
				opponentPlayer.ChangePlayerState(ePlayerState.Revert);
			}
			else if (i_CardToUse.GetType() == eCardType.DiceZeroOtherPlayer)
			{
				opponentPlayer.ChangePlayerState(ePlayerState.ZeroDice);
			}
		}
	}

}