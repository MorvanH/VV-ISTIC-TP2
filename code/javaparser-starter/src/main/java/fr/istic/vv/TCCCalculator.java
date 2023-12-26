package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.ArrayList;
import java.util.List;

public class TCCCalculator extends VoidVisitorAdapter<Void> {
    private List<String> instanceVariable;
    private List<String> methodInstanceVariable;
    private List<TCCNode> nodes;

    @Override
    public void visit(CompilationUnit unit, Void arg) {
        for(TypeDeclaration<?> type : unit.getTypes()) {
            type.accept(this, null);
        }
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, Void arg) {
        instanceVariable = declaration.getFields()
                .stream()
                .map(FieldDeclaration::getVariables)
                .flatMap(List::stream)
                .map(v -> v.getName().asString())
                .toList();
        nodes = new ArrayList<>();

        // Create all TCC nodes
        for(MethodDeclaration method : declaration.getMethods()) {
            String currentMethod = method.getNameAsString(); // Used for debug
            methodInstanceVariable = new ArrayList<>();
            method.getBody().ifPresent(body -> body.accept(this, null));
            nodes.add(new TCCNode(methodInstanceVariable));
        }

        // Print TCC
        String currentClass = declaration.getFullyQualifiedName().get(); // Used for debug
        System.out.println("Class: " + currentClass + " - TCC: " + getTCC());
    }

    @Override
    public void visit(FieldAccessExpr expr, Void arg) {
        if(instanceVariable.contains(expr.getNameAsString()) && !methodInstanceVariable.contains(expr.getNameAsString())) {
            methodInstanceVariable.add(expr.getNameAsString());
        }
    }

    public float getTCC(){
        int nbEdges = 0;
        int nbPairs = 0;

        // Count edges and pairs
        for(int i = 0; i <nodes.size(); i++) {
            for(int j = i + 1; j < nodes.size(); j++) {
                nbPairs++;
                if(nodes.get(i).sharesInstanceVariable(nodes.get(j))) {
                    nbEdges++;
                }
            }
        }

        return (float) nbEdges / nbPairs;
    }
}
