package yuanmengzeng.practice;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.List;

public class TreeQuestion {

    public static class Node{
        public int val;
        public Node left;
        public Node right;
    }

    public static class CategoryNode{
        public int value;
        public ArrayList<CategoryNode> subCategoryNode;
    }
    class S{
        public int count;
        public float popularity;
        public CategoryNode node;
    }
    private S maxNode;
    public CategoryNode getMostPopularNode(CategoryNode rootCategoryNode){
        maxNode = new S();
        maxNode(rootCategoryNode);
        return maxNode.node;
    }
    public S maxNode(CategoryNode root){
        S s = new S();
        s.popularity = root.value;
        s.node = root;
        s.count = 1;
        if (root.subCategoryNode == null || root.subCategoryNode.size()==0){

            return s;
        }
        float sum = root.value;
        for (CategoryNode child: root.subCategoryNode){
            S childS = maxNode(child);
            s.count+=childS.count;
            sum  += childS.popularity*childS.count;
        }
        float pop = sum/s.count;
        s.popularity = pop;
        if (pop>maxNode.popularity){
            maxNode = s;
        }
        return s;
    }

    /**
     * 给一组String，每个String由0或1组成，如 0001  100  001
     * 定义两个string的距离为：相同前缀外的数字个数总和。
     * 如： 0001 和 001 的相同前缀为00，则距离为count('01')+count('1') = 2+1 = 3
     * 求该组String里任意两个String的最大距离
     * @param list
     * @return
     */
    public int maxDistance(List<String> list){
        int max = 0;
        Node root = new Node();
        for(String s: list){
            Node cur = root;
            for(int i=0; i<s.length(); i++){
                int subLen = s.length()-i;
                cur.val = Math.max(cur.val,s.length()-i);
                if(s.charAt(i)=='0'){
                    if(cur.right!=null){
                        max = Math.max(subLen+cur.right.val+1,max);
                    }
                    if (cur.left==null){
                        cur.left = new Node();
                    }
                    cur = cur.left;
                }else {
                    if(cur.left!=null){
                        max = Math.max(subLen+cur.left.val+1,max);
                    }
                    if (cur.right==null){
                        cur.right = new Node();
                    }
                    cur = cur.right;
                }
            }
        }
        return max;
    }

    /**
     * 0001  100  001
     */
    public int maxDis(List<String> strings){
        int max = 0;
        Node root = new Node();
        for(String s : strings){
            Node cur = root;
            cur.val = Math.max(root.val,s.length());
            for(int i=0; i<s.length(); i++){
                int subLen = s.length()-i-1;
                if(s.charAt(i)=='0'){
                    if(cur.left==null){
                        cur.left = new Node();
                    }
                    cur.left.val = Math.max(subLen,cur.left.val);
                    if(cur.right!=null){
                        max = Math.max(cur.right.val+1+cur.left.val+1,max);
                    }
                    cur = cur.left;
                }else{
                    if (cur.right==null){
                        cur.right = new Node();
                    }
                    cur.right.val = Math.max(subLen,cur.right.val);
                    if (cur.left!=null){
                        max = Math.max(cur.left.val+1+cur.right.val+1,max);
                    }
                    cur = cur.right;
                }
            }
        }
        return max;
    }
    private int maxDepth;
    public int getHeight(TreeNode<Integer> root){
        int[] max = {1};
        helper(root, 1,max);
        return max[0];
    }
    public void helper(TreeNode<Integer> root, int depth, int[] max){
        if(root == null)
            return;
        if(root.left == null && root.right == null){
            max[0] = Math.max(depth, max[0]);
            return;
        }
        helper(root.left, depth + 1,max);
        helper(root.right, depth + 1,max);
    }

    class TreeNode<Type> {
        Type label;
        TreeNode<Type> left;
        TreeNode<Type> right;
        public TreeNode(Type label){
            this.label = label;
        }
    }
}

