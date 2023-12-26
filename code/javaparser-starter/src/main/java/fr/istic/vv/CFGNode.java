package fr.istic.vv;

import java.util.HashSet;
import java.util.Set;

import static java.lang.System.exit;

public class CFGNode {
    enum NodeType{
        START,
        END,
        COND,
        RETURN,
        BASIC
    }

    private final NodeType type;
    private CFGNode left;
    private CFGNode right;

    public CFGNode(NodeType type){
        this.type = type;
        this.left = null;
        this.right = null;
    }

    public CFGNode(NodeType type, CFGNode left, CFGNode right){
        this.type = type;
        this.left = left;
        this.right = right;
    }



    public void setNode(CFGNode newNode){
        switch(this.type){
            case START:
                this.left = newNode;
                break;
            case END:
                System.out.println("Error: Node end cannot have children");
                exit(1);
            case COND:
                if(this.left == null || this.left.type == NodeType.END){
                    this.left = newNode;
                }
                else if(this.right == null){
                    this.right = newNode;
                }
                else{
                    System.out.println("Error: Node already has two children");
                    exit(1);
                }
                break;
            case RETURN:
                if (this.left == null) {
                    this.left = newNode;
                }
                else{
                    System.out.println("Error: Node already has a child");
                    exit(1);
                }
                break;
            case BASIC:
                if (this.left == null || this.left.type == NodeType.END) {
                    this.left = newNode;
                } else {
                    System.out.println("Error: Node already has a child");
                    exit(1);
                }
                break;
        }
    }

    public float getCyclomaticComplexity() {
        int nbNodes = getNbNodes(new HashSet<>());
        int nbEdges = getNbEdges(new HashSet<>());
        return nbEdges - nbNodes + 2;
    }

    private int getNbNodes(Set<CFGNode> visitedNodes){
        if(visitedNodes.contains(this)){
            return 0;
        }
        visitedNodes.add(this);
        switch (this.type){
            case START:
                return 2 + this.left.getNbNodes(visitedNodes); // +2 for the start and end nodes
            case RETURN:
                return 1;
            case COND:
                if(this.right == null){
                    return 1 + this.left.getNbNodes(visitedNodes);
                }
                else{
                    return 1 + this.left.getNbNodes(visitedNodes) + this.right.getNbNodes(visitedNodes);
                }
            case BASIC:
                return 1 + this.left.getNbNodes(visitedNodes);
        }

        return 0;
    }

    private int getNbEdges(Set<CFGNode> visitedNodes){
        if(visitedNodes.contains(this)){
            return 0;
        }
        visitedNodes.add(this);
        switch (this.type){
            case START, BASIC:
                return 1 + this.left.getNbEdges(visitedNodes);
            case END:
                return 0;
            case COND:
                if(this.right == null){
                    return 1 + this.left.getNbEdges(visitedNodes);
                }
                else{
                    return 2 + this.left.getNbEdges(visitedNodes) + this.right.getNbEdges(visitedNodes);
                }
            case RETURN:
                return 1;
        }

        return 0;
    }
}
