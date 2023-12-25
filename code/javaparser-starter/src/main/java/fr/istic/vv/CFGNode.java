package fr.istic.vv;

import static java.lang.System.exit;

public class CFGNode {
    enum NodeType{
        START,
        END,
        IF,
        WHILE,
        FOR,
        RETURN
    }

    private NodeType type;
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
            case START, WHILE, FOR:
                this.left = newNode;
                break;
            case END:
                System.out.println("Error: Node end cannot have children");
                exit(1);
            case IF:
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
                if(this.left == null){
                    this.left = newNode;
                }
                else{
                    System.out.println("Error: Node already has a child");
                    exit(1);
                }
        }
    }

    public float getCyclomaticComplexity() {
        int nbNodes = getNbNodes();
        int nbEdges = getNbEdges();
        return nbEdges - nbNodes + 2;
    }

    private int getNbNodes(){
        switch (this.type){
            case START:
                return 2 + this.left.getNbNodes(); // +2 for the start and end nodes
            case RETURN:
                return 1;
            case IF:
                if(this.right == null){
                    return 1 + this.left.getNbNodes();
                }
                else{
                    return 1 + this.left.getNbNodes() + this.right.getNbNodes();
                }
            case WHILE, FOR:
                return 1 + this.left.getNbNodes();
        }

        return 0;
    }

    private int getNbEdges(){
        switch (this.type){
            case START:
                return 1 + this.left.getNbEdges();
            case END:
                return 0;
            case IF:
                if(this.right == null){
                    return 1 + this.left.getNbEdges();
                }
                else{
                    return 2 + this.left.getNbEdges() + this.right.getNbEdges();
                }
            case RETURN:
                return 1;
        }

        return 0;
    }
}
