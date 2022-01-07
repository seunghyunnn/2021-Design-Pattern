# Overview

A basic ballboy game written in JavaFX. Try to reach the finish without colliding with the enemies!

# Acknowledgement for the base code  

This code is designed for the University of Sydney, School of Computer Sciences. 

# Getting Started 

#### Running 

`gradle run`

#### Building 

`gradle build`

# Game Rule
You can control the ballboy. 

If you reach to the end of the game (i.e. ballboy touches tree figure), 
you will level-up.

If there is no more level, then you win the game. 

If the ballboy collides with enemy, the ballboy will go back to 
its starting point. 

The square cat orbiting around the ballboy can eliminate the 
enemy by touching it.

Your score is changed whenever you remove the enemy with color 
Red, Green, or Blue. This level-specific score will be added to you 
final total score every time you level-up or finishes the game.

You can save the game status and come back to that specific moment 
when you want to. 


# Game Controls

The ballboy is controlled through the left, right and up arrow keys.

Pressing up will boost the bounce height, with a maximum bounce height being the level height.

Pressing left or right will move the entity in the given direction.

Lifting the left or right key after pressing it, without having at least one of the two keys down, will result
in the bounce height being reduced.

# Configuration

The level configuration is to be located in the resources directory with the name `config.json`.

The root level configuration consists of `currentLevelIndex', specifying which level to load from the `levels` array. 

The following fields are necessary for the level configuration: `levelWidth`, `levelHeight`, `floor`, `levelGravity`, `maxHeroVelocityX` and `scoreOn`. An example is shown below:

``` json
  "_levelWidthComment": "The actual width of the level. This will be used to enforce game boundaries",
  "levelWidth": 2000.0,
  "_levelHeightComment": "The actual height of the level. This will also be used to enforce game boundaries",
  "levelHeight": 620.0,
  "_floorComment": "The configuration of the levels floor",
  "floor": {
    "_heightComment": "The floor height. Dynamic entities will not be allowed below this",
    "height": 600.0,
    "_colorComment": "Color must be a web color, e.g. #0033cc",
    "color": "#001100"
  },
  "_levelGravityComment": "The gravity applied to bouncing entities. This is in pixels per millisecond",
  "levelGravity": 700.0,
  "_maxHeroVelocityX": "The maximum horizontal velocity of the hero. This is used to provide usable game controls",
  "maxHeroVelocityX": 50,
  "scoreOn": "--B",
```

The generic entities for the level are configured as shown below:
``` json
   "_genericEntitiesComment":"A list of all generic entities. They are generic in the sense that the level treats them as a batch, with no specific understanding or logic for each",
   "genericEntities":[
   {
      "type": "cloud",
      "startX": 234.0,
      "startY": 0.1,
      "horizontalVelocity": -20.2,
      "_imageComment": "This is an optional field. A suitable default will be selected if it is not provided.",
      "image": "cloud_1.png"
   },
   {
      "type":"static",
      "posX":400,
      "posY":565,
      "_heightComment":"The width will be derived from the height and image dimensions, with the ratio being preserved.",
      "height":40,
      "image":"bolder.png"
   },
   {
      "type": "enemy",
      "startX": 700.0,
      "startY": 540.1,
      "startVelocityX": 12.0,
      "height": 40.0,
      "_imageComment": "This is an optional field. A suitable default will be selected if it is not provided.",
      "image": "slimeBa.png",
      "color": "B",
      "behaviour": "passive"
    },
   {
      "type":"background",
      "posX":0.0,
      "posY":400.0,
      "height":200,
      "image":"landscape_0000_1_trees.png"
   }
]
```

The hero is configured as shown below:
```json
  "hero": {
    "type": "hero",
    "startX": 150.0,
    "startY": 300.0,
    "_sizeComment": "options include small, medium, large. If not specified this field will default to normal",
    "size": "large",
    "image": "ch_stand2.png"
  }
```

The finish is configured as shown below:

```json
  "finish": {
    "type": "finish",
    "posX": 1200.0,
    "posY": 520.0,
    "height": 80.0,
    "width": 40
  }
