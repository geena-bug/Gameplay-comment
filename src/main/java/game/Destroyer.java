package game;

import java.util.Random;

import static game.GamePlay.getShipName;
class Destroyer extends Ship {
  // Name of the destroyer
  private String sName;

  // Constructor for Destroyer, initializing attributes based on preferred target
  public Destroyer(String sName, ShipType preferredTarget) {
    super(preferredTarget);
    this.sName = sName;
    // Randomly generate attack strength between 50 and 75
    this.nAttack = new Random().nextInt(26) + 50;
    // Set initial hit points, armor, and repair rate for the Destroyer
    this.nHitPoints = 100;
    this.nArmour = 25;
    this.nRepairRate = 15;
  }

  // Getter method to retrieve the name of the destroyer
  public String getName() {
    return sName;
  }

  // String representation of the Destroyer, including its name and other attributes
  @Override
  public String toString() {
    return "Name = '" + getName() + super.toString();
  }

  // Method to handle defending against an attack from another ship
  @Override
  public void defendAttack(Ship obAttacker) {
    // Check if the Destroyer is not destroyed
    if (!isDestroyed()) {
      // Generate a random probability for the success of the attack
      double attackSuccessProbability = Math.random();
      // Select a target based on the preferred target probabilities
      ShipType target = sPreferredTarget[new Random().nextInt(sPreferredTarget.length)];

      // Define the attack success probability for each target type
      double targetProbability =
              switch (target) {
                case BATTLESHIP -> 0.1;
                case CARRIER -> 0.7;
                case DESTROYER -> 0.1;
                case SUBMARINE -> 0.05;
                case PATROLBOAT -> 0.05;
              };

      // Check if the attack is successful based on probabilities
      if (attackSuccessProbability > targetProbability) {
        // Calculate damage inflicted by the attacker, considering armor
        int damage = Math.max(0, obAttacker.getAttack() - nArmour);
        // Reduce Destroyer's hit points by the calculated damage
        nHitPoints -= damage;
        // Ensure hit points do not go below 0
        if (this.nHitPoints < 0) {
          nHitPoints = 0;
        }
        // Check if the Destroyer is destroyed after the attack
        if (isDestroyed()) {
          // If destroyed, record the attacker's ship name as the destroyer
          String obAttackerShipName = getShipName(obAttacker);
          setDestroyedBy(obAttackerShipName);
        }
      }
    }
  }
}
