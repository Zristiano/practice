package yuanmengzeng.practice;

import java.util.Comparator;
import java.util.PriorityQueue;

public class HeapQuestion {

    public int sortFruits(int[] fruits){
        if (fruits==null || fruits.length==0){
            return 0;
        }
        int[]  heap = new int[fruits.length+1];  // we will start putting fruit into the array at index 1;
        heap[0] = 0;
        int heapSize = 1;
        for (int i=0; i<fruits.length; i++){
            if (fruits[i]!=0){
                heapSize = push(fruits[i],heap,heapSize);
            }
        }
        int workLoad = 0;
        while (heapSize>2){
            int group0 = pop(heap,heapSize);
            heapSize--;
            int group1 = pop(heap,heapSize);
            heapSize--;
            workLoad = workLoad + group0+group1;
            heapSize = push(group0+group1,heap,heapSize);
        }
        return workLoad;
    }

    private int push(int f, int[] heap, int size){
        heap[size] = f;
        ++size;
        shiftUp(heap,size);
        return size;
    }

    public void shiftUp(int[] heap, int size){
        int child = size-1;
        while((child)/2>0){
            int father = child/2;
            if (heap[father]>heap[child]){
                int temp = heap[father];
                heap[father] = heap[child];
                heap[child] = temp;
                child = father;
            }else {
                break;
            }
        }
    }

    private int pop(int[] heap, int size){
        int top = heap[1];
        heap[1] = heap[size-1];
        shiftDown(heap,size);
        return top;
    }

    public void shiftDown(int[] heap, int size){
        int father = 1;
        while (father*2 < size){
            int minIdx;
            int childLeft = father*2;
            int childRight = childLeft+1;
            if (childRight<size){
                minIdx = heap[childLeft]<heap[childRight]?childLeft:childRight;
            }else {
                minIdx = childLeft;
            }
            if (heap[father]>heap[minIdx]){
                int temp = heap[father];
                heap[father] = heap[minIdx];
                heap[minIdx] = temp;
                father = minIdx;
            }else {
                break;
            }
        }
    }


    public double mincostToHireWorkers(int[] quality, int[] wage, int K) {

        PriorityQueue<double[]> heap = new PriorityQueue<>(
                (d1,d2)->{
                    return 0;
        });


        PriorityQueue<double[]> valueHeap = new PriorityQueue(new Comparator<double[]>(){
            public int compare(double[] d1, double[] d2){
                if( d1[0]==d2[0]) return 0;
                if(d1[0]>d2[0])  return 1;
                return -1;
            }
        });

        for(int i=0; i<quality.length; i++){
            double value = (double)wage[i]/quality[i];
            double[] valueMap = {value,(double)i};
            valueHeap.add(valueMap);
        }

        double ratioValue = 0.0;
        PriorityQueue<Double> qualityHeap = new PriorityQueue<Double>(new Comparator<Double>(){
            public int compare(Double d1, Double d2){
                if( d1==d2) return 0;
                if(d1<d2)  return 1;
                return -1;
            }
        });
        double sumQuality = 0;
        while(!valueHeap.isEmpty()){
            double[] item = valueHeap.poll();
            if(qualityHeap.size()!=K){
                ratioValue = item[0];
                qualityHeap.offer((double)quality[(int)item[1]]);
                sumQuality += quality[(int)item[1]];
            }else{
                double sumWage1 = sumQuality*ratioValue;
                double maybeSumQuality = sumQuality-qualityHeap.peek()+quality[(int)item[1]];
                double sumWage2 = maybeSumQuality * item[0];
                if(sumWage2<sumWage1){
                    qualityHeap.poll();
                    qualityHeap.offer((double)quality[(int)item[1]]);
                    ratioValue = item[0];
                    sumQuality = maybeSumQuality;
                }else if(sumWage2 == sumWage1 && maybeSumQuality< sumQuality){
                    qualityHeap.poll();
                    qualityHeap.offer((double)quality[(int)item[1]]);
                    ratioValue = item[0];
                    sumQuality = maybeSumQuality;
                }
            }
        }
        return ratioValue*sumQuality;
    }
}
