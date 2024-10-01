package dumbledore.dev.interfaces;

import java.util.List;

public class TreeNode {
    private String title;
    private int depth;
    private String parent;
    private List<TreeNode> parentNodes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
    public List<TreeNode> getParentNodes() {
        return this.parentNodes;
    }
    public void setParentNodes(List<TreeNode> parentNodes) {
        this.parentNodes = parentNodes;
    }
}