# Brainstorming

Ideas
-
* Pokemon game app
    * lets users interact with 3 pokemon a day, can either fight or catch it
* team planner for team fight tactics
    * lets users plan teams for team fight tactics with positioning and more
* Gym app
    * helps users do specific exercise and diets depending on wether they want to gain muscle, lose weight or be lean


Evaluation
* REAL APP HERE
    * Mobile: App sees the pokemon they own and wild pokemons
    * Story: The app lets players catch and interact with pokemons and play game
    * Market: App for anyone who likes pokemons
    * Habit: This app may be addictive but like wordle, it limits you with how much you interaction you have
    * Scope: Uses database for local save, pulls 5 random pokemon to interact with everyday can be challenging but not impossible
  <br>
* team planner for team fight tactics
    * Mobile: The app uses current team fight tactics set and lets people plan their units
    * Story: people can play their units without having to be in the game
    * Market: people who plays team fight tactics
    * Habit: people might spend a lot of time in the app when planning their teams
    * Scope: This app will be technically challenging to create, but should be possible.
    * 
  <br>
*  Gym app
    * Mobile: app uses input from the user and guide them to reach their goals
    * Story: People can get help with their physical health goals
    * Market: people whose interested 
    * Habit: This app can be addictive for someone who wants to try different types of games within their taste.
    * Scope: This app should be easy, research is required for the different data to be given to the user

THE FINAL DECISION
## **Pokemon game App**
### 1. User Features (Required and Optional)
**Required Features**

1. Have players to choose from 3 different pokemons
2.  Be able to save your own pokemon in local database
3.  Have a wild pokemon interaction feature

**Optional Features**

1.  Have different pokeballs that increases the catch chances
2.  Have a shop feature that lets users spend money
3.  See the list of pokemons interacted

### 2. Screen Archetypes

-   First pokemon interaction
    -   lets players interact and choose different pokemons  
    -   places them as a buddy pokemon
-   Interacting with wild pokemons
    -   randomized pokemon that can interact with either fight or catch
 - Caught pokemons
    - Pokemons caught would be saved and displayed
 - Pokemon detail view
   - pokemon would have their picture
   - pokemon description
   - moves they have


### **Flow Navigation** (Screen to Screen)

1.  **Pokemon Interaction Screen**
    
    -   Navigates to:
        -   **Pokemon Caught Screen** (when pokemon is caught).
        -  **Pokemon defeated** (when the pokemon is defeated)
        -   **Current Pokemon Out Screen** (Triggered by selecting "View Current Pokemon").
    -   Returns to the main menu upon back navigation.
2.  **Pokemon Caught Screen**
    
    -   Navigates to:
        -   **Pokemon Details Screen** (Triggered by selecting a specific caught Pokemon to view details).
    -   Returns to the main menu upon back navigation.

3. **Pokemon Caught Screen**
      - Navigates to:
          - **Current Pokemon Out Screen**(Showing the xp gained and the current level)
5.  **Current Pokemon Out Screen**

    -   Navigates to:
        -   **Pokemon Interaction Screen** (Triggered by selecting "Back to Interaction").
        -   **Pokemon Battle Screen** (Triggered by selecting "Start Battle").
    -   Returns to the main menu upon back navigation.


### Wireframes

<img src='https://i.imgur.com/puTox6f.jpeg' title='Video Walkthrough' width='' alt='Video Walkthrough' />
