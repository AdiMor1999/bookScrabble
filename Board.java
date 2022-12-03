package test;
import java.util.ArrayList;

public class Board {
    private static final int lengthRowCol = 15; // Define

    private Square[][] board= new Square[lengthRowCol][lengthRowCol];
    //green-0
    //blue-1
    //lightBlue-2
    //yellow-3
    //red-4
    private int[][] temp=  {{4,0,0,2,0,0,0,4,0,0,0,2,0,0,4},
                            {0,3,0,0,0,1,0,0,0,1,0,0,0,3,0},
                            {0,0,3,0,0,0,2,0,2,0,0,0,3,0,0},
                            {2,0,0,3,0,0,0,2,0,0,0,3,0,0,2},
                            {0,0,0,0,3,0,0,0,0,0,3,0,0,0,0},
                            {0,1,0,0,0,1,0,0,0,1,0,0,0,1,0},
                            {0,0,2,0,0,0,2,0,2,0,0,0,2,0,0},
                            {4,0,0,2,0,0,0,0,0,0,0,2,0,0,4},
                            {0,0,2,0,0,0,2,0,2,0,0,0,2,0,0},
                            {0,1,0,0,0,1,0,0,0,1,0,0,0,1,0},
                            {0,0,0,0,3,0,0,0,0,0,3,0,0,0,0},
                            {2,0,0,3,0,0,0,2,0,0,0,3,0,0,2},
                            {0,0,3,0,0,0,2,0,2,0,0,0,3,0,0},
                            {0,3,0,0,0,1,0,0,0,1,0,0,0,3,0},
                            {4,0,0,2,0,0,0,4,0,0,0,2,0,0,4}};
    private Board() {
        for (int i=0;i<lengthRowCol;i++)
        {
            for (int j=0;j<lengthRowCol;j++)
            {
                board[i][j]=new Square(temp[i][j],null);
            }
        }
    }

    private static Board b=null;
    public static Board getBoard(){//singleton - the first that call to getBoard() create the object and everyone else gets reference to the same object
        if(b==null)
        {
            b=new Board();
        }
        return b;
    }

