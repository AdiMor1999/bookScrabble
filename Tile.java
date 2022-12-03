package test;


import java.util.Objects;
import java.util.Random;

public class Tile {
    public final char letter;
    public final int score;

    private Tile(char letter, int score) {//private because we don't want everyone to create Tile
        this.letter = letter;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && score == tile.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    public char getLetter() {
        return letter;
    }

    public int getScore() {
        return score;
    }

    public static class Bag{ //the only class that create Tile
        int [] maxQuantities={9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1};
        int[] quantitiesArr = maxQuantities.clone();//how many tile for each letter array of quantities
        Tile[] tiles= new Tile[26]; //if we want the tile
        int countTilesBag=98;//count for the Tiles in the bag
        private static Bag b=null;
        int[] scores={1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
        char[] letters={'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

        private Bag() { //private because we don't want one Bag
            for (int i=0;i<26;i++)
            {
                this.tiles[i]=new Tile(letters[i],scores[i]);
            }
        }
        public static Bag getBag(){//singleton - the first that call to getBag() create the object and everyone else gets reference to the same object
           if(b==null)
           {
               b=new Bag();
           }
           return b;
        }
        public Tile getRand(){
            Random rand=new Random();
            int random = rand.nextInt(26);//generate random values from 0-25
            int count=0;
            while (this.quantitiesArr[random]==0 && count==26 )//this letter is over
            {
                random++;
                if(random==26)
                {
                    random=0;
                }
                count++;
            }
            if(count==26)
            {
                return null;
            }

            this.quantitiesArr[random]--;//we rand this index number that represent a count of letter
            countTilesBag--;
            return this.tiles[random];//we return the Tile in the random index
        }
        public Tile getTile(char ch){
            if(ch<'A'||ch>'Z'){
                return null;
            }
            int index=ch-'A';
            if(this.quantitiesArr[index]>0)
            {
                this.quantitiesArr[index]--;
                countTilesBag--;
                return this.tiles[index];
            }
            else return null;
        }
        public void put(Tile t){
            int index=t.getLetter()-'A';
            if(this.quantitiesArr[index]==this.maxQuantities[index])
            {
                return;
            }
            this.quantitiesArr[index]++;
            countTilesBag++;
        }
        public int size(){
            return this.countTilesBag;
        }
        public int[] getQuantities(){
           return quantitiesArr.clone();
        }
    }
}
