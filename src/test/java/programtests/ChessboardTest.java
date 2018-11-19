//package programtests;
//
//import chessprogram.god.eRandomBoard;
//import chessprogram.god.Chessboard;
//import org.junit.Assert;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//class ChessboardTest {
//
//    private static Chessboard[] bs;
//    
//    @BeforeAll
//    static void setUp() {
//        bs = eRandomBoard.boardForTests();
//        eRandomBoard.printBoards(bs);
//    }
//
//    @Test
//    void equals() {
//        
//        for (Chessboard b : bs){
//            Assert.assertEquals(b, b);
//        }
//        
//    }
//}