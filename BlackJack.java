//Group: Adam Barakat, Jose Arellano, Ethan Smith, Luis Cruz Pereda


import java.util.Random;
import java.util.Scanner;


public class BlackJack {

  private static Card[] cards = new Card[52];

  private static int currentCardIndex = 0;
  private static int suitIndex = 0;

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    boolean turn = true;
    String playerDecision = "" ;
    //added a for loop so the game can be played multiple times
    //for (int i = 0; i < turn; i++) {
    while(turn) {
      initializeDeck();
      shuffleDeck();
      int playerTotal = 0;
      int dealerTotal = 0;
      playerTotal = dealInitialPlayerCards();

      //fix dealInitialDealerCards
      dealerTotal = dealInitialDealerCards();

      //fix playerTurn
      playerTotal = playerTurn(scanner, playerTotal);
      if (playerTotal > 21) {
        System.out.println("You busted! Dealer wins.");
        return;
      }

      //fix dealerTurn
      dealerTotal = dealerTurn(dealerTotal);
   
      determineWinner(playerTotal, dealerTotal);
      //added
      //asks player if they want to play again
      System.out.println("Would you like to play another hand?");
     
      playerDecision = scanner.nextLine().toLowerCase();
      
      while(!(playerDecision.equals("no") || (playerDecision.equals("yes")) )){
        System.out.println("Invalid action. Please type 'hit' or 'stand'.");
        playerDecision = scanner.nextLine().toLowerCase();
      }
      if (playerDecision.equals("no"))
          turn = false;
    }
    System.out.println("Thanks for playing!");
  }

  // algorithm to create deck
  private static void initializeDeck() {
    //for (int i = 0; i < DECK.length; i++) {
    String[] SUITS = { "Hearts", "Diamonds", "Clubs","Spades" };
    String[] RANKS = { "2", "3", "4", "5", "6", "7", "8", "9","10", "Jack", "Queen", "King","Ace" };
    int rankIndex = 0; //removed "suitsIndex = 0"
    for (int i = 0; i < cards.length; i++) {
      //DECK[i] = i;
      //public Card(int value, String suit, String rank) {
      int val = 10;
      if(rankIndex < 9){
        val = Integer.parseInt(RANKS[rankIndex]);

    } else if (RANKS[rankIndex].equals("Ace")) {
      val = 11; //ace is 11, not 10
    } else {
      val = 10; //other suits are 10
    }
      
      cards[i] = new Card( val, SUITS[suitIndex], RANKS[rankIndex]);
      suitIndex++;
      if (suitIndex == 4) {
        suitIndex = 0;
        rankIndex++;
      }
    }
  }
  // algorithm to shuffle deck
  private static void shuffleDeck() {
    Random random = new Random();
    for (int i = 0; i < cards.length; i++) {
      int index = random.nextInt(cards.length);
      Card temp = cards[i];
      cards[i] = cards[index];
      cards[index] = temp;
    }

    currentCardIndex = 0; //reset to the start of the shuffled deck
  }
  // algorithm to deal initial player cards
  private static int dealInitialPlayerCards() {
    /*int card1 = dealCard();
    int card2 = dealCard();*/
    Card card1 = dealCard();
    Card card2 = dealCard();
    
    //System.out.println("Your cards: " + RANKS[card1] + " of " + SUITS[card1 / 13] + " and " + RANKS[card2] + " of " + SUITS[card2 / 13]);
    System.out.println("Your cards: " + card1.getRank() + " of " + card1.getSuit() + " and " + card2.getRank() + " of " + card2.getSuit());
    
    int playerTotal = card1.getValue() + card2.getValue();
    int aceCount = (card1.getRank().equals("Ace") ? 1 : 0) + (card2.getRank().equals("Ace") ? 1 : 0);

    while(playerTotal > 21 && aceCount > 0) {
      playerTotal -= 10; //this would make the ace 1 instead of 11 if it busts the player otherwise
      aceCount--;
    }
        
    //return cardValue(card1) + cardValue(card2);
    return playerTotal;
  }
  // alogrithm to deal initial dealer cards
  private static int dealInitialDealerCards() {
    Card card1 = dealCard();
    System.out.println("Dealer's card: " + card1);

    int dealerTotal = card1.getValue();
    int aceCount = (card1.getRank().equals("Ace") ? 1 : 0);

    //adjusts if bust due to ace
    while(dealerTotal > 21 && aceCount > 0) {
      dealerTotal -= 10;
      aceCount--;
    }

    return card1.getValue();
  }

  private static int playerTurn(Scanner scanner, int playerTotal) {
    int aceCount = 0;
//initial double ace check
    if(playerTotal == 22) {
      playerTotal = 12;
      aceCount = 1;
    }

    while (true) {
      System.out.println("Your total is " + playerTotal + ". Do you want to hit or stand?");
      String action = scanner.nextLine().toLowerCase();

      if (action.equals("hit")) {
        Card newCard = dealCard();
        playerTotal += newCard.getValue();

        if(newCard.getRank().equals("Ace")) {
          aceCount++;
        }
        //adjusts total if bust (assuming there is an ace)
        while(playerTotal > 21 && aceCount > 0) {
          playerTotal -= 10;
          aceCount--;
        }

        System.out.println("You drew a " + newCard );

        if (playerTotal > 21) {
          //added
          //resets playerTotal so the game can be played multiple times
          System.out.println("you busted Dealer wins!" );
          playerTotal = 0;
          return playerTotal;
        }
      } else if (action.equals("stand")) {
        break;
      } else {
        System.out.println("Invalid action. Please type 'hit' or 'stand'.");
      }
    }
    return playerTotal;
  }

  // algorithm for dealer's turn
  private static int dealerTurn(int dealerTotal) {
    int aceCount = 0;

    //check for dobule ace
    if(dealerTotal == 22) {
      dealerTotal = 12;
      aceCount = 1;
    }

    while (dealerTotal < 17) {
      Card newCard = dealCard();
      dealerTotal += newCard.getValue();

      if(newCard.getRank().equals("Ace")) {
        aceCount++;
      }

      while(dealerTotal > 21 && aceCount > 0) {
        dealerTotal -= 10;
        aceCount--;
      }
  
    }
    System.out.println("Dealer's total is " + dealerTotal);
    return dealerTotal;
  }
  // algorithm to determine the winner
  private static void determineWinner(int playerTotal, int dealerTotal) {
    if (dealerTotal > 21 || playerTotal > dealerTotal) {
      System.out.println("You win!");
    } else if (dealerTotal == playerTotal) {
      System.out.println("It's a tie!");
    } else {
      System.out.println("Dealer wins!");
      playerTotal = 0;
    }
  }
  // algroithm to deal a card
  //private static int dealCard() {
  private static Card dealCard() {
    //return DECK[currentCardIndex++] % 13;
    return cards[currentCardIndex++];
  }
  //removed cardvalue() because we use the card class (accessors) to get the values now
}

