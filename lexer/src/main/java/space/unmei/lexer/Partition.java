package space.unmei.lexer;

import java.util.concurrent.ThreadLocalRandom;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Objects;

public class Partition implements Iterable<Integer>{

   private Set<Integer> nodes;

   public Partition() {
       this.nodes = new HashSet<>();
   }

   public Partition(Collection<Integer> nodes){
        this.nodes = new HashSet<>(nodes);
   }

   public void addNode(int x){
        this.nodes.add(x);
   }

   public int pickOne(){
        return this.nodes.iterator().next();
   }

   @Override
   public Iterator<Integer> iterator() {
       return this.nodes.iterator();
   }

   public int size(){
       return this.nodes.size();
   }

   @Override
   public boolean equals(Object o) {
       if (this == o) return true;
       if (!(o instanceof Partition other)) return false;
       return nodes.equals(other.nodes);
   }

   @Override
   public int hashCode() {
       return nodes.hashCode();
   }
}
