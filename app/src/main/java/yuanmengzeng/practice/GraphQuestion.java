package yuanmengzeng.practice;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GraphQuestion {

    // Expedia OA    E:\文件\找工\Expedia oa\bestfriend
    public int findMinTrioScore(int pNum, int[] friend_from, int[] friend_to){
        int min = pNum;
        HashMap<Integer, List<Integer>> relations = new HashMap<>();
        for(int i= 0; i<friend_from.length; i++){
            List<Integer> singleRel = relations.get(friend_from[i]);
            if (singleRel==null){
                singleRel = new LinkedList<>();
                relations.put(friend_from[i],singleRel);
            }
            singleRel.add(friend_to[i]);
        }
        Set<Set<Integer>> allTrios = new HashSet<>();
        for (Integer node: relations.keySet()){
            Set<Set<Integer>> trios = findTrios(node,relations,allTrios);
            for (Set<Integer> trio: trios){
                HashSet<Integer> trioRel = new HashSet<>();
                for (Integer single: trio){
                    trioRel.addAll(relations.get(single));
                }
                if (min>trioRel.size()-3){
                    min = trioRel.size()-3;
                }
                Utils.println("trio:%s   score:%d",trio.toString(),trioRel.size()-3);
            }
        }
        return min;
    }

    private Set<Set<Integer>> findTrios(int node, HashMap<Integer,List<Integer>> relations,Set<Set<Integer>> allTrios){
        Set<Set<Integer>> trios = new HashSet<>();
        List<Integer> singleRe = relations.get(node);
        for (Integer second: singleRe){
            for (Integer third: relations.get(second)){
                HashSet<Integer> trio = new HashSet<>();
                trio.add(node);
                trio.add(second);
                trio.add(third);
                if (allTrios.contains(trio)) {
                    trio.remove(third);
                    continue;
                }
                for (Integer forth: relations.get(third)){
                    if (forth == node){
                        trios.add(trio);
                        allTrios.add(trio);
                        Utils.println("find trio -> %d %d %d",node, second, third);
                    }
                }
            }
        }
        Utils.println("trios.size->"+trios.size());
        return trios;
    }

    /**
     * 24331 11582
     * 40676 17095
     * 36278 30532
     * 41086 28684
     * 20917 27058
     * 12962 30532
     * 12962 27058
     * 12686 41706
     * 11904 36954
     * 36278 27058
     * 20917 30532
     * 28688 28593
     * 37112 27058
     * 18069 27058
     * 32436 27058
     * 37390 21596
     * 18069 30532
     * 27549 28756
     * 32436 30532
     * 37112 30532
     *
     * Expedia oa
     * E:\文件\找工\Expedia oa\ZigZag_Move
     */
    public int maxPoints(List<String> points){
        int res = 0;
        HashMap<String, Set<String>> cor = new HashMap<>();
        for (String point: points){
            String[] ss = point.split(" ");
            Set<String> verPoint = cor.get(ss[0]);
            if (verPoint==null){
                verPoint = new TreeSet<>();
                cor.put(ss[0],verPoint);
            }
            verPoint.add(ss[1]);
        }
        HashMap<String , Integer> singlePath = new HashMap<>();
        for(String s: cor.keySet()){
            Set<String> ySet = cor.get(s);
            String[] yArray = new String[ySet.size()];
            ySet.toArray(yArray);
            for (int i=0; i<yArray.length-1; i++){
                for(int j=i+1;j<yArray.length;j++){
                    String p = yArray[i]+yArray[j];
                    int point = singlePath.getOrDefault(p,0);
                    point++;
                    res = Math.max(point,res);
                    singlePath.put(p,point);
                }
            }
        }
        return res;
    }

    /**
     * google 自行车问题
     * @param map
     * @param x 人的x坐标
     * @param y 人的y坐标
     * @return
     */
    public int[] findNearestBike(int[][] map, int x, int y){
        if (map[x][y]==1){
            int[] res = new int[3];
            res[0]= x;
            res[1]= y;
            return res;
        }
        boolean[][] visit = new boolean[map.length][map[0].length];
        visit[x][y] = true;
        LinkedList<Point> queue = new LinkedList<>();
        queue.offer(new Point(x,y));
        while (!queue.isEmpty()){
            Point pos = queue.poll();
            if (pos.x -1 >=0 ){
                if (map[pos.x-1][pos.y]==1){
                    return bikeRes(pos.x-1,pos.y,x,y);
                }else if(!visit[pos.x-1][pos.y]){
                    visit[pos.x-1][pos.y] = true;
                    queue.offer(new Point(pos.x-1,pos.y));
                }

            }
            if (pos.x +1 <map.length ){
                if (map[pos.x+1][pos.y]==1){
                    return bikeRes(pos.x+1,pos.y,x,y);
                }else if(!visit[pos.x+1][pos.y]){
                    visit[pos.x+1][pos.y] = true;
                    queue.offer(new Point(pos.x+1,pos.y));
                }

            }
            if (pos.y-1>=0){
                if (map[pos.x][pos.y-1]==1){
                    return bikeRes(pos.x,pos.y-1,x,y);
                }else if(!visit[pos.x][pos.y-1]){
                    visit[pos.x][pos.y-1] = true;
                    queue.offer(new Point(pos.x,pos.y-1));
                }
            }

            if (pos.y+1<map[0].length){
                if (map[pos.x][pos.y+1]==1){
                    return bikeRes(pos.x,pos.y+1,x,y);
                }else if(!visit[pos.x][pos.y+1]){
                    visit[pos.x][pos.y+1] = true;
                    queue.offer(new Point(pos.x,pos.y+1));
                }
            }
        }
        return null;

    }

    private int[] bikeRes(int bikeX, int bikeY, int pX, int pY){
        int[] res = new int[3];
        res[0] = bikeX;
        res[1] = bikeY;
        res[2] = Math.abs(bikeX-pX)+Math.abs(bikeY-pY);
        return  res;
    }


    public int countConnectedGraph(int[][] board){
        int cnt = 0;
        boolean[][] visited = new boolean[board.length][board[0].length];
        loopCount = 0;
        for (int i=0; i<board.length; i++){
            for(int j=0; j<board[0].length; j++){
                if (board[i][j]==1 && DFS(board,i,j,false,false,true,true,visited)){
                    cnt++;
                }else {
                    loopCount++;
                }
            }
        }
        return cnt;
    }

    private int loopCount = 0;

    private boolean DFS(int[][] board, int i, int j, boolean leftWard,boolean upWard, boolean rightWard, boolean downWard, boolean[][] visited){
        //base case
        if(visited[i][j]) return false;
        visited[i][j] = true;

        //recursive rule
        //leftWard
        for (int nextJ = j-1; leftWard&&nextJ>=0; nextJ--){
            loopCount++;
            if (board[i][nextJ]==1){
                DFS(board,i,nextJ,true,true,false,true,visited);
                break;
            }
        }

        // rightWard
        for (int nextJ = j+1; rightWard && nextJ<board[0].length; nextJ++){
            loopCount++;
            if (board[i][nextJ]==1){
                DFS(board,i,nextJ,false,true,true,true,visited);
                break;
            }
        }
        //upWard
        for (int nextI = i-1; upWard && nextI>=0; nextI--){
            loopCount++;
            if (board[nextI][j]==1){
                DFS(board,nextI,j,true,true,true,false,visited);
                break;
            }
        }

        //downWard
        for (int nextI = i+1; downWard && nextI<board.length; nextI++){
            loopCount++;
            if (board[nextI][j]==1){
                DFS(board,nextI,j,true,false,true,true,visited);
                break;
            }
        }
        return true;
    }

    public int getLoopCount(){
        return loopCount;
    }
}
