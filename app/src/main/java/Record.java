public class Record implements Comparable<Record>{
    public String name;
    public String game;
    public long ts;
    public Record(String rawRecord) {
        String[] ss = rawRecord.split("\t");
        if (ss.length!=3){
            name = "";
            game = "";
            ts = 0;
        }else {
            name = ss[0];
            game = ss[1];
            ts = Long.parseLong(ss[2]);
        }

    }

    @Override
    public String toString() {
        return name + "\t" + game + "\t" + ts;
    }

    @Override
    public int compareTo(Record o) {
        return (int)(ts-o.ts);
    }
}
