
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

public class TripleCounter {

    public static final int FILE_LINE_LIMIT = 10000000;

    private int fileCounter = 0;

    PriorityQueue<Record> recordHeap = new PriorityQueue<>();

    Map<String, Integer> tripleCounterMap = new HashMap<>();
    Map<String, LinkedList<String>> userTripMap = new HashMap<>();


    public static void main(String[] args){
        args = new String[1];
        args[0] = "app/res/testcase1.txt";
        if (args==null || args.length==0) {
            printHelpInfo();
            return;
        }
        TripleCounter tripleCounter = new TripleCounter();
        tripleCounter.sortLogFile(args[0]);

    }

    private void sortLogFile(String fileName){
        if (fileName==null || fileName.isEmpty()) {
            System.out.println("Please specify a file");
            return;
        }
        int lineCnt = 0;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);
            String s;
            while ((s = reader.readLine())!=null){
                lineCnt++;
                if(s.isEmpty()) continue;
                addToHeap(new Record(s));
            }
            if(recordHeap.size()>0){
                writeSortedRecordToTempFile();
            }
            mergeSortRecord();
            CountTriple();
        } catch (FileNotFoundException e) {
            System.out.printf("file %s does not exist\n", fileName);
        } catch (IOException e){
            System.out.printf(" parsing error in line %d\n", lineCnt);
        }
    }


    private void addToHeap(Record record) throws IOException {
        recordHeap.add(record);
        if(recordHeap.size()>=FILE_LINE_LIMIT){
            writeSortedRecordToTempFile();
        }
    }

    private void writeSortedRecordToTempFile() throws IOException{
        File file = new File(getTempFileName(fileCounter));
        fileCounter++;
        if (file.exists()){
            file.delete();
        }
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        while (!recordHeap.isEmpty()){
            Record polled = recordHeap.poll();
            writer.write(polled.toString());
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

    private void mergeSortRecord() throws IOException {
        BufferedReader[] readers = new BufferedReader[fileCounter];
        Map<Record, Integer> recordFileMap = new HashMap<>();
        for(int i=0; i<fileCounter; i++){
            FileReader fileReader = new FileReader(getTempFileName(i));
            readers[i] =  new BufferedReader(fileReader);
            Record record = new Record(readers[i].readLine());
            recordHeap.add(record);
            recordFileMap.put(record, i);
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("SortedRecord.temp"));
        while (!recordHeap.isEmpty()){
            Record record = recordHeap.poll();
            writer.write(record.toString());
            writer.newLine();
            int tempFile = recordFileMap.remove(record);
            String s = readers[tempFile].readLine();
            if (s!=null){
                Record newRecord = new Record(s);
                recordHeap.add(newRecord);
                recordFileMap.put(newRecord, tempFile);
            }
        }
        writer.flush();
        for(int i=0; i<fileCounter; i++){
            readers[i].close();
            File file =new File(getTempFileName(i));
            if (file.exists()){
                file.delete();
            }
        }
    }

    private String getTempFileName(int temp){
        return "SortedRecord"+temp+".temp";
    }

    private void CountTriple() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("SortedRecord.temp"));
        String s ;
        while ((s=bufferedReader.readLine())!=null){
            Record record = new Record(s);
            LinkedList<String> gameList = userTripMap.get(record.name);
            if (gameList==null){
                gameList = new LinkedList<>();
                userTripMap.put(record.name, gameList);
            }
            gameList.add(record.game);
            if (gameList.size()>=3){
                StringBuilder sb = new StringBuilder();
                String gameKey = sb.append('(').append(gameList.get(0)).append(", ").append(gameList.get(1)).append(", ").append(gameList.get(2)).append(")").toString();
                int count = tripleCounterMap.getOrDefault(gameKey, 1);
                tripleCounterMap.put(gameKey, count+1);
                gameList.remove(0);
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("Triple.out"));
        for (Map.Entry<String, Integer>  entry : tripleCounterMap.entrySet() ){
            writer.write(entry.getKey() + "\t"+ entry.getValue());
            writer.newLine();
        }
        writer.flush();
    }

    private static void printHelpInfo(){
        System.out.println("Usage:");
        System.out.println("java TripleCounter <filename>");
    }
}