    //return a matrix of tiles according to the board
    public Tile[][] getTiles()
    {
        Tile[][] tiles = new Tile[15][15];
        for (int i=0;i<lengthRowCol;i++)
        {
            for (int j=0;j<lengthRowCol;j++)
            {
                tiles[i][j]=b.board[i][j].t;
            }
        }
        return tiles;
    }
    public boolean isFirstWord(Word word)//return true if it is the first word it must be on star[7][7]
    {

        int len=word.getTiles().length;
        if(word.isVertical())
        {

            if(word.getCol()!=7)
            {
                return false;
            }
            else
            {
                if((word.getRow()>7)|| (word.getRow()+len-1)<7)
                {
                    return false;
                }
            }
            //the problem is that we return true also for the second word because she is also on star,and then we double the score,and it's not good
            //fix-if also the letter in the place of star is null return f because is not the first word - the first word is full.
            if(word.getTiles()[7 - word.getRow()] == null){
                return false;
            }
            return true;
        }
        else //horizontal
        {
            if(word.getRow()!=7){
                return false;
            }
            else{
                if((word.getCol()>7)|| (word.getCol()+len-1)<7){
                    return false;
                }
            }
            if(word.getTiles()[7 - word.getCol()] == null){
                return false;
            }
            return true;
        }

    }
    public boolean isInBoard(Word word)//return true if the whole word can be inside the bored
    {
        if(word.isVertical())
        {
            if(word.getRow()<0||word.getRow()>=lengthRowCol)
                return false;
            if(lengthRowCol-word.getRow()<word.getTiles().length)
            {
                return false;
            }
            return true;
        }
        else //horizontal
        {
            if(word.getCol()<0||word.getCol()>=lengthRowCol)
                return false;
            if(lengthRowCol-word.getCol()<word.getTiles().length)
            {
                return false;
            }
            return true;
        }
    }
    public boolean isLeaningOnTile(Word word)//return true if the word leans on some tile
    {
        int len=word.getTiles().length;
        int lastTileV =word.getRow()+len-1;
        int lastTileH =word.getCol()+len-1;
        int indexTile=0;
        if(word.isVertical())
        {
            //Overlapping tile=חופף
            for (int i= word.getRow();i<=lastTileV;i++)
            {

                if(b.board[i][word.getCol()].t !=null&& word.getTiles()[indexTile]==null)
                    return true;
                indexTile++;
            }
            //adjacent tile = צמוד
            for (int i=word.getRow();i<=lastTileV;i++)
            {
                if(b.board[i][word.getCol()-1]!=null || b.board[i][word.getCol()+1]!=null || b.board[i-1][word.getCol()]!=null || b.board[i+1][word.getCol()]!=null)
                    return true;
            }
        }
        else ////horizontal
        {
            //Overlapping tile=חופף
            for (int j= word.getCol();j<=lastTileH;j++)
            {
                if(b.board[word.getRow()][j].t !=null&& word.getTiles()[indexTile]==null)
                    return true;
                indexTile++;
            }
            //adjacent tile = צמוד
            for (int j=word.getCol();j<=lastTileH;j++)
            {
                if(b.board[word.getRow()][j-1]!=null || b.board[word.getRow()][j+1]!=null || b.board[word.getRow()-1][j]!=null || b.board[word.getRow()+1][j]!=null)
                    return true;
            }
        }
        return false;
    }
    public boolean boardLegal(Word word)//return true if the whole word can be inside the board+Lean on one of the existing tiles+No replacement of existing tiles required
    {
       if(b.board[7][7].t==null)// if is it tha first word it must be on start
       {
           if(isFirstWord(word)==false)
           {
               return false;
           }
       }
        if(!(isInBoard(word)))//the whole word can be inside the board
        {
            return false;
        }
        if(b.board[7][7].t!=null)//not the first word
        {
            if(!(isLeaningOnTile(word)))//Lean on one of the existing tiles
            {
                return false;
            }
        }
        return true;
    }
    public boolean dictionaryLegal(Word word)//return true if the word legal
    {
        return true;
    }
    public Word findWord(int row, int col, ScanMode scanMode){//gets place in the board and try to find a new word
        ArrayList<Tile> arrayTile=new ArrayList<>();//in order to save the nwe word that created
        BoardScanner boardScanner=new BoardScanner(b,row,col,scanMode);//boardScanner in order to scan the word without knowing if it vertical
        while (boardScanner.getCurrentTile() != null){
            boardScanner.MoveBack();//move to the beginning of the word
        }
        boardScanner.MoveNext();//=null so move one step to the first letter
        int startRow = boardScanner.getCurrentRow(); //save the row of the first letter
        int startCol = boardScanner.getCurrentCol(); //save the col of the first letter

        while (boardScanner.getCurrentTile() != null){ //As long as I haven't reached the end of the word, we will add the letters to the arrayTile
            arrayTile.add(boardScanner.getCurrentTile());
            boardScanner.MoveNext();
        }
        if(arrayTile.size() == 1){//This square doesn't create a new word
            return null;
        }
        //if we find a new word we will return her
        return new Word(arrayTile.toArray(new Tile[arrayTile.size()]),startRow,startCol, scanMode == ScanMode.VERTICAL);
    }
    public ArrayList<Word> getWords(Word word)//return a vector of all the new words that will be created
    {
        ArrayList<Word> arrayWord=new ArrayList<>();
        arrayWord.add(word);
        ScanMode scanMode = word.isVertical()? ScanMode.VERTICAL : ScanMode.HORIZONTAL;
        ScanMode oppositeScanMode = word.isVertical()? ScanMode.HORIZONTAL : ScanMode.VERTICAL;
        BoardScanner boardScanner=new BoardScanner(b,word.getRow(),word.getCol(),scanMode);
        Word newWord;
        int index =0;
        Tile[] tile = word.getTiles();
        while (boardScanner.getCurrentTile() != null){
            if(tile[index] != null){
                newWord = findWord(boardScanner.getCurrentRow(),boardScanner.getCurrentCol(),oppositeScanMode);
                if(newWord != null){
                    arrayWord.add(newWord);
                }
            }
            index++;
            boardScanner.MoveNext();
        }
            return arrayWord;
        }
    public int getScore(Word word)//calculate the score of the word + bonus
    {
        int score = 0;
        int wordX2 = 0;
        int wordX3 = 0;
        ScanMode scanMode = word.isVertical()? ScanMode.VERTICAL : ScanMode.HORIZONTAL;
        BoardScanner boardScanner=new BoardScanner(b,word.getRow(),word.getCol(),scanMode);
        while (boardScanner.getCurrentTile() != null){
            Square currentSquare = boardScanner.getCurrentSquare();
            int color = currentSquare.colorSquare;
            int squareFactor = 1;
            switch (color){
                case 1:
                    squareFactor = 3;
                    break;
                case 2:
                    squareFactor = 2;
                    break;
                case 3:
                    wordX2++;
                    break;
                case 4:
                    wordX3++;
                    break;
            }
            score += currentSquare.t.score * squareFactor;
            boardScanner.MoveNext();
        }
        if(wordX2 > 0){
            score *= wordX2 * 2;
        }
        if(wordX3 > 0){
            score *= wordX3 * 3;
        }
        return score;
    }
    public boolean insertWord(Word word){//insert a word to the board
        ScanMode scanMode = word.isVertical()? ScanMode.VERTICAL : ScanMode.HORIZONTAL;
        BoardScanner boardScanner=new BoardScanner(b,word.getRow(),word.getCol(),scanMode);
        Tile[] tiles = word.getTiles();
        for (int i = 0; i < tiles.length; i++) {
            Square currentSquare = boardScanner.getCurrentSquare();
            if(tiles[i] != null){
                currentSquare.t = word.getTiles()[i];
            }
            boardScanner.MoveNext();
        }
        return true;
    }
    public boolean removeWord(Word word){
        ScanMode scanMode = word.isVertical()? ScanMode.VERTICAL : ScanMode.HORIZONTAL;
        BoardScanner boardScanner=new BoardScanner(b,word.getRow(),word.getCol(),scanMode);
        for (int i = 0; i < word.getTiles().length; i++) {
            boardScanner.getCurrentSquare().t = null;
            boardScanner.MoveNext();
        }
        return true;
    }
    public int tryPlaceWord(Word word)//return the total score
    {
        if(!(dictionaryLegal(word)))//if the word is illegal return 0
            return 0;
        if(!(boardLegal(word)))//if the word doesn't fit to the bord return 0
            return 0;

        boolean inserted = insertWord(word);
        if(!inserted){
            return 0;
        }
        ArrayList<Word> arrayWord= getWords(word); // all the new words that formed by putting the word
        int sumPlaceWord=0;
        for (int i=0;i<arrayWord.size();i++) //loop of the arr word and calculate the score if it legal
        {
            if(!(dictionaryLegal(arrayWord.get(i)))){
                removeWord(word);
                return 0;
            }
           sumPlaceWord+= getScore(arrayWord.get(i));
        }
        if(isFirstWord(word)){
            sumPlaceWord*=2;
        }
        return sumPlaceWord;
    }
    public class Square{
        int colorSquare;
        Tile t;

