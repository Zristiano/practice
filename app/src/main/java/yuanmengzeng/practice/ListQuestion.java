package yuanmengzeng.practice;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Q1: Leecode 2
 * <p>
 * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order and each of
 * their nodes contain a single digit. Add the two numbers and return it as a linked list.You may assume the two numbers do not contain
 * any leading zero, except the number 0 itself.
 * </p>
 *     Example:
 * <p>
 * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
 * </p>
 * Output: 7 -> 0 -> 8
 * <p>
 * Explanation: 342 + 465 = 807.
 * </p>
 * <br></br>
 * Q2: Leetcode 3
 * <p>
 * Merge k sorted linked lists and return it as one sorted list. Analyze and describe its complexity.
 * </p>
 * Example:
 * <p>
 * Input:   1->4->5, 1->3->4, 2->6
 * </p>
 * Output:  1->1->2->3->4->4->5->6
 */
public class ListQuestion {
    public static class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
  }

    /**
     * <p>
     *     LeetCode 2
     * </p>
     * <br>
     * pay attention to :
     * <p>
     *      case 1 : when the highest digit should carry 1;
     * </p>
     *      case 2 : either number's digits is longer than the other's;
     * <p>
     *      case 3 : based on case 2, when the higher digit need consecutively carry 1. For example, 999876 + 44;
     * </p>
     * @param l1
     * @param l2
     * @return
     */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2){
        if (l1 ==null || l2 ==null){
            throw new IllegalArgumentException("l1 and l2 cannot be null");
        }
        boolean carryFlag = false;
        ListNode node1 = l1;
        ListNode node2 = l2;
        ListNode prev = null;
        while (node1!=null && node2 !=null){
            prev = node1;
            node1.val = node1.val+node2.val+(carryFlag?1:0);
            carryFlag = node1.val>=10;
            node1.val = node1.val%10;
            node1 = node1.next;
            node2 = node2.next;
        }
        if (node1==null && node2 ==null){
            if (carryFlag){
                ListNode lastNode = new ListNode(1);
                lastNode.next =null;
                prev.next = lastNode;
            }
        }else if (node1==null){
            prev.next = node2;
            node1 = node2;
        }
        while (carryFlag){
            if (node1==null){
                ListNode lastNode = new ListNode(1);
                lastNode.next = null;
                prev.next = lastNode;
                carryFlag = false;
            }else {
                node1.val++;
                carryFlag = node1.val>=10;
                node1.val = node1.val%10;
                prev = node1;
                node1=node1.next;
            }
        }
        return l1;
    }

    /**
     * used for {@link ListQuestion#addTwoNumbers(ListNode l1, ListNode l2)
     */
    public ListQuestion.ListNode productReverseList(int num){
        String s = String.valueOf(num);
        int len = s.length();
        ListQuestion.ListNode node = new ListQuestion.ListNode(Integer.valueOf(s.substring(len-1,len)));
        ListQuestion.ListNode head = node;
        for (int i = len-1;i>0;i--){
            node.next = new ListQuestion.ListNode(Integer.valueOf(s.substring(i-1,i)));
            node = node.next;
        }
        return head;
    }

    /**
     * used for {@link ListQuestion#mergeKLists(ListNode[] lists)
     */
    public ListQuestion.ListNode productList(int num){
        String s = String.valueOf(num);
        ListQuestion.ListNode node = new ListQuestion.ListNode(Integer.valueOf(s.substring(0,1)));
        ListQuestion.ListNode head = node;
        for (int i = 1; i<s.length(); i++){
            node.next = new ListQuestion.ListNode(Integer.valueOf(s.substring(i,i+1)));
            node = node.next;
        }
        return head;
    }

    public void traverseList(ListNode head){
        if (head==null){
            Utils.println("list is empty");
        }else {
            ListNode p = head;
            while (p!=null){
                System.out.print(p.val+" ");
                p = p.next;
            }
        }
    }

    /**
     * Leetcode 23
     * @param lists  the array stored several head of sorted linked lists;
     * @return the head of a sorted linked list integrating all the input linked lists.
     */
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists==null){
            return null;
        }
        PriorityQueue<ListNode> headQueue = new PriorityQueue<>(new Comparator<ListNode>() {
            @Override
            public int compare(ListNode o1, ListNode o2) {
                return o1.val-o2.val;
            }
        });
        for (ListNode node:lists){
            if (node!=null){
                headQueue.offer(node);
            }
        }
        ListNode head = new ListNode(0);
        ListNode cur = head;
        while (!headQueue.isEmpty()){
            cur.next = headQueue.poll();
            cur = cur.next;
            if (cur.next!=null){
                headQueue.offer(cur.next);
            }
        }
        return head.next;
    }

    public ListNode sortList(ListNode head){
        if(head == null || head.next == null){
            return null;
        }
        PriorityQueue<ListNode> pq = new PriorityQueue<ListNode>(new Comparator<ListNode>(){
            public int compare(ListNode l1, ListNode l2){
                int diff = l1.val - l2.val;
                return diff==0? 1:diff;
            }
        });
        ListNode vhead = new ListNode(0);
        ListNode fhead = vhead;
        while(head != null){
            pq.add(head);
            head = head.next;
        }
        while(!pq.isEmpty()){
            vhead.next = pq.poll();
            vhead = vhead.next;
        }
        vhead.next = null;
        // vhead.next = pq.poll();
        return fhead.next;
    }

    /**
     * LeetCode 19
     */
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ArrayList<ListNode> list = new ArrayList<>();
        ListNode p = head;
        do{
            list.add(p);
            p = p.next;
        }while(p!=null);
        int removeIdx = list.size()-n;
        if(removeIdx==0){  // remove the first node from the start
            return list.get(1);
        }else{
            ListNode temp = list.get(removeIdx-1);
            temp.next = temp.next.next;
        }
        return head;
    }


    /**
     * leetcode 25
     */
    public ListNode reverseKGroup(ListNode head, int k) {
        if(k<=1){
            return head;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pHeadPrev = dummy;
        ListNode pTail = head;
        ListNode pNextStart = head;
        do{
            for(int i=0; i<k-1; i++){
                if(pTail.next==null){
                    pHeadPrev.next = pNextStart;
//                    dummy.next.next = null;
                    return dummy.next;
                }
                pTail = pTail.next;
            }
            pNextStart = pTail.next;
            pTail = head;
            head = reverseSingleList(head,k);
            pHeadPrev.next = head;
            pHeadPrev = pTail;
            head = pNextStart;
            pTail = head;
        }while(pTail!=null);
//        dummy.next.next = null;
//        pHeadPrev.next = pNextStart;
        return dummy.next;
    }

    /**
     * dummy->A->B->C->D
     */
    public ListNode reverseSingleList(ListNode head, int len){
        ListNode nextStart = head.next.next;
        ListNode cur = head.next;
        head.next = null;
        for(int i=0; i<len-1; i++){
            cur.next = head;
            head = cur;
            cur = nextStart;
            if(nextStart!=null){
                nextStart = nextStart.next;
            }
        }
        return head;
    }

    public boolean insertMeeting(int[][] schedule, int start, int end){
        Arrays.sort(schedule,new Comparator<int[]>(){
            public int compare(int[] o1, int[] o2){
                return o1[0]-o2[0];
            }
        });
        int lo = 0;
        int hi = schedule.length-1;
        if (start>=schedule[schedule.length-1][1]){
            return true;
        }
        if (end<=schedule[0][0])
        {
            return true;
        }
        while (lo<hi-1){
            int mid = lo +(hi-lo)/2;
            if (schedule[mid][0]<=start){
                lo = mid;
            }else {
                hi  = mid;
            }
        }

        return start>=schedule[lo][1] && end<=schedule[hi][0];
    }


    public void isAvailable(List<int[][]> schedules){
        PriorityQueue<int[]> planQueue = new PriorityQueue<>(new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0]-o2[0];
            }
        });
        for (int[][] schedule : schedules){
            planQueue.addAll(Arrays.asList(schedule));
        }

        int lastTime = 0;
        while(!planQueue.isEmpty()){
            int[] meeting = planQueue.poll();
            if (lastTime<meeting[0]){
                Utils.println("[ "+lastTime+" , "+meeting[0]);
                lastTime = meeting[1];
            }
        }
    }

    public int getVisited(int n, List<Integer> sprints){
        int max = 0;
        int idx = 0;
        int[] freq = new int[n];
        TreeSet<Integer> boderVisited = new TreeSet<>(sprints);
        Integer[] visits = new Integer[boderVisited.size()];
        boderVisited.toArray(visits);

        int j = Arrays.binarySearch(visits,sprints.get(0));
        for (int i=0; i<sprints.size() -1; i++){
            if (sprints.get(i)>=sprints.get(i+1)){
                while (j>=0 && visits[j]>=sprints.get(i+1)){
                    freq[visits[j]]++;
                    if (max<freq[visits[j]] || (max==freq[visits[j]] && visits[j]<idx)){
                        max = freq[visits[j]];
                        idx = visits[j];
                    }
                    j--;
                }
                j++;
            }else{
                while (j<visits.length && visits[j]<=sprints.get(i+1)){
                    freq[visits[j]]++;
                    if (max<=freq[visits[j]] || (max==freq[visits[j]] && visits[j]<idx)){
                        max = freq[visits[j]];
                        idx = visits[j];
                    }
                    j++;
                }
                j--;
            }
        }
        return idx;
    }

    public int getMostVisited(int m, int[] sprint){
        int max = 0;
        int idx = 0;
        int[] visit = new int[m];
        for (int i=0;i<sprint.length; i++){
            visit[sprint[i]] = 1;
        }
        for (int i=0; i<sprint.length-1; i++){
            if (sprint[i]>=sprint[i+1]){
                for (int j=sprint[i]; j>=sprint[i+1]; j--){

                }
            }else{
                for (int j=sprint[i];j<=sprint[i+1];j++){

                }
            }
        }
        LinkedList<Point> visits = new LinkedList<>();
        ListIterator<Point> it = visits.listIterator();
        for (int i=0; i<sprint.length-1; i++){
            if (sprint[i]>=sprint[i+1]){
                if (it.hasNext()){
                    it.next();
                }
                for (int j=sprint[i]; j>=sprint[i+1]; j--){
                    Point p ;
                    if (it.hasPrevious()){
                        p = it.previous();
                        p.y++;
                    }else{
                        p = new Point(j,1);
                        it.add(p);
                        it.previous();
                    }
                    if (p.y>= max && idx>=j){
                        idx = j;
                        max = p.y;
                    }
                }
            }else{
                for (int j=sprint[i];j<=sprint[i+1];j++){
                    Point p ;
                    if (it.hasNext()){
                        p = it.next();
                        p.y++;
                    }else {
                        p = new Point(j,1);
                        it.add(p);
                    }
                    if (p.y>= max && idx<=j){
                        idx = j;
                        max = p.y;
                    }
                }
                it.previous();
            }
        }

        return idx;
    }


    // <id, shares, price, timestamp>
    public static List<Integer> getUnallottedUsers(List<List<Integer>> bids, int totalShares) {
        final int id =0, sh=1, pr=2, ti =3;
        PriorityQueue<List<Integer>> queue = new PriorityQueue<>(new Comparator<List<Integer>>() {
            @Override
            public int compare(List<Integer> o1, List<Integer> o2) {
                if (o1.get(pr).intValue()!=(o2.get(pr)).intValue()){
                    return o2.get(pr)-o1.get(pr);
                }
                return o1.get(ti)-o2.get(ti);
            }
        });
        queue.addAll(bids);
        List<List<Integer>> group = new ArrayList<>();
        int sharesNeedNum = 0;
        TreeSet<Integer> unReceivedUser = new TreeSet<>();
        while (queue.size()>0 && totalShares>0){
            if (group.size()==0){
                List<Integer> item = queue.poll();
                sharesNeedNum += item.get(sh);
                group.add(item);
                continue;
            }
            if (queue.peek().get(pr).intValue() == group.get(0).get(pr)){
                List<Integer> item = queue.poll();
                sharesNeedNum +=item.get(sh);
                group.add(item);
                continue;
            }
            if (totalShares<group.size()){
                for (int i= totalShares; i<group.size(); i++){
                    unReceivedUser.add(group.get(i).get(id));
                }
                break;
            }
            totalShares-=sharesNeedNum;
            sharesNeedNum = 0;
            if (totalShares>0){
                group.clear();
            }
        }
        while (queue.size()>0){
            unReceivedUser.add(queue.poll().get(id));
        }
        Integer[] midware = new Integer[unReceivedUser.size()];
        return Arrays.asList(unReceivedUser.toArray(midware));
    }

    /**
     * scalyr oa 2
     * @param bids  bids
     * @param totalShares totalShares
     * @return id of users who don't receive any shares
     */
    public static List<Integer> getUnallottedUsers_Shunda(List<List<Integer>> bids, int totalShares) {
        List<Integer> ans=new ArrayList();
        if(bids.size()==0)
            return ans;
        int count=0;
        Queue<List<Integer>> pq = new PriorityQueue<List<Integer>>(new Comparator<List<Integer>>() {
            public int compare(List<Integer> a, List<Integer> b) {
                if(a.get(2)==b.get(2)){
                    return a.get(3)-b.get(3);
                }
                else{
                    return b.get(2)-a.get(2);
                }
            }});
        for(int i=0;i<bids.size();i++){
            pq.add(bids.get(i));
        }
        while(totalShares>0&&pq.size()>0){
            count=0;
            int max=pq.peek().get(2);
            int sum=0;
            for(List<Integer> k:pq){
                if(k.get(2)==max){
                    count++;
                    sum+=k.get(1);
                }
                else
                    break;
            }
            if(count>totalShares){
                while(totalShares>0){
                    pq.poll();
                    count--;
                    totalShares--;
                }
            }
            else if(sum>totalShares){
                while(count>0){
                    pq.poll();
                    count--;
                }
                totalShares=0;
            }
            else if(sum<=totalShares){
                while(count>0){
                    pq.poll();
                    count--;
                }
                totalShares-=sum;
            }
        }
        while(pq.size()>0){
            ans.add(pq.poll().get(0));
        }
        Collections.sort(ans, new Comparator<Integer>(){
            public int compare(Integer a,Integer b) {
                return a-b;
            }
        });
        return ans;
    }
    // 2 2 5  4 14

    /**
     * 5
     * 2
     * 8
     * 4
     * 10
     * 6
     * 20
     */
    public long maximumAmount(List<Integer> a, long k){
        Collections.sort(a);
        long res = 0;
        int point = a.size()-1;
        long currentPrice = a.get(point);
        long sameCount = 1;
        while(k>0){
            while (point>0 && a.get(point-1)==currentPrice) {
                sameCount ++;
                point--;
            }
            k = k-sameCount;
            if (k>=0){
                res+= sameCount*currentPrice;
            }else {
                res+= (k+sameCount)*currentPrice;
            }
            currentPrice--;
        }
        return res;
    }

}
