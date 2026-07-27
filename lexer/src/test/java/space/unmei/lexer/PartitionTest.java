package space.unmei.lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PartitionTest{

    @Test
    void partitionEqualTest(){
        Partition p1 = new Partition();
        Partition p2 = new Partition();

        p1.addNode(1);
        p1.addNode(2);

        p2.addNode(2);
        p2.addNode(1);

        assertEquals(p1, p2);


    }




}
