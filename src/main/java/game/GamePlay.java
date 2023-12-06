package game;

import java.util.*;

public class GamePlay {
  // Lists to store fleets for Player 1 and Player 2
  private static final ArrayList<Ship> player1Fleet = new ArrayList<>();
  private static final ArrayList<Ship> player2Fleet = new ArrayList<>();
  // Random number generator for various game events
  private static final Random random = new Random();

  // Map to track the damage inflicted by each ship
  private static final Map<Ship, Integer> mostDestructiveShipByDamageMap = new HashMap<>();

  // Variables to track the most destructive ship by hit points
  private static String mostDestructiveShipByHitPoint = null;
  private static Integer mostDestructiveShipByHitPointPoint = 0;

  // Main method to start the battle rounds
  public static void main(String[] args) {
    battleRound();
  }

  // Method to create fleets for both players
  private static void createFleets() {

    // Creating ships for Player 1 and Player 2 fleets of different types
    // (BattleShip, Carrier, Destroyer, Submarine, PatrolBoat)
    // with distinct names and ship types
    for (int i = 0; i < 10; i++) {
      player1Fleet.add(new BattleShip("Player1 BattleShip " + (i + 1), ShipType.BATTLESHIP));
      player2Fleet.add(new BattleShip("Player2 BattleShip " + (i + 1), ShipType.BATTLESHIP));
    }
    for (int i = 0; i < 5; i++) {
      player1Fleet.add(new Carrier("Player1 Carrier " + (i + 1), ShipType.CARRIER));
      player2Fleet.add(new Carrier("Player2 Carrier " + (i + 1), ShipType.CARRIER));
    }
    for (int i = 0; i < 5; i++) {
      player1Fleet.add(new Destroyer("Player1 Destroyer " + (i + 1), ShipType.DESTROYER));
      player2Fleet.add(new Destroyer("Player2 Destroyer " + (i + 1), ShipType.DESTROYER));
    }
    for (int i = 0; i < 5; i++) {
      player1Fleet.add(new Submarine("Player1 Submarine " + (i + 1), ShipType.SUBMARINE));
      player2Fleet.add(new Submarine("Player2 Submarine " + (i + 1), ShipType.SUBMARINE));
    }
    for (int i = 0; i < 20; i++) {
      player1Fleet.add(new PatrolBoat("Player1 PatrolBoat " + (i + 1), ShipType.PATROLBOAT));
      player2Fleet.add(new PatrolBoat("Player2 PatrolBoat " + (i + 1), ShipType.PATROLBOAT));
    }
  }

  // Method to repair damaged ships in a fleet
  private static void repairShips(ArrayList<Ship> fleet, Set<Ship> damagedShips) {
    // Iterate through each ship in the fleet and repair if it is damaged and not destroyed
    for (Ship ship : fleet) {
      if (damagedShips.contains(ship) && !ship.isDestroyed()) {
        ship.repair();
      }
    }
  }

  // Method to simulate attacks between Player 1 and Player 2 fleets
  private static void attack() {
    // Set to store ships that were damaged during the attack
    Set<Ship> damagedShips = new HashSet<>();

    // Loop through each ship in Player 1 fleet to attack Player 2 fleet
    for (Ship attacker : player1Fleet) {
      if (!attacker.isDestroyed()) {
        Ship target = player2Fleet.get(random.nextInt(player2Fleet.size()));
        attacker.defendAttack(target);
        if (target.isDestroyed()) {
          damagedShips.add(target);
        }
        // Update damage map and mark damaged ships
        updateDamageMap(target, Math.max(0, attacker.getAttack() - target.getArmour()));
      }
    }
    // Repeat the process for Player 2 attacking Player 1 fleet
    for (Ship attacker : player2Fleet) {
      if (!attacker.isDestroyed()) {
        Ship target = player1Fleet.get(random.nextInt(player1Fleet.size()));
        attacker.defendAttack(target);
        if (target.isDestroyed()) {
          damagedShips.add(target);
        }
        updateDamageMap(target, Math.max(0, attacker.getAttack() - target.getArmour()));
      }
    }
    // Repair damaged ships in both fleets
    repairShips(player1Fleet, damagedShips);
    repairShips(player2Fleet, damagedShips);
  }

  // Method to execute a battle round
  public static void battleRound() {
    // Initialize fleets, round counter, and enter the main game loop
    // Display fleet status before and after each round
    // Execute attacks and update fleet status
    // Display winner and game statistics at the end
    createFleets();
    int round = 0;

    while (Boolean.TRUE.equals(!isPlayer1FleetDestroyed())
        && Boolean.TRUE.equals(!isPlayer2FleetDestroyed())) {
      round++;
      System.out.println("Round " + round);
      displayFleetStatus("Player 1 Fleet:", player1Fleet);
      displayFleetStatus("Player 2 Fleet:", player2Fleet);
      attack();
      displayFleetStatus("Player 1 Fleet after Round " + round + ":", player1Fleet);
      displayFleetStatus("Player 2 Fleet after Round " + round + ":", player2Fleet);
    }
    displayWinner();
    displayFinishRound(round);
    displayRemainingShipsWithHitPoint();
    displayShipsDestroyedInformation();
    displayMvpMostDestructiveShipByHitPoint();
    displayMvpMostDestructiveShip();
  }

