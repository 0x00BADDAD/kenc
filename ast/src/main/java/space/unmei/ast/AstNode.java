package space.unmei.ast;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public class AstNode{

    public Pos pos; // conscious choice to keep this public

    public AstNode(Pos pos){
        this.pos = pos;
    }

    private void printMargin(int margin){
        System.out.printf(String.valueOf("-").repeat(margin));
    }

    public void printNode(int indent){
        this.printMargin(indent);
        // there will be two kinds of field that every node will have
        // (1) that extend (directly or indirectly) the AstNode class
        // (2) those that do not
        Class<?> clazz = this.getClass();

        String runTimeName = clazz.getName();
        System.out.printf(" "+runTimeName+"\n");


        List<Field> fields = Arrays.asList(clazz.getDeclaredFields());
        // flatten by removing the List
        List<Object> fieldObjs = new ArrayList<>();
        List<AstNode> childNodes = new ArrayList<>();

        for (Field field : fields) {
            field.setAccessible(true);

            try{
                Object value = field.get(this);

                if (value instanceof List<?> list) {
                    for (Object element : list) {
                        if(element instanceof AstNode node){
                            childNodes.add(node);
                        }else{
                            fieldObjs.add(element);
                        }
                    }
                }else{
                    if(value instanceof AstNode node){
                        childNodes.add(node);
                    }else{
                        // this object could be null
                        fieldObjs.add(value);
                    }
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        for(Object obj: fieldObjs){
            this.printMargin(indent+2);
            if(obj != null){
                System.out.printf(" "+obj.getClass().getName()+"\n");
            }else{
                System.out.printf(" This field is null" + "\n");
            }
        }
        for(AstNode node: childNodes){
            node.printNode(indent+2);
        }
    }

}