```

The complete configuration shown above is sourced from `config.json` in the resources directory.

#### Example Configuration

The current levels have slimes as the enemies and a tree as the finish.

- The current default level is index 0, and is a demonstration of all the required features (including but not limited to those listed below):
  - This may take a few tries to finish, especially if you're not familiar with the inputs.
  - The three enemies have different behaviours
  - Colliding with an enemy will reset the hero to the starting position
  - Colliding with the finish (the tree) will bring end the game.
  - The camera will follow the hero both vertically and horizontally.
  - The floor is configurable in color, as outlined above.
  - Clouds move at a constant speed, and do not collide with foreground objects.
  - Static immobile objects are included in the form of rocks and two stone walls.
  - The ballboy can be loaded in three sizes (small medium and large)
- It is easy to reach the finish with the third configuration, which can be loaded by changing `  "currentLevelIndex": 0` to `  "currentLevelIndex": 1`. You can find the configuration file in the resources directory.

# Design Patterns 

### Factory Method 

###### Product

See Entity in Entity.java

###### Concrete Products

See StaticEntityImpl in StaticEntityImpl.java, and DynamicEntityImpl in DynamicEntityImpl.java.

###### Creator

See EntityFactory in EntityFactory.java

###### Concrete Creator

- StaticEntityFactory in StaticEntityFactory.java
- FinishFactory in FinishFactory.java
- CloudFactory in CloudFactory.java
- EnemyFactory in EnemyFactory.java
- BallboyFactory in BallboyFactory.java
- SquareCatFactory in SquareCatFactory.java
These are registered in EntityFactoryRegistry in EntityFactoryRegistry.java, and injected in the root level App class in App.java


### Strategy Pattern

#### 1. Entity Behaviour 

###### Strategy

See BehaviourStrategy in BehaviourStrategy.java

###### ConcreteStrategy

- AggressiveEnemyBehaviourStrategy in AggressiveEnemyBehaviourStrategy.java
- PassiveBehaviourStrategy in PassiveBehaviourStrategy.java
- ScaredEnemyBehaviourStrategy in ScaredEnemeyBehaviourStrategy.java
- FloatingCloudBehaviourStrategy in FloatingCloudBehaviourStrategy.java
- OrbitingEntityBehaviourStrategy in OrbitingEntityBehaviourStrategy.java

###### Context

See DynamicEntityImpl in DynamicEntityImpl.java

#### 2. CollisionResolution

###### Strategy

See CollisionStrategy in CollisionStrategy.java

###### ConcreteStrategy

- BallboyCollisionStrategy in BallboyCollisionStrategy.java
- EnemyCollisionStrategy in EnemyCollisionStrategy.java
- PassiveCollisionStrategy in PassiveCollisionStrategy.java
- SquareCatCollisionStrategy in SquareCatCollisionStrategy.java

###### Context

See DynamicEntityImpl in DynamicEntityImpl.java

#### 3. BallBoy

###### ConcreteDecorator

See ControllableDynamicEntity.java

###### ConcreteComponent

See DynamicEntityImpl.java

#### 4. Level Transition and Score

###### Observer

See Observer.java

###### Subject

See Subject.java

###### ConcreteObserver

See App.java, GameEngine.java, GameEngineImpl.java, GameWindow.java. 

###### ConcreteSubject

See Level.java, LevelImp.java, GameEngine.java, GameEngineImpl.java

#### 5. Save and Load

###### Memento

See the Memento.java, State.java.

###### Caretaker

See the Caretaker.java.

###### Originator

See the KeyboardInputHandler.

###### ConstraintSolver

See the GameEngineImpl.java, GameEngine.java.

###### Prototype

See the Prototype.java

###### Concrete Prototype

- See BehaviourStrategy.java and the behaviour strategies that implement this interface (AggresiveEnemyBehaviourStrategy.java, FloatingCloudBehaviourStrategy.java, OrbitingEntityBehaviourStrategy.java,
PassiveEntityBehaviourStrategy.java, ScaredEnemyBehaviourStrategy.java)
- See CollisionStrategy.java and the collision strategies that implement this interface(BallboyCollisionStrategy.java, EnemyCollisionStrategy.java, PassiveCollisionStrategy.java, SquareCatCollisionStrategy.java)
- See the Enity.java and the entities that impelement this interface(ControllableDynamicEntity.java, DynamicEntity.java, DynamicEntityImpl.java, StaticEntity.java, StaticEntityImpl.java)
- See Level.java and LevelImpl.java
- See Memento.java

###### Client 

-See Memento.java
-See Caretaker.java



# Image Sources

- [Boulder](https://www.pngwing.com/en/free-png-zrzud/download)
- [Stone Block](https://www.deviantart.com/sarahstudiosart/art/Stone-Wall-1-458649249)

