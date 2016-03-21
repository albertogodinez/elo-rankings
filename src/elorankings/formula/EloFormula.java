package elorankings.formula;


public class EloFormula {
    private String isWinner, winnersStatus, losersStatus;
    private double winnersRating, losersRating, wKRating, lKRating, pointPercentage;
    private int winnerTotalSets, 
            loserTotalSets, 
            winnerEffectiveSets,
            loserEffectiveSets,
            winnerTourneySetsPlayed,
            loserTourneySetsPlayed;

    public int getWinnerTourneySetsPlayed() {
        return winnerTourneySetsPlayed;
    }

    public void setWinnerTourneySetsPlayed(int winnerTourneySetsPlayed) {
        this.winnerTourneySetsPlayed = winnerTourneySetsPlayed;
    }

    public int getLoserTourneySetsPlayed() {
        return loserTourneySetsPlayed;
    }

    public void setLoserTourneySetsPlayed(int loserTourneySetsPlayed) {
        this.loserTourneySetsPlayed = loserTourneySetsPlayed;
    }

    public String getWinner() {
        return isWinner;
    }

    public void setWinner(String winner) {
        this.isWinner = winner;
    }

    public void setWinnerStatus(String winnersStatus) {
        this.winnersStatus = winnersStatus;
    }

    public void setLoserStatus(String losersStatus) {
        this.losersStatus = losersStatus;
    }

    public double getWinnerRating() {
        return winnersRating;
    }

    public void setWinnerRating(double winnerRating) {
        this.winnersRating = winnerRating;
    }

    public double getLoserRating() {
        return losersRating;
    }

    public void setLoserRating(double losersRating) {
        this.losersRating = losersRating;
    }

    public void setWinnerTotalSets(int winnerTotalSets) {
        this.winnerTotalSets = winnerTotalSets;
    }
    
    public int getWinnerTotalSets(){
        return winnerTotalSets;
    }

    public void setLoserTotalSets(int loserTotalSets) {
        this.loserTotalSets = loserTotalSets;
    }
    
    public int getLoserTotalSets(){
        return loserTotalSets;
    }
    
    public void setPointPercentage(double pointPercentage){
        this.pointPercentage = pointPercentage;
    }
    
    public double getPointPercentage(){
        return pointPercentage;
    }
    
    private void calculateEffectiveGames(){
        if(winnersRating > 2355)
            winnerEffectiveSets = 50;
        else{
            winnerEffectiveSets = (int)(50/Math.sqrt(0.662+(.00000739*Math.pow((2569-winnersRating),2))));
            
            if(winnerEffectiveSets > winnerTotalSets)
                winnerEffectiveSets = winnerTotalSets;
        }
        System.out.println("Winner effective sets: " + winnerEffectiveSets);
        if(losersRating > 2355)
            loserEffectiveSets = 50;
        else{
            loserEffectiveSets = (int)(50/Math.sqrt(0.662+(.00000739*Math.pow((2569-losersRating),2))));
            
            if(loserEffectiveSets > loserTotalSets)
                loserEffectiveSets = loserTotalSets;
        }
        System.out.println("\nLosers effective sets: " + loserEffectiveSets);
    }
    
    private double obtainKRating(double rating, int tourneySetsPlayed, int effectiveSets){
        double kRating = 20;
        //if(rating > )
       /* if(rating >2200 && rating<2500){
            kRating = 800*(6.5-(.0025*rating));
            kRating = kRating/(tourneySetsPlayed+effectiveSets);
        }
        else if(rating > 2500){
            kRating = 200/(tourneySetsPlayed+effectiveSets);
        }
        else
            kRating = 800/(tourneySetsPlayed+effectiveSets);*/
        System.out.println("Current kRating " + kRating + " for player with rating: " + rating);
        return kRating;
    }
    
    private double obtainWinningProbability(double playersRating, double opponentsRating){
        double winningProb = 1+ Math.pow(10, (-1*(playersRating-opponentsRating)/400));
        winningProb = 1.0/winningProb;
        System.out.println(playersRating + " winning probability is: " + winningProb);
        return winningProb;
    }
    
    @SuppressWarnings("unused")
    //MIGHT USE THIS METHOD LATER
	private double specialRatingFormula(boolean isWinner, double playersRating, double opponentsRating){
        double pointDiff = playersRating-opponentsRating;
        int addedValue = 400;
        //System.out.println("Point difference: " + pointDiff);
        /*if(pointDiff > 400.0 || pointDiff < -400.0){
            addedValue = 200;
            System.out.println("got to here");
            /*if(isWinner)
                winnerTotalSets++;
            else
                loserTotalSets++;
            return playersRating;*/
        //}
        
        double tempRating;
        int tempSets;
        if(isWinner){
           // System.out.println("got past first if");
            tempSets = winnerTotalSets+1;
            tempRating = ((playersRating*winnerTotalSets)+(opponentsRating+addedValue))/(tempSets);
            winnerTotalSets++;
            return tempRating;
        }
        else{
            tempSets = loserTotalSets+1;
            tempRating = ((playersRating*loserTotalSets)+(opponentsRating-addedValue))/(tempSets);
            loserTotalSets++;
            return tempRating;
        }     
    }
    
    private double mainRatingFormula(boolean isWinner,String opponentsStatus, double playersRating, double opponentsRating){
       /* double pointDiff = playersRating - opponentsRating;
        if(pointDiff < 0)
            pointDiff = pointDiff*-1;
        if(opponentsStatus.equals("unrated") && pointDiff >= 400){
            return playersRating;
        }*/
        double tempRating,
                winningProb = obtainWinningProbability(playersRating,opponentsRating);
        
        if(isWinner){
            tempRating = playersRating + (pointPercentage*(wKRating*(1-winningProb)));
            winnerTotalSets++;
        }
        else{
            tempRating = playersRating + (pointPercentage*(lKRating*(0-winningProb)));
            loserTotalSets++;
        }
        
        return tempRating;
    }
    
    public void calculateRating(){
        double tempWRating, tempLRating;
        
        calculateEffectiveGames();
        
        if(winnersStatus.equals("unrated")){
            //System.out.println("winnersRating: " + winnersRating + "    losersRating: "+ losersRating);
            //tempWRating = specialRatingFormula(true, winnersRating, losersRating);
            wKRating = obtainKRating(winnersRating, winnerTourneySetsPlayed, winnerEffectiveSets);
            tempWRating = mainRatingFormula(true,losersStatus, winnersRating, losersRating);
        }
        else{
            wKRating = obtainKRating(winnersRating, winnerTourneySetsPlayed, winnerEffectiveSets);
            tempWRating = mainRatingFormula(true,losersStatus, winnersRating, losersRating);
        }
        
        if(losersStatus.equals("unrated")){
            //System.out.println("winnersRating: " + winnersRating + "    losersRating: "+ losersRating);
            //tempLRating = specialRatingFormula(false, losersRating, winnersRating);
            lKRating = obtainKRating(losersRating, loserTourneySetsPlayed, loserEffectiveSets);
            tempLRating = mainRatingFormula(false, winnersStatus, losersRating, winnersRating);
        }
        else{
            lKRating = obtainKRating(losersRating, loserTourneySetsPlayed, loserEffectiveSets);
            tempLRating = mainRatingFormula(false, winnersStatus, losersRating, winnersRating);
        }
        
        winnersRating = tempWRating;
        losersRating = tempLRating;
        //System.out.println("Winner new score: " + tempWRating +
                           //"\nLoser new score: " + tempLRating);
    }
}
