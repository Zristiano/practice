package yuanmengzeng.practice;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * store some sentences and then retrieve the sentence which contains all the given key words.
 * <p>
 * sentence0: "today is a rainy day"
 * sentence1: "I want to have a bicycle trip this weekends"
 * sentence2: "I will be very exciting if I do it with Lina"
 * key words: "I" "I"
 * desirable result: "I will be very exciting if I do it with Lina"
 * </p>
 */
public class FindAndSaveKeyWords {
    HashSet<String> storage = new HashSet<>();

    /**
     * save given sentences in this class
     *
     * @param sentence s
     */
    public void save(String sentence) {
        if (Utils.isEmpty(sentence)) {
            return;
        }
        storage.add(" " + sentence + " ");
    }

    /**
     * find the sentence previously saved in this class by {@link FindAndSaveKeyWords#save(String)} by matching every keys
     *
     * @param keys the keys used to match the specified sentence
     * @return the sentence
     */
    public String find(String... keys) {
        if (keys == null || keys.length == 0) {
            return null;
        }
        Pattern[] patterns = new Pattern[keys.length];
        for (int i = 0; i < patterns.length; i++) {
            patterns[i] = Pattern.compile(" " + keys[i] + " ");
        }
        for (String sentence : storage) {
            boolean hasKey = false;
            String temp = sentence;
            for (Pattern p : patterns) {
                Matcher matcher = p.matcher(temp);
                String nextTemp = matcher.replaceFirst("");
                if (!temp.equals(nextTemp)) {
                    hasKey = true;
                    temp = nextTemp;
                } else {
                    hasKey = false;
                    break;
                }
            }
            if (hasKey) {
                return sentence;
            }
        }
        return null;
    }


}
