package game;

import static game.GamePlay.getShipName;

import java.util.Random;
public class Carrier extends Ship {
  // Name of the carrier
  private final String sName;

  // Constructor for Carrier, initializing attributes based on preferred target
  public Carrier(String sName, ShipType preferredTarget) {
    super(preferredTarget);
    this.sName = sName;
    // Randomly generate the number of planes between 10 and 25
    int planes = new Random().nextInt(16) + 10;
    // Calculate attack strength based on the number of planes
    this.nAttack = planes * 10;
    // Set initial hit points, armor, and repair rate for the Carrier
    this.nHitPoints = 500;
    this.nArmour = 50;
    this.nRepairRate = 25;
  }

  // Getter method to retrieve the name of the carrier
  public String getName() {
    return sName;
  }

  // String representation of the Carrier, including its name and other attributes
  @Override
  public String toString() {
    return "Name = '" + getName() + super.toString();
  }

  // Method to handle defending against an attack from another ship
  @Override
  public void defendAttack(Ship obAttacker) {
    // Check if the Carrier is not destroyed
    if (!isDestroyed()) {
      // Generate a random probability for the success of the attack
      double attackSuccessProbability = Math.random();
      // Select a target based on the preferred target probabilities
      ShipType target = sPreferredTarget[new Random().nextInt(sPreferredTarget.length)];

      // Define the attack success probability for each target type
      double targetProbability =
              switch (target) {
                case BATTLESHIP -> 0.1;
                case CARRIER -> 0.05;
                case DESTROYER -> 0.05;
                case SUBMARINE -> 0.4;
                case PATROLBOAT -> 0.4;
              };

      // Check if the attack is successful based on probabilities
      if (attackSuccessProbability > targetProbability) {
        // Calculate damage inflicted by the attacker, considering armor
        int damage = Math.max(0, obAttacker.getAttack() - nArmour);
        // Reduce Carrier's hit points by the calculated damage
        nHitPoints -= damage;
        // Ensure hit points do not go below 0
        if (this.nHitPoints < 0) {
          nHitPoints = 0;
        }
        // Check if the Carrier is destroyed after the attack
        if (isDestroyed()) {
          // If destroyed, record the attacker's ship name as the destroyer
          String obAttackerShipName = getShipName(obAttacker);
          setDestroyedBy(obAttackerShipName);
        }
      }
    }
  }
}