  // Method to display the winner of the game
  private static void displayWinner() {
    // Determine the winner based on total health points
    // Display winner or declare a draw if no winner
    System.out.println("\n\n********** WINNER ********");
    String winner = determineWinner();
    if ("It's a draw!".equalsIgnoreCase(winner)) {
      System.out.println("Maximum rounds reached. The game is a draw.");
    } else {
      System.out.println("Game over. " + winner + " is the winner!");
    }
  }

  // Method to display information about the completion of the game
  private static void displayFinishRound(int round) {
    // Display the completion round of the game
    System.out.println("\n\n****** Game Completion round ******");
    System.out.println("Finished the game at round " + round);
  }

  // Method to display the remaining ships with hit points
  private static void displayRemainingShipsWithHitPoint() {
    // Display remaining ships for the winning player
    System.out.println("\n\n********** Ships Remaining ************");
    String winner = determineWinner();
    if ("Player 1".equalsIgnoreCase(winner)) {
      displayFleetStatus(player1Fleet);
    } else if ("Player ".equalsIgnoreCase(winner)) {
      displayFleetStatus(player2Fleet);
    }
  }

  // Method to determine the winner of the game
  private static String determineWinner() {

    // Calculate total health points for both players
    // Determine the winner based on total health points
    // Return the result as a string
    int totalPlayer1Health = player1Fleet.stream().mapToInt(ship -> ship.nHitPoints).sum();
    int totalPlayer2Health = player2Fleet.stream().mapToInt(ship -> ship.nHitPoints).sum();
    if (totalPlayer1Health > totalPlayer2Health) {
      return "Player 1";
    } else if (totalPlayer2Health > totalPlayer1Health) {
      return "Player 2";
    } else {
      return "It's a draw!";
    }
  }

  // Method to display the status of a fleet with a header
  private static void displayFleetStatus(String header, ArrayList<Ship> fleet) {
    // Display the header and status of each ship in the fleet
    System.out.println(header);
    for (Ship ship : fleet) {
      displayShipStatus(ship);
    }
  }

  // Overloaded method to display the status of a fleet without a header
  private static void displayFleetStatus(ArrayList<Ship> fleet) {
    // Display the status of each ship in the fleet
    for (Ship ship : fleet) {
      displayShipStatus(ship);
    }
  }

  // Constant variable of HitPoint label for reuse
  private static final String HP_LABEL = " - HP: ";

  // Method to display the status of an individual ship
  private static void displayShipStatus(Ship ship) {
    // Display the name and health points of the ship
    int health = Math.max(0, ship.nHitPoints);
    System.out.println(getShipName(ship) + HP_LABEL + health);
  }

  // Method to get the name of a ship as a string
  public static String getShipName(Ship ship) {
    // Determine the name of the ship based on its type
    String shipName = null;
    if (ship instanceof BattleShip battleship) {
      shipName = battleship.getName();
    } else if (ship instanceof Carrier carrier) {
      shipName = carrier.getName();
    } else if (ship instanceof Destroyer destroyer) {
      shipName = destroyer.getName();
    } else if (ship instanceof Submarine submarine) {
      shipName = submarine.getName();
    } else if (ship instanceof PatrolBoat patrolBoat) {
      shipName = patrolBoat.getName();
    }
    // Return the ship name as a string
    return shipName;
  }

  // Method to display information about destroyed ships and their destroyers
  private static void displayShipsDestroyedInformation() {
    // Display information about destroyed ships and their destroyers
    System.out.println(
        "\n\n******** Ships destroyed and what opponent ship destroyed it **********");
    Map<Ship, String> destroyedShips = new HashMap<>();
    for (Ship ship : player1Fleet) {
      if (ship.isDestroyed() && ship.destroyedBy != null) {
        destroyedShips.put(ship, ship.destroyedBy);
      }
    }
    for (Ship ship : player2Fleet) {
      if (ship.isDestroyed() && ship.destroyedBy != null) {
        destroyedShips.put(ship, ship.destroyedBy);
      }
    }
    for (Map.Entry<Ship, String> entry : destroyedShips.entrySet()) {
      System.out.println(getShipName(entry.getKey()) + " destroyed by " + entry.getValue());
    }
  }

  // Method to display the most destructive ship by hit points
  private static void displayMvpMostDestructiveShipByHitPoint() {
    // Calculate and display the most destructive ship by hit points
    System.out.println(
        "\n\n******** MVP ship that inflicted the most hit point damage ************");
    calculateHitPointMVP();
    System.out.println(
        "MVP for hit point damage is: "
            + mostDestructiveShipByHitPoint
            + " with a damage level of "
            + mostDestructiveShipByHitPointPoint);
  }

