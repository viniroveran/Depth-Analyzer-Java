package dumbledore.dev.interfaces;


public class NodeComparator implements java.util.Comparator<TreeNode> {
    @Override
    public int compare(TreeNode a, TreeNode b) {
        return a.getDepth() - b.getDepth();
    }
}