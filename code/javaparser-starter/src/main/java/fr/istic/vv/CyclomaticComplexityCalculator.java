package fr.istic.vv;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

class CyclomaticComplexityCalculator extends VoidVisitorAdapter<Void> {
    private CFGNode root;
    private CFGNode currentNode;
    private CFGNode lastNode;

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
        for(MethodDeclaration method : declaration.getMethods()) {
            String currentMethod = method.getNameAsString();
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
        CFGNode ifNode = new CFGNode(CFGNode.NodeType.COND);
        currentNode.setNode(ifNode);
        currentNode = ifNode;

        stmt.getThenStmt().accept(this, null);
        currentNode = ifNode;
        if(stmt.hasElseBlock()){
            stmt.getElseStmt().get().accept(this, null);
        }
    }

    @Override
    public void visit(WhileStmt stmt, Void arg) {
        CFGNode whileNode = new CFGNode(CFGNode.NodeType.COND);
        currentNode.setNode(whileNode);
        currentNode = whileNode;
        stmt.getBody().accept(this, null);

        // If the condition is false, the loop is not executed
        currentNode.setNode(whileNode);
        currentNode = whileNode;
    }

    @Override
    public void visit(ForStmt stmt, Void arg) {
        CFGNode assignNode = new CFGNode(CFGNode.NodeType.BASIC);
        currentNode.setNode(assignNode);
        currentNode = assignNode;
        CFGNode forCondNode = new CFGNode(CFGNode.NodeType.COND);
        currentNode.setNode(forCondNode);
        currentNode = forCondNode;
        stmt.getBody().accept(this, null);

        // If the condition is false, the loop is not executed
        currentNode.setNode(forCondNode);
        currentNode = forCondNode;

    }

    @Override
    public void visit(DoStmt stmt, Void arg) {
        CFGNode doNode = new CFGNode(CFGNode.NodeType.COND);
        currentNode.setNode(doNode);
        currentNode = doNode;
        stmt.getBody().accept(this, null);

        // If the condition is false, the loop is not executed
        currentNode.setNode(doNode);
        currentNode = doNode;
    }

    @Override
    public void visit(ReturnStmt stmt, Void arg) {
        CFGNode returnNode = new CFGNode(CFGNode.NodeType.RETURN);
        returnNode.setNode(lastNode);
        currentNode.setNode(returnNode);
        currentNode = returnNode;
    }

    @Override
    public void visit(ExpressionStmt stmt, Void arg) {
        CFGNode blockNode = new CFGNode(CFGNode.NodeType.BASIC);
        currentNode.setNode(blockNode);
        currentNode = blockNode;
        blockNode.setNode(lastNode);
    }
}