  // Method to display the most destructive ship in terms of destroyed opposing ships
  private static void displayMvpMostDestructiveShip() {
    // Find and display the most destructive ship in terms of destroyed opposing ships
    System.out.println("\n\n******** MVP ship that destroyed the most opposing ships ************");
    HashMap<Ship, Integer> mostDestructiveShip = findMvpMostDestructiveShip();
    for (Map.Entry<Ship, Integer> entry : mostDestructiveShip.entrySet()) {
      System.out.println(
          getShipName(entry.getKey())
              + " destroyed a total of  "
              + entry.getValue()
              + " from the opposing side");
    }
  }

  // Method to find the most destructive ship in terms of destroyed opposing ships
  private static HashMap<Ship, Integer> findMvpMostDestructiveShip() {
    // Find and return the most destructive ship and the count of destroyed opposing ships
    HashMap<Ship, Integer> mostDestructiveShip = new HashMap<>();
    Ship player1MVPByDestroyed = null;
    int player1MaxDestroyedCount = Integer.MIN_VALUE;
    for (Ship player1Ship : player1Fleet) {
      int destroyedCount = 0;
      for (Ship player2Ship : player2Fleet) {
        if (player2Ship.destroyedBy != null
            && player2Ship.destroyedBy.equalsIgnoreCase(getShipName(player1Ship))) {
          destroyedCount++;
        }
      }
      if (destroyedCount > player1MaxDestroyedCount) {
        player1MaxDestroyedCount = destroyedCount;
        player1MVPByDestroyed = player1Ship;
      }
    }
    Ship player2MVPByDestroyed = null;
    int player2MaxDestroyedCount = Integer.MIN_VALUE;

    for (Ship player2Ship : player1Fleet) {
      int destroyedCount = 0;

      for (Ship player1Ship : player1Fleet) {
        if (player1Ship.destroyedBy != null
            && player1Ship.destroyedBy.equalsIgnoreCase(getShipName(player2Ship))) {
          destroyedCount++;
        }
      }
      if (destroyedCount > player2MaxDestroyedCount) {
        player2MaxDestroyedCount = destroyedCount;
        player2MVPByDestroyed = player2Ship;
      }
    }
    if (player2MaxDestroyedCount > player1MaxDestroyedCount) {
      mostDestructiveShip.put(player2MVPByDestroyed, player2MaxDestroyedCount);
    } else if (player2MaxDestroyedCount < player1MaxDestroyedCount) {
      mostDestructiveShip.put(player1MVPByDestroyed, player1MaxDestroyedCount);
    } else {
      mostDestructiveShip.put(player2MVPByDestroyed, player2MaxDestroyedCount);
    }
    return mostDestructiveShip;
  }

  // Method to calculate the MVP for hit point damage
  private static void calculateHitPointMVP() {

    // Calculate the MVP for hit point damage based on the damage map
    for (Ship ship : player1Fleet) {
      int damage = mostDestructiveShipByDamageMap.getOrDefault(ship, 0);
      if (damage > mostDestructiveShipByHitPointPoint) {
        mostDestructiveShipByHitPointPoint = damage;
        mostDestructiveShipByHitPoint = ship.toString();
      }
    }
    for (Ship ship : player2Fleet) {
      int damage = mostDestructiveShipByDamageMap.getOrDefault(ship, 0);
      if (damage > mostDestructiveShipByHitPointPoint) {
        mostDestructiveShipByHitPointPoint = damage;
        mostDestructiveShipByHitPoint = ship.toString();
      }
    }
  }

  // Method to update the damage map for a ship
  private static void updateDamageMap(Ship ship, int damage) {
    // Update the damage map for the given ship with the specified damage
    mostDestructiveShipByDamageMap.put(
        ship, mostDestructiveShipByDamageMap.getOrDefault(ship, 0) + damage);
  }

  // Method to check if Player 1 fleet is destroyed
  private static Boolean isPlayer1FleetDestroyed() {
    // Check if all ships in Player 1 fleet are destroyed
    // Return true if all destroyed, false otherwise
    int destroyedShips = 0;
    for (Ship player1Ship : player1Fleet) {
      if (player1Ship.isDestroyed()) {
        destroyedShips = destroyedShips + 1;
      }
    }
      return player1Fleet.size() == destroyedShips;
  }

  // Method to check if Player 2 fleet is destroyed
  private static Boolean isPlayer2FleetDestroyed() {
    // Check if all ships in Player 2 fleet are destroyed
    // Return true if all destroyed, false otherwise
    int destroyedShips = 0;
    for (Ship player2Ship : player2Fleet) {
      if (player2Ship.isDestroyed()) {
        destroyedShips = destroyedShips + 1;
      }
    }
      return player2Fleet.size() == destroyedShips;
  }
}
