package programtests;

import chessprogram.miscAdmin.RandomBoard;
import chessprogram.chessboard.Chessboard;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ChessboardTest {

    private static Chessboard[] bs;
    
    @BeforeAll
    static void setUp() {
        bs = RandomBoard.boardForTests();
        RandomBoard.printBoards(bs);
    }

    @Test
    void equals() {
        
        for (Chessboard b : bs){
            Assert.assertEquals(b, b);
        }
        
    }
}