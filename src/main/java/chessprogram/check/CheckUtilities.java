package chessprogram.check;

import chessprogram.chessboard.Chessboard;
import chessprogram.chessboard.Copier;
import chessprogram.moveGeneration.MoveGeneratorPseudo;

import static chessprogram.chessboard.BitIndexing.UNIVERSE;

class CheckUtilities {

    static long kingDangerSquares(Chessboard board, boolean white){
        Chessboard boardWithoutMyKing = Copier.copyBoard(board, white, true);
        return MoveGeneratorPseudo.generatePseudoCaptureTable(boardWithoutMyKing, !white, 0, UNIVERSE, UNIVERSE);
    }

}
