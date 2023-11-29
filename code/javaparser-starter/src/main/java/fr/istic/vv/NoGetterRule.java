package fr.istic.vv;


import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.List;

public class NoGetterRule extends VoidVisitorWithDefaults<Void> {
    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        List<String> attributes = declaration.getFields()
                .stream()
                .map(FieldDeclaration::getVariables)
                .flatMap(List::stream)
                .map(v -> v.getName().asString())
                .toList();
        for(MethodDeclaration method : declaration.getMethods()) {
            for(String attribute: attributes) {
                if(method.getNameAsString().toLowerCase().equals("get" + attribute)) {
                    System.out.print(declaration.getFullyQualifiedName().get() + " >   ");
                    System.out.println("Getter on field \'" + attribute + "\' : \'" + method.getNameAsString() + "\' is not allowed");
                }
            }
        }
    }


}

