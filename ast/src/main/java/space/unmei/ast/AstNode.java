package space.unmei.ast;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class AstNode{

    public Pos pos; // conscious choice to keep this public

    public AstNode(Pos pos){
        this.pos = pos;
    }

    public void printNode() {
        PrintContext ctx = new PrintContext();
        ctx.print(this, 0);
    }

    private static class PrintContext {

        private final Map<Object, Integer> ids = new IdentityHashMap<>();
        private final Set<Object> printed = java.util.Collections.newSetFromMap(new IdentityHashMap<>());
        private int nextId = 1;

        private int getId(Object obj) {
            return ids.computeIfAbsent(obj, k -> nextId++);
        }

        private void print(AstNode node, int indent) {

            int id = getId(node);

            printIndent(indent);
            System.out.println( node.getClass().getSimpleName() + "#" + id);
            // Prevent infinite recursion for cyclic references.
            if (!printed.add(node)) {
                printIndent(indent + 1);
                System.out.println("<already printed>");
                return;
            }

            Class<?> clazz = node.getClass();

            while (clazz != null && clazz != Object.class) {
                 for (Field field : clazz.getDeclaredFields()) {

                        if (Modifier.isStatic(field.getModifiers())) {
                            continue;
                        }

                        field.setAccessible(true);

                        try {
                            Object value = field.get(node);
                            printIndent(indent + 1);
                            System.out.print(field.getName() + " = ");

                            if (value == null) {
                                System.out.println("null");

                            } else if (value instanceof AstNode child) {

                                int childId = getId(child);

                                System.out.println(child.getClass().getSimpleName() + "#" + childId);
                                print(child, indent + 2);

                            } else {
                                System.out.println(value.getClass().getSimpleName() + "#" + getId(value) + " = " + value);
                            }

                        } catch (IllegalAccessException e) {
                            printIndent(indent + 1);
                            System.out.println(field.getName() + " = < inaccessible >");
                        }
                     }

                     clazz = clazz.getSuperclass();
                }
        }

        private void printIndent(int count) {
            System.out.print("    ".repeat(count));
        }
    }

}
