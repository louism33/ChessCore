package chessprogram.god;

import chessprogram.god.Chessboard;
import chessprogram.god.dCopier;
import chessprogram.god.MoveGeneratorPseudo;

import static chessprogram.god.dBitIndexing.UNIVERSE;

class cCheckUtilities {

    static long kingDangerSquares(Chessboard board, boolean white){
        Chessboard boardWithoutMyKing = dCopier.copyBoard(board, white, true);
        return MoveGeneratorPseudo.generatePseudoCaptureTable(boardWithoutMyKing, !white, 0, UNIVERSE, UNIVERSE);
    }

}
