import chessprogram.god.*;
import org.junit.Test;

import static chessprogram.god.BitOperations.getIndexOfFirstPiece;
import static chessprogram.god.BitboardResources.NORTH_EAST;
import static chessprogram.god.BitboardResources.NORTH_WEST;
import static chessprogram.god.Square.*;

public class RayPinnedTest {

    @Test
    public void rayTest(){
        long a = 0x0000000000000001L;
        long b = 0x0000000001000000L;
        Art.printLong(a);
        Art.printLong(b);

//        Art.printLong(a-b);
        Art.printLong(b & (b-1));
    }

    @Test
    public void adjTest(){

        Setup.setup();
        final int length = BitboardResources.inBetweenSquares.length;

        for (int i = 0; i < 64; i++){
            Art.printLong(BitboardResources.inBetweenSquares[0][i]);
        }
        
    }

}
