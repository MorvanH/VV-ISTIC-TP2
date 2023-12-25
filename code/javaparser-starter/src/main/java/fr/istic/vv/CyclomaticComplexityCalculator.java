package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.ast.visitor.VoidVisitorWithDefaults;

import java.util.ArrayList;
import java.util.List;

class CyclomaticComplexityCalculator extends VoidVisitorAdapter<Void> {
    private CFGNode root;
    private CFGNode currentNode;
    private CFGNode lastNode;

    private String currentMethod;
    public float getCyclomaticComplexity() {
        return root.getCyclomaticComplexity();
    }

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
            currentMethod = method.getNameAsString();
            this.root = new CFGNode(CFGNode.NodeType.START);
            this.currentNode = this.root;
            this.lastNode = new CFGNode(CFGNode.NodeType.END);
            root.setNode(lastNode);

            method.getBody().ifPresent(body -> body.accept(this, null));

            System.out.println("Method: " + currentMethod + " - Cyclomatic Complexity: " + getCyclomaticComplexity());
        }
    }

    @Override
    public void visit(IfStmt stmt, Void arg) {
        CFGNode ifNode = new CFGNode(CFGNode.NodeType.IF);
        currentNode.setNode(ifNode);
        currentNode = ifNode;

        stmt.getThenStmt().accept(this, null);
        if(stmt.hasElseBlock()){
            currentNode = ifNode;
            stmt.getElseStmt().get().accept(this, null);
        }
    }

    @Override
    public void visit(BlockStmt stmt, Void arg) {
        for(Statement s : stmt.getStatements()){
            s.accept(this, null);
        }
    }

    @Override
    public void visit(WhileStmt stmt, Void arg) {
        CFGNode whileNode = new CFGNode(CFGNode.NodeType.WHILE);
        currentNode.setNode(whileNode);
        currentNode = whileNode;

        stmt.getBody().accept(this, null);
    }

    @Override
    public void visit(ForStmt stmt, Void arg) {
        CFGNode forNode = new CFGNode(CFGNode.NodeType.FOR);
        currentNode.setNode(forNode);
        currentNode = forNode;

        stmt.getBody().accept(this, null);
    }

    @Override
    public void visit(ReturnStmt stmt, Void arg) {
        CFGNode returnNode = new CFGNode(CFGNode.NodeType.RETURN);
        returnNode.setNode(lastNode);
        currentNode.setNode(returnNode);
        currentNode = returnNode;
    }
}