        public Square(int scoreSquare, Tile t) {
            this.colorSquare = scoreSquare;
            this.t = t;
        }

        public int getColorSquare() {
            return colorSquare;
        }

        public Tile getT() {
            return t;
        }
    }
    enum ScanMode {
        HORIZONTAL,VERTICAL
    }
    public class BoardScanner{
        Board b;
        int currentRow;
        int currentCol;
        ScanMode scanMode;
        public BoardScanner(Board b, int currentRow, int currentCol,ScanMode scanMode) {
            this.b = b;
            setPosition(currentRow,currentCol);
            setScanMode(scanMode);
        }
        public void setPosition(int currentRow, int currentCol){
            this.currentRow = currentRow;
            this.currentCol = currentCol;
        }
        public void setScanMode(ScanMode scanMode){
            this.scanMode = scanMode;
        }
        public boolean isInBoard(){//return t if the row and col is in the board
            if(currentRow<0 || currentRow>=lengthRowCol)
                return false;
            if(currentCol<0 || currentCol>=lengthRowCol)
                return false;
            return true;
        }
        public Tile getCurrentTile() {//return the current tile
            return isInBoard() ? b.board[currentRow][currentCol].t : null;
        }
        public Square getCurrentSquare() {//return the current square
            return isInBoard() ? b.board[currentRow][currentCol] : null;
        }
        public int getCurrentRow(){
            return this.currentRow;
        }
        public int getCurrentCol(){
            return this.currentCol;
        }
        public void MoveNext(){//if the word is horizontal col++, if the word is vertical row++
            switch(this.scanMode) {
                case HORIZONTAL:
                    currentCol ++;
                    break;
                case VERTICAL:
                    currentRow ++;
                    break;
            }
        }
        public void MoveBack(){//if the word is horizontal col--, if the word is vertical row--
            switch(this.scanMode) {
                case HORIZONTAL:
                    currentCol --;
                    break;
                case VERTICAL:
                    currentRow --;
                    break;
            }
        }
    }
}
