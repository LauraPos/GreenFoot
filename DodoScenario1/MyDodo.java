    import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
    
    /**
     *
     * @author Sjaak Smetsers & Renske Smetsers-Weeda
     * @version 3.0 -- 20-01-2017
     */
    public class MyDodo extends Dodo
    {
        private int myNrOfEggsHatched;
    
        public MyDodo() {
            super( EAST );
            myNrOfEggsHatched = 0;
        }
    
        public void act() {
        }
    
        /**
         * Move one cell forward in the current direction.
         * 
         * <P> Initial: Dodo is somewhere in the world
         * <P> Final: If possible, Dodo has moved forward one cell
         *
         */
        public void move() {
            if ( canMove() ) {
                step();
            } else {
                showError( "I'm stuck!" );
            }
        }
    
        /**
         * Test if Dodo can move forward, (there are no obstructions
         *    or end of world in the cell in front of her).
         * 
         * <p> Initial: Dodo is somewhere in the world
         * <p> Final:   Same as initial situation
         * 
         * @return boolean true if Dodo can move (no obstructions ahead)
         *                 false if Dodo can't move
         *                      (an obstruction or end of world ahead)
         */
        public boolean canMove() {
            if ( borderAhead() || fenceAhead() ){
                return false;
            } else {
                return true;
            }
        }
        
        public void walkToEndLayingEggsInEmptyNests() {
        while (!borderAhead()) {
            move();
            if (onNest() && !onEgg()) {
                layEgg();
            }
        }
        }
        
        public void walkAroundFencedArea() {
        while (!onEgg()) {
            turnRight();
            for (int i = 0; i < 4; i++) {
                if (canMove()) {
                    move();
                    break;
                }
                turnLeft();
            }
        }
        }
            
        public void eggTrailToNest() {
        while (!onNest()) {
            if (onEgg()) {
                hatchEgg();
            } else if ((eggAhead() || nestAhead()) && canMove()) {
                step();
            } else {
                turnRight();
            }
        }
        }
        
        public void faceEast() {
        while (getDirection() != EAST) {
            turnRight();
        }
        }
        
        public boolean validCoordinates(int x, int y) {
        return x >= 0 && x < getWorld().getWidth() && y >= 0 && y < getWorld().getHeight();
        }
        
        public void layTrailOfEggs(int amount) {
        if (amount <= 0) {
            showError("Aantal moet groter zijn dan 0!");
            return;
        }
        if (amount > getWorld().getWidth()) {
            showError("Aantal is te groot voor de wereld!");
            return;
        }
        
        layEgg();
        int count = 1;
        while (count < amount) {
            move();
            layEgg();
            count++;
        }
        }
    
        public void goToLocation(int coordX, int coordY) {
        if (!validCoordinates(coordX, coordY)) {
            showError("Invalid coordinates");
            return;
        }
        while (getX() != coordX || getY() != coordY) {
            if (getX() < coordX) {
                setDirection(EAST);
                move();
            } else if (getX() > coordX) {
                setDirection(WEST);
                move();
            } else if (getY() < coordY) {
                setDirection(SOUTH);
                move();
            } else if (getY() > coordY) {
                setDirection(NORTH);
                move();
            }
        }
        }
        
        public int countEggsInRow() {
        int count = 0;
        if (onEgg()) {
            count++;
        }
        while (canMove()) {
            move();
            if (onEgg()) {
                count++;
            }
        }
        return count;
        }
    
        public void countEggsInWorld() {
        int startX = getX();
        int startY = getY();
        int totalCount = 0;
     
        setDirection(EAST);
        goToLocation(1, 1);
     
        for (int row = 0; row < getWorld().getHeight(); row++) {
            int eggsInRow = countEggsInRow();
            totalCount += eggsInRow;
            System.out.println("Rij " + row + ": " + eggsInRow + " eieren");
     
            if (row < getWorld().getHeight() - 1) {
                setDirection(WEST);
                while (getX() != 0) {
                    move();
                }
                setDirection(SOUTH);
                move();
                setDirection(EAST);
            }
        }
            goToLocation(startX, startY);
            showCompliment("Er liggen in totaal " + totalCount + " eieren in de wereld");
        }
        
        public int findRowWithMostEggs() {
        int startX = getX();
        int startY = getY();
        int maxEggs = 0;
        int rowWithMaxEggs = 0;
    
        goToLocation(0, 0);
    
        for (int row = 0; row < getWorld().getHeight(); row++) {
            goToLocation(0, row);
            faceEast();
            int eggsInRow = countEggsInRow();
    
            if (eggsInRow > maxEggs) {
                maxEggs = eggsInRow;
                rowWithMaxEggs = row;
            }
        }
    
        System.out.println("Rij met de meeste eieren: " + rowWithMaxEggs + " (" + maxEggs + " eieren)");
    
        goToLocation(startX, startY);
        return rowWithMaxEggs;
        }   
        
        public void fillWorldWithDoublingPattern() {
        int startX = getX();
        int startY = getY();
        int row = 0;
        int eggsThisRow = 1;

        while (validCoordinates(startX, startY + row)) {
            goToLocation(startX, startY + row);
            faceEast();
            layTrailOfEggs(eggsThisRow);
            eggsThisRow = eggsThisRow * 2;
            row++;
        }

        goToLocation(startX, startY);
        }
        
        public void buildSturdyMonument() {
        int startX = getX();
        int startY = getY();
        int startDirection = getDirection();
        int rowsAvailable = getWorld().getHeight() - startY;
        int colsAvailable = getWorld().getWidth()  - startX;
 
        setDirection(EAST);
 
        int eggsInRow = 1;
        int rowsBuilt = 0;
 
        while (rowsBuilt < rowsAvailable && eggsInRow <= colsAvailable) {
            
            for (int col = 0; col < eggsInRow; col++) {
                
            if (canLayEgg()) layEgg();
            
            if (col < eggsInRow - 1) move();
            }

            rowsBuilt++;
 
            boolean nextRowFits = rowsBuilt < rowsAvailable
            && eggsInRow * 2 <= colsAvailable;

            if (!nextRowFits) break;
 
            setDirection(WEST);

            while (getX() != startX) move();

            setDirection(SOUTH);

            move();

            setDirection(EAST);

            eggsInRow *= 2;

        }
 
        goToLocation(startX, startY);
        setDirection(startDirection);

        }
 
        
        public void fillWorldWithPyramidPattern() {
        int startX = getX();
        int startY = getY();
        int row = 0;

        while (validCoordinates(startX, startY + row)) {
            int eggsThisRow = row * 2 + 1;
            int leftX = startX - row;
            int rightX = startX + row;

            if (validCoordinates(leftX, startY + row) && validCoordinates(rightX, startY + row)) {
            goToLocation(leftX, startY + row);
            faceEast();
            layTrailOfEggs(eggsThisRow);
            } else {
            showError("Piramide past niet meer in de wereld op deze rij");
            }

            row++;
        }
        goToLocation(startX, startY);
        }
        
        public double calculateAverageEggsPerRow() {
        int startX = getX();
        int startY = getY();
        int totalCount = 0;
        int totalRows = getWorld().getHeight();

        goToLocation(0, 0);
        faceEast();

        for (int row = 0; row < totalRows; row++) {
        goToLocation(0, row);
        faceEast();
        int eggsInRow = countEggsInRow();
        totalCount += eggsInRow;
        System.out.println("Rij " + row + ": " + eggsInRow + " eieren");
        }

        double average = (double) totalCount / totalRows;
        System.out.println("Gemiddeld aantal eieren per rij: " + average);

        goToLocation(startX, startY);
        return average;
        }
        
        
        
        public void solveMaze() {
        while (!onNest()) {
            turnRight();
            for (int i = 0; i < 4; i++) {
                if (canMove()) {
                    move();
                    break;
                }
                turnLeft();
            }
        }
        }
        
        
        /**
         * Mimi gaat over het hekje springen
         */
    
        public void climbOverFence(){
            if(fenceAhead() && getDirection() == EAST){
                setDirection(NORTH);
                move();
                setDirection(EAST);
                move();
                move();
                setDirection(SOUTH);
                move();
                setDirection(EAST);
            }
            else if(fenceAhead() && getDirection() == WEST){
                setDirection(NORTH);
                move();
                setDirection(WEST);
                move();
                move();
                setDirection(SOUTH);
                move();
                setDirection(WEST);
            }
            else if(fenceAhead() && getDirection() == SOUTH){
                setDirection(WEST);
                move();
                setDirection(SOUTH);
                move();
                move();
                setDirection(EAST);
                move();
                setDirection(SOUTH);  
            }
        }
    
         public void walkToWorldEdgeClimbingOverFences() {
        while (!borderAhead()) {
            if (fenceAhead()) {
                climbOverFence();
            } else {
                move();
            }
            if (onNest()) {
                layEgg();
                break;
            }
        }
        }
        
        /**
         * de grainaheadmethode
         */
    
        public boolean grainAhead(){
            move();
            if (onGrain() == true){
                stepOneCellBackwards();
                return true;
            }
            else {
                setDirection(EAST);
                move();
                return false;
            }
        }
        
        public void stepOneCellBackwards(){
            turn180();
            move();
            turn180();
        }
    
        public void pickUpGrainsAndPrintCoordinates(){
            while (!borderAhead()){
                move();
                if(onGrain()){
                    System.out.println(getX() + "," + getY());
                    pickUpGrain();
                }
            }
        }
    
        /**
         * Hatches the egg in the current cell by removing
         * the egg from the cell.
         * Gives an error message if there is no egg
         * 
         * <p> Initial: Dodo is somewhere in the world. There is an egg in Dodo's cell.
         * <p> Final: Dodo is in the same cell. The egg has been removed (hatched).     
         */    
        public void hatchEgg () {
            if ( onEgg() ) {
                pickUpEgg();
                myNrOfEggsHatched++;
            } else {
                showError( "There was no egg in this cell" );
            }
        }
    
        /**
         * Returns the number of eggs Dodo has hatched so far.
         * 
         * @return int number of eggs hatched by Dodo
         */
        public int getNrOfEggsHatched() {
            return myNrOfEggsHatched;
        }
    
        /**
         * Move given number of cells forward in the current direction.
         * 
         * <p> Initial:   
         * <p> Final:  
         * 
         * @param   int distance: the number of steps made
         */
        public void jump( int distance ) {
            int nrStepsTaken = 0;               // set counter to 0
            while ( nrStepsTaken < distance ) { // check if more steps must be taken  
                move();                         // take a step
                nrStepsTaken++;   
                System.out.println("moved" + nrStepsTaken);// increment the counter
            }
        }
    
        /**
         * Walks to edge of the world printing the coordinates at each step
         * 
         * <p> Initial: Dodo is on West side of world facing East.
         * <p> Final:   Dodo is on East side of world facing East.
         *              Coordinates of each cell printed in the console.
         */
    
        public void walkToWorldEdgePrintingCoordinates( ){
            while( ! borderAhead() && canMove() ){
                //System.out.println(getX() + "," + getY());
                move();
            }
        }
    
        public void goBackToStartOfRowAndFaceBack(){
            setDirection(WEST);
            walkToWorldEdgePrintingCoordinates();
            turn180();
        }
    
        /**
         * Test if Dodo can lay an egg.
         *          (there is not already an egg in the cell)
         * 
         * <p> Initial: Dodo is somewhere in the world
         * <p> Final:   Same as initial situation
         * 
         * @return boolean true if Dodo can lay an egg (no egg there)
         *                 false if Dodo can't lay an egg
         *                      (already an egg in the cell)
         */
    
        public boolean canLayEgg(){
            if( onEgg() ){
                return false;
            }else{
                return true;
            }
    
        }
    
        public void gotoEgg()
        {
            while (!onEgg())
            {
                move();
            }
        }
    
        /**
         * dodo turns 180 degrees
         */
        public void turn180() {
            if(getDirection()== WEST ){
                setDirection(EAST);
            }
            else setDirection(WEST);
        }
    }