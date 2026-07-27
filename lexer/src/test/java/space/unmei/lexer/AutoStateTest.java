package space.unmei.lexer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class AutoStateTest{

    @Test
    void autoStateSetEqual(){
        Set<AutoState> s1 = new HashSet<>();
        Set<AutoState> s2 = new HashSet<>();

        s1.add(new AutoState("a_1", new ArrayList<>(), new ArrayList<>()));
        s1.add(new AutoState("a_2", new ArrayList<>(), new ArrayList<>()));

        s2.add(new AutoState("a_2", new ArrayList<>(), new ArrayList<>()));
        s2.add(new AutoState("a_1", new ArrayList<>(), new ArrayList<>()));

        assertEquals(s1, s2);
    }



